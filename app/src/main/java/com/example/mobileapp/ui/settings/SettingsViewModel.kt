package com.example.mobileapp.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileapp.data.SessionManager
import com.example.mobileapp.data.model.User
import kotlinx.coroutines.launch

class SettingsViewModel(private val sessionManager: SessionManager) : ViewModel() {

    private val _userInfo = MutableLiveData<User?>()
    val userInfo: LiveData<User?> = _userInfo

    private val _logoutSuccess = MutableLiveData<Boolean>()
    val logoutSuccess: LiveData<Boolean> = _logoutSuccess

    init {
        loadUserInfo()
    }

    private fun loadUserInfo() {
        _userInfo.value = sessionManager.getCurrentUser()
    }

    fun logout() {
        viewModelScope.launch {
            try {
                sessionManager.logout()
                _logoutSuccess.value = true
            } catch (e: Exception) {
                _logoutSuccess.value = false
            }
        }
    }
}