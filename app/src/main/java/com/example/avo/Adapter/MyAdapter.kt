package com.example.avo.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import com.example.avo.R
import com.example.avo.Users

class MyAdapter : RecyclerView.Adapter<MyAdapter.MyViewHolder>(){

    private val userList = ArrayList<Users>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.user_item,
            parent, false
        )
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = userList[position]

        holder.fn.text = currentItem.fname
        holder.ui.text = currentItem.uid
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun updateUserList(userList:List<Users>)
    {
        this.userList.clear()
        this.userList.addAll(userList)
        notifyDataSetChanged()
    }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val fn : TextView = itemView.findViewById(R.id.item_firstname)
        val ui : TextView = itemView.findViewById(R.id.item_detalhes)
    }

}