package com.example.avo.Repository

import androidx.lifecycle.MutableLiveData
import com.example.avo.Users
import com.example.avo.adannoumenct
import com.google.firebase.database.*

class UserRepository {

    private val databaseReference : DatabaseReference = FirebaseDatabase.getInstance().getReference("announcements")

    @Volatile private var INSTANCE : UserRepository ?= null

    fun getInstance() : UserRepository{
        return INSTANCE ?: synchronized(this){

            val instance = UserRepository()
            INSTANCE = instance
            instance
        }
    }

    fun loadUsers(userList : MutableLiveData<List<adannoumenct>>){
        databaseReference.addValueEventListener(object  : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                try
                {
                    val _userList : List<adannoumenct> = snapshot.children.map { dataSnapshot ->

                        dataSnapshot.getValue(adannoumenct::class.java)!!
                    }

                    userList.postValue(_userList)
                }
                catch (e : Exception)
                {

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}