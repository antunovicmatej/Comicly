package com.example.fragmentstudy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val swapAct = findViewById<Button>(R.id.btnGoToSecond)
        swapAct.setOnClickListener {
            val switchActivityIntent = Intent(this, MainActivity2::class.java)
            startActivity(switchActivityIntent)
        }
    }
}