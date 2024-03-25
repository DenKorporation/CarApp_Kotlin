package by.bsuir.carapp.data

import com.google.firebase.Timestamp

data class UserData(
    var uid:String? = null,
    val firstname:String? = null,
    val lastname:String? = null,
    val birthday: Timestamp? = null,
    val gender:String? = null,
    val address:String? = null,
    val phone:String? = null,
    val carCountry:String? = null,
    val carBody:String? = null,
    val carDrive:String? = null,
    val transmission:String? = null,
    var favCars:List<String> = arrayListOf()
)
