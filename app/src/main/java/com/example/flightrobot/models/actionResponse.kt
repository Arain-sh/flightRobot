package com.example.flightrobot.models

data class actionResponse(
    val code: Int,
    val `data`: MutableList<Data>,
    val status: String
) {
    data class Data(
        val created_at: Any,
        val description: String,
        val id: Int,
        val name: String,
        val task_id: Int,
        val updated_at: Any
    )
}