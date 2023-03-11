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

            radioGroupBinary.setOnCheckedChangeListener { _, checkedId ->
                val radioButton = findViewById<RadioButton>(checkedId)
                onRadioBinaryButtonClicked(radioButton)
            }

            radioGroupOctal.setOnCheckedChangeListener { _, checkedId ->
                val radioButton = findViewById<RadioButton>(checkedId)
                onRadioOctalButtonClicked(radioButton)
            }

            radioGroupDecimal.setOnCheckedChangeListener { _, checkedId ->
                val radioButton = findViewById<RadioButton>(checkedId)
                onRadioDecimalButtonClicked(radioButton)
            }

            radioGroupHexaDecimal.setOnCheckedChangeListener { _, checkedId ->
                val radioButton = findViewById<RadioButton>(checkedId)
                onRadioHexaDecimalButtonClicked(radioButton)
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

    private fun onRadioBinaryButtonClicked(radioButton: RadioButton?) {
        if (isBinaryNumber(inputValue)) {
            binding.resultView.text = when (radioButton?.id) {
                R.id.binary_to_octal -> {
                    handelSelectedRadioButton(radioButton)
                    toOctalString(parseInt(inputValue, 2))
                }
                R.id.binary_to_decimal -> {
                    handelSelectedRadioButton(radioButton)
                    parseInt(inputValue, 2).toString()
                }
                else -> {
                    radioButton?.let { handelSelectedRadioButton(it) }
                    inputValue.toInt(2).toString(16)
                } // binary to hexa-decimal

            }
        }
    }

    private fun onRadioOctalButtonClicked(radioButton: RadioButton?) {

        if (isOctalNumber(inputValue)) {

            binding.resultView.text = when (radioButton?.id) {
                R.id.octal_to_binary -> {
                    handelSelectedRadioButton(radioButton)
                    inputValue.toInt(8).toString(2)
                }
                R.id.octal_to_decimal -> {
                    handelSelectedRadioButton(radioButton)
                    inputValue.toInt(8).toString()
                }
                else -> {
                    radioButton?.let { handelSelectedRadioButton(it) }
                    inputValue.toBigInteger(8).toString(16)
                } // octal to hexa-decimal
            }
        }
    }

    private fun onRadioDecimalButtonClicked(radioButton: RadioButton?) {

        if (isDecimalNumber(inputValue)) {
            binding.resultView.text = when (radioButton?.id) {
                R.id.decimal_to_binary -> {
                    handelSelectedRadioButton(radioButton)
                    inputValue.toInt().toString(2)
                }
                R.id.decimal_to_octal -> {
                    handelSelectedRadioButton(radioButton)
                    inputValue.toInt().toString(8)
                }
                else -> {
                    radioButton?.let { handelSelectedRadioButton(it) }
                    inputValue.toInt().toString(16)
                } // decimal to hexa-decimal
            }
        }
    }

    private fun onRadioHexaDecimalButtonClicked(radioButton: RadioButton?) {
        if (isHexaDecimalNumber(inputValue)) {
            binding.resultView.text = when (radioButton?.id) {
                R.id.hexa_decimal_to_binary -> {
                    handelSelectedRadioButton(radioButton)
                    inputValue.toInt(16).toString(2)
                }
                R.id.hexa_decimal_to_octal -> {
                    handelSelectedRadioButton(radioButton)
                    inputValue.toInt(16).toString(8)
                }
                else -> {
                    radioButton?.let { handelSelectedRadioButton(it) }
                    inputValue.toInt(16).toString()
                } // hexa-decimal to decimal
            }
        }
    }


    private fun isBinaryNumber(input: String): Boolean {
        if (input.isNotEmpty() && input.toLongOrNull(2) != null) {
            return true
        } else {
            Toast.makeText(this, "Please enter binary number", Toast.LENGTH_SHORT).show()
        }
        return false
    }

    private fun isOctalNumber(input: String): Boolean {
        if (input.isNotEmpty() && input.toIntOrNull(8) != null) {
            return true
        } else {
            Toast.makeText(this, "Please enter octal number", Toast.LENGTH_SHORT).show()
        }
        return false
    }

    private fun isDecimalNumber(input: String): Boolean {
        if (input.isNotEmpty() && input.toDouble() % 1 == 0.0) {
            return true
        } else {
            Toast.makeText(this, "Please enter decimal number", Toast.LENGTH_SHORT).show()
        }
        return false
    }

    private fun isHexaDecimalNumber(input: String): Boolean {
        if (input.isNotEmpty() && input.toLongOrNull(16) != null) {
            return true
        } else {
            Toast.makeText(this, "Please enter hexa-decimal number", Toast.LENGTH_SHORT).show()
        }
        return false
    }


    private fun handelSelectedRadioButton(radioButton: RadioButton) {
        val radioButtons = mutableListOf<RadioButton>()
        binding.apply {
            radioButtons.apply {
                add(binaryToOctal)
                add(binaryToDecimal)
                add(binaryToHexaDecimal)
                add(octalToBinary)
                add(octalToDecimal)
                add(octalToHexaDecimal)
                add(decimalToBinary)
                add(decimalToOctal)
                add(decimalToHexaDecimal)
                add(hexaDecimalToBinary)
                add(hexaDecimalToOctal)
                add(hexaDecimalToDecimal)
            }
        }

        if (radioButtons.contains(radioButton)) {
            radioButton.setBackgroundResource(R.drawable.rounded_radio_button_selector)
        }
        radioButtons.filterNot {
            it == radioButton
        }.forEach {
            it.setBackgroundResource(0)
        }

    }


    private fun copyValue() {
        val result = binding.resultView.text.toString()
        if (result.isNotEmpty()) {
            val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("label", result)
            clipboardManager.setPrimaryClip(clipData)
            Toast.makeText(this, "Text copied", Toast.LENGTH_SHORT).show()
        }
    }
}