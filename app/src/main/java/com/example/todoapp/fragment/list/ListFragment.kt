package com.example.todoapp.fragment.list

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import androidx.recyclerview.widget.ItemTouchHelper

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager


import com.example.todoapp.R
import com.example.todoapp.data.models.ToDoData
import com.example.todoapp.data.viewmodel.ToDoViewModel
import com.example.todoapp.databinding.FragmentListBinding
import com.example.todoapp.fragment.SharedViewModel
import com.example.todoapp.fragment.list.adapter.ListAdapter
import com.example.todoapp.utils.hideKeyboard
import com.example.todoapp.utils.observeOnce
import com.google.android.material.snackbar.Snackbar
import jp.wasabeef.recyclerview.animators.LandingAnimator
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator


class ListFragment : Fragment(),SearchView.OnQueryTextListener {

    private val mToDoViewModel:ToDoViewModel by viewModels()
    private val mSharedViewModel:SharedViewModel by viewModels()

     private val adapter: ListAdapter by lazy{ ListAdapter() }

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentListBinding.inflate(inflater, container, false)
      //  val view=  inflater.inflate(R.layout.fragment_list, container, false)

        binding.lifecycleOwner=this
        binding.mSharedViewModel=mSharedViewModel


        //Recyclerview
        setupRecyclerView()

        mToDoViewModel.getAllData.observe(viewLifecycleOwner,Observer{ data->
            mSharedViewModel.checkDatabaseEmpty(data)
            adapter.setData(data)
        })

        //set Menu
       // setHasOptionsMenu(true)

        hideKeyboard(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuhost:MenuHost=requireActivity()
        menuhost.addMenuProvider(object :MenuProvider{
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.list_fragment_menu,menu)
                val search :MenuItem=menu.findItem(R.id.menu_search)
                val searchView: SearchView? =search.actionView as? SearchView
                searchView?.isSubmitButtonEnabled=true
                searchView?.setOnQueryTextListener(this@ListFragment)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when(menuItem.itemId){
                    R.id.menu_delete_all-> confirmRemoval()
                    R.id.menu_priority_high->mToDoViewModel.sortByHighPriority.observe(viewLifecycleOwner,Observer{adapter.setData(it) })
                    R.id.menu_priority_low->mToDoViewModel.sortByLowPriority.observe(viewLifecycleOwner,Observer{adapter.setData(it) })
                    android.R.id.home -> requireActivity().onBackPressed()
                }
                return true
            }


        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }




    private fun confirmRemoval() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            mToDoViewModel.deleteAll()
            Toast.makeText(
                requireContext(),
                "Successfully Removed Everything",
                Toast.LENGTH_SHORT
            ).show()

        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Delete Everything")
        builder.setMessage("Are you sure you want to remove everything ?")
        builder.create().show()
    }



    private fun setupRecyclerView(){
        val recyclerView=binding.recyclerView
        recyclerView.adapter= adapter
        recyclerView.layoutManager=StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        recyclerView.itemAnimator=SlideInUpAnimator().apply { addDuration=300 }
        swipeToDelete(recyclerView)

    }

    private fun swipeToDelete(recyclerView: RecyclerView){
        val swipeToDeleteCallback=object :SwipeToDelete(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
               val deletedItem=adapter.datalist[viewHolder.adapterPosition]
                //delete Item
                mToDoViewModel.deletedata(deletedItem)
                adapter.notifyItemRemoved(viewHolder.adapterPosition)
             //   Toast.makeText(requireContext(),"Sucessfully Removed: '${deletedItem.title}'",Toast.LENGTH_SHORT).show()
                //Restore Deleted Item
                restoreDeletedData(viewHolder.itemView,deletedItem)
            }
        }
        val itemTouchHelper=ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

    }

    private fun restoreDeletedData(view:View,deletedItem:ToDoData){
        val snackbar=Snackbar.make(view,"Deleted:'${deletedItem.title}'",Snackbar.LENGTH_LONG)
        snackbar.setAction("Undo"){
            mToDoViewModel.insertData(deletedItem)
          //  adapter.notifyItemChanged(position)
        }
        snackbar.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if(query!=null){
            searchThroughDatabase(query)
        }
        return true
    }
    override fun onQueryTextChange(query: String?): Boolean {
        if(query!=null){
            searchThroughDatabase(query)
        }
        return true
    }

    private fun searchThroughDatabase(query: String) {
        val searchQuery= "%$query%"
        mToDoViewModel.searchDatabase(searchQuery).observeOnce(viewLifecycleOwner, Observer {list->
            list?.let {
                Log.d("List Fragment","searchthroughDatabase")
                adapter.setData(it)
            }

        })

    }


}