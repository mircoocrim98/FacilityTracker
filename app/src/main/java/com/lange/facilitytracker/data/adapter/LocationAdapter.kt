package com.lange.facilitytracker.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.lange.facilitytracker.MainViewModel
import com.lange.facilitytracker.data.model.Address
import com.lange.facilitytracker.databinding.ItemAdressBinding
import com.lange.facilitytracker.ui.LocationpickerFragmentDirections

class LocationAdapter(
    private val addresses: List<Address>,
    private val viewModel: MainViewModel
) : RecyclerView.Adapter<LocationAdapter.LocationViewHolder>(){

    inner class LocationViewHolder ( val binding: ItemAdressBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val binding = ItemAdressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LocationViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return addresses.size
    }
    data class AddressModel(val street: String, val houseNumber: String, val postalCode: String, val city: String)

    fun splitAddress(address: String): AddressModel? {
        // Regular expression to match the address components
        val regex = """(.+?)\s(\d+),\s([A-Z]{2})-(\d+)\s(.+)""".toRegex()

        // Match the regex with the address
        val matchResult = regex.matchEntire(address)

        // If the regex matches, destructure the matched groups
        return matchResult?.destructured?.let { (street, houseNumber, _, postalCode, city) ->
            AddressModel(street, houseNumber, postalCode, city)
        }
    }
    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val address = addresses[position]

        with(holder.binding){
            tvStreet.text = "${splitAddress(address.Adresse.toString())?.street} ${splitAddress(address.Adresse.toString())?.houseNumber}"
            tvCity.text = "${splitAddress(address.Adresse.toString())?.postalCode}, ${splitAddress(address.Adresse.toString())?.city}"

            root.setOnClickListener {
                viewModel.setCurrentAddress(address)
                val direction = LocationpickerFragmentDirections.toCompleteWork()
                it.findNavController().navigate(direction)
            }
        }
    }
}
