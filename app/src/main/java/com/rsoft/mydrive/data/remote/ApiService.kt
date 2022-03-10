package com.rsoft.mydrive.data.remote

import com.rsoft.mydrive.data.model.AuthToken
import com.rsoft.mydrive.data.model.DFiles
import com.rsoft.mydrive.data.model.UserInfo
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.*


interface ApiService {

    @FormUrlEncoded
    @POST("oauth2/v4/token")
    fun getAccessToken(
        @Field("code") code: String?,
        @Field("client_id") client_id: String?,
        @Field("grant_type") grant_type: String?,
        @Field("redirect_uri") redirect_uri: String?
    ): Single<AuthToken>

    @GET("drive/v3/files")
    fun getGDriveFiles(
        @Header("Authorization") authHeader: String
    ): Single<DFiles>

    @DELETE("drive/v3/files/{id}")
    fun deleteItem(
        @Header("Authorization") authHeader: String,
        @Path("id") itemId: String
    ): Single<String>

    @GET("drive/v3/files/{id}?alt=media")
    fun downloadFile(
        @Header("Authorization") authHeader: String,
        @Path("id") itemId: String
    ): Single<ResponseBody>

    @GET("oauth2/v3/userinfo?alt=json")
    fun getProfileInfo(@Header("Authorization") authHeader: String): Single<UserInfo>

}