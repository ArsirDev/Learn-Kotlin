package com.example.learnkotlin.data.remote.api

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
import com.example.learnkotlin.util.EndPoint.DELETE_KUIS
import com.example.learnkotlin.util.EndPoint.DELETE_MATERI
import com.example.learnkotlin.util.EndPoint.GET_ALL_INPUT_KUIS
import com.example.learnkotlin.util.EndPoint.GET_ALL_INPUT_MATERI
import com.example.learnkotlin.util.EndPoint.GET_DETAIL_INPUT_KUIS
import com.example.learnkotlin.util.EndPoint.GET_DETAIL_INPUT_MATERI
import com.example.learnkotlin.util.EndPoint.GET_INPUT_KUIS_BY_ID
import com.example.learnkotlin.util.EndPoint.GET_INPUT_MATERI_BY_ID
import com.example.learnkotlin.util.EndPoint.LOGIN
import com.example.learnkotlin.util.EndPoint.REGISTER
import com.example.learnkotlin.util.EndPoint.SET_INPUT_KUIS
import com.example.learnkotlin.util.EndPoint.SET_INPUT_MATERI
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiInterface {

    @FormUrlEncoded
    @POST(LOGIN)
    suspend fun setLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<LoginResponse>

    @Multipart
    @POST(REGISTER)
    suspend fun setRegister(
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("status") status: RequestBody,
        @Part file: MultipartBody.Part,
        @Part("number_phone") number_phone: RequestBody,
        @Part("password") password: RequestBody,
        @Part("password_confirmation") password_confirmation: RequestBody
    ): Response<RegisterResponse>

    @Multipart
    @POST(SET_INPUT_MATERI)
    suspend fun setInputMateri(
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part("another_description") another_description: RequestBody,
        @Part file: MultipartBody.Part,
    ): Response<SetInputMateriResponse>

    @GET(DELETE_MATERI)
    suspend fun getDeleteMateri(
        @Query("id") Id: Int
    ): Response<DeleteResponse>

    @GET(GET_DETAIL_INPUT_MATERI)
    suspend fun getDetailInputMateri(
        @Query("id") Id: Int
    ): Response<GetDetailMateriResponse>

    @GET(GET_ALL_INPUT_MATERI)
    suspend fun getAllInputMateri(): Response<GetAllInputMateriResponse>

    @GET(GET_INPUT_MATERI_BY_ID)
    suspend fun getInputMateriById(): Response<GetInputMateriByIdResponse>

    @Multipart
    @POST(SET_INPUT_KUIS)
    suspend fun setInputKuis(
        @Part("title") title: RequestBody,
        @Part("question") question: RequestBody,
        @Part("answer_a") answer_a: RequestBody,
        @Part("answer_b") answer_b: RequestBody,
        @Part("answer_c") answer_c: RequestBody,
        @Part("answer_d") answer_d: RequestBody,
        @Part("correct_answer") correct_answer: RequestBody,
        @Part file: MultipartBody.Part,
    ): Response<SetInputKuisResponse>

    @GET(GET_DETAIL_INPUT_KUIS)
    suspend fun getDetailInputKuis(
        @Query("id") Id: Int
    ): Response<GetDetailKuisResponse>

    @GET(DELETE_KUIS)
    suspend fun getDeleteKuis(
        @Query("id") Id: Int
    ): Response<DeleteResponse>

    @GET(GET_ALL_INPUT_KUIS)
    suspend fun getAllInputKuis(): Response<GetAllInputKuisResponse>

    @GET(GET_INPUT_KUIS_BY_ID)
    suspend fun getInputKuisById(): Response<GetInputKuisByIdResponse>
}