package com.example.roomdatabase

import android.app.AlertDialog
import android.app.Dialog
import android.content.ClipData
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.R
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.roomdatabase.databinding.ActivityMainBinding
import com.example.roomdatabase.databinding.DialogUpdateBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    var binding : ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        val employeeDao = (application as EmployeeApp).db.employeeDao()
        binding?.btnAdd?.setOnClickListener{
            addRecord(employeeDao)
        }

        //Loading data tasks should be done in the background
        lifecycleScope.launch{
            employeeDao.fetchAllEmployees().collect{
                val list = ArrayList(it)
                setUpRecyclerViewData(list,employeeDao)
            }
        }
    }
    private fun addRecord(employeeDAO : EmployeeDAO){
        val name = binding?.etName?.text.toString()
        val email = binding?.etEmailId?.text.toString()
        if(name.isNotEmpty() && email.isNotEmpty()){
            //These tasks should be done in the background
            lifecycleScope.launch{
               employeeDAO.insert(EmployeeEntity(name=name,email=email))
                //ApplicationContext passed since were in the background
               Toast.makeText(applicationContext,"Record saved",Toast.LENGTH_LONG).show()
                binding?.etName?.text?.clear()
                binding?.etEmailId?.text?.clear()
            }
        }else{
            Toast.makeText(applicationContext,"Name or email can not be blank",Toast.LENGTH_LONG).show()
        }
    }

    private fun setUpRecyclerViewData(employeeList : ArrayList<EmployeeEntity>,employeeDAO: EmployeeDAO){
        if(employeeList.isNotEmpty()){
           val itemAdapter = ItemAdapter(employeeList,
           {
               updateID ->
               updateRecordDialog(updateID,employeeDAO)
           },
               {
                   deleteID ->
                   deleteRecordDialog(deleteID,employeeDAO)
               }
           )
            binding?.rvItemsList?.layoutManager = LinearLayoutManager(this)
            binding?.rvItemsList?.adapter = itemAdapter
            binding?.rvItemsList?.visibility = View.VISIBLE
            binding?.tvName?.visibility = View.VISIBLE
            binding?.tvNoRecordsAvailable?.visibility = View.GONE
        }else{
            binding?.rvItemsList?.visibility = View.GONE
            binding?.tvName?.visibility = View.GONE
            binding?.tvNoRecordsAvailable?.visibility = View.VISIBLE
        }
    }

    private fun updateRecordDialog(id:Int,employeeDAO: EmployeeDAO){
        val updateDialog = Dialog(this,com.example.roomdatabase.R.style.Theme_Dialog_Custom)
        updateDialog.setCancelable(false)
        //Inflate the layout file for the update dialog
        val binding = DialogUpdateBinding.inflate(layoutInflater)
        updateDialog.setContentView(binding.root)
        lifecycleScope.launch{
            employeeDAO.fetchEmployeeById(id).collect{
                binding.etUpdateName.setText(it.name)
                binding.etUpdateEmailId.setText(it.email)
            }
        }
        binding.tvUpdate.setOnClickListener {
            val name = binding.etUpdateName.text.toString()
            val email = binding.etUpdateEmailId.text.toString()
            if(name.isNotEmpty() && email.isNotEmpty()){
                //When making changes the ID has to be passed
                lifecycleScope.launch {
                    employeeDAO.update(EmployeeEntity(id = id ,name = name,email = email))
                    Toast.makeText(applicationContext,"Record updated",Toast.LENGTH_LONG).show()
                    updateDialog.dismiss()
                }
            }else{
                Toast.makeText(applicationContext,"Name or email can not be blank",Toast.LENGTH_LONG).show()
            }
        }
        binding.tvCancel.setOnClickListener{
            updateDialog.dismiss()
        }
        updateDialog.show()
    }

    private fun deleteRecordDialog(id:Int,employeeDAO: EmployeeDAO){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Record")
        builder.setIcon(com.example.roomdatabase.R.drawable.ic_baseline_delete)
        builder.setPositiveButton("Yes"){
            dialogInterface,_ ->
            lifecycleScope.launch{
                employeeDAO.delete(EmployeeEntity(id))
                Toast.makeText(applicationContext,"Record deleted successfully",Toast.LENGTH_LONG).show()
            }
            dialogInterface.dismiss()
        }
        builder.setNegativeButton("No"){
            dialogInterface,_ ->
            dialogInterface.dismiss()
        }
        val alertDialog : AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

}