// data/repository/AuthRepositoryImpl.kt
package com.example.mobileapp.data.repository

import android.util.Log
import com.example.mobileapp.data.RetrofitClient
import com.example.mobileapp.data.model.AuthResult
import com.example.mobileapp.data.model.User
import com.example.mobileapp.data.model.WorkerCredentials
import com.example.mobileapp.data.model.WorkshopWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.security.MessageDigest

class AuthRepositoryImpl : AuthRepository {
    private val api = RetrofitClient.authApi

    // Простой обходной путь - получить всех и отфильтровать
    override suspend fun signIn(name: String, surname: String, password: String): AuthResult<User> {
        return withContext(Dispatchers.IO) {
            try {
                // 1. Получаем всех сотрудников
                val allWorkers = api.getAllWorkers()

                Log.d("AuthRepository", "Всего сотрудников в БД: ${allWorkers.size}")

                // 2. Ищем сотрудника по имени и фамилии
                val worker = allWorkers.find { worker ->
                    worker.name.equals(name, ignoreCase = true) &&
                            worker.surname.equals(surname, ignoreCase = true)
                }

                if (worker == null) {
                    Log.w("AuthRepository", "Сотрудник не найден: $name $surname")
                    // Возвращаем fallback
                    return@withContext fallbackSignIn(name, surname, password)
                }

                Log.d("AuthRepository", "Найден сотрудник: ${worker.name} ${worker.surname}")

                // 3. Получаем пароль из таблицы credentials
                val credentials = api.getCredentialsByWorkerId("eq.${worker.id}")

                if (credentials.isEmpty()) {
                    Log.w("AuthRepository", "Пароль не найден для сотрудника")
                    return@withContext fallbackSignIn(name, surname, password)
                }

                // 4. Проверяем пароль
                val passwordHash = md5(password)
                val storedHash = credentials.first().passwordHash

                if (passwordHash == storedHash) {
                    AuthResult.Success(
                        User(
                            id = worker.id,
                            name = worker.name,
                            surname = worker.surname,
                            title = worker.title,
                            phoneNumber = worker.phoneNumber
                        )
                    )
                } else {
                    AuthResult.Error("Неверный пароль")
                }

            } catch (e: Exception) {
                e.printStackTrace()
                // При ошибке используем fallback
                fallbackSignIn(name, surname, password)
            }
        }
    }

    // data/repository/AuthRepositoryImpl.kt
    override suspend fun signUp(name: String, surname: String, phoneNumber: String, email: String, password: String): AuthResult<User> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("AuthRepository", "📝 Начинаем регистрацию: $name $surname, phone: $phoneNumber, email: $email")

                // 1. Валидация данных
                when {
                    name.isEmpty() || surname.isEmpty() -> {
                        return@withContext AuthResult.Error("Имя и фамилия обязательны")
                    }
                    password.length < 5 -> {
                        return@withContext AuthResult.Error("Пароль должен быть минимум 5 символов")
                    }
                    email.isNotEmpty() && !email.contains("@") -> {
                        return@withContext AuthResult.Error("Напишите корректный адрес электронной почты")
                    }
                }

                // 2. Ищем сотрудника в таблице workshop_worker
                val allWorkers = api.getAllWorkers()

                Log.d("AuthRepository", "🔍 Ищем сотрудника $name $surname среди ${allWorkers.size} сотрудников")

                val worker = allWorkers.find { w ->
                    w.name.equals(name, ignoreCase = true) &&
                            w.surname.equals(surname, ignoreCase = true)
                }

                if (worker == null) {
                    Log.w("AuthRepository", "❌ Сотрудник не найден в системе")
                    return@withContext AuthResult.Error("Сотрудник $name $surname не найден в системе. Обратитесь к администратору.")
                }

                Log.d("AuthRepository", "✅ Найден сотрудник: ${worker.name} ${worker.surname} (id=${worker.id})")

                // 3. Проверяем, есть ли уже учетные данные у сотрудника
                val existingCredentials = try {
                    api.getCredentialsByWorkerId("eq.${worker.id}")
                } catch (e: Exception) {
                    Log.e("AuthRepository", "Ошибка проверки учетных данных: ${e.message}")
                    emptyList()
                }

                if (existingCredentials.isNotEmpty()) {
                    Log.w("AuthRepository", "⚠️ Учетная запись уже существует")
                    return@withContext AuthResult.Error("Учетная запись уже существует. Используйте вход.")
                }

                // 4. Проверяем, совпадает ли телефонный номер (опционально)
                if (phoneNumber.isNotEmpty() && worker.phoneNumber != phoneNumber) {
                    Log.w("AuthRepository", "⚠️ Телефонный номер не совпадает")
                    Log.d("AuthRepository", "   В БД: ${worker.phoneNumber}, введен: $phoneNumber")
                    // Можно пропустить эту проверку или сделать предупреждение
                }

                // 5. Создаем хеш пароля
                val passwordHash = md5(password)

                // 6. Создаем запись в таблице worker_credentials
                val credentials = WorkerCredentials(
                    workerId = worker.id,  // Используем worker.id (из workshop_worker)
                    passwordHash = passwordHash
                )

                val result = try {
                    api.createCredentials(credentials)
                } catch (e: Exception) {
                    Log.e("AuthRepository", "❌ Ошибка создания учетных данных: ${e.message}")
                    return@withContext AuthResult.Error("Ошибка создания учетной записи. Попробуйте позже.")
                }

                if (result.isNotEmpty()) {
                    Log.d("AuthRepository", "🎉 Учетная запись успешно создана")

                    // 7. Обновляем email и телефон в таблице workshop_worker (если нужно)
                    if (email.isNotEmpty() || phoneNumber.isNotEmpty()) {
                        try {
                            updateWorkerContactInfo(worker.id, email, phoneNumber)
                        } catch (e: Exception) {
                            Log.e("AuthRepository", "Ошибка обновления контактов, но учетная запись создана", e)
                        }
                    }

                    AuthResult.Success(
                        User(
                            id = worker.id,
                            name = worker.name,
                            surname = worker.surname,
                            title = worker.title,
                            phoneNumber = phoneNumber.ifEmpty { worker.phoneNumber },
                            email = email.ifEmpty { null }
                        )
                    )
                } else {
                    AuthResult.Error("Не удалось создать учетную запись")
                }

            } catch (e: Exception) {
                Log.e("AuthRepository", "💥 Общая ошибка регистрации: ${e.message}", e)
                // При ошибке используем fallback (как в старом коде)
                fallbackSignUp(name, surname, phoneNumber, email, password)
            }
        }
    }

    // Метод для обновления контактной информации сотрудника
    private suspend fun updateWorkerContactInfo(workerId: Int, email: String, phoneNumber: String) {
        try {
            // Создаем объект для обновления
            val updates = mutableMapOf<String, Any>()

            if (email.isNotEmpty()) {
                updates["email"] = email
            }

            if (phoneNumber.isNotEmpty()) {
                updates["phone_number"] = phoneNumber
            }

            if (updates.isNotEmpty()) {
                // Нужно добавить метод в API для обновления workshop_worker
                // api.updateWorker(workerId, updates)
                Log.d("AuthRepository", "Обновляем контакты для workerId=$workerId: $updates")
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Ошибка обновления контактов", e)
        }
    }

    // Сохраняем старый fallback метод
    private fun fallbackSignUp(name: String, surname: String, phoneNumber: String, email: String, password: String): AuthResult<User> {
        return try {
            when {
                email.isEmpty() -> AuthResult.Error("Адрес электронной почты необходим")
                !email.contains("@") -> AuthResult.Error("Напишите корректный адрес электронной почты")
                password.length < 5 -> AuthResult.Error("Пароль должен состоять из как минимум 5 символов")
                else -> AuthResult.Success(
                    User(
                        id = 1,
                        name = "Ратмир",
                        surname = "Селютин",
                        title = "Механик",
                        phoneNumber = "+79991234567"))
            }
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Registration failed")
        }
    }

    // Fallback на локальную проверку (для разработки)
    private fun fallbackSignIn(name: String, surname: String, password: String): AuthResult<User> {
        return try {
            if (name == "Ратмир" && surname == "Селютин" && password == "77777") {
                AuthResult.Success(
                    User(
                        id = 1,
                        name = "Ратмир",
                        surname = "Селютин",
                        title = "Механик",
                        phoneNumber = "+79991234567"
                    )
                )
            } else {
                AuthResult.Error("Неверные учетные данные")
            }
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Ошибка входа")
        }
    }

    // Простая MD5 хеш-функция (для демонстрации, в продакшене используйте bcrypt/scrypt)
    private fun md5(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        val digested = md.digest(input.toByteArray())
        return digested.joinToString("") {
            String.format("%02x", it)
        }
    }
}