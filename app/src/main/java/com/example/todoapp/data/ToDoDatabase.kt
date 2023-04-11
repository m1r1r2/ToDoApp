package com.example.todoapp.data

import android.annotation.SuppressLint
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.todoapp.data.models.ToDoData

@Database(entities = [ToDoData::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
 abstract class ToDoDatabase :RoomDatabase(){

  abstract fun toDoDao():ToDoDao

  companion object{

      @Volatile
      private var  Instance :ToDoDatabase?=null

      @SuppressLint("SuspiciousIndentation")
      fun getDatabase(context: Context) :ToDoDatabase{
          val tempinstance= Instance
          if(tempinstance!=null){
              return  tempinstance
          }
          synchronized(this){
              val instance=Room.databaseBuilder(context.applicationContext,ToDoDatabase::class.java,"todo_database").build()

           Instance= instance
              return instance
          }
      }
  }

}