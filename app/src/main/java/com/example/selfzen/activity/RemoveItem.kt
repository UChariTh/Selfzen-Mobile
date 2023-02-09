package com.example.selfzen.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.FontResourcesParserCompat
import com.example.selfzen.R
import com.google.firebase.database.FirebaseDatabase

    private lateinit var itemName : TextView
    private lateinit var itemQty :TextView
    private lateinit var totalPrice:TextView
    private  lateinit var btn_remove: Button
    private lateinit var itemCode:TextView


class ItemDetail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)

        itemName = findViewById(R.id.txt_RName)
        itemCode = findViewById(R.id.txt_code)
        itemQty = findViewById(R.id.txt_RQty)
        totalPrice = findViewById(R.id.txt_RPrice)
        btn_remove =findViewById(R.id.btn_remove)

        setValuesToViews()

        btn_remove.setOnClickListener{
            removeItem(
                intent.getStringExtra("cartID").toString()
            )
        }

    }

    private fun removeItem( id:String ) {

        val dbRef = FirebaseDatabase.getInstance().getReference("Cart").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this,"Item Removed Successful",Toast.LENGTH_LONG).show()
            val intent = Intent(this,CartPage::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{ error ->

            Toast.makeText(this,"Item Removed Unsuccessful",Toast.LENGTH_LONG).show()
        }
    }


    private fun setValuesToViews(){

        itemCode.text=intent.getStringExtra("cartID")
        itemName.text=intent.getStringExtra("proName")
        itemQty.text=intent.getStringExtra("proQty")
        totalPrice.text=intent.getStringExtra("totalPrice")
    }
}