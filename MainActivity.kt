package com.example.usdinrcalculator

import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.widget.*
import android.text.InputType
import android.text.Editable
import android.text.TextWatcher
import android.view.ViewGroup

class MainActivity : android.app.Activity() {
    private lateinit var usd: EditText
    private lateinit var rate: EditText
    private lateinit var result: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pad = 24
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(pad, pad, pad, pad)
            setBackgroundColor(Color.WHITE)
        }

        val title = TextView(this).apply {
            text = "USD → INR Calculator"
            textSize = 26f
            setTextColor(Color.BLACK)
            setPadding(0, 12, 0, 24)
        }
        root.addView(title)

        usd = field("US Dollar (USD)", "1")
        rate = field("Rate (₹ per $1)", "95.60")
        root.addView(usd)
        root.addView(rate)

        result = TextView(this).apply {
            textSize = 30f
            setTextColor(Color.BLACK)
            gravity = Gravity.CENTER
            setPadding(16, 28, 16, 28)
            setBackgroundColor(Color.rgb(245,247,250))
        }
        val lp = LinearLayout.LayoutParams(-1, ViewGroup.LayoutParams.WRAP_CONTENT)
        lp.topMargin = 24
        root.addView(result, lp)

        val quick = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
        }
        listOf("5","10","50","100","500").forEach { value ->
            val b = Button(this).apply {
                text = "$$value"
                setOnClickListener { usd.setText(value); calculate() }
            }
            quick.addView(b, LinearLayout.LayoutParams(0, 56, 1f))
        }
        root.addView(quick)

        val note = TextView(this).apply {
            text = "Tip: Change the rate to match your bank/broker rate."
            textSize = 13f
            setTextColor(Color.DKGRAY)
            setPadding(0, 20, 0, 0)
        }
        root.addView(note)

        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, st: Int, c: Int, a: Int) {}
            override fun onTextChanged(s: CharSequence?, st: Int, b: Int, c: Int) { calculate() }
            override fun afterTextChanged(e: Editable?) {}
        }
        usd.addTextChangedListener(watcher)
        rate.addTextChangedListener(watcher)

        setContentView(root)
        calculate()
    }

    private fun field(label: String, value: String): EditText {
        val e = EditText(this)
        e.hint = label
        e.setText(value)
        e.textSize = 18f
        e.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        e.setSingleLine(true)
        val p = LinearLayout.LayoutParams(-1, 60)
        p.bottomMargin = 12
        e.layoutParams = p
        return e
    }

    private fun calculate() {
        val a = usd.text.toString().toDoubleOrNull()
        val r = rate.text.toString().toDoubleOrNull()
        result.text = if (a != null && r != null && a >= 0 && r > 0)
            "₹%,.2f".format(java.util.Locale.US, a * r)
        else "Enter a valid amount and rate"
    }
}
