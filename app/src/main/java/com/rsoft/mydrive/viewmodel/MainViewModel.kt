package com.rsoft.mydrive.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rsoft.mydrive.data.model.DFile
import com.rsoft.mydrive.repository.MainRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repo: MainRepo) : ViewModel() {
    private val TAG = "MainViewModel"
    private val disposable = CompositeDisposable()
    private val _fileLiveData = MutableLiveData<List<DFile>>()
    val fileLiveData: LiveData<List<DFile>> = _fileLiveData
    fun getDriveData() {
        disposable.add(
            repo.getDriveFiles()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.d(TAG, "getDriveData: $it")
                    _fileLiveData.value = it.files

                }, {
                    it.printStackTrace()
                    Log.e(TAG, "getDriveData: exception $")
                })
        )
    }

    fun deleteFile(id: String) {
        disposable.add(
            repo.deleteFile(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.d(TAG, "deleteFile: $it")

                }, {
                    it.printStackTrace()
                    Log.e(TAG, "getDriveData: exception ${it.message}")
                })
        )

    }

    fun downloadFile(fileId: String, path: String) {
        disposable.add(
            repo.downloadFile(fileId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap { rb ->
                    Single.create<String> {
                        saveFile(rb, path)
                    }

                }
                .subscribe({

                }, {

                })
        )
    }

    private fun saveFile(body: ResponseBody?, path: String): String {
        if (body == null)
            return ""
        var input: InputStream? = null
        try {
            input = body.byteStream()

            val fos = FileOutputStream(File(path, "${System.currentTimeMillis()}"))
            fos.use { output ->
                val buffer = ByteArray(4 * 1024)
                var read: Int
                while (input.read(buffer).also { read = it } != -1) {
                    output.write(buffer, 0, read)
                }
                output.flush()

            }
            return path
        } catch (e: Exception) {
            Log.e("saveFile", e.toString())
        } finally {
            input?.close()
        }
        return ""
    }
}