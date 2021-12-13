package com.myelsadev.bookmyride

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.utils.MDUtil.textChanged
import com.myelsadev.bookmyride.base.BaseActivity
import com.myelsadev.bookmyride.databinding.ActivityPlaceListBinding
import com.myelsadev.bookmyride.databinding.ItemPlaceBinding

class PlaceListActivity : BaseActivity<ActivityPlaceListBinding>() {

    private val addresses = arrayListOf<LocationModel>()

    override fun getLayout(inflater: LayoutInflater): ActivityPlaceListBinding {
        return ActivityPlaceListBinding.inflate(inflater)
    }


    override fun bindViews(savedInstanceState: Bundle?) {
        addresses.add(LocationModel(0.0, 0.0, "Dyu Art Cafe, Koramangala"))
        addresses.add(LocationModel(0.0, 0.0, "Mango Vietnamese Coffee, Jayanagar"))
        addresses.add(LocationModel(0.0, 0.0, "Third Wave Coffee Roasters, Bangalore"))
        addresses.add(LocationModel(0.0, 0.0, "Hole Lotta Love Cafe, Koramangala"))
        addresses.add(LocationModel(0.0, 0.0, "Cafe Down The Alley, Banashankari"))
        addresses.add(LocationModel(0.0, 0.0, "Two Friends Cauldron, JP Nagar"))
        addresses.add(LocationModel(0.0, 0.0, "Glen’s Bakehouse, Koramangala"))

        binding.placeList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@PlaceListActivity)
            adapter = PlaceAdapter(addresses){
                val i = Intent()
                i.putExtra("location",it)
                setResult(Activity.RESULT_OK,i )
                finish()
            }
        }
        binding.back.setOnClickListener {
            onBackPressed()
        }
        binding.searchQuery.textChanged { newText->
            val list = arrayListOf<LocationModel>()
            addresses.forEach {
                if (it.address.contains("$newText",true)){
                    list.add(it)
                }
            }
            (binding.placeList.adapter as PlaceAdapter).updateData(list)
        }
    }

    class PlaceAdapter(
        var addresses: ArrayList<LocationModel>,
        private val itemClick: (LocationModel) -> Unit
    ) : RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder>() {

        class PlaceViewHolder(val binding: ItemPlaceBinding) :
            RecyclerView.ViewHolder(binding.root) {
            fun bind(locationModel: LocationModel) {
                binding.placeName.text = locationModel.address
            }
        }

        fun updateData(addresses: ArrayList<LocationModel>){
            this.addresses = addresses
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
            val binding = ItemPlaceBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return PlaceViewHolder(binding)
        }

        override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
            holder.bind(addresses[position])
            holder.binding.root.setOnClickListener {
                itemClick(addresses[position])
            }
        }

        override fun getItemCount(): Int = addresses.size
    }
}