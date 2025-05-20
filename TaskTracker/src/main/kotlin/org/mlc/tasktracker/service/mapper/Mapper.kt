package org.mlc.tasktracker.service.mapper

import org.mlc.tasktracker.dto.task.TaskResponseDTO
import org.mlc.tasktracker.dto.tasklist.TaskListResponseDTO
import org.mlc.tasktracker.dto.user.UserResponseDTO
import org.mlc.tasktracker.model.Task
import org.mlc.tasktracker.model.TaskList
import org.mlc.tasktracker.model.User

fun Task.toResponseDTO(): TaskResponseDTO {
  return TaskResponseDTO(
    id = id,
    name = name,
    description = description,
    completed = completed,
    dueDate = dueDate,
    priority = priority,
    taskListId = taskList.id
  )
}

fun TaskList.toResponseDTO(): TaskListResponseDTO {
  return TaskListResponseDTO(
    id = id,
    title = title,
    userId = user.id,
    tasks = tasks.map { it.toResponseDTO() },
  )
}

fun User.toResponseDTO(): UserResponseDTO {
  return UserResponseDTO(
    id = id,
    userName = userName
  )
}