package com.rsoft.mydrive.view

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.rsoft.mydrive.R
import com.rsoft.mydrive.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    lateinit var rv: RecyclerView
    lateinit var adapter: FileAdapter
    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rv = findViewById(R.id.rv)
        adapter = FileAdapter {
            // viewModel.deleteFile(it.id!!)
            Log.d(TAG, "mime : ${it.mimeType}")
           downloadFile(it.id!!)

        }
        rv.adapter = adapter
        viewModel.fileLiveData.observe(this) {
            // setAdapter
            it?.let { items -> adapter.setDataItems(it.toMutableList()) }

        }
        viewModel.getDriveData()
    }

    private fun downloadFile(fileId: String) {

        viewModel.downloadFile(fileId, filesDir.absolutePath)
    }
}