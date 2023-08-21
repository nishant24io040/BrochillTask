package com.example.brochilltask

import TweetAdapter
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley

class TweetsList : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var tweetAdapter: TweetAdapter
    private lateinit var requestQueue: RequestQueue
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var create: ImageButton
    private lateinit var token : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tweets_list)
        create = findViewById(R.id.button1)
        recyclerView = findViewById(R.id.rcview)
        recyclerView.layoutManager = LinearLayoutManager(this)

        requestQueue = Volley.newRequestQueue(this)
        sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE)

        create.setOnClickListener{
            val intent = Intent(this,CreateTweets::class.java)
            startActivity(intent)
        }
        token = sharedPreferences.getString("token", "").toString()

    }

    override fun onStart() {
        super.onStart()
        fetchTweets(token)
    }
    private fun fetchTweets(token: String?) {
        val url = "https://wern-api.brochill.app/tweets"

        val request = object : JsonArrayRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                val tweetList = mutableListOf<Tweet>()
                for (i in 0 until response.length()) {
                    val tweetObject = response.getJSONObject(i)
                    val content = tweetObject.getString("tweet")
                    tweetList.add(Tweet( content))
                }
                Log.e("size",tweetList.size.toString())
                tweetAdapter = TweetAdapter(tweetList)
                recyclerView.adapter = tweetAdapter
            },
            Response.ErrorListener { error ->
                // Handle error
            }){
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["x-api-key"] = "$token"
                return headers
            }
        }

        requestQueue.add(request)
    }
}