package com.example.learnkotlin.presentation.input.ui.kuis

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnkotlin.data.remote.dto.GeneralResponse
import com.example.learnkotlin.data.remote.dto.SetInputKuisResponse
import com.example.learnkotlin.domain.repository.AppsRepository
import com.example.learnkotlin.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class InputKuisViewModel @Inject constructor(
    private val repository: AppsRepository
) : ViewModel() {

    private val _inputKuis = MutableLiveData<Result<SetInputKuisResponse>>()

    private val _updatekuis = MutableLiveData<Result<GeneralResponse>>()

    fun setInputKuis(
        title: String,
        question: String,
        answer_a: String,
        answer_b: String,
        answer_c: String,
        answer_d: String,
        correct_answer: String,
        image: File
    ) = viewModelScope.launch {
        _inputKuis.postValue(Result.Loading())

        val inputKuis = repository.setInputKuis(
            title,
            question,
            answer_a,
            answer_b,
            answer_c,
            answer_d,
            correct_answer,
            image
        )

        _inputKuis.postValue(inputKuis)
    }

    fun getInputKuis(): LiveData<Result<SetInputKuisResponse>> = _inputKuis

    fun setUpdateKuis(
        id: Int,
        title: String,
        question: String,
        answer_a: String,
        answer_b: String,
        answer_c: String,
        answer_d: String,
        correct_answer: String,
        image: File
    ) = viewModelScope.launch {
        _updatekuis.postValue(Result.Loading())

        val inputKuis = repository.setUpdateKuis(
            id,
            title,
            question,
            answer_a,
            answer_b,
            answer_c,
            answer_d,
            correct_answer,
            image
        )

        _updatekuis.postValue(inputKuis)
    }

    fun getUpdateKuis(): LiveData<Result<GeneralResponse>> = _updatekuis


}