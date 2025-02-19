package com.talia.tipcalculator

import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.TypedValue
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.talia.tipcalculator.databinding.ActivitySplitTipBinding

class SplitTipActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplitTipBinding

    // List to hold dynamically created EditTexts for each person's amount input.
    private val amountEditTexts = mutableListOf<EditText>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplitTipBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initially hide dynamic views, calculate button, and results container.
        binding.txtEnterAmounts.visibility = android.view.View.GONE
        binding.dynamicContainer.visibility = android.view.View.GONE
        binding.btnCalcTip.visibility = android.view.View.GONE
        binding.resultsContainer.visibility = android.view.View.GONE

        // Listen for changes in edtNumPeople to dynamically create amount input fields.
        binding.edtNumPeople.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Reset dynamic views when number of people changes.
                resetDynamicViews()

                val numPeople = s.toString().toIntOrNull()
                if (numPeople != null && numPeople > 0) {
                    binding.txtEnterAmounts.visibility = android.view.View.VISIBLE
                    binding.dynamicContainer.visibility = android.view.View.VISIBLE
                    for (i in 1..numPeople) {
                        val editText = EditText(this@SplitTipActivity)
                        // Set layout parameters matching the XML style.
                        val params = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            dpToPx(64)
                        )
                        params.setMargins(dpToPx(32), dpToPx(16), dpToPx(32), 0)
                        editText.layoutParams = params
                        editText.hint = "Amount for person $i"
                        editText.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
                        editText.setPadding(dpToPx(16), dpToPx(16), dpToPx(16), dpToPx(16))
                        editText.setBackgroundResource(R.drawable.rounded_edittext)
                        editText.setHintTextColor(resources.getColor(R.color.white))
                        binding.dynamicContainer.addView(editText)
                        amountEditTexts.add(editText)
                    }
                    binding.btnCalcTip.visibility = android.view.View.VISIBLE
                } else {
                    binding.txtEnterAmounts.visibility = android.view.View.GONE
                    binding.dynamicContainer.visibility = android.view.View.GONE
                    binding.btnCalcTip.visibility = android.view.View.GONE
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
        })

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
                Toast.makeText(this, "Invalid bill or tip percentage", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Sum the individual amounts.
            var totalEntered = 0.0
            for (editText in amountEditTexts) {
                val amount = editText.text.toString().toDoubleOrNull() ?: 0.0
                totalEntered += amount
            }

            if (kotlin.math.abs(totalEntered - bill) > 0.01) { // allow slight precision error
                Toast.makeText(
                    this,
                    "The total of individual amounts ($totalEntered) does not match the bill amount ($bill)",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            // Calculate tip for each amount and display.
            binding.resultsContainer.removeAllViews() // Clear previous results.
            for ((index, editText) in amountEditTexts.withIndex()) {
                val amount = editText.text.toString().toDoubleOrNull() ?: 0.0
                val tipForPerson = amount * tipPercent / 100
                val textView = TextView(this)
                textView.text = "Person ${index+1} tip: $%.2f".format(tipForPerson)
                // Set layout params similar to our styled views.
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(dpToPx(32), dpToPx(8), dpToPx(32), 0)
                textView.layoutParams = params
                textView.setTextColor(resources.getColor(R.color.white))
                binding.resultsContainer.addView(textView)
            }
            binding.resultsContainer.visibility = android.view.View.VISIBLE
        }
    }

    // Helper function to convert dp to pixels.
    private fun dpToPx(dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), resources.displayMetrics
        ).toInt()
    }

    private fun resetDynamicViews() {
        binding.dynamicContainer.removeAllViews()
        amountEditTexts.clear()
        binding.resultsContainer.visibility = android.view.View.GONE
        binding.btnCalcTip.visibility = android.view.View.GONE
    }
}
