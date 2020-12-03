package com.apolis.groceryapp.models

data class LoginResponse(
    val token: String,
    val user: User
)

data class User (
    val _id: String? = null,
    val firstName: String? = null,
    val email: String? = null,
    val password: String? = null,
    val mobile: String? = null
)