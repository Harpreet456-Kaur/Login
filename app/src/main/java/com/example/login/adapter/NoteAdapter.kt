package com.example.login.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.login.Models.NoteModel
import com.example.login.R
import com.example.login.views.HomeFragment

class NoteAdapter(val list: ArrayList<NoteModel>, val noteInterface: HomeFragment) :
    RecyclerView.Adapter<NoteAdapter.viewHolder>() {

    private lateinit var mListener: OnItemClickListener
    var mlist = list

    //private var onClickListener: OnClickListener? = null


    class viewHolder(val itemView: View,listener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.home_name)
        val notes = itemView.findViewById<TextView>(R.id.home_notes)
        val time = itemView.findViewById<TextView>(R.id.home_time)
        val icon = itemView.findViewById<ImageButton>(R.id.iconBtn)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
            icon.setOnClickListener {
                listener.onIconClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : viewHolder {
//        val binding = AddBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val view = LayoutInflater.from(parent.context).inflate(R.layout.add, parent,false)
        return viewHolder(view,mListener)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.name.text = list[position].name
        holder.notes.text = list[position].note
        holder.time.text = list[position].time
    }

//    fun setOnClickListener(onClickListener: OnClickListener) {
//        this.onClickListener = onClickListener
//    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        mListener = listener
    }

    // onClickListener Interface
    interface OnItemClickListener {
        fun onItemClick(position: Int)
        fun onIconClick(position: Int)
    }
}