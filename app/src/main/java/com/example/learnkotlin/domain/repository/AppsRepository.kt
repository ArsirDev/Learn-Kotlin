package com.example.learnkotlin.domain.repository

import com.example.learnkotlin.data.remote.dto.*
import com.example.learnkotlin.util.Result
import java.io.File

interface AppsRepository {

    suspend fun setLogin(email: String, password: String): Result<LoginResponse>

    suspend fun setRegister(
        name: String,
        email: String,
        status: String,
        image: File,
        number_phone: String,
        password: String,
        password_confirmation: String
    ): Result<RegisterResponse>

    suspend fun setInputMateri(
        title: String,
        description: String,
        another_description: String,
        image: File
    ): Result<SetInputMateriResponse>

    suspend fun setUpdateMateri(
        id: Int,
        title: String,
        description: String,
        another_description: String,
        image: File
    ): Result<GeneralResponse>

    suspend fun getDeleteMateri(
        id: Int
    ): Result<DeleteResponse>

    suspend fun getDetailMateri(
        id: Int
    ): Result<GetDetailMateriResponse>

    suspend fun getAllInputMateri(
    ): Result<GetAllInputMateriResponse>

    suspend fun getInputMateriById(): Result<GetInputMateriByIdResponse>

    suspend fun setInputKuis(
        title: String,
        question: String,
        answer_a: String,
        answer_b: String,
        answer_c: String,
        answer_d: String,
        correct_answer: String,
        image: File
    ): Result<SetInputKuisResponse>

    suspend fun setUpdateKuis(
        id: Int,
        title: String,
        question: String,
        answer_a: String,
        answer_b: String,
        answer_c: String,
        answer_d: String,
        correct_answer: String,
        image: File
    ): Result<GeneralResponse>

    suspend fun getDeleteKuis(
        id: Int
    ): Result<DeleteResponse>

    suspend fun getDetailKuis(
        id: Int
    ): Result<GetDetailKuisResponse>

    suspend fun getAllInputKuis(
    ): Result<GetAllInputKuisResponse>

    suspend fun getInputKuisById(): Result<GetInputKuisByIdResponse>
}