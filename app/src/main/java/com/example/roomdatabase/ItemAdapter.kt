package com.example.roomdatabase

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdatabase.databinding.ItemsRowBinding

//RecyclerView doesn't inherit from onClickListeners and that is why the listeners have to be passed as parameters
//This part{ private val updateListener:(id:Int)} is the method signature of the lambda,the parameters of the method body.
//The Unit part is describing what method body x{Which is user designed} is supposed to return
class ItemAdapter(private val items : ArrayList<EmployeeEntity>,
                  private val updateListener:(id:Int) -> Unit,
                  private val deleteListener:(id:Int) -> Unit
                  ) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>()
{
    //The viewHolder
    class ItemViewHolder(binding: ItemsRowBinding) : RecyclerView.ViewHolder(binding.root){
        //Setup the different item views
        val llMain = binding.llMain
        val tvName = binding.tvName
        val tvEmail = binding.tvEmail
        val ivEdit = binding.ivEdit
        val ivDelete = binding.ivDelete
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(ItemsRowBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        //Item view is an implicit variable in the ViewHolder(ItemViewHolder) class
        val context = holder.itemView.context
        val item = items[position]

        holder.tvName.text = item.name
        holder.tvEmail.text = item.email

        //At every second object
        if(position % 2 == 0){
            holder.llMain.setBackgroundColor(ContextCompat.getColor(holder.itemView.context,R.color.light_grey))
        }else{
            holder.llMain.setBackgroundColor(ContextCompat.getColor(holder.itemView.context,R.color.white))
        }

        //ItemOnClickListeners
        holder.ivEdit.setOnClickListener{
            //On which of the items was this imageView clicked?
            //Calling the lambda and passing the parameters required
            updateListener(item.id)
        }
        holder.ivDelete.setOnClickListener{
            //On which of the items was this imageView clicked?
            //Calling the lambda and passing the parameters required
            deleteListener(item.id)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

}