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
import com.example.avo.ViewAnnouncemnt
import com.example.avo.adannoumenct

class MyAdapter(private val context: Context) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    private var userList = emptyList<adannoumenct>()


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.item_firstname)
        val emailTextView: TextView = itemView.findViewById(R.id.item_detalhes)
        val cardView: CardView = itemView.findViewById(R.id.cards)
        val price: TextView = itemView.findViewById(R.id.Preco)
        val textView: TextView = itemView.findViewById(R.id.ad) // TextView com o id "id"

        init {
            textView.visibility = View.INVISIBLE // Torna a TextView invisível quando o ViewHolder é criado
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentUser = userList[position]
        holder.nameTextView.text = currentUser.fname
        holder.emailTextView.text = currentUser.about
        holder.textView.text = currentUser.adid
        holder.price.text = currentUser.price +   " €/hora"

        holder.cardView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ViewAnnouncemnt::class.java)
            intent.putExtra("testedeid", currentUser.adid)
            // Add other values as needed
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun updateUserList(newUserList: List<adannoumenct>) {
        userList = newUserList
        notifyDataSetChanged()
    }
}
