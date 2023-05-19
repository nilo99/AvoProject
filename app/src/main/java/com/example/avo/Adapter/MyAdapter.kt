package com.example.avo.Adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.avo.R
import com.example.avo.Repository.UserInfoActivity
import com.example.avo.Users

class MyAdapter(private val context: Context) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    private var userList = emptyList<Users>()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.item_firstname)
        val emailTextView: TextView = itemView.findViewById(R.id.item_detalhes)
        val phoneTextView: TextView = itemView.findViewById(R.id.marca)
        val cardView: CardView = itemView.findViewById(R.id.cards)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentUser = userList[position]
        holder.nameTextView.text = currentUser.fname
        holder.emailTextView.text = currentUser.email
        holder.phoneTextView.text = currentUser.uid

        holder.cardView.setOnClickListener {
            val intent = Intent(context, UserInfoActivity::class.java)
            intent.putExtra("user", currentUser)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun updateUserList(newUserList: List<Users>) {
        userList = newUserList
        notifyDataSetChanged()
    }
}
