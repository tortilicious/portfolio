package org.mlc.tasktracker.dto.task

import kotlinx.datetime.LocalDate
import org.mlc.tasktracker.model.TaskPriority

/**
 * Data Transfer Object (DTO) for creating a new task.
 * This structure is expected in the request body when a client wants to create a task.
 *
 * @property name The name or title of the task. This field is mandatory.
 * @property description An optional detailed description of the task. Defaults to null.
 * @property dueDate An optional due date for the task. Defaults to null.
 * @property priority The priority of the task. Defaults to [TaskPriority.MID] if not specified.
 */
data class CreateTaskRequestDTO(
  val name: String,
  val description: String? = null,
  val dueDate: java.time.LocalDate? = null, // Consistent with your latest update, making it optional
  val priority: TaskPriority = TaskPriority.MID,
)
