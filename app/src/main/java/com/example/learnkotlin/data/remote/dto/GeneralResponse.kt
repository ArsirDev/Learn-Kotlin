package com.example.learnkotlin.data.remote.dto

import com.google.gson.annotations.SerializedName

data class GeneralResponse(

	@field:SerializedName("data")
	val data: Boolean,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String
)
