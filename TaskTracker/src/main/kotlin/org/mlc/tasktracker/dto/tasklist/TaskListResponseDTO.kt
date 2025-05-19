package org.mlc.tasktracker.dto.tasklist

import org.mlc.tasktracker.dto.task.TaskResponseDTO

/**
 * Data Transfer Object (DTO) for representing a task list in API responses.
 * This structure is used when returning task list details to the client.
 *
 * @property id The unique identifier of the task list.
 * @property title The title or name of the task list.
 * @property userId The identifier of the user to whom this task list belongs.
 * @property tasks A list of tasks associated with this task list, represented by [TaskResponseDTO].
 * Defaults to an empty list if there are no tasks or if they are not included.
 */
data class TaskListResponseDTO(
  val id: String,
  val title: String,
  val userId: String,
  val tasks: List<TaskResponseDTO> = emptyList()
)
