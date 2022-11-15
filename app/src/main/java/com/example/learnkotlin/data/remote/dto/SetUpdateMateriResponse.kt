package com.example.learnkotlin.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SetUpdateMateriResponse(

    @field:SerializedName("data")
    val data: DataSetInputMateri,

    @field:SerializedName("success")
    val success: Boolean,

    @field:SerializedName("message")
    val message: String
)

data class DataSetUpdateMateri(

    @field:SerializedName("image")
    val image: String,

    @field:SerializedName("updated_at")
    val updatedAt: String,

    @field:SerializedName("owner_id")
    val ownerId: Int,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("created_at")
    val createdAt: String,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("another_description")
    val anotherDescription: String
)