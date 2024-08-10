package com.rivaphy.suitmediatestapp.response

import com.google.gson.annotations.SerializedName

data class UserResponse(
	val perPage: Int? = null,
	val total: Int? = null,
	val data: List<DataItem?>? = null,
	val page: Int? = null,
	val totalPages: Int? = null,
)

data class DataItem(
	@SerializedName("id")
	val id: Int,
	@SerializedName("first_name")
	val firstName: String,
	@SerializedName("last_name")
	val lastName: String,
	@SerializedName("email")
	val email: String,
	@SerializedName("avatar")
	val avatar: String
)
