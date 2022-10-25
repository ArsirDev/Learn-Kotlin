package com.example.learnkotlin.presentation.detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnkotlin.data.remote.dto.GetDetailMateriResponse
import com.example.learnkotlin.domain.repository.AppsRepository
import com.example.learnkotlin.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MateriDetailViewModel @Inject constructor(
    private val repository: AppsRepository
): ViewModel() {

    private val _detailKuis = MutableLiveData<Result<GetDetailMateriResponse>>()

    fun fetchDetailMateri(
        id: Int
    ) = viewModelScope.launch {
        _detailKuis.postValue(Result.Loading())

        val detail = repository.getDetailMateri(id)

        _detailKuis.postValue(detail)
    }

    fun getDetailMateri(): LiveData<Result<GetDetailMateriResponse>> = _detailKuis
}