package com.rsoft.mydrive.repository

import android.content.SharedPreferences
import com.rsoft.mydrive.common.Const
import com.rsoft.mydrive.data.model.DFiles
import com.rsoft.mydrive.data.model.UserInfo
import com.rsoft.mydrive.data.remote.ApiService
import io.reactivex.Single
import okhttp3.ResponseBody
import javax.inject.Inject

class MainRepo @Inject constructor(
    private val apiService: ApiService,
    private val preferences: SharedPreferences
) {


    fun getAccessTokenFromRemote(code: String) =

        apiService.getAccessToken(
            code = code,
            client_id = Const.CLIENT_ID,
            "authorization_code",
            redirect_uri = Const.REDIRECT_URI


        )


    fun getAccessFromLocal() = preferences.getString(Const.TOKEN_KEY, "") ?: ""

    fun saveAccessToken(token: String) {
        preferences.edit().putString(Const.TOKEN_KEY, token)
            .apply()
    }

    fun getDriveFiles(): Single<DFiles> {
        val accesToken = preferences.getString(Const.TOKEN_KEY, "") ?: ""
        val tokenType = preferences.getString(Const.TOKEN_TYPE_KEY, "") ?: ""
        return apiService.getGDriveFiles(authHeader = "Bearer $accesToken")
    }

    fun deleteFile(id: String): Single<String> {
        val accesToken = preferences.getString(Const.TOKEN_KEY, "") ?: ""
        val tokenType = preferences.getString(Const.TOKEN_TYPE_KEY, "") ?: ""
        return apiService.deleteItem("Bearer $accesToken", id)

    }

    fun downloadFile(fileId: String): Single<ResponseBody> {
        val accesToken = preferences.getString(Const.TOKEN_KEY, "") ?: ""
        val tokenType = preferences.getString(Const.TOKEN_TYPE_KEY, "") ?: ""
        return apiService.downloadFile("Bearer $accesToken", fileId)
    }

    fun getProfile() :Single<UserInfo>{
        val accesToken = preferences.getString(Const.TOKEN_KEY, "") ?: ""
        val tokenType = preferences.getString(Const.TOKEN_TYPE_KEY, "") ?: ""
       return apiService.getProfileInfo("Bearer $accesToken")
    }
}