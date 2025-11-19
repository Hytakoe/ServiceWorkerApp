package com.example.mobileapp.ui.main_menu

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapp.R
import com.example.mobileapp.data.model.CarModel

class CarRecyclerViewAdapter(_context: Context?, _carModels: MutableList<CarModel>) : RecyclerView.Adapter<CarRecyclerViewAdapter.MyViewHolder>() {

    val context: Context? = _context
    val carModels: MutableList<CarModel> = _carModels

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.rv_main_row, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tvCarName.text = carModels[position].carName
        holder.tvJob.text = carModels[position].job
        holder.tvComment.text = carModels[position].comment
        holder.imageView.setImageResource(carModels[position].image)
    }

    override fun getItemCount(): Int {
        val count = carModels.size
        return count
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById(R.id.imageView)
        var tvCarName: TextView = itemView.findViewById(R.id.tvCarName)
        var tvJob: TextView = itemView.findViewById(R.id.tvJob)
        var tvComment: TextView = itemView.findViewById(R.id.tvComment)
    }
}