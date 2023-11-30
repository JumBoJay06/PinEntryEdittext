package com.example.pinentryedittext

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val pinCode = 59478

    var isError = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edit1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                p0?.let {
                    if (it.length == 5) {
                        hideKeyboard()
                        if (it.toString().toInt() == pinCode) {
                            Toast.makeText(this@MainActivity, "success !!", Toast.LENGTH_SHORT).show()
                            isError = false
                        } else {
                            edit1.setEditBlockBackground(R.drawable.edit_text_border_fail)
                            isError = true
                            Toast.makeText(this@MainActivity, "fail !!", Toast.LENGTH_SHORT).show()
//                            Thread {
//                                Thread.sleep(500)
//                                runOnUiThread {
//                                    edit1.editableText.clear()
//                                }
//                            }.start()
                        }
                    } else {
                        edit1.setEditBlockBackground(R.drawable.edit_text_border)
                    }
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        })

        edit1.setOnClickListener {
            if (isError) {
                edit1.clearText()
            }
        }
    }

    fun hideKeyboard() {
        edit1.windowInsetsController?.hide(WindowInsets.Type.ime())
    }
}