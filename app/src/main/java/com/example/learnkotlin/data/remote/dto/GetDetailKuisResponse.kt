package com.example.learnkotlin.data.remote.dto

import com.google.gson.annotations.SerializedName

data class GetDetailKuisResponse(

	@field:SerializedName("data")
	val dataDetailKuisItem: DataDetailKuisItem,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class DataDetailKuisItem(

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("question")
	val question: String,

	@field:SerializedName("owner_id")
	val ownerId: String,

	@field:SerializedName("correct_answer")
	val correctAnswer: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("answer_b")
	val answerB: String,

	@field:SerializedName("answer_a")
	val answerA: String,

	@field:SerializedName("answer_d")
	val answerD: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("answer_c")
	val answerC: String
)
