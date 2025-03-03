package com.talia.tipcalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.talia.tipcalculator.databinding.ActivitySingleTipBinding

class SingleTipActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySingleTipBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingleTipBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCalcTip.setOnClickListener {
            val billText = binding.edtBillAmt.text.toString()
            val tipPercentText = binding.edtTipPercent.text.toString()

            if (billText.isEmpty() || tipPercentText.isEmpty()) {
                Toast.makeText(this, "Please enter bill amount and tip percentage", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val bill = billText.toDoubleOrNull()
            val tipPercent = tipPercentText.toDoubleOrNull()

            if (bill == null || tipPercent == null) {
                Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val tipAmount = bill * tipPercent / 100
            binding.txtTipAmt.text = "Tip: R%.2f".format(tipAmount)
        }
    }
}
