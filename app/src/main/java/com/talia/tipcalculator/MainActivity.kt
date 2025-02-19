package com.talia.tipcalculator

import android.os.Bundle
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.TextView

//handling user interaction and calculations when clicking the calculate button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        //finding UI components by their ids

        val billAmt = findViewById<EditText>(R.id.edtBillAmt)
        val tipPercent = findViewById<EditText>(R.id.edtTip)
        val calculate = findViewById<Button>(R.id.btnCalculate)
        val tip = findViewById<TextView>(R.id.txtTipAmount)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }

    }
}

