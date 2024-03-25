package by.bsuir.carapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.bsuir.carapp.data.Car
import by.bsuir.carapp.data.UserData
import by.bsuir.carapp.services.AuthenticationService
import by.bsuir.carapp.services.DatabaseService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CarListViewModel : ViewModel() {
    private val _db = DatabaseService(null)
    private val _user = AuthenticationService().getSignedInUser()
    private val _dbUid = DatabaseService(_user?.uid)

    private val _cars = MutableStateFlow<List<Car>>(emptyList())
    val cars: StateFlow<List<Car>> = _cars

    private val _userData = MutableStateFlow<UserData?>(null)
    val userData: StateFlow<UserData?> = _userData

    init {
        viewModelScope.launch {
            _db.getCarsRealtime().collect { listOfCars ->
                _cars.value = listOfCars
            }
        }
        viewModelScope.launch {
            _dbUid.getUserDataRealtime().collect { userData ->
                _userData.value = userData
            }
        }
    }
}