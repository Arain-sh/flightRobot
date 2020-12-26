package com.example.flightrobot.models

data class sysinfoResponse(
    val code: Int,
    val `data`: List<Data>,
    val status: String
) {
    data class Data(
        val card: String,
        val cpu: String,
        val created_at: Any,
        val graph: Int,
        val id: Int,
        val jxb_hxj: Int,
        val jxb_jxb: Int,
        val jxb_sj: Int,
        val name: String,
        val sxt_01: Int,
        val sxt_02: Int,
        val sxt_03: Int,
        val updated_at: String
    )
}