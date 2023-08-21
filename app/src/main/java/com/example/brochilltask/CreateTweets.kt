package com.example.brochilltask

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class CreateTweets : AppCompatActivity() {
    private lateinit var tweetEditText: EditText
    private lateinit var requestQueue: RequestQueue
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var btnsumit: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_tweets)
        tweetEditText = findViewById(R.id.editTextText)
        btnsumit = findViewById(R.id.button)
        requestQueue = Volley.newRequestQueue(this)
        sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE)
        btnsumit.setOnClickListener{
            postTweet()
        }
    }
    fun postTweet() {
        val token = sharedPreferences.getString("token", "")
        val tweetContent = tweetEditText.text.toString()

        val url = "https://wern-api.brochill.app/tweets"

        val requestBody = JSONObject().apply {
            put("tweet", tweetContent)
        }

        val request = object : JsonObjectRequest(
            Request.Method.POST, url, requestBody,
            Response.Listener { response ->
                // Handle successful tweet post
                // You can finish the activity or show a success message
                finish()
            },Response.ErrorListener { error ->
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