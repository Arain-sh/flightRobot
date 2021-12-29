package com.example.flightrobot.models

data class operationResponse(
    val code: Int,
    val `data`: MutableList<Data>,
    val status: String
) {
    data class Data(
        val action_id: Int,
        val created_at: String,
        var degree: String,
        val description: String,
        var element: String,
        var showObject: String,
        var showdcs_id: String,
        var showtarget_value: String,
        val id: Int,
        val name: String,
        var `object`: String,
        var type: String,
        val updated_at: String
    )
}