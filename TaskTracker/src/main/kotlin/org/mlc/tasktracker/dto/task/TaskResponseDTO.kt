package org.mlc.tasktracker.dto.task

import org.mlc.tasktracker.model.TaskPriority

/**
 * Data Transfer Object (DTO) for representing a task in API responses.
 * This structure is used when returning task details to the client.
 *
 * @property id The unique identifier of the task.
 * @property name The name or title of the task.
 * @property description An optional detailed description of the task.
 * @property completed A boolean flag indicating whether the task is completed (true) or not (false).
 * @property dueDate An optional due date for the task.
 * @property priority The priority of the task (e.g., HIGH, MID, LOW).
 * @property taskListId The identifier of the task list to which this task belongs.
 */
data class TaskResponseDTO(
    val id: String,
    val name: String,
    val description: String?,
    val completed: Boolean,
    val dueDate: java.time.LocalDate?, // Consistent with your latest update
    val priority: TaskPriority,
    val taskListId: String
)
