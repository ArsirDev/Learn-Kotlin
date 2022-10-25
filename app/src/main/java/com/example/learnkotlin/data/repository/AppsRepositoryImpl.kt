package com.example.learnkotlin.data.repository

import com.example.learnkotlin.data.remote.api.ApiInterface
import com.example.learnkotlin.data.remote.dto.DeleteResponse
import com.example.learnkotlin.data.remote.dto.GetAllInputKuisResponse
import com.example.learnkotlin.data.remote.dto.GetAllInputMateriResponse
import com.example.learnkotlin.data.remote.dto.GetDetailKuisResponse
import com.example.learnkotlin.data.remote.dto.GetDetailMateriResponse
import com.example.learnkotlin.data.remote.dto.GetInputKuisByIdResponse
import com.example.learnkotlin.data.remote.dto.GetInputMateriByIdResponse
import com.example.learnkotlin.data.remote.dto.LoginResponse
import com.example.learnkotlin.data.remote.dto.RegisterResponse
import com.example.learnkotlin.data.remote.dto.SetInputKuisResponse
import com.example.learnkotlin.data.remote.dto.SetInputMateriResponse
import com.example.learnkotlin.domain.repository.AppsRepository
import com.example.learnkotlin.util.ResponseHandler
import com.example.learnkotlin.util.Result
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class AppsRepositoryImpl @Inject constructor(
    private val apiService: ApiInterface,
    private val responseHandler: ResponseHandler
): AppsRepository {

    override suspend fun setLogin(email: String, password: String): Result<LoginResponse> = responseHandler.handleResponse {
        apiService.setLogin(email, password)
    }

    override suspend fun setRegister(
        name: String,
        email: String,
        status: String,
        image: File,
        number_phone: String,
        password: String,
        password_confirmation: String
    ): Result<RegisterResponse> = responseHandler.handleResponse {
        val requestBody = image.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val multipartBody = MultipartBody.Part.createFormData("image", image.name, requestBody)

        val nameRequest = name.toRequestBody("text/plain".toMediaTypeOrNull())
        val emailRequest = email.toRequestBody("text/plain".toMediaTypeOrNull())
        val number_phoneRequest = number_phone.toRequestBody("text/plain".toMediaTypeOrNull())
        val statusRequest = status.toRequestBody("text/plain".toMediaTypeOrNull())
        val passwordRequest = password.toRequestBody("text/plain".toMediaTypeOrNull())
        val password_confirmationRequest = password_confirmation.toRequestBody("text/plain".toMediaTypeOrNull())
        apiService.setRegister(
            nameRequest,
            emailRequest,
            statusRequest,
            multipartBody,
            number_phoneRequest,
            passwordRequest,
            password_confirmationRequest
        )
    }

    override suspend fun setInputMater(
        title: String,
        description: String,
        another_description: String,
        image: File
    ): Result<SetInputMateriResponse> = responseHandler.handleResponse {
        val requestBody = image.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val multipartBody = MultipartBody.Part.createFormData("image", image.name, requestBody)

        val titleRequest = title.toRequestBody("text/plain".toMediaTypeOrNull())
        val descriptionRequest = description.toRequestBody("text/plain".toMediaTypeOrNull())
        val another_descriptionRequest = another_description.toRequestBody("text/plain".toMediaTypeOrNull())
        apiService.setInputMateri(
            titleRequest,
            descriptionRequest,
            another_descriptionRequest,
            multipartBody
        )
    }

    override suspend fun getDeleteMateri(id: Int): Result<DeleteResponse> = responseHandler.handleResponse {
        apiService.getDeleteMateri(id)
    }

    override suspend fun getDetailMateri(id: Int): Result<GetDetailMateriResponse> = responseHandler.handleResponse {
        apiService.getDetailInputMateri(id)
    }

    override suspend fun getAllInputMateri(): Result<GetAllInputMateriResponse> = responseHandler.handleResponse {
        apiService.getAllInputMateri()
    }

    override suspend fun getInputMateriById(): Result<GetInputMateriByIdResponse> = responseHandler.handleResponse {
        apiService.getInputMateriById()
    }

    override suspend fun setInputKuis(
        title: String,
        question: String,
        answer_a: String,
        answer_b: String,
        answer_c: String,
        answer_d: String,
        correct_answer: String,
        image: File
    ): Result<SetInputKuisResponse> = responseHandler.handleResponse {
        val requestBody = image.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val multipartBody = MultipartBody.Part.createFormData("image", image.name, requestBody)

        val titleRequest = title.toRequestBody("text/plain".toMediaTypeOrNull())
        val questionRequest = question.toRequestBody("text/plain".toMediaTypeOrNull())
        val answer_aRequest = answer_a.toRequestBody("text/plain".toMediaTypeOrNull())
        val answer_bRequest = answer_b.toRequestBody("text/plain".toMediaTypeOrNull())
        val answer_cRequest = answer_c.toRequestBody("text/plain".toMediaTypeOrNull())
        val answer_dRequest = answer_d.toRequestBody("text/plain".toMediaTypeOrNull())
        val correct_answerRequest = correct_answer.toRequestBody("text/plain".toMediaTypeOrNull())
        apiService.setInputKuis(
            titleRequest,
            questionRequest,
            answer_aRequest,
            answer_bRequest,
            answer_cRequest,
            answer_dRequest,
            correct_answerRequest,
            multipartBody
        )
    }

    override suspend fun getDeleteKuis(id: Int): Result<DeleteResponse> = responseHandler.handleResponse {
        apiService.getDeleteKuis(id)
    }

    override suspend fun getDetailKuis(id: Int): Result<GetDetailKuisResponse> = responseHandler.handleResponse {
        apiService.getDetailInputKuis(id)
    }

    override suspend fun getAllInputKuis(): Result<GetAllInputKuisResponse> = responseHandler.handleResponse {
        apiService.getAllInputKuis()
    }

    override suspend fun getInputKuisById(): Result<GetInputKuisByIdResponse> = responseHandler.handleResponse {
        apiService.getInputKuisById()
    }
}