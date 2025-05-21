package org.mlc.tasktracker.service

import org.mlc.tasktracker.dto.task.CreateTaskRequestDTO
import org.mlc.tasktracker.dto.task.TaskResponseDTO
import org.mlc.tasktracker.dto.task.UpdateTaskRequestDTO
import org.mlc.tasktracker.exception.AuthorizationException
import org.mlc.tasktracker.exception.TaskNotFoundException
import org.mlc.tasktracker.exception.TaskListNotFoundException
import org.mlc.tasktracker.exception.UserNotFoundException
import org.mlc.tasktracker.model.utils.TaskSortField


interface TaskService {
    /**
     * Creates a new task associated with a specific user's task list.
     *
     * @param userId The unique identifier of the user creating the task.
     * @param taskListId The unique identifier of the task list to which the task will be added.
     * @param createTaskRequestDTO The DTO containing the details of the task to be created.
     * @return A [TaskResponseDTO] representing the newly created task.
     * @throws UserNotFoundException if the user with the given [userId] is not found.
     * @throws TaskListNotFoundException if the task list with the given [taskListId] is not found
     * or does not belong to the user with the given [userId].
     */
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

    /**
     * Retrieves all tasks belonging to a specific task list for a given user.
     * Tasks can be optionally filtered by their completion status and sorted.
     *
     * @param userId The unique identifier of the user who owns the task list.
     * @param taskListId The unique identifier of the task list whose tasks are to be retrieved.
     * @param completed Optional boolean to filter tasks by their completion status.
     * `true` for completed tasks, `false` for pending tasks, `null` for all tasks.
     * @param sortBy Optional field name by which to sort the tasks (e.g., "dueDate", "priority", "name").
     * These should match property names in the Task entity.
     * @param order Optional sort order ("asc" for ascending, "desc" for descending). Defaults to ascending.
     * @return A [List] of [TaskResponseDTO]s representing the tasks, filtered and sorted as requested.
     * Returns an empty list if the task list is not found or does not belong to the user.
     */
    fun getTasksByTaskListId(
        userId: String,
        taskListId: String,
        completed: Boolean?,
        sortBy: TaskSortField?,
        order: String?
    ): List<TaskResponseDTO>

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