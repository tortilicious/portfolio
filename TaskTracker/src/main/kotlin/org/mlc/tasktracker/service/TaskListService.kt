package org.mlc.tasktracker.service

import org.mlc.tasktracker.dto.tasklist.CreateTaskListRequestDTO
import org.mlc.tasktracker.dto.tasklist.TaskListResponseDTO
import org.mlc.tasktracker.dto.tasklist.UpdateTaskListRequestDTO

/**
 * Service interface for managing [TaskList] entities.
 * Defines the contract for business operations related to task lists,
 * ensuring that operations are performed in the context of a specific user.
 */
interface TaskListService {

    /**
     * Creates a new task list for a specified user.
     *
     * @param userId The unique identifier of the user who will own this task list.
     * @param createRequestDTO The DTO containing the information for the new task list, primarily the title.
     * @return A [TaskListResponseDTO] representing the newly created task list.
     * @throws UserNotFoundException if the user with the given [userId] is not found.
     * @throws IllegalArgumentException if the [createRequestDTO] is invalid (e.g., blank title).
     */
    fun createTaskList(userId: String, createRequestDTO: CreateTaskListRequestDTO): TaskListResponseDTO

    /**
     * Retrieves all task lists belonging to a specific user.
     *
     * @param userId The unique identifier of the user whose task lists are to be retrieved.
     * @return A [List] of [TaskListResponseDTO]s representing the user's task lists.
     * Returns an empty list if the user has no task lists.
     * @throws UserNotFoundException if the user with the given [userId] is not found.
     */
    fun getTaskListsByUserId(userId: String): List<TaskListResponseDTO>

    /**
     * Retrieves a specific task list by its ID, but only if it belongs to the specified user.
     *
     * @param userId The unique identifier of the user who should own the task list.
     * @param taskListId The unique identifier of the task list to retrieve.
     * @return A [TaskListResponseDTO] representing the found task list.
     * @throws TaskListNotFoundException if the task list is not found or does not belong to the user.
     */
    fun getTaskListByIdAndUserId(taskListId: String, userId: String): TaskListResponseDTO

    /**
     * Updates an existing task list for a specified user.
     * Only the owner of the task list can update it.
     *
     * @param userId The unique identifier of the user attempting to update the task list.
     * @param taskListId The unique identifier of the task list to be updated.
     * @param updateRequestDTO The DTO containing the updated information for the task list (e.g., new title).
     * @return A [TaskListResponseDTO] representing the updated task list.
     * @throws TaskListNotFoundException if the task list is not found.
     * @throws AuthorizationException if the user is not authorized to update the task list.
     * @throws IllegalArgumentException if the [updateRequestDTO] is invalid.
     */
    fun updateTaskList(
        userId: String,
        taskListId: String,
        updateRequestDTO: UpdateTaskListRequestDTO
    ): TaskListResponseDTO

    /**
     * Deletes a specific task list, but only if it belongs to the specified user.
     * All tasks within the list will also be deleted due to cascading settings in the entity.
     *
     * @param userId The unique identifier of the user attempting to delete the task list.
     * @param taskListId The unique identifier of the task list to be deleted.
     * @return `true` if the task list was successfully found and deleted.
     * @throws TaskListNotFoundException if the task list is not found.
     * @throws AuthorizationException if the user is not authorized to delete the task list.
     */
    fun deleteTaskList(userId: String, taskListId: String): Boolean
}