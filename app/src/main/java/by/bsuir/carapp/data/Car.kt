package by.bsuir.carapp.data

data class Car(
    var uid: String? = null,
    val name: String? = null,
    val years: String? = null,
    val description: String? = null,
    val country: String? = null,
    val body: String? = null,
    val drive: String? = null,
    val transmission: String? = null,

    var isFav: Boolean = false
)
