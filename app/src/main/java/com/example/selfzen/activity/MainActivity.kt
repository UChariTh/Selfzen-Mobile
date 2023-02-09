package com.example.selfzen.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.selfzen.R
import com.vishnusivadas.advanced_httpurlconnection.PutData
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var mobileNo : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mobileNo= findViewById(R.id.txt_TP)

        val regPageLink = findViewById<TextView>(R.id.txt_register)
        val btn_start = findViewById<Button>(R.id.btn_sign)

        regPageLink.setOnClickListener{
            val intent = Intent(this, RegistrationPage::class.java)
            startActivity(intent)
        }
        btn_start.setOnClickListener{
            val Bill_ID = (0..100).random()
            val MobileNo = mobileNo.text.toString()
            val Satatus = "Unpaid"

            if(MobileNo.isEmpty()){
                mobileNo.error ="Please Enter Registered Mobile No"
            }else {

                if (MobileNo != "") {
                    val handler = Handler(Looper.getMainLooper())
                    handler.post {
                        val field = arrayOfNulls<String>(3)
                        field[0] = "bill_id"
                        field[1] = "mobile_num"
                        field[2] = "status"

                        val data = arrayOfNulls<String>(3)
                        data[0] = Bill_ID.toString()
                        data[1] = MobileNo
                        data[2] = Satatus

                        val putData =
                            PutData("http://192.168.8.187//SelfzenFiles/generate_Bill_ID.php", "POST", field, data)
                        if (putData.startPut()) {
                            if (putData.onComplete()) {
                                val result: String = putData.getResult()
                                if (result == "Sign Up Success") {
                                    Toast.makeText(applicationContext, result, Toast.LENGTH_SHORT)
                                        .show()
                                    val intent = Intent(applicationContext, MainActivity::class.java)
                                    startActivity(intent)
                                    finish()
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

                startActivity(
                    Intent(this, AddItemName::class.java)
                        .putExtra("Bill_ID", Bill_ID.toString())
                )
            }
        }
    }


}


