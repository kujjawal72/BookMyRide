package com.myelsadev.bookmyride

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LocationModel(val lat: Double, val lang: Double, val address: String):Parcelable