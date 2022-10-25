package com.example.learnkotlin.presentation.input.ui.materi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnkotlin.data.remote.dto.SetInputMateriResponse
import com.example.learnkotlin.domain.repository.AppsRepository
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

    fun setInputMateri(
        title: String,
        description: String,
        another_descriotion: String,
        image: File
    ) = viewModelScope.launch {
        _input.postValue(Result.Loading())

        val input = repository.setInputMater(
            title,
            description,
            another_descriotion,
            image
        )

        _input.postValue(input)
    }

    fun getInputMateri(): LiveData<Result<SetInputMateriResponse>> = _input
}