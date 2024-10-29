package com.example.myapplication

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Spinner
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.round

class MainActivity : AppCompatActivity() {
    private lateinit var amountEditText1: EditText
    private lateinit var amountEditText2: EditText
    private lateinit var currencySpinner1: Spinner
    private lateinit var currencySpinner2: Spinner

    // Giả định tỷ giá chuyển đổi cho đơn giản
    private val exchangeRates = mapOf(
        "USD" to 1.0,
        "EUR" to 0.85,
        "JPY" to 110.0,
        "VND" to 23000.0
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        amountEditText1 = findViewById(R.id.amountEditText1)
        amountEditText2 = findViewById(R.id.amountEditText2)
        currencySpinner1 = findViewById(R.id.currencySpinner1)
        currencySpinner2 = findViewById(R.id.currencySpinner2)

        // Thiết lập Spinner với các đồng tiền
        val currencies = exchangeRates.keys.toList()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencies)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        currencySpinner1.adapter = adapter
        currencySpinner2.adapter = adapter

        // Xử lý thay đổi EditText hoặc Spinner
        amountEditText1.addTextChangedListener(createTextWatcher(true))
        amountEditText2.addTextChangedListener(createTextWatcher(false))
        currencySpinner1.onItemSelectedListener = createItemSelectedListener(true)
        currencySpinner2.onItemSelectedListener = createItemSelectedListener(false)
    }

    private fun createTextWatcher(isFirstEditText: Boolean): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (isFirstEditText) {
                    convertCurrency(amountEditText1, amountEditText2, currencySpinner1, currencySpinner2)
                } else {
                    convertCurrency(amountEditText2, amountEditText1, currencySpinner2, currencySpinner1)
                }
            }
        }
    }

    private fun createItemSelectedListener(isFirstSpinner: Boolean) = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
            if (isFirstSpinner) {
                convertCurrency(amountEditText1, amountEditText2, currencySpinner1, currencySpinner2)
            } else {
                convertCurrency(amountEditText2, amountEditText1, currencySpinner2, currencySpinner1)
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>) {}
    }

    private fun convertCurrency(sourceEditText: EditText, targetEditText: EditText, sourceSpinner: Spinner, targetSpinner: Spinner) {
        val sourceAmount = sourceEditText.text.toString().toDoubleOrNull() ?: 0.0
        val sourceCurrency = sourceSpinner.selectedItem.toString()
        val targetCurrency = targetSpinner.selectedItem.toString()

        val sourceRate = exchangeRates[sourceCurrency] ?: 1.0
        val targetRate = exchangeRates[targetCurrency] ?: 1.0

        val targetAmount = sourceAmount * (targetRate / sourceRate)
        targetEditText.setText((round(targetAmount * 100) / 100.0).toString())
    }
}
