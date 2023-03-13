package com.example.calculator

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import java.lang.Integer.*

class CalculatorActivity : AppCompatActivity() {

    private lateinit var fromRadioGroup: RadioGroup
    private lateinit var toRadioGroup: RadioGroup
    private lateinit var copyResult: ImageButton
    private lateinit var clearText: ImageButton
    private lateinit var input: EditText
    private lateinit var result: TextView

    private var inputValue: String = ""
    private var selectedChios = false
    private var fromBase = 0

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)

        initialView()
        initialEditText()
        setupCallback()
    }
    private fun initialView() {
        fromRadioGroup = findViewById(R.id.fromRadioGroup)
        toRadioGroup = findViewById(R.id.toRadioGroup)
        copyResult = findViewById(R.id.copyResult)
        input = findViewById(R.id.input)
        result = findViewById(R.id.resultView)
        clearText = findViewById(R.id.clear_text)
    }

    private fun setupCallback(){
        copyResult.setOnClickListener {
            copyValue()
        }
        fromRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            val radioButton = findViewById<RadioButton>(checkedId)
            onFromRadioButtonClicked(radioButton)
        }
        toRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            val radioButton = findViewById<RadioButton>(checkedId)
            onToRadioButtonClicked(radioButton)
        }
    }

    private fun initialEditText() {
        input.addTextChangedListener { editable ->
            clearText.apply {
                isVisible = editable.toString().isNotEmpty()
                setOnClickListener {
                    editable?.clear()
                }
            }
            inputValue = editable.toString()
            toRadioGroup.clearCheck()
        }
    }

    private fun onFromRadioButtonClicked(radioButton: RadioButton?) {
        selectedChios = true
        when (radioButton?.id) {
            R.id.fromBinary -> fromBase = BASE_BINARY
            R.id.fromOctal -> fromBase = BASE_OCTAL
            R.id.fromDecimal -> fromBase = BASE_DECIMAL
            R.id.fromHexaDecimal -> fromBase = BASE_HEXA_DECIMAL
        }
    }

    private fun onToRadioButtonClicked(radioButton: RadioButton?) {
        when (radioButton?.id) {
            R.id.toBinary -> isValidValue(inputValue, fromBase, to = BASE_BINARY)
            R.id.toOctal -> isValidValue(inputValue, fromBase, to = BASE_OCTAL)
            R.id.toDecimal -> isValidValue(inputValue, fromBase, to = BASE_DECIMAL)
            R.id.toHexaDecimal -> isValidValue(inputValue, fromBase, to = BASE_HEXA_DECIMAL)
        }
    }


    private fun convert(value: String, from: Int, to: Int) {
        try {
           result.text = value.toLong(from).toString(to).uppercase()
        } catch (e: Exception) {
            makeToast(e.message.toString())
        }
    }

    private fun isValidValue(value: String, from: Int, to: Int) {
        when {
            inputValue.isEmpty() -> makeToast(getString(R.string.title_valid_value))
            !selectedChios -> makeToast(getString(R.string.title_select_chios))
            else -> convert(value, from, to)
        }
    }

    private fun makeToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun copyValue() {
        val result = result.text.toString()
        if (result.isNotEmpty()) {
            val clipboardManager =
                getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("label", result)
            clipboardManager.setPrimaryClip(clipData)
            makeToast(getString(R.string.text_copied))
        }
    }

    companion object {
        const val BASE_BINARY = 2
        const val BASE_OCTAL = 8
        const val BASE_DECIMAL = 10
        const val BASE_HEXA_DECIMAL = 16
    }
}