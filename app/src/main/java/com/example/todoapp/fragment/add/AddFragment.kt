package com.example.todoapp.fragment.add

import android.os.Bundle

import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R

import com.example.todoapp.data.models.ToDoData
import com.example.todoapp.data.viewmodel.ToDoViewModel
import com.example.todoapp.databinding.FragmentAddBinding

import com.example.todoapp.fragment.SharedViewModel

class AddFragment : Fragment() {

    private val toDoViewModel:ToDoViewModel by viewModels()
    private val msharedViewModel:SharedViewModel by viewModels()

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentAddBinding.inflate(inflater, container, false)

        //set Menu
       // setHasOptionsMenu(true)

        binding.prioritiesSpinner.onItemSelectedListener=msharedViewModel.listener

                return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.add_fragment_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if (menuItem.itemId == R.id.menu_add) {
                    insertDaoToDb()
                } else if (menuItem.itemId == android.R.id.home) {
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }




    private fun insertDaoToDb() {
        val mTitle=binding.titleEt.text.toString()
        val priority=binding.prioritiesSpinner.selectedItem.toString()
        val mdescription=binding.descriptionEt.text.toString()

        val validation=msharedViewModel.verifydatafromuser(mTitle,mdescription)
        if(validation){
            //Insert Data
            val newdata=ToDoData(
                0,
                mTitle,
                msharedViewModel.ParsePriority(priority),
                mdescription
            )
            toDoViewModel.insertData(newdata)
            Toast.makeText(requireContext(),"successfully added",Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_addFragment_to_list_fragment)
        }
        else
        {
            Toast.makeText(requireContext(),"fill all the entries",Toast.LENGTH_LONG).show()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}