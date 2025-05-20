package org.mlc.tasktracker.dto.task

import org.mlc.tasktracker.model.TaskPriority

/**
 * Data Transfer Object (DTO) for updating an existing task.
 * All fields are optional, allowing for partial updates. Only the fields provided
 * in the request body will be considered for an update.
 *
 * @property name An optional new name or title for the task.
 * @property description An optional new detailed description for the task.
 * @property completed An optional new completion status for the task.
 * @property dueDate An optional new due date for the task.
 * @property priority An optional new priority for the task.
 */
data class UpdateTaskRequestDTO(
  val name: String? = null,
  val description: String? = null,
  val completed: Boolean? = null,
  val dueDate: java.time.LocalDate? = null,
  val priority: TaskPriority? = null
)
