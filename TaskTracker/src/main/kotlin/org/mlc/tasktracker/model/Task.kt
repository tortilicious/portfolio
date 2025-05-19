package org.mlc.tasktracker.model

import kotlinx.datetime.LocalDate


data class Task(
  val id: Int,
  val name: String,
  val description: String,
  val completed: Boolean,
  val dueDate: LocalDate,
  val priority: TaskPriority,
  val taskListId: Int
)