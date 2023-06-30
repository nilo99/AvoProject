package com.example.avo.Models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.avo.Repository.UserRepository
import com.example.avo.Users
import com.example.avo.adannoumenct

class UserViewModel : ViewModel() {
    private val repository : UserRepository
    private val _allUsers = MutableLiveData<List<adannoumenct>>()
    val allUsers : LiveData<List<adannoumenct>> = _allUsers

    init {
        repository = UserRepository().getInstance()
        repository.loadUsers(_allUsers)
    }
}