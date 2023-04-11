package com.example.todoapp.fragment

import android.view.View
import android.widget.Spinner
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.example.todoapp.R
import com.example.todoapp.data.models.Priority
import com.example.todoapp.data.models.ToDoData
import com.example.todoapp.fragment.list.ListFragmentDirections
import com.google.android.material.floatingactionbutton.FloatingActionButton

class BindingsAdapter {

    companion object{
        @BindingAdapter("android:navigateToAddFragment")
        @JvmStatic
        fun navigateToAddFragment(view :FloatingActionButton, navigate:Boolean){
            view.setOnClickListener{
                if(navigate){
                    view.findNavController().navigate((R.id.action_list_fragment_to_addFragment))
                }
            }

        }

        @BindingAdapter("android:emptydatabase")
        @JvmStatic
        fun emptyDatabase(view:View,emptydatabase:MutableLiveData<Boolean>){
            when(emptydatabase.value){
                true->view.visibility=View.VISIBLE
                false->view.visibility=View.INVISIBLE
                else -> {}
            }
        }
        @BindingAdapter("android:parsePriorityToInt")
        @JvmStatic
        fun parsePriorityToInt(view:Spinner,priority: Priority){
            when(priority){
                Priority.HIGH->{view.setSelection(0)}
                Priority.MEDIUM->{view.setSelection(1)}
                Priority.LOW->{view.setSelection(2)}

            }
        }
        @BindingAdapter("android:parsePriorityColor")
        @JvmStatic
        fun parsePriorityColor(cardView: CardView,priority: Priority){
            when(priority){
                Priority.HIGH->{cardView.setCardBackgroundColor(cardView.context.getColor(R.color.red))}
                Priority.MEDIUM->{cardView.setCardBackgroundColor(cardView.context.getColor(R.color.yellow))}
                Priority.LOW->{cardView.setCardBackgroundColor(cardView.context.getColor(R.color.green))}
            }
        }
        @BindingAdapter("android:sendDataToUpdateFragment")
        @JvmStatic
        fun sendDataToUpdateFragment(view: ConstraintLayout,currentitem:ToDoData){
            view.setOnClickListener(){
                val action =ListFragmentDirections.actionListFragmentToUpdateFragment(currentitem)
                view.findNavController().navigate(action)
            }
        }
    }
}