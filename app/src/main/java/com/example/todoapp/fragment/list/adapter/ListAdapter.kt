package com.example.todoapp.fragment.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.data.models.ToDoData
import com.example.todoapp.databinding.RowLayoutBinding

class ListAdapter : RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

     var datalist= emptyList<ToDoData>()

    class MyViewHolder( private val binding:RowLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(toDoData: ToDoData){
            binding.todoData=toDoData
            binding.executePendingBindings()

        }
        companion object{
            fun  from(parent: ViewGroup) : MyViewHolder {
                val layoutInflater=LayoutInflater.from(parent.context)
                val binding=RowLayoutBinding.inflate(layoutInflater,parent,false)
                return  MyViewHolder(binding)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)

    }

    override fun getItemCount(): Int {
        return  datalist.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentitem=datalist[position]
        holder.bind(currentitem)

    }

    fun setData(toDoData: List<ToDoData>){
        val toDoDiffUtil=ToDoDiffUtil(datalist,toDoData)
        val toDoDiffResult=DiffUtil.calculateDiff(toDoDiffUtil)
        this.datalist = toDoData
        toDoDiffResult.dispatchUpdatesTo(this)

    }

}