package com.example.mobileapp.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mobileapp.R
import com.example.mobileapp.data.SessionManager
import com.example.mobileapp.databinding.FragmentSettingsBinding
import com.example.mobileapp.ui.sign_in.SignInActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())

        setupUserInfo()
        setupLogoutButton()
        //setupClearDataButton()
    }

    private fun setupUserInfo() {
        val currentUser = sessionManager.getCurrentUser()

        if (currentUser != null) {
            // Отображаем информацию о пользователе
            binding.tvUserName.text = "${currentUser.name} ${currentUser.surname}"
            binding.tvUserTitle.text = currentUser.title
            binding.tvUserId.text = "ID: ${currentUser.id}"

            // Показываем блок с информацией
            binding.userInfoLayout.visibility = View.VISIBLE
            binding.tvNoUser.visibility = View.GONE
        } else {
            // Пользователь не авторизован
            binding.userInfoLayout.visibility = View.GONE
            binding.tvNoUser.visibility = View.VISIBLE
            binding.btnLogout.text = "Войти"
        }
    }

    private fun setupLogoutButton() {
        binding.btnLogout.setOnClickListener {
            if (sessionManager.isLoggedIn()) {
                showLogoutConfirmationDialog()
            } else {
                // Если не авторизован, переход на экран входа
                navigateToSignIn()
            }
        }
    }


    private fun showLogoutConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Выход из аккаунта")
            .setMessage("Вы уверены, что хотите выйти из аккаунта?")
            .setPositiveButton("Выйти") { dialog, which ->
                performLogout()
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun showClearDataDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Очистка кэша")
            .setMessage("Очистить кэш приложения? Это не затронет ваши данные на сервере.")
            .setPositiveButton("Очистить") { dialog, which ->
                // Здесь можно добавить очистку кэша если нужно
                com.example.mobileapp.ToastUtils.showToast(
                    requireContext(),
                    "Кэш очищен"
                )
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun performLogout() {
        sessionManager.logout()

        // Показать сообщение об успешном выходе
        com.example.mobileapp.ToastUtils.showToast(
            requireContext(),
            "Вы успешно вышли из аккаунта"
        )

        // Обновить UI
        setupUserInfo()

        // Перейти на экран входа
        navigateToSignIn()
    }

    private fun navigateToSignIn() {
        val intent = Intent(requireActivity(), SignInActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}