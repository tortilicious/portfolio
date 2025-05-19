package org.mlc.tasktracker.model

data class TaskList(
  val id: String,
  val title: String,
  val tasks: List<Task>,
  val userId: Int
)
