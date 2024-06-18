package com.lange.facilitytracker.data.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.lange.facilitytracker.MainViewModel
import com.lange.facilitytracker.R
import com.lange.facilitytracker.TypeOfWorkEnum
import com.lange.facilitytracker.data.model.Job
import com.lange.facilitytracker.databinding.ItemJobBinding
import com.lange.facilitytracker.ui.ToDoFragmentDirections

class ToDoAdapter(
    private val jobs: List<Job>,
    private val viewModel: MainViewModel,
    private val lifecycleOwner: LifecycleOwner,
): RecyclerView.Adapter<ToDoAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemJobBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemJobBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

        viewModel.address.observe(lifecycleOwner){ address ->
            Log.e("ADAPTER2", job.job_type.toString())
            val addressString = address.Adresse
            val immageResource = when (job.job_type) {
                0 -> R.drawable.icon_cleaning
                2 -> R.drawable.icon_maintenance
                4 -> R.drawable.icon_damage2
                else -> null
            }

            with(holder.binding) {
                tvStreet.text = "${splitAddress(addressString.toString())?.street} ${splitAddress(addressString.toString())?.houseNumber}"
                tvCity.text = "${splitAddress(addressString.toString())?.postalCode}, ${splitAddress(addressString.toString())?.city}"
                if (immageResource != null) {
                    ivJobicon.setImageResource(immageResource)
                }
                root.setOnClickListener {
                    viewModel.setCurrentJob(job)
                    when (job.job_type){
                        0 -> viewModel.currentTypeOfWork = TypeOfWorkEnum.cleaning
                        2 -> viewModel.currentTypeOfWork = TypeOfWorkEnum.maintenance
                        4 -> viewModel.currentTypeOfWork = TypeOfWorkEnum.damagereport
                    }
                    val direction = ToDoFragmentDirections.actionToDoFragmentToUpdateTodoFragment()
                    it.findNavController().navigate(direction)
                }
            }
        }
    }
}