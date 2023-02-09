package com.example.selfzen.activity

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.example.selfzen.R
import com.example.selfzen.model.Item
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add_item.*

private const val CAMERA_REQUEST_CODE=101
class AddItem : AppCompatActivity() {

    private lateinit var mobileNo : TextView
    private lateinit var codeScanner: CodeScanner

    private lateinit var ItemName : TextView
    private lateinit var ItemPrice : TextView
    private lateinit var ItemQty : EditText

    private lateinit var dbRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)
        setupPermissions()
        codeScanner()

        mobileNo = findViewById(R.id.txt_mobileno)
        val MobileNo = intent.getStringExtra("MobileNo")
        mobileNo.text = ""+MobileNo
        ItemName = findViewById(R.id.tv_textview)
        val proName = intent.getStringExtra("proName")
        ItemName.text = ""+proName

        ItemQty = findViewById(R.id.txt_qty)

        dbRef = FirebaseDatabase.getInstance().getReference("Cart")

        btn_next.setOnClickListener{

            insertProductData()

        }

    }

    private fun insertProductData() {

        val MobileNo = mobileNo.text.toString()
        val ProName = ItemName.text.toString()
        val Price = ItemPrice.text.toString()
        val ProQty = ItemQty.text.toString()

        if(Price.isEmpty()){
            ItemPrice.error = "Please Scan Price Tage"
        }
        if(ProQty.isEmpty()){
            ItemQty.error = "Please Enter Quantity"
        }else {

            val UnitPrice = Price.toInt()
            val Qty = ProQty.toInt()
            val ProPrice = UnitPrice * Qty
            val TotalPrice = ProPrice.toString()

            val CartID = dbRef.push().key!!
            val item = Item(CartID, MobileNo, ProName, TotalPrice, ProQty)
            LoadPage()
            dbRef.child(CartID).setValue(item)
                .addOnCompleteListener {
                    Toast.makeText(this, "Data Inserted Successfully", Toast.LENGTH_LONG).show()

                }.addOnFailureListener { err ->
                    Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
                }
        }
    }

    private fun LoadPage(){
        val intent = Intent(this,AddItemName::class.java)
        startActivity(intent)
    }


    private fun codeScanner(){
        val scannerView = findViewById<CodeScannerView>(R.id.scanner_view)
        ItemPrice = findViewById(R.id.txt_price)

        codeScanner = CodeScanner(this , scannerView)
        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS

            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.CONTINUOUS
            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback = DecodeCallback {

                runOnUiThread{
                    ItemPrice.text = it.text
                }
            }
            errorCallback = ErrorCallback {

                runOnUiThread{
                    Log.e("Main" , "Camera initialization error: ${it.message}")
                }
            }
        }

        scannerView.setOnClickListener{
            codeScanner.startPreview()
        }
    }
    override fun onResume(){
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun setupPermissions(){
        val permission:Int = ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA)

        if(permission != PackageManager.PERMISSION_GRANTED){
            makeRequest()
        }
    }
    private fun makeRequest(){
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA),
            CAMERA_REQUEST_CODE)

    }

    override fun onRequestPermissionsResult(requestCode: Int,permissions: Array<out String>,grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            CAMERA_REQUEST_CODE ->{
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this,"You need the camera permission to be able to use this app",Toast.LENGTH_SHORT).show()
                }else{
                    //SUCCESSFULLY
                }
            }
        }
    }
}



