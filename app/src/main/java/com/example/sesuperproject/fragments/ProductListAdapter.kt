package com.example.sesuperproject.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sesuperproject.R
import com.example.sesuperproject.models.Item
import com.example.sesuperproject.models.TransactionDetail

class ProductListAdapter (private val itemData: List<Item>, private val detailData: List<TransactionDetail>) : RecyclerView.Adapter<ProductListAdapter.ViewHolder>(){

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){

        val productName: TextView
        val productQty: TextView
        val productPrice: TextView

        init{
            productName = view.findViewById(R.id.lblProductName)
            productQty = view.findViewById(R.id.lblProductQty)
            productPrice = view.findViewById(R.id.lblProductPrice)
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_product, parent, false)

        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sortedTransactionData = detailData.sortedBy {
            it.itemId
        }

        val sortedItemData = itemData.sortedBy {
            it.itemId
        }

        holder.productName.text = sortedItemData[position].itemName
        holder.productQty.text = sortedTransactionData[position].quantity.toString()
        holder.productPrice.text = sortedItemData[position].itemPrice.toString()
    }

    override fun getItemCount(): Int {
        return itemData.size
    }
}