package com.example.lv1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import java.util.logging.Logger;

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("MainActivity", "onCreate")

        val name = findViewById<TextView>(R.id.name)
        val nameInput = findViewById<TextView>(R.id.nameInput)
        val userSubmit = findViewById<Button>(R.id.userSubmit)
        val bio = findViewById<TextView>(R.id.bio)
        val bioInput = findViewById<TextView>(R.id.bioInput)

        userSubmit.setOnClickListener{
            name.setText(nameInput.text)
            bio.setText(bioInput.text)
        }

        val bmiSubmit = findViewById<Button>(R.id.bmiSubmit)
        val height = findViewById<TextView>(R.id.height)
        val weight = findViewById<TextView>(R.id.weight)

        bmiSubmit.setOnClickListener{
            val convHeight = height.text.toString().toFloat()
            val convWeight = weight.text.toString().toFloat()
            val bmi: Float = calculateBMI(convHeight, convWeight)
            Toast.makeText(this, bmi.toString(), Toast.LENGTH_LONG).show()
        }
    }

    fun calculateBMI(height: Float, weight: Float): Float{
        return weight / (height * height)
    }

    override fun onPause() {
        super.onPause()
        Log.d("MainActivity", "onPause")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MainActivity", "onDestroy")
    }

    override fun onResume() {
        super.onResume()
        Log.d("MainActivity", "onResume")
    }
}

