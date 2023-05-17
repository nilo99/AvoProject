package com.example.avo.Adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.avo.R
import com.example.avo.UserInfoFragment
import com.example.avo.Users

class MyAdapter : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

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
            val userInfoFragment = UserInfoFragment()
            val bundle = Bundle().apply {
                putSerializable("user", currentUser)
            }
            userInfoFragment.arguments = bundle
            val fragmentManager = (holder.itemView.context as FragmentActivity).supportFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, userInfoFragment)
                .addToBackStack(null)
                .commit()
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

