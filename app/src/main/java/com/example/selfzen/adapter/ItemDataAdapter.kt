package com.example.selfzen.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.selfzen.R
import com.example.selfzen.model.Item
import org.w3c.dom.Text

class ItemDataAdapter(private var proList:ArrayList<Item>) : RecyclerView.Adapter<ItemDataAdapter.ViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClickListener(position: Int)
    }

    fun setOnItemClickListener(clickListener:onItemClickListener){
        mListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.iterm_cart,parent,false)
        return ViewHolder(itemView,mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentPro = proList[position]
        holder.name.text = currentPro.proName
        holder.price.text = currentPro.totalPrice
        holder.qty.text = currentPro.proQty

    }

    override fun getItemCount(): Int {
        return proList.size
    }

    class ViewHolder (itemView: View,clickListener : onItemClickListener) : RecyclerView.ViewHolder(itemView){

        val name : TextView = itemView.findViewById(R.id.txt_itemName)
        val price : TextView = itemView.findViewById(R.id.txt_itemPrice)
        val qty : TextView = itemView.findViewById(R.id.txt_itemQty)


        init {
            itemView.setOnClickListener{
                clickListener.onItemClickListener(adapterPosition)
            }
        }
    }
}