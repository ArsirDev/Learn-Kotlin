package com.example.learnkotlin.presentation.register.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnkotlin.data.remote.dto.RegisterResponse
import com.example.learnkotlin.domain.repository.AppsRepository
import com.example.learnkotlin.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: AppsRepository
) : ViewModel() {

    private val _register = MutableLiveData<Result<RegisterResponse>>()

    fun setRegister(
        name: String,
        email: String,
        status: String,
        image: File,
        number_phone: String,
        password: String,
        password_confirmation: String
    ) = viewModelScope.launch {
        _register.postValue(Result.Loading())

        val register = repository.setRegister(
            name,
            email,
            status,
            image,
            number_phone,
            password,
            password_confirmation
        )

        _register.postValue(register)
    }

    fun getRegister(): LiveData<Result<RegisterResponse>> = _register
}