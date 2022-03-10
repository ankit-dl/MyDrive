package com.rsoft.mydrive.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rsoft.mydrive.R
import com.rsoft.mydrive.data.model.DFile

class FileAdapter (val listener:(DFile)->Unit): RecyclerView.Adapter<FileAdapter.FileViewHolder>() {

   private var data = ArrayList<DFile>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {

        return FileViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.file_item, parent,false)
        )
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        val item = data[position]
        holder.textView.text = item.name
        holder.itemView.setOnClickListener{
            listener.invoke(item)
        }

    }

    override fun getItemCount() =
        data.size

    fun setDataItems(list: MutableList<DFile>) {
        data.addAll(list)
        notifyDataSetChanged()

    }

    class FileViewHolder( itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView = itemView.findViewById<ImageView>(R.id.imageView)
        var textView = itemView.findViewById<TextView>(R.id.textView)

    }
}