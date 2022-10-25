package com.example.learnkotlin.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("data")
	val data: DataRegister,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class DataRegister(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("token")
	val token: String
)
