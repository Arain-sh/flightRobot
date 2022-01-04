package com.example.flightrobot.models

data class ctOrderResponse(
    val code: Int,
    val `data`: Data,
    val status: String
) {
    data class Data(
        val closed: Boolean,
        val created_at: String,
        val id: Int,
        val no: String,
        val run_status: String,
        val run_step: Int,
        val status: String,
        val task_id: Int,
        val updated_at: String,
        val user_id: Int
    )
}