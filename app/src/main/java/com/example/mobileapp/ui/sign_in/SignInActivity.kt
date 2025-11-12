package com.example.mobileapp.ui.sign_in

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.example.mobileapp.R
import com.example.mobileapp.databinding.ActivitySignInBinding
import com.example.mobileapp.ui.main_menu.MainMenuActivity
import com.example.mobileapp.ui.sign_up.SignUpActivity
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private val viewModel: SignInViewModel by viewModel()

    private val INTENT_USER_EMAIL = "UserEmail"

    @SuppressLint("ServiceCast", "ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //binding.tILEmail.errorIconDrawable = null
        binding.tilName.errorIconDrawable = null
        binding.tilSurame.errorIconDrawable = null
        binding.tilPassword.errorIconDrawable = null
        //binding.tILEmail.editText?.setText(intent.getStringExtra(INTENT_USER_EMAIL) ?: "")

        binding.btnGoToRegistration.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            val userName = binding.tilName.editText?.text.toString()
            //if (Utils.Companion.isEmailValid(userEmail) && !Utils.Companion.isEmailInDB(userEmail)) intent.putExtra(INTENT_USER_EMAIL, userEmail)
            startActivity(intent)
        }
        binding.btnSignIn.setOnClickListener {
            val intent = Intent(this, MainMenuActivity::class.java)
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

        binding.btnSignIn.setOnClickListener {
            viewModel.onSignInClicked()
        }
    }

    private fun updateUI(state: SignInViewModel.SignInUiState) {
        //binding.progressBar.isVisible = state.isLoading
        //binding.btnSignIn.isEnabled = !state.isLoading
        //binding.btnSignIn.text = if (state.isLoading) "Loading..." else getString(R.string.sign_in)

        state.errorMessage?.let { message ->
            Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
            // После показа ошибки можно очистить ее
            viewModel.clearError()
        }

        if (state.isSignInSuccess) {
            navigateToMain()
            viewModel.resetSuccessState()  // Сбрасываем флаг успеха
        }
    }

    private fun navigateToMain() {

    }

    /*private fun allInputsAreValid(): Boolean {
        var noErrors = true
        // сначала проверяется корректность написания эл. почты (ошибка только у email)
        if (!Utils.Companion.isEmailValid(binding.tILEmail.editText?.text.toString())) {
            noErrors = false
            binding.tILEmail.error = getString(R.string.wrong_email_format)
            binding.tILPassword.isErrorEnabled = false
        }
        // затем проверяется наличие эл. почты в БД
        else if (!Utils.Companion.isEmailInDB(binding.tILEmail.editText?.text.toString())){
            noErrors = false
            binding.tILEmail.error = getString(R.string.email_not_in_DB)
            binding.tILPassword.isErrorEnabled = false
        }
        // в конце проверяется совпадение hash'а пароля в БД по почте
        else if (!Utils.Companion.isEmailInDB(binding.tILEmail.editText?.text.toString())) {
            noErrors = false
            binding.tILEmail.error = getString(R.string.wrong_email_or_password)
            binding.tILPassword.error = getString(R.string.wrong_email_or_password)
        }
        // если все верно, то ошибки предыдущих нажатий убираются
        else {
            binding.tILEmail.isErrorEnabled = false
            binding.tILPassword.isErrorEnabled = false
        }
        return noErrors
    }*/
}