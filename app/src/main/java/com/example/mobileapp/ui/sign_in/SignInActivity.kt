package com.example.mobileapp.ui.sign_in

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import com.example.mobileapp.R
import com.example.mobileapp.data.model.AuthResult
import com.example.mobileapp.databinding.ActivitySignInBinding
import com.example.mobileapp.ui.main_menu.MainMenuActivity
import com.example.mobileapp.ui.sign_up.SignUpActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private val viewModel: SignInViewModel by viewModel()

    @SuppressLint("ServiceCast", "ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tilName.errorIconDrawable = null
        binding.tilSurame.errorIconDrawable = null
        binding.tilPassword.errorIconDrawable = null

        binding.btnGoToRegistration.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.main.setOnTouchListener { view, event ->
            currentFocus?.clearFocus()
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.main.windowToken, 0)
            false
        }

        setupObservers()

        setupTextWatchers()

        binding.btnSignIn.setOnClickListener {
            viewModel.onSignInClicked()
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                updateUI(state)
            }
        }

        lifecycleScope.launch {
            viewModel.authState.collect { result ->
                result?.let {
                    when (it) {
                        is AuthResult.Success -> {
                            navigateToMain()
                            viewModel.clearAuthState()
                        }
                        is AuthResult.Error -> {
                            showError(it.message)
                            viewModel.clearAuthState()
                        }
                        AuthResult.Loading -> TODO()
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.loading.collect { isLoading ->
                binding.progressBar.isVisible = isLoading
                binding.btnSignIn.isEnabled = !isLoading
                binding.btnSignIn.text = if (isLoading) "Загрузка..." else getString(R.string.sign_in)
            }
        }
    }

    private fun setupTextWatchers() {
        binding.tilName.editText?.doAfterTextChanged { text ->
            viewModel.onNameChanged(text.toString())
            clearNameError()
        }

        binding.tilSurame.editText?.doAfterTextChanged { text ->
            viewModel.onSurameChanged(text.toString())
            clearSurnameError()
        }

        binding.tilPassword.editText?.doAfterTextChanged { text ->
            viewModel.onPasswordChanged(text.toString())
            clearPasswordError()
        }
    }

    private fun updateUI(state: SignInViewModel.SignInUiState) {
        state.nameError?.let {
            binding.tilName.error = it
        } ?: run {
            clearNameError()
        }

        state.surnameError?.let {
            binding.tilSurame.error = it
        } ?: run {
            clearSurnameError()
        }

        state.passwordError?.let {
            binding.tilPassword.error = it
        } ?: run {
            clearPasswordError()
        }

        state.errorMessage?.let {
            showError(it)
            lifecycleScope.launch {
                viewModel.clearError()
            }
        }
    }

    private fun clearNameError() {
        binding.tilName.error = null
        binding.tilName.isErrorEnabled = false
    }

    private fun clearSurnameError() {
        binding.tilSurame.error = null
        binding.tilSurame.isErrorEnabled = false
    }

    private fun clearPasswordError() {
        binding.tilPassword.error = null
        binding.tilPassword.isErrorEnabled = false
    }

    private fun showError(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainMenuActivity::class.java)
        startActivity(intent)
        finish()
    }
}