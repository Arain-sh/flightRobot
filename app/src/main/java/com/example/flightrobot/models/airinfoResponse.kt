package com.example.flightrobot.models

data class airinfoResponse(
    val code: Int,
    val `data`: List<Data>,
    val status: String
) {
    data class Data(
        val bjfyj: String,
        val bjhgj: String,
        val bjzyfyj: String,
        val bjzyhgj: String,
        val chj: String,
        val created_at: Any,
        val czsd: String,
        val fdjnj: String,
        val fdjpqwd: String,
        val fdjymjd: String,
        val fdjzs: String,
        val gd: String,
        val gj: String,
        val hx: String,
        val id: Int,
        val jd: String,
        val mhs: String,
        val name: String,
        val plj: String,
        val sd: String,
        val updated_at: String,
        val wd: String,
        val wxdgd: String,
        val yxczsd: String,
        val yxgd: String,
        val yxhx: String,
        val yxsd: String
    )
}