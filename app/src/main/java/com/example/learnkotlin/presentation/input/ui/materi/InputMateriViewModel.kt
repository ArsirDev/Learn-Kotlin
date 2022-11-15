package com.example.learnkotlin.presentation.input.ui.materi

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnkotlin.data.remote.dto.GeneralResponse
import com.example.learnkotlin.data.remote.dto.SetInputMateriResponse
import com.example.learnkotlin.data.remote.dto.SetUpdateMateriResponse
import com.example.learnkotlin.domain.repository.AppsRepository
import com.example.learnkotlin.util.P_E_M
import com.example.learnkotlin.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class InputMateriViewModel @Inject constructor(
    private val repository: AppsRepository
): ViewModel() {

    private val _input = MutableLiveData<Result<SetInputMateriResponse>>()

    private val _update = MutableLiveData<Result<GeneralResponse>>()

    fun setInputMateri(
        title: String,
        description: String,
        another_descriotion: String,
        image: File
    ) = viewModelScope.launch {
        _input.postValue(Result.Loading())

        val input = repository.setInputMateri(
            title,
            description,
            another_descriotion,
            image
        )

        _input.postValue(input)
    }

    fun getInputMateri(): LiveData<Result<SetInputMateriResponse>> = _input

    fun setUpdateMateri(
        id: Int,
        title: String,
        description: String,
        another_descriotion: String,
        image: File
    ) = viewModelScope.launch {
        _update.postValue(Result.Loading())

        val update = repository.setUpdateMateri(
            id,
            title,
            description,
            another_descriotion,
            image
        )

        _update.postValue(update)
    }

    fun getUpdate(): LiveData<Result<GeneralResponse>> = _update
}