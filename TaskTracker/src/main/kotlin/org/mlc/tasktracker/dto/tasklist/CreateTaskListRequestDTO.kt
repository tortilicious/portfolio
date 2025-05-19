package org.mlc.tasktracker.dto.tasklist

/**
 * Data Transfer Object (DTO) for creating a new task list.
 * This structure is expected in the request body when a client wants to create a task list.
 *
 * @property title The title or name of the task list. This field is mandatory.
 */
data class CreateTaskListRequestDTO(
  val title: String,
)
