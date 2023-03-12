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
import com.example.calculator.databinding.ActivityCalculatorBinding
import java.lang.Integer.*

class CalculatorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalculatorBinding
    private var inputValue: String = ""
    private var selectedChios = false
    private var from = 0

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalculatorBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.apply {
            initialEditText()
            copyValue.setOnClickListener {
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
    }

    private fun initialEditText() {
        binding.apply {
            input.addTextChangedListener { editable ->
                clearText.apply {
                    isVisible = editable.toString().isNotEmpty()
                    setOnClickListener {
                        editable?.clear()
                    }
                }
                inputValue = editable.toString()
            }
        }
    }

    private fun onFromRadioButtonClicked(radioButton: RadioButton?) {
        selectedChios = true
        when (radioButton?.id) {
            R.id.fromBinary -> from = BASE_BINARY
            R.id.fromOctal -> from = BASE_OCTAL
            R.id.fromDecimal -> from = BASE_DECIMAL
            R.id.fromHexaDecimal -> from = BASE_HEXA_DECIMAL
        }
    }

    private fun onToRadioButtonClicked(radioButton: RadioButton?) {
        when (radioButton?.id) {
            R.id.toBinary -> isValidValue(inputValue, from, to = BASE_BINARY)
            R.id.toOctal -> isValidValue(inputValue, from, to = BASE_OCTAL)
            R.id.toDecimal -> isValidValue(inputValue, from, to = BASE_DECIMAL)
            R.id.toHexaDecimal -> isValidValue(inputValue, from, to = BASE_HEXA_DECIMAL)
        }
    }


    private fun convert(value: String, from: Int, to: Int) {
        try {
            binding.resultView.text = value.toLong(from).toString(to).uppercase()
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
        val result = binding.resultView.text.toString()
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