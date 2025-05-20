package org.mlc.tasktracker.service.mapper

import org.mlc.tasktracker.dto.task.TaskResponseDTO
import org.mlc.tasktracker.model.Task

fun Task.toResponseDTO(): TaskResponseDTO {
  return TaskResponseDTO(
    id, name, description, completed, dueDate, priority, taskList.id
  )
}