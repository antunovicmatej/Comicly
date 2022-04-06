package com.example.fragmentstudy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val btn1 = findViewById<Button>(R.id.firstFrag)
        btn1.setOnClickListener{
            val firstFragment = Fragment1()
            val manager = supportFragmentManager
            val transaction = manager.beginTransaction()

            transaction.replace(R.id.firstFrag, firstFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        val btn2 = findViewById<Button>(R.id.secondFrag)
        btn2.setOnClickListener{
            val secondFragment = Fragment1()
            val manager = supportFragmentManager
            val transaction = manager.beginTransaction()

            transaction.replace(R.id.secondFrag, secondFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }
}