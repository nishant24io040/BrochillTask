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
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class SignUpActivity : AppCompatActivity() {
    private lateinit var firstNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var submitButton: Button
    private lateinit var tv2: TextView
    private lateinit var requestQueue: RequestQueue
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        firstNameEditText = findViewById(R.id.firstNameEditText)
        lastNameEditText = findViewById(R.id.lastNameEditText)
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        submitButton = findViewById(R.id.submitButton)
        tv2 = findViewById(R.id.tv2)

        tv2.setOnClickListener{
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
        requestQueue = Volley.newRequestQueue(this)
        sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE)

        submitButton.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        val url = "https://wern-api.brochill.app/register"

        val requestBody = JSONObject().apply {
            put("first_name", firstNameEditText.text.toString())
            put("last_name", lastNameEditText.text.toString())
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
                Toast.makeText(this, "Something Went to Wrong", Toast.LENGTH_SHORT).show()
            })

        requestQueue.add(request)
    }
}