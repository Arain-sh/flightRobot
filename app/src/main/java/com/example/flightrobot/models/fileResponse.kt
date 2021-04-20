package com.example.flightrobot.models

data class fileResponse(
    val code: Int,
    val `data`: List<Data>,
    val status: String
) {
    data class Data(
        val created_at: Any,
        val description: String,
        val id: Int,
        val image: String,
        val title: String,
        val updated_at: Any,
        val url: String
    )
}