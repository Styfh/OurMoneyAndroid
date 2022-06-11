package com.example.sesuperproject.models

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sesuperproject.R

class NextTitleListAdapter(private val titleData: List<Title>) : RecyclerView.Adapter<NextTitleListAdapter.ViewHolder>(){

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val titleView: TextView

        init{
            titleView = view.findViewById<TextView>(R.id.lblTitle)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NextTitleListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_title, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: NextTitleListAdapter.ViewHolder, position: Int) {
        holder.titleView.text = titleData[position].titleName
    }

    override fun getItemCount(): Int {
        return titleData.size
    }
}