package org.mlc.tasktracker.model

data class TaskList(
  val id: Int,
  val title: String,
  val tasks: List<Task>,
  val userId: Int
)
