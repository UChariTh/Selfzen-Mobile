package com.example.selfzen.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.*
import com.example.selfzen.R
import com.vishnusivadas.advanced_httpurlconnection.PutData
import kotlinx.android.synthetic.main.activity_add_item_name.*

private const val CAMERA_REQUEST_CODE=101
class AddItemName : AppCompatActivity() {

    private lateinit var Bill_No : TextView
    private lateinit var codeScanner: CodeScanner
    private lateinit var Pro_ID : TextView
    private lateinit var Qty : EditText


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item_name)

        setupPermissions()
        codeScanner()

        Bill_No = findViewById(R.id.billNo)
        Qty = findViewById(R.id.qty)
        Pro_ID = findViewById(R.id.pro_id)

        val Bill_ID = intent.getStringExtra("Bill_ID")
        Bill_No.text = "Bill No : "+Bill_ID

        add.setOnClickListener{

            val Item_ID = Pro_ID.getText().toString()
            val Qty = Qty.getText().toString()


            if (Bill_ID !="" && Item_ID !="" && Qty !="") {
                val handler = Handler(Looper.getMainLooper())
                handler.post {
                    val field = arrayOfNulls<String>(3)
                    field[0] = "bill_id"
                    field[1] = "item_id"
                    field[2] = "qty"

                    val data = arrayOfNulls<String>(3)
                    data[0] = Bill_ID
                    data[1] = Item_ID
                    data[2] = Qty

                    val putData =
                        PutData("http://192.168.8.187//SelfzenFiles/addItemBill.php", "POST", field, data)
                    if (putData.startPut()) {
                        if (putData.onComplete()) {
                            val result: String = putData.getResult()
                            if (result == "Item Add Success") {
                                Toast.makeText(applicationContext, result, Toast.LENGTH_SHORT)
                                    .show()
                                Pro_ID.text = "Product ID Empty"


                            } else {
                                Toast.makeText(applicationContext, result, Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }
                }
            } else {
                Toast.makeText(applicationContext, "All Fields are Required", Toast.LENGTH_SHORT)
                    .show()
            }


        }
        cart.setOnClickListener{



                    startActivity(
                        Intent(this, EndShopping::class.java)
                            .putExtra("Bill_ID", Bill_ID.toString()))





        }
    }

    private fun codeScanner() {
        val scannerView = findViewById<CodeScannerView>(R.id.scanner_view)


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
                    Pro_ID.text = it.text
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
                    Toast.makeText(this,"You need the camera permission to be able to use this app",
                        Toast.LENGTH_SHORT).show()
                }else{
                    //SUCCESSFULLY
                }
            }
        }
    }


}