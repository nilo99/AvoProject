package com.example.avo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class UserInfoFragment : Fragment() {

    private lateinit var user: Users

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            user = it.getSerializable("user") as Users
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_user_info, container, false)

        // Display the user information in the layout
        view.findViewById<TextView>(R.id.tv_name).text = user.fname
        view.findViewById<TextView>(R.id.tv_email).text = user.email
        view.findViewById<TextView>(R.id.tv_phone).text = user.uid

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(user: Users) =
            UserInfoFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("user", user)
                }
            }
    }
}