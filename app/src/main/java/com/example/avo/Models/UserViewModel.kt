package com.example.avo.Models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.avo.Repository.UserRepository
import com.example.avo.Users

class UserViewModel : ViewModel() {
    private val repository : UserRepository
    private val _allUsers = MutableLiveData<List<Users>>()
    val allUsers : LiveData<List<Users>> = _allUsers

    init {
        repository = UserRepository().getInstance()
        repository.loadUsers(_allUsers)
    }
}