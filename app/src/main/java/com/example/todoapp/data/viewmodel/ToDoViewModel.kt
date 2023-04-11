package com.example.todoapp.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.ToDoDatabase
import com.example.todoapp.data.models.ToDoData
import com.example.todoapp.data.repository.ToDoRepository
import com.example.todoapp.fragment.list.ListFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ToDoViewModel(application: Application):AndroidViewModel(application) {

    private val toDoDao= ToDoDatabase.getDatabase(application).toDoDao()

    private val repository:ToDoRepository

     val getAllData : LiveData<List<ToDoData>>

     val sortByHighPriority : LiveData<List<ToDoData>>
     val sortByLowPriority : LiveData<List<ToDoData>>



    init {
        repository=ToDoRepository(toDoDao)
        getAllData=repository.getAllData
        sortByHighPriority=repository.sortByHighPriority
        sortByLowPriority=repository.sortByLowPriority
    }

    fun insertData(toDoData: ToDoData){
        viewModelScope.launch (Dispatchers.IO){
            repository.insertData(toDoData)
        }
    }

    fun updatedata(toDoData: ToDoData){
        viewModelScope.launch(Dispatchers.IO){
            repository.updatedata(toDoData)
        }
    }

    fun deletedata(toDoData: ToDoData){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteItem(toDoData)
        }
    }

    fun deleteAll(){
        viewModelScope.launch (Dispatchers.IO){
            repository.deleteAll()
        }
    }

    fun searchDatabase(searchQuery:String):LiveData<List<ToDoData>>{
        return repository.searchDatabase(searchQuery)
    }
}