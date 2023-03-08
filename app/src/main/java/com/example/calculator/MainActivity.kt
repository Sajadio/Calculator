package com.example.calculator

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.lang.Integer.*

class MainActivity : AppCompatActivity() {

    private lateinit var input: EditText
    private lateinit var result: TextView
    private lateinit var radioGroup: RadioGroup
    private lateinit var copy: ImageButton

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        input = findViewById(R.id.input)
        result = findViewById(R.id.resultView)
        radioGroup = findViewById(R.id.radioGroup)
        copy = findViewById(R.id.copyValue)

        copy.setOnClickListener {
            copyValue()
        }

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            val radioButton = findViewById<RadioButton>(checkedId)
            onRadioButtonClicked(radioButton)
        }

    }

    private fun copyValue() {
        if (result.text.isNotEmpty()) {
            val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("label", result.text)
            clipboardManager.setPrimaryClip(clipData)
            Toast.makeText(this, "Text copied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onRadioButtonClicked(radioButton: RadioButton) {
        try {
            val inputNumber = input.text.toString().toInt()
            result.text = when (radioButton.id) {
                R.id.binary -> toBinaryString(inputNumber)
                R.id.hexa_decimal -> toHexString(inputNumber)
                R.id.octal -> toOctalString(inputNumber)
                else -> inputNumber.toDouble().toString()
            }
        } catch (e: Exception) {
            Toast.makeText(this, e.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }
}