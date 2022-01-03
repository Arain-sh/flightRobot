data class taskRes(
    val code: Int,
    val `data`: Data,
    val status: String
) {
    data class Data(
        val actions: List<Action>,
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
            val operations: List<Operation>,
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