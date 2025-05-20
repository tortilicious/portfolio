package org.mlc.tasktracker.dto.tasklist

/**
 * Data Transfer Object (DTO) for updating an existing task list.
 *
 * @property title The new title or name for the task list. This field is mandatory for an update.
 */
data class UpdateTaskListRequestDTO(
    val title: String,
)
