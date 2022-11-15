package com.example.learnkotlin.presentation.home.ui.kuis.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnkotlin.data.remote.dto.DeleteResponse
import com.example.learnkotlin.data.remote.dto.GetAllInputKuisResponse
import com.example.learnkotlin.data.remote.dto.GetInputKuisByIdResponse
import com.example.learnkotlin.domain.repository.AppsRepository
import com.example.learnkotlin.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KuisViewModel @Inject constructor(
    private val repository: AppsRepository
): ViewModel() {
    private val _getAllKuis = MutableLiveData<Result<GetAllInputKuisResponse>>()

    private val _deleteKuis = MutableLiveData<Result<DeleteResponse>>()

    fun fetchDeletKuis(
        id: Int
    ) = viewModelScope.launch {
        _deleteKuis.postValue(Result.Loading())

        val delete = repository.getDeleteKuis(id)

        _deleteKuis.postValue(delete)
    }

    fun getDeleteKuis(): LiveData<Result<DeleteResponse>> = _deleteKuis

    fun fetchAllKuis() = viewModelScope.launch {
        _getAllKuis.postValue(Result.Loading())

        val allKuis = repository.getAllInputKuis()

        _getAllKuis.postValue(allKuis)
    }

    fun getAllKuis(): LiveData<Result<GetAllInputKuisResponse>> = _getAllKuis

    private val _getKuisById = MutableLiveData<Result<GetInputKuisByIdResponse>>()

    fun fetchInpuKuisbyId() = viewModelScope.launch {
        _getKuisById.postValue(Result.Loading())
        val inpuKuisById = repository.getInputKuisById()
        _getKuisById.postValue(inpuKuisById)
    }

    fun getInputKuisById(): LiveData<Result<GetInputKuisByIdResponse>> = _getKuisById
}