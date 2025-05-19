package org.mlc.tasktracker.model

import jakarta.persistence.Entity
import jakarta.persistence.Table
import kotlinx.datetime.LocalDate



@Entity
@Table(name = "task")
data class Task(
  val id: String,
  val name: String,
  val description: String,
  val completed: Boolean,
  val dueDate: LocalDate,
  val priority: TaskPriority,
  val taskListId: Int
)