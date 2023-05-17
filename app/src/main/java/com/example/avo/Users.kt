package com.example.avo
import java.io.Serializable

data class Users (
    var fname : String? = null,
    var lname : String? = null,
    var email : String? = null,
    var password : String? = null,
    var uid : String? = null,
    var checkbox : String? = "0"
) : Serializable