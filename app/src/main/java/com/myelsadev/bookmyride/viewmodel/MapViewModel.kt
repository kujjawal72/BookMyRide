package com.myelsadev.bookmyride.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.myelsadev.bookmyride.LocationModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MapViewModel: ViewModel() {

    private val mapAPIs = MapAPIs.getInstance()
    private val compositeDisposable = CompositeDisposable()

    val processingResponse = MutableLiveData<Boolean>()

    private val _requestCurrentLocation = MutableLiveData<Boolean>()
    val requestCurrentLocation: LiveData<Boolean> = _requestCurrentLocation

    private val _currentLocationData = MutableLiveData<LocationModel?>()
    val currentLocationData: LiveData<LocationModel?> = _currentLocationData

    fun requestCurrentLocation(){
        _requestCurrentLocation.value = true
    }

    fun setCurrentLocation(locationData: LocationModel?){
        _currentLocationData.value = locationData
    }


    private val TAG = "MapViewModel"



    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}