package com.example.flightrobot.models

data class orderResponse(
    val code: Int,
    val `data`: MutableList<Data>,
    val status: String
) {
    data class Data(
        val created_at: String,
        val id: Int,
        val no: String,
        val run_status: String,
        val run_step: Int,
        val status: String,
        val task_id: String,
        val updated_at: String,
        val user_id: String,
        //val closed: Boolean
    )
}