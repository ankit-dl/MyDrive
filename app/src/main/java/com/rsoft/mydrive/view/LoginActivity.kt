package com.rsoft.mydrive.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.rsoft.mydrive.common.Const.CODE
import com.rsoft.mydrive.common.Const.ERROR_CODE
import com.rsoft.mydrive.common.Const.REDIRECT_HOST
import com.rsoft.mydrive.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private var code: String? = null
    private var error: String? = null
    private val TAG = "LoginActivity"
    private val viewModel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.tokenLiveData.observe(this) { token ->
            if (token.isNotEmpty()) navigateToNext()
            else {
                requestLogin()
            }
        }
        val data = intent.data
        if (data != null && !TextUtils.isEmpty(data.scheme)) {
            if (REDIRECT_HOST == data.host) {
                code = data.getQueryParameter(CODE)
                error = data.getQueryParameter(ERROR_CODE)
                if (!TextUtils.isEmpty(code)) {
                    code?.let { viewModel.requestAccessToken(it) }
                }
                if (!TextUtils.isEmpty(error)) {
                    //a problem occurs, the user reject our granting request or something like that
                    Toast.makeText(this, "request rejected", Toast.LENGTH_LONG)
                        .show()
                    Log.e(
                        TAG,
                        "onCreate: handle result of authorization with error :$error"
                    )
                    //then die
                    finish()
                }
            }
        } else {

            viewModel.getAccessToken()

        }
    }

    private fun requestLogin() {
        val url = viewModel.buildLoginUrl()
        url?.let {
            val i = Intent(Intent.ACTION_VIEW)
            Log.e(TAG, "the url is : " + url.toUrl().toString())
            i.data = Uri.parse(url.toUrl().toString())
            i.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(i)
            finish()
        }

    }

    private fun navigateToNext() {
        Toast.makeText(this, "next screen", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

}
