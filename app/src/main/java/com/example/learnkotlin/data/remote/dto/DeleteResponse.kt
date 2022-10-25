package com.example.learnkotlin.data.remote.dto

import com.google.gson.annotations.SerializedName

data class DeleteResponse(

	@field:SerializedName("data")
	val data: Int,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String
)
