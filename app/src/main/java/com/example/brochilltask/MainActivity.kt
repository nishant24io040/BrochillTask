package com.example.brochilltask

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

class MainActivity : AppCompatActivity() {
    private lateinit var welcomeText: TextView
    private lateinit var requestQueue: RequestQueue
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var next: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        welcomeText = findViewById(R.id.txv)
        next = findViewById(R.id.nxt)

        next.setOnClickListener{
            val intent = Intent(this, TweetsList::class.java)
            startActivity(intent)
        }
        // Initialize RequestQueue and SharedPreferences
        requestQueue = Volley.newRequestQueue(this)
        sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE)

        // Get the token from SharedPreferences
        val token = sharedPreferences.getString("token", "")

        // Make API call with the token
        makeWelcomeApiCall(token)

    }

    private fun makeWelcomeApiCall(token: String?) {
        val url = "https://wern-api.brochill.app/welcome"

        val request = object : JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                // Handle response from the server
                val message = response.optString("message")
                welcomeText.text = message
            },
            Response.ErrorListener { error ->
                // Handle error
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["x-api-key"] = "$token"
                return headers
            }
        }

        requestQueue.add(request)
    }
}
