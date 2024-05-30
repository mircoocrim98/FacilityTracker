package com.lange.facilitytracker.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lange.facilitytracker.MainViewModel
import com.lange.facilitytracker.data.model.Task
import com.lange.facilitytracker.databinding.ItemTaskBinding

class TaskAdapter (
private val tasks: MutableList<Task>,
private val viewModel: MainViewModel
) : RecyclerView.Adapter<TaskAdapter.TasksViewHolder>(){

    inner class TasksViewHolder ( val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TasksViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        val task = tasks[position]

        holder.binding.itemText.text = task.key
        holder.binding.itemCheckbox.isChecked = task.value

        holder.binding.itemCheckbox.setOnClickListener {
            tasks[position].value = !task.value
        }

    }

    fun addItems(newItems: List<Task>){
        val startPosition = tasks.size
        tasks.addAll(newItems)
        notifyItemRangeChanged(startPosition,newItems.size)
    }
    fun addItem(newItem: String){
        tasks.add(Task(newItem))
        notifyItemInserted(tasks.size-1)
    }

    fun getCheckedItems(): List<Task>{
        return tasks
    }

}