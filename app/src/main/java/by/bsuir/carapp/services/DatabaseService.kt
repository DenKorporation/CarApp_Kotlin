package by.bsuir.carapp.services

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.bsuir.carapp.data.Car
import by.bsuir.carapp.data.UserData
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class DatabaseService(private val uid: String?) {
    private val db = Firebase.firestore
    private val userCollection = db.collection("users")
    private val carsCollection = db.collection("cars")

    fun createUserData(): Task<Void> {
        val userData = hashMapOf(
            "firstname" to "",
            "lastname" to "",
            "birthday" to Timestamp.now(),
            "gender" to "Мужчина",
            "address" to "",
            "phone" to "",
            "carCountry" to "DE",
            "carBody" to "",
            "carDrive" to "",
            "transmission" to "",
            "favouriteCars" to listOf<String>()
        )

        return uid?.let { userCollection.document(it).set(userData) }
            ?: throw IllegalStateException("UID cannot be null")
    }

    fun updateUserData(
        firstname: String,
        lastname: String,
        birthday: Timestamp,
        gender: String,
        address: String,
        phone: String,
        carCountry: String,
        carBody: String,
        carDrive: String,
        transmission: String
    ): Task<Void> {
        val userData = mapOf(
            "firstname" to firstname,
            "lastname" to lastname,
            "birthday" to birthday,
            "gender" to gender,
            "address" to address,
            "phone" to phone,
            "carCountry" to carCountry,
            "carBody" to carBody,
            "carDrive" to carDrive,
            "transmission" to transmission
        )

        return uid?.let { userCollection.document(it).update(userData) }
            ?: throw IllegalStateException("UID cannot be null")
    }

    fun updateUserFavCars(favCars: List<String>): Task<Void> {
        return uid?.let { userCollection.document(it).update("favouriteCars", favCars) }
            ?: throw IllegalStateException("UID cannot be null")
    }

    fun deleteUserData(): Task<Void> {
        return uid?.let { userCollection.document(it).delete() }
            ?: throw IllegalStateException("UID cannot be null")
    }

    fun getUserDataRealtime(): Flow<UserData?> = callbackFlow {
        val snapshotListener =
            uid?.let {
                userCollection.document(it).addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        close(e)
                        return@addSnapshotListener
                    }
                    val userData = snapshot?.toObject<UserData?>()
                    userData?.uid = it
                    val temp = snapshot?.get("favouriteCars") as List<*>?
                    if(temp != null){
                        userData?.favCars = temp as List<String>
                    }
                    trySend(userData).isSuccess
                }
            }
        awaitClose {
            snapshotListener?.remove()
        }
    }

    fun getCarsRealtime(): Flow<List<Car>> = callbackFlow {
        val snapshotListener = carsCollection.addSnapshotListener { snapshot, e ->
            if (e != null) {
                close(e)
                return@addSnapshotListener
            }
            val cars = snapshot?.documents?.mapNotNull {
                val car = it.toObject<Car>()
                car?.uid = it.id
                car?.isFav = false
                car
            }.orEmpty()
            trySend(cars).isSuccess
        }
        awaitClose {
            snapshotListener.remove()
        }
    }
}