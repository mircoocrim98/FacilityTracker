package com.lange.facilitytracker.data.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.lange.facilitytracker.MainViewModel
import com.lange.facilitytracker.R
import com.lange.facilitytracker.data.model.Job
import com.lange.facilitytracker.databinding.ItemJobdoneBinding

class OverviewAdapter(
    private val jobs: List<Job>,
    private val viewModel: MainViewModel,
    private val lifecycleOwner: LifecycleOwner,
): RecyclerView.Adapter<OverviewAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemJobdoneBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemJobdoneBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return jobs.size
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



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val job = jobs[position]
        viewModel.getAddressByIdFromDB(job.id_address!!)
        val addressString = job.address?.Adresse

        val immageResource = when (job.job_type) {
            1 -> R.drawable.icon_cleaning
            3 -> R.drawable.icon_maintenance
            5 -> R.drawable.icon_damage2
            else -> null
        }
        with(holder.binding) {
            tvStreetJobdone.text = "${splitAddress(addressString.toString())?.street} ${splitAddress(addressString.toString())?.houseNumber}"
            tvCityJobdone.text = "${splitAddress(addressString.toString())?.postalCode}, ${splitAddress(addressString.toString())?.city}"
            if (immageResource != null) {
                ivJobiconJobdone.setImageResource(immageResource)
            }
        }
    }
}