package com.example.selfzen.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.selfzen.R
import com.example.selfzen.adapter.ItemDataAdapter
import com.example.selfzen.model.Item
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_cart_page.*

class CartPage : AppCompatActivity() {

    private lateinit var proRecyclerView: RecyclerView
    private lateinit var proList: ArrayList<Item>
    private lateinit var dbRef : DatabaseReference
    private lateinit var builder : AlertDialog.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_page)

        proRecyclerView = findViewById(R.id.rvProducts)
        proRecyclerView.layoutManager = LinearLayoutManager(this)
        proRecyclerView.setHasFixedSize(true)

        builder = AlertDialog.Builder(this)
        proList = arrayListOf<Item>()

        getProductData()
        val MobileNo = intent.getStringExtra("MobileNo")
        btn_finsh.setOnClickListener{

            builder.setTitle("Read")
                .setMessage("If you finished add items to the cart, Now you must go to the cashier and show " +
                        "your smart cart QR code. Do you want to continue ?")
                .setPositiveButton("Yes"){dialogInterface,it ->

                    startActivity(
                        Intent(this, EndShopping::class.java)
                            .putExtra("MobileNo", MobileNo.toString()))

                }.setNegativeButton("No"){dialogInterface,it ->
                        dialogInterface.cancel()
                }.show()

        }
    }

    private fun getProductData() {
        proRecyclerView.visibility = View.GONE
        image_cart.visibility = View.GONE
        text.visibility= View.GONE
        btn_finsh.visibility= View.GONE

        dbRef = FirebaseDatabase.getInstance().getReference("Cart")

        dbRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                proList.clear()
                if (snapshot.exists()){
                    for (proSnap in snapshot.children){
                        val itemdata = proSnap.getValue(Item::class.java) // GET DATA FROM FIREBASE DATABASE
                        if (itemdata != null) {
                            proList.add(itemdata)
                        }
                    }
                    val mAdapter = ItemDataAdapter(proList)
                    proRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : ItemDataAdapter.onItemClickListener{
                        override fun onItemClickListener(position: Int) {
                            val intent = Intent(this@CartPage,ItemDetail::class.java)

                            //PUT EXTRAS
                            intent.putExtra("cartID",proList[position].cartID)
                            intent.putExtra("mobileNo",proList[position].MobileNo)
                            intent.putExtra("proName",proList[position].proName)
                            intent.putExtra("proQty",proList[position].proQty)
                            intent.putExtra("totalPrice",proList[position].totalPrice)
                            startActivity(intent)
                        }

                    })

                    proRecyclerView.visibility = View.VISIBLE
                    text.visibility= View.VISIBLE
                    image_cart.visibility = View.VISIBLE
                    btn_finsh.visibility= View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}