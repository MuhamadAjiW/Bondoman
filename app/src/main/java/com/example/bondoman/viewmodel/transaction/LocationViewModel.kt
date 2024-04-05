package com.example.bondoman.viewmodel.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LocationViewModel : ViewModel() {
    private val _location = MutableLiveData<Pair<Double?, Double?>>(Pair(null, null))

    val location: LiveData<Pair<Double?, Double?>>
        get() = _location

    fun setLoc(lat: Double?, lng: Double?) {
        _location.value = Pair(lat, lng)
    }
}