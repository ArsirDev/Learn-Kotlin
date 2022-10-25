package com.example.learnkotlin.presentation.detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnkotlin.data.remote.dto.GetDetailKuisResponse
import com.example.learnkotlin.domain.repository.AppsRepository
import com.example.learnkotlin.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KuisDetailViewModel @Inject constructor(
    private val repository: AppsRepository
): ViewModel() {

    private val _detailKuis = MutableLiveData<Result<GetDetailKuisResponse>>()

    fun fetchDetailKuis(
        id: Int
    ) = viewModelScope.launch {
        _detailKuis.postValue(Result.Loading())

        val detail = repository.getDetailKuis(id)

        _detailKuis.postValue(detail)
    }

    fun getDetailKuis(): LiveData<Result<GetDetailKuisResponse>> = _detailKuis
}