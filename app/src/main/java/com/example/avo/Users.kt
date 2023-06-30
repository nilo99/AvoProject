package com.example.avo
import java.io.Serializable


data class Users(

    var fname: String?,
    var email:String?,
    var uid:String?
)
    : Serializable
data class adannoumenct (
    var about : String? = null,
    var address : String? = null,
    var numberPeople : String? = null,
    var latitude: String? = null,
    var longitude: String? = null,
    var fname : String? = null,
    var adid : String? = null,
var price : String?= null
) : Serializable