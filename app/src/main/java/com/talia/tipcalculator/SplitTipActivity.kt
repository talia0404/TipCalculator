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

    private val amountEditTexts = mutableListOf<EditText>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplitTipBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.txtEnterAmounts.visibility = android.view.View.GONE
        binding.dynamicContainer.visibility = android.view.View.GONE
        binding.btnCalcTip.visibility = android.view.View.GONE
        binding.resultsContainer.visibility = android.view.View.GONE

        binding.edtNumPeople.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                resetDynamicViews()

                val numPeople = s.toString().toIntOrNull()
                if (numPeople != null && numPeople > 0) {
                    binding.txtEnterAmounts.visibility = android.view.View.VISIBLE
                    binding.dynamicContainer.visibility = android.view.View.VISIBLE
                    for (i in 1..numPeople) {
                        val editText = EditText(this@SplitTipActivity)
                        val parameters = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            dpToPx(64)
                        )

                        parameters.setMargins(dpToPx(32), dpToPx(16), dpToPx(32), 0)
                        editText.layoutParams = parameters
                        editText.hint = "Amount for person Ri"
                        editText.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
                        editText.setPadding(dpToPx(16), dpToPx(16), dpToPx(16), dpToPx(16))
                        editText.setBackgroundResource(R.drawable.rounded_edittext)
                        editText.setHintTextColor(resources.getColor(R.color.white))
                        binding.dynamicContainer.addView(editText)
                        amountEditTexts.add(editText)
                    }

                    binding.btnCalcTip.visibility = android.view.View.VISIBLE

                } else

                {
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

            var totalEntered = 0.0
            for (editText in amountEditTexts) {
                val amount = editText.text.toString().toDoubleOrNull() ?: 0.0
                totalEntered += amount
            }

            if (kotlin.math.abs(totalEntered - bill) > 0.01) {
                Toast.makeText(
                    this,
                    "The total of individual amounts (RtotalEntered) does not match the bill amount (Rbill)",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            binding.resultsContainer.removeAllViews()
            for ((index, editText) in amountEditTexts.withIndex()) {
                val amount = editText.text.toString().toDoubleOrNull() ?: 0.0
                val tipForPerson = amount * tipPercent / 100
                val textView = TextView(this)
                textView.text = "Person R{index+1} tip: R%.2f".format(tipForPerson)

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
