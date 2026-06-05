package com.example.mobileapp.ui.sign_up

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import com.example.mobileapp.R
import com.example.mobileapp.data.model.AuthResult
import com.example.mobileapp.databinding.ActivitySignUpBinding
import com.example.mobileapp.ui.sign_in.SignInActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private val viewModel: SignUpViewModel by viewModel()

    @SuppressLint("ServiceCast", "ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val ime = insets.getInsets(WindowInsetsCompat.Type.ime())
            val bottom = if (ime.bottom > 0) ime.bottom else systemBars.bottom
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, bottom)
            insets
        }

        binding.tilName.errorIconDrawable = null
        binding.tilSurame.errorIconDrawable = null
        binding.tilEmail.errorIconDrawable = null
        binding.tilPhoneNumber.errorIconDrawable = null
        binding.tilPassword.errorIconDrawable = null

        binding.btnGoToLogin.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnSignUp.setOnClickListener {
            val name = binding.tilName.editText?.text.toString().trim()
            val surname = binding.tilSurame.editText?.text.toString().trim()
            val email = binding.tilEmail.editText?.text.toString().trim()
            val phoneNumber = binding.tilPhoneNumber.editText?.text.toString().trim()
            val password = binding.tilPassword.editText?.text.toString()

            if (validateInput(name, surname, email, phoneNumber, password)) {
                lifecycleScope.launch {
                    val result = viewModel.signUp(name, surname, phoneNumber, email, password)

                    when (result) {
                        is AuthResult.Success -> {
                            Snackbar.make(binding.root, "Регистрация успешна!", Snackbar.LENGTH_SHORT).show()
                            val intent = Intent(this@SignUpActivity, SignInActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        is AuthResult.Error -> {
                            Snackbar.make(binding.root, result.message, Snackbar.LENGTH_LONG).show()
                        }

                        AuthResult.Loading -> TODO()
                    }
                }
            }
        }

        binding.main.setOnTouchListener { view, event ->
            currentFocus?.clearFocus()
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.main.windowToken, 0)
            false
        }
    }

    private fun validateInput(
        name: String,
        surname: String,
        email: String,
        phoneNumber: String,
        password: String
    ): Boolean {
        var isValid = true

        if (name.isEmpty()) {
            binding.tilName.error = "Введите имя"
            isValid = false
        } else {
            binding.tilName.error = null
        }

        if (surname.isEmpty()) {
            binding.tilSurame.error = "Введите фамилию"
            isValid = false
        } else {
            binding.tilSurame.error = null
        }

        if (email.isEmpty()) {
            binding.tilEmail.error = "Введите email"
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.error = "Некорректный email"
            isValid = false
        } else {
            binding.tilEmail.error = null
        }

        if (password.length < 5) {
            binding.tilPassword.error = "Пароль минимум 5 символов"
            isValid = false
        } else {
            binding.tilPassword.error = null
        }

        return isValid
    }
}