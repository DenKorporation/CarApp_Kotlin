package by.bsuir.carapp.ui.components

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import by.bsuir.carapp.data.Car
import by.bsuir.carapp.viewmodel.CarListViewModel

@Composable
fun CarList(onlyFavourites: Boolean, viewModel: CarListViewModel, onCarSelected: (Car) -> Unit) {

    val cars = viewModel.cars.collectAsState().value
    val userData = viewModel.userData.collectAsState().value

    LazyColumn(modifier = Modifier.padding(bottom = 15.dp)) {
        items(cars) { car ->
            CarTile(onlyFavourites = onlyFavourites, car = car, userData = userData, onCarSelected)
        }
    }
}