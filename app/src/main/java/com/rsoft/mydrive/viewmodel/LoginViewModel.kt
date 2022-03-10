package com.rsoft.mydrive.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rsoft.mydrive.common.Const.API_SCOPE
import com.rsoft.mydrive.common.Const.CLIENT_ID
import com.rsoft.mydrive.common.Const.CODE
import com.rsoft.mydrive.common.Const.REDIRECT_URI
import com.rsoft.mydrive.repository.MainRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.json.JSONArray
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repo: MainRepo) : ViewModel() {
    private val TAG = "LoginViewModel"
    val disposable = CompositeDisposable()
    private val _tokenLiveData = MutableLiveData<String>()
    val tokenLiveData: LiveData<String> = _tokenLiveData

    fun getAccessToken() {
        val token = repo.getAccessFromLocal()
        _tokenLiveData.value = token

    }

    fun requestAccessToken(code: String) {
        disposable.add(
            repo.getAccessTokenFromRemote(code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    repo.saveAccessToken(it.accessToken ?: "")
                    _tokenLiveData.value = it.accessToken ?: ""
                }, {
                    it.printStackTrace()
                    Log.d(TAG, "requestAccessToken: ")
                })
        )

    }

    fun buildLoginUrl() =
        "https://accounts.google.com/o/oauth2/v2/auth".toHttpUrlOrNull()?.newBuilder()
            ?.addQueryParameter("client_id", CLIENT_ID)?.addQueryParameter("scope", API_SCOPE)
            ?.addQueryParameter("redirect_uri", REDIRECT_URI)
            ?.addQueryParameter("response_type", CODE)?.build()



}