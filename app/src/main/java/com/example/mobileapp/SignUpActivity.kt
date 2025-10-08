package com.example.mobileapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)

        val btnGoToSignIn = findViewById<Button>(R.id.btnGoToLogin)
        btnGoToSignIn.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
                    startActivity(intent)
        }
    }
}