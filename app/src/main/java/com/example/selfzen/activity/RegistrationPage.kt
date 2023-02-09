package com.example.selfzen.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.selfzen.R
import com.vishnusivadas.advanced_httpurlconnection.PutData
import kotlinx.android.synthetic.main.activity_registration_page.*


class RegistrationPage : AppCompatActivity() {

    private lateinit var LName: EditText
    private lateinit var FName : EditText
    private lateinit var Email : EditText
    private lateinit var TPNo : EditText
    private lateinit var Address : EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration_page)

        FName = findViewById(R.id.f_name)
        LName = findViewById(R.id.l_name)
        Email = findViewById(R.id.email)
        TPNo= findViewById(R.id.tp_no)
        Address = findViewById(R.id.address)

        btn_submit.setOnClickListener{

            val UFName = FName.getText().toString()
            val ULName = LName.getText().toString()
            val UEmail = Email.getText().toString()
            val UTPNo = TPNo.getText().toString()
            val UAddress = Address.getText().toString()

            if (UFName != "" && ULName != "" && UEmail != "" && UTPNo != "" && UAddress != "") {
                val handler = Handler(Looper.getMainLooper())
                handler.post {
                    val field = arrayOfNulls<String>(5)
                    field[0] = "mobile_num"
                    field[1] = "f_name"
                    field[2] = "l_name"
                    field[3] = "email"
                    field[4] = "address"
                    val data = arrayOfNulls<String>(5)
                    data[0] = UTPNo
                    data[1] = UFName
                    data[2] = ULName
                    data[3] = UEmail
                    data[4] = UAddress
                    val putData =
                        PutData("http://192.168.8.187//SelfzenFiles/signup.php", "POST ", field, data)
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

        }

    }

}


