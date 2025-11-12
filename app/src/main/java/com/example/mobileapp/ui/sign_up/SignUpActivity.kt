package com.example.mobileapp.ui.sign_up

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.example.mobileapp.R
import com.example.mobileapp.databinding.ActivitySignInBinding
import com.example.mobileapp.databinding.ActivitySignUpBinding
import com.example.mobileapp.ui.sign_in.SignInActivity
import com.example.mobileapp.ui.sign_in.SignInViewModel
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private val viewModel: SignUpViewModel by viewModel()

    private val INTENT_USER_EMAIL = "UserEmail"

    @SuppressLint("ServiceCast", "ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tilName.errorIconDrawable = null
        binding.tilSurame.errorIconDrawable = null
        binding.tilEmail.editText?.setText(intent.getStringExtra(INTENT_USER_EMAIL) ?: "")
        binding.tilPhoneNumber.errorIconDrawable = null
        binding.tilPassword.errorIconDrawable = null

        binding.btnGoToLogin.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            val userName = binding.tilName.editText?.text.toString()
            //if (Utils.Companion.isEmailValid(userEmail) && !Utils.Companion.isEmailInDB(userEmail)) intent.putExtra(INTENT_USER_EMAIL, userEmail)
            startActivity(intent)
        }

        binding.main.setOnTouchListener { view, event ->
            currentFocus?.clearFocus()
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.main.windowToken, 0)
        }

        viewModel.uiState.observe(this) { state ->
            updateUI(state)
        }

        binding.tilName.editText?.doAfterTextChanged {
                text -> viewModel.onNameChanged(text.toString())
        }
        binding.tilSurame.editText?.doAfterTextChanged {
                text -> viewModel.onSurameChanged(text.toString())
        }

        binding.tilPassword.editText?.doAfterTextChanged {
                text -> viewModel.onPasswordChanged(text.toString())
        }

        binding.btnSignUp.setOnClickListener {
            viewModel.onSignUpClicked()
        }
    }

    private fun updateUI(state: SignUpViewModel.SignUpUiState) {
        //binding.progressBar.isVisible = state.isLoading
        binding.btnSignUp.isEnabled = !state.isLoading
        binding.btnSignUp.text = if (state.isLoading) "Loading..." else getString(R.string.sign_up)

        state.errorMessage?.let { message ->
            Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
            // После показа ошибки можно очистить ее
            viewModel.clearError()
        }

        if (state.isSignUpSuccess) {
            navigateToMain()
            viewModel.resetSuccessState()  // Сбрасываем флаг успеха
        }
    }

    private fun navigateToMain() {

    }
}