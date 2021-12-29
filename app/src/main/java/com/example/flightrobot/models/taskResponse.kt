package com.example.flightrobot.models

data class taskResponse(
    val code: Int,
    val `data`: MutableList<Data>,
    val status: String
) {
    data class Data(
        val created_at: Any,
        val description: String,
        val id: Int,
        val name: String,
        val run_count: Int,
        val updated_at: Any
    )
}