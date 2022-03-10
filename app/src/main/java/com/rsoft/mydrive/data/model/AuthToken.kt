package com.rsoft.mydrive.data.model

import com.google.gson.annotations.SerializedName

data class AuthToken(
    @SerializedName("access_token")
    var accessToken: String? = null,
    @SerializedName("token_type")
    val tokenType: String? = null,
    @SerializedName("expires_in")
    val expiresIn: Long = 0,
    @SerializedName("refresh_token")
    val refreshToken: String? = null
)