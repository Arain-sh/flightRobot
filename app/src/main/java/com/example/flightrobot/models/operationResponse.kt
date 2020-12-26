package com.example.flightrobot.models

data class operationResponse(
    val code: Int,
    val `data`: List<Data>,
    val status: String
) {
    data class Data(
        val action_id: Int,
        val created_at: String,
        val degree: String,
        val description: String,
        val element: String,
        val id: Int,
        val name: String,
        val `object`: String,
        val type: String,
        val updated_at: String
    )
}