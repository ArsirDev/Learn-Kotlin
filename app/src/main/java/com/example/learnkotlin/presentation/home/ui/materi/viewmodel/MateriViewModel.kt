package com.example.learnkotlin.presentation.home.ui.materi.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnkotlin.data.remote.dto.DeleteResponse
import com.example.learnkotlin.data.remote.dto.GetAllInputMateriResponse
import com.example.learnkotlin.data.remote.dto.GetInputMateriByIdResponse
import com.example.learnkotlin.domain.repository.AppsRepository
import com.example.learnkotlin.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MateriViewModel @Inject constructor(
    private val repository: AppsRepository
): ViewModel() {

    private val _getAllMateri = MutableLiveData<Result<GetAllInputMateriResponse>>()

    private val _getDelete = MutableLiveData<Result<DeleteResponse>>()

    fun fetchDeleteMateri(
        id: Int
    ) = viewModelScope.launch {
        _getDelete.postValue(Result.Loading())

        val delete = repository.getDeleteMateri(id)

        _getDelete.postValue(delete)
    }

    fun geDeleteMateri(): LiveData<Result<DeleteResponse>> = _getDelete

    fun fetchAllMateri() = viewModelScope.launch {
        _getAllMateri.postValue(Result.Loading())

        val allMateri = repository.getAllInputMateri()

        _getAllMateri.postValue(allMateri)
    }

    fun getAllMateri(): LiveData<Result<GetAllInputMateriResponse>> = _getAllMateri

    private val _getMateriById = MutableLiveData<Result<GetInputMateriByIdResponse>>()

    fun fetchInpuMateribyId() = viewModelScope.launch {
        _getMateriById.postValue(Result.Loading())

        val inpuMateriById = repository.getInputMateriById()

        _getMateriById.postValue(inpuMateriById)
    }

    fun getInputMateriById(): LiveData<Result<GetInputMateriByIdResponse>> = _getMateriById
}