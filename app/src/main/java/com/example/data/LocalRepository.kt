package com.example.data

class LocalRepository {
    private val tasks = mutableListOf<TaskExecutionModel>()

    fun recordTask(task: TaskExecutionModel) {
        tasks.add(task)
    }

    fun getTaskHistory(): List<TaskExecutionModel> {
        return tasks.toList()
    }

    fun clearHistory() {
        tasks.clear()
    }
}
