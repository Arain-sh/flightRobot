package com.example.flightrobot.models

data class operationResponse(
    val code: Int,
    val `data`: List<Data>,
    val status: String
) {
    data class Data(
        val action_id: Int,
        val created_at: String,
        var degree: String,
        val description: String,
        var element: String,
        val id: Int,
        val name: String,
        var `object`: String,
        var type: String,
        val updated_at: String
    )
}