package com.gmail.maystruks08.whatweathernow.data

import com.squareup.moshi.Moshi

object JsonUtil {

    val moshi: Moshi = Moshi.Builder().build()

    inline fun <reified T> toJson(obj: T): String {
        val jsonAdapter = moshi.adapter(T::class.java)
        return jsonAdapter.toJson(obj)
    }

    inline fun <reified T> fromJson(jsonString: String): T {
        val jsonAdapter = moshi.adapter(T::class.java)
        return jsonAdapter.fromJson(jsonString) as T
    }
}