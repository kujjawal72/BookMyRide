package com.myelsadev.bookmyride

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.myelsadev.bookmyride.base.BaseFragment
import com.myelsadev.bookmyride.databinding.FragmentBookRideBinding
import com.myelsadev.bookmyride.viewmodel.MapViewModel

class BookRideFragment : BaseFragment<FragmentBookRideBinding>() {

    companion object {

        private const val PLACE_REQUEST_PICKUP = 1001
        private const val PLACE_REQUEST_DROP = 1002

        fun newInstance() = BookRideFragment().apply {
            arguments = bundleOf()
        }
    }

    override fun getLayout(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        attachToParent: Boolean
    ): FragmentBookRideBinding {
        return FragmentBookRideBinding.inflate(inflater, parent, attachToParent)
    }
    private lateinit var mViewModel: MapViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProvider(requireActivity())[MapViewModel::class.java]
    }

    private var pickupLocation: LocationModel? = null
    private var dropLocation: LocationModel? = null

    private var car = "Hatchback"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel.currentLocationData.observe(viewLifecycleOwner,{
            updatePickup(it)
        })
        binding!!.currentLocation.setOnClickListener {
            mViewModel.requestCurrentLocation()
        }
        binding!!.llPickup.setOnClickListener {
            /*val fields = listOf(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG)

            // Start the autocomplete intent.
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .build(requireContext())
            startActivityForResult(intent, PLACE_REQUEST_PICKUP)*/
            val i = Intent(requireContext(),PlaceListActivity::class.java)
            startActivityForResult(i,PLACE_REQUEST_PICKUP)
        }
        binding!!.llDrop.setOnClickListener {
            /*val fields = listOf(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG)

            // Start the autocomplete intent.
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .build(requireContext())
            startActivityForResult(intent, PLACE_REQUEST_DROP)*/
            val i = Intent(requireContext(),PlaceListActivity::class.java)
            startActivityForResult(i,PLACE_REQUEST_DROP)
        }

        binding!!.hatchback.setOnClickListener {
            car = "Hatchback"
            binding!!.hatchback.setBackgroundResource(R.drawable.shape_selected)
            binding!!.suv.setBackgroundResource(0)
            binding!!.sedan.setBackgroundResource(0)
        }
        binding!!.suv.setOnClickListener {
            car = "SUV"
            binding!!.hatchback.setBackgroundResource(0)
            binding!!.suv.setBackgroundResource(R.drawable.shape_selected)
            binding!!.sedan.setBackgroundResource(0)
        }
        binding!!.sedan.setOnClickListener {
            car = "Sedan"
            binding!!.hatchback.setBackgroundResource(0)
            binding!!.suv.setBackgroundResource(0)
            binding!!.sedan.setBackgroundResource(R.drawable.shape_selected)
        }
        binding!!.rideNow.setOnClickListener {
            if (pickupLocation == null){
                Toast.makeText(requireContext(), "Select pickup", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (dropLocation == null){
                Toast.makeText(requireContext(), "Select drop", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            MaterialDialog(requireContext())
                .title(text = "Booking Confirmed!!")
                .message(text = "Your Booking has been confirmed,\nBooking Details:\nPickup: ${pickupLocation!!.address}\nDrop: ${dropLocation!!.address}\nPickup time: Now\nCar: $car")
                .positiveButton {
                    it.dismiss()
                }.show()
        }

        binding!!.rideLater.setOnClickListener {
            if (pickupLocation == null){
                Toast.makeText(requireContext(), "Select pickup", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (dropLocation == null){
                Toast.makeText(requireContext(), "Select drop", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            MaterialDialog(requireContext())
                .title(text = "Booking Confirmed!!")
                .message(text = "Your Booking has been confirmed,\nBooking Details:\nPickup: ${pickupLocation!!.address}\nDrop: ${dropLocation!!.address}\nPick up time: later\n" +
                        "Car: $car")
                .positiveButton {
                    it.dismiss()
                }.show()
        }

    }

    private fun updatePickup(it: LocationModel?) {
        it.also { pickupLocation = it }
        binding!!.txtPickupLocation.text = pickupLocation?.address
    }

    private fun updateDrop(it: LocationModel?) {
        dropLocation = it
        binding!!.txtDropLocation.text = dropLocation?.address
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PLACE_REQUEST_PICKUP) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    data?.let {
                        updatePickup(it.getParcelableExtra("location"))
                    }
                    /*data?.let {
                        val place = Autocomplete.getPlaceFromIntent(data)
                        updatePickup(LocationModel(place.latLng.latitude,place.latLng.longitude,place.name))

                    }*/
                }
                /*AutocompleteActivity.RESULT_ERROR -> {
                    // TODO: Handle the error.
                    data?.let {
                        val status = Autocomplete.getStatusFromIntent(data)
                        Toast.makeText(requireContext(), "$status", Toast.LENGTH_SHORT).show()
                        Log.d("TAG", "onActivityResult: $status")
                    }
                }*/
                Activity.RESULT_CANCELED -> {
                    // The user canceled the operation.
                }
            }
            return
        }else if (requestCode == PLACE_REQUEST_DROP){
            when (resultCode) {
                Activity.RESULT_OK -> {
                    data?.let {
                        updateDrop(it.getParcelableExtra("location"))
                    }
                    /*data?.let {
                        val place = Autocomplete.getPlaceFromIntent(data)
                        updateDrop(LocationModel(place.latLng.latitude,place.latLng.longitude,place.name))
                    }*/
                }
                /*AutocompleteActivity.RESULT_ERROR -> {
                    // TODO: Handle the error.
                    data?.let {
                        val status = Autocomplete.getStatusFromIntent(data)
                        Toast.makeText(requireContext(), "$status", Toast.LENGTH_SHORT).show()

                    }
                }*/
                Activity.RESULT_CANCELED -> {
                    // The user canceled the operation.
                }
            }
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


}