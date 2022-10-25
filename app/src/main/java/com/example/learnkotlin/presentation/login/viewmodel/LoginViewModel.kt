package com.example.learnkotlin.presentation.login.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnkotlin.data.remote.dto.LoginResponse
import com.example.learnkotlin.domain.repository.AppsRepository
import com.example.learnkotlin.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AppsRepository
): ViewModel() {

    private val _login = MutableLiveData<Result<LoginResponse>>()

    private val _uiEvent = MutableSharedFlow<String>()

    val uiEvent get() = _uiEvent.asSharedFlow()

    fun onValidation(email: String, password: String) = viewModelScope.launch {
        if (email.isEmpty() || password.isEmpty()) {
            _uiEvent.emit("Field tidak boleh kosong")
            return@launch
        }

        val emailsPattern = Patterns.EMAIL_ADDRESS
        if (!emailsPattern.matcher(email).matches()) {
            _uiEvent.emit("Email tidak valid")
            return@launch
        }

        setLogin(email, password)
    }

    private fun setLogin(
        email: String,
        password: String
    ) = viewModelScope.launch {
        _login.postValue(Result.Loading())

        val login = repository.setLogin(email, password)

        _login.postValue(login)
    }

    fun getLogin(): LiveData<Result<LoginResponse>> = _login
}