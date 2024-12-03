package com.example.pokemonapp

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        getStarters()
    }
    private fun getStarters() {
        val url = "https://pokeapi.co/api/v2/pokemon/3"
        val client = AsyncHttpClient()
        val params = RequestParams()
        client[
                url,
                params,
                object: JsonHttpResponseHandler(){
                    override fun onFailure(
                        statusCode: Int,
                        headers: Headers?,
                        response: String,
                        throwable: Throwable
                    ) {
                        throwable?.message?.let {
                            println(params.toString())
                            Log.e("FAIL", response)
                        }

                    }
                    override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                        val gson = Gson()
                        val statsJSON = json.jsonObject.getJSONArray("stats").toString();
                        val typeJSON = json.jsonObject.getJSONArray("types").getJSONObject(2).getJSONObject("type")
                        val name = json.jsonObject.getString("name")
                        println(statsJSON)
                        println(typeJSON)
                        println(name)


                    }
                }
        ]

    }
}