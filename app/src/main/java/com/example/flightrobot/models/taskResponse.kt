package com.example.flightrobot.models

data class taskResponse(
    val code: Int,
    val `data`: MutableList<Data>,
    val status: String
) {
    data class Data(
        val actions: MutableList<Action>,
        val created_at: Any,
        val description: String,
        val id: Int,
        val name: String,
        val run_count: Int,
        val updated_at: Any
    ) {
        data class Action(
            val created_at: Any,
            val description: String,
            val id: Int,
            val name: String,
            val operations: MutableList<Operation>,
            val task_id: Int,
            val updated_at: Any
        ) {
            data class Operation(
                val action_id: Int,
                val created_at: Any,
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
    }
}