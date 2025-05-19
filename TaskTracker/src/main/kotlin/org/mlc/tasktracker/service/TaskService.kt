package org.mlc.tasktracker.service

import org.mlc.tasktracker.dto.task.CreateTaskRequestDTO
import org.mlc.tasktracker.dto.task.TaskResponseDTO
import org.mlc.tasktracker.dto.task.UpdateTaskRequestDTO
import org.mlc.tasktracker.dto.tasklist.TaskListResponseDTO

interface TaskService {
  fun createTask(userId: String, taskListId: String, createTaskRequestDTO: CreateTaskRequestDTO): TaskResponseDTO
  fun getTaskById(userId: String, taskId: String): TaskResponseDTO?
  fun getTasksByTaskListId(userId: String, taskListId: String, completed: Boolean?): List<TaskResponseDTO>
  fun updateTask(userId: String, taskId: String, updateTaskRequestDTO: UpdateTaskRequestDTO): TaskResponseDTO?
  fun deleteTask(userId: String, taskId: String): Boolean
}