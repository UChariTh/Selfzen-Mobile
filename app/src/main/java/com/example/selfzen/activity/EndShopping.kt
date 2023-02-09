package com.example.selfzen.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.selfzen.R
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.android.synthetic.main.activity_cart_page.*
import kotlinx.android.synthetic.main.activity_end_shopping.*

class EndShopping : AppCompatActivity() {


    private lateinit var QRCodeView : ImageView
    private lateinit var builder : AlertDialog.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_end_shopping)

        QRCodeView = findViewById(R.id.qrCode)
        builder = AlertDialog.Builder(this)

        getQrCodeBitmap(
            intent.getStringExtra("Bill_ID").toString()
        )


        btn_exit.setOnClickListener{
            builder.setTitle("Read")
                .setMessage("If you settle your bill, Press \"Yes\" and end shopping. If you not settle your bill yet, Press \"No\".")
                .setPositiveButton("Yes"){dialogInterface,it ->

                    startActivity(
                        Intent(this, MainActivity::class.java))

                }.setNegativeButton("No"){dialogInterface,it ->
                    dialogInterface.cancel()
                }.show()
        }

    }
    fun getQrCodeBitmap(Code: String): Bitmap {
        val size = 512 //pixels
        val qrCodeContent = "$Code"
        val bits = QRCodeWriter().encode(qrCodeContent, BarcodeFormat.QR_CODE, size, size)
        return Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565).also {
            for (x in 0 until size) {
                for (y in 0 until size) {
                    it.setPixel(x, y, if (bits[x, y]) Color.BLACK else Color.WHITE)
                }
            }
            QRCodeView.setImageBitmap(it)
        }
    }
}