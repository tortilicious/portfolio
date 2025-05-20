package org.mlc.tasktracker.service

import org.mlc.tasktracker.dto.task.CreateTaskRequestDTO
import org.mlc.tasktracker.dto.task.TaskResponseDTO
import org.mlc.tasktracker.dto.task.UpdateTaskRequestDTO

interface TaskService {
    fun createTask(userId: String, taskListId: String, createTaskRequestDTO: CreateTaskRequestDTO): TaskResponseDTO

    /**
     * Retrieves a specific task by its ID, ensuring that it belongs to the specified user.
     *
     * @param userId The unique identifier of the user attempting to access the task.
     * @param taskId The unique identifier of the task to retrieve.
     * @return A [TaskResponseDTO] representing the found task.
     * @throws TaskNotFoundException if the task is not found.
     * @throws AuthorizationException if the user is not authorized to access the task.
     */
    fun getTaskById(userId: String, taskId: String): TaskResponseDTO

    fun getTasksByTaskListId(userId: String, taskListId: String, completed: Boolean?): List<TaskResponseDTO>

    /**
     * Updates an existing task if it belongs to the specified user.
     *
     * @param userId The unique identifier of the user attempting to update the task.
     * @param taskId The unique identifier of the task to be updated.
     * @param updateTaskRequestDTO The DTO containing the updated information for the task.
     * @return A [TaskResponseDTO] representing the updated task.
     * @throws TaskNotFoundException if the task is not found.
     * @throws AuthorizationException if the user is not authorized to update the task.
     */
    fun updateTask(userId: String, taskId: String, updateTaskRequestDTO: UpdateTaskRequestDTO): TaskResponseDTO

    /**
     * Deletes a specific task if it belongs to the specified user.
     *
     * @param userId The unique identifier of the user attempting to delete the task.
     * @param taskId The unique identifier of the task to be deleted.
     * @return `true` if the task was successfully found and deleted.
     * @throws TaskNotFoundException if the task is not found.
     * @throws AuthorizationException if the user is not authorized to delete the task.
     */
    fun deleteTask(userId: String, taskId: String): Boolean
}