package com.example.brochilltask

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var requestQueue: RequestQueue
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var submitButton: Button
    private lateinit var tv1: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        emailEditText = findViewById(R.id.editTextEmail)
        passwordEditText = findViewById(R.id.editTextPassword)
        submitButton = findViewById(R.id.buttonLogin)
        tv1 = findViewById(R.id.textView)

        tv1.setOnClickListener{
            val intent = Intent(this,SignUpActivity::class.java)
            startActivity(intent)
        }
        requestQueue = Volley.newRequestQueue(this)
        sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE)

        submitButton.setOnClickListener {
            loginuser()
        }

    }

    private fun loginuser() {
        val url = "https://wern-api.brochill.app/login"

        val requestBody = JSONObject().apply {
            put("email", emailEditText.text.toString())
            put("password", passwordEditText.text.toString())
        }

        val request = JsonObjectRequest(
            Request.Method.POST, url, requestBody,
            { response ->
                // Handle response from the server
                val firstName = response.optString("first_name")
                val lastName = response.optString("last_name")
                val email = response.optString("email")
                val id = response.optString("id")
                val token = response.optString("token")

                val editor = sharedPreferences.edit()
                editor.putString("firstName", firstName)
                editor.putString("lastName", lastName)
                editor.putString("email", email)
                editor.putString("id", id)
                editor.putString("token", token)
                editor.apply()

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            },
            { error ->
                Toast.makeText(this, "Email and Passaword may be Wrong", Toast.LENGTH_SHORT).show()
            })

        requestQueue.add(request)
    }
}