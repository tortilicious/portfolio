package org.mlc.tasktracker.service

import org.mlc.tasktracker.dto.task.CreateTaskRequestDTO
import org.mlc.tasktracker.dto.task.TaskResponseDTO
import org.mlc.tasktracker.dto.task.UpdateTaskRequestDTO
import org.mlc.tasktracker.exception.AuthorizationException
import org.mlc.tasktracker.exception.TaskListNotFoundException
import org.mlc.tasktracker.exception.TaskNotFoundException
import org.mlc.tasktracker.exception.UserNotFoundException
import org.mlc.tasktracker.model.Task
import org.mlc.tasktracker.repository.TaskListRepository
import org.mlc.tasktracker.repository.TaskRepository
import org.mlc.tasktracker.repository.UserRepository
import org.mlc.tasktracker.service.mapper.toResponseDTO
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * Implementation of the [TaskService] interface for managing [Task] entities.
 * This service handles the business logic related to task operations,
 * interacting with [TaskRepository], [TaskListRepository], and [UserRepository].
 *
 * @property taskRepository Repository for accessing and managing [Task] data.
 * @property taskListRepository Repository for accessing and managing [TaskList] data.
 * @property userRepository Repository for accessing and managing [User] data.
 */
@Service
class TaskServiceImpl(
    private val taskRepository: TaskRepository,
    private val taskListRepository: TaskListRepository,
    private val userRepository: UserRepository
) : TaskService {

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
    @Transactional
    override fun createTask(
        userId: String,
        taskListId: String,
        createTaskRequestDTO: CreateTaskRequestDTO
    ): TaskResponseDTO {
        val user = userRepository.findByIdOrNull(userId)
            ?: throw UserNotFoundException("User with ID $userId not found")

        val taskList = taskListRepository.findByIdAndUserId(taskListId, userId)
            ?: throw TaskListNotFoundException("Task list with ID $taskListId not found or does not belong to user $userId")

        val newTask = Task(
            id = UUID.randomUUID().toString(),
            name = createTaskRequestDTO.name,
            description = createTaskRequestDTO.description,
            dueDate = createTaskRequestDTO.dueDate,
            priority = createTaskRequestDTO.priority,
            taskList = taskList,
        )

        taskRepository.save(newTask)
        return newTask.toResponseDTO()
    }

    /**
     * Retrieves a specific task by its ID, ensuring that it belongs to the specified user.
     *
     * @param userId The unique identifier of the user attempting to access the task.
     * @param taskId The unique identifier of the task to retrieve.
     * @return A [TaskResponseDTO] representing the found task if it exists and belongs to the user, otherwise `null`.
     * @throws AuthorizationException if the user with the given [userId] is not authorized to access the task with the given [taskId].
     */
    @Transactional(readOnly = true)
    override fun getTaskById(userId: String, taskId: String): TaskResponseDTO {
        return taskRepository.findByIdOrNull(taskId)?.let { task ->
            if (task.taskList.user.id == userId) {
                task.toResponseDTO()
            } else {
                throw AuthorizationException("User with ID $userId is not authorized to access task with ID $taskId")
            }
        } ?: throw TaskNotFoundException("Task with ID $taskId not found")
    }

    /**
     * Retrieves all tasks belonging to a specific task list, optionally filtered by their completion status.
     *
     * @param userId The unique identifier of the user who owns the task list.
     * @param taskListId The unique identifier of the task list whose tasks are to be retrieved.
     * @param completed Optional boolean to filter tasks by their completion status (`true` for completed, `false` for pending, `null` for all).
     * @return A [List] of [TaskResponseDTO] representing the tasks in the specified task list, filtered by completion status if provided.
     * Returns an empty list if the task list is not found or does not belong to the user.
     */
    @Transactional(readOnly = true)
    override fun getTasksByTaskListId(userId: String, taskListId: String, completed: Boolean?): List<TaskResponseDTO> {
        val taskList = taskListRepository.findByIdAndUserId(taskListId, userId)
            ?: return emptyList()
        return if (completed == null) {
            taskRepository.findByTaskListId(taskListId).map { it.toResponseDTO() }
        } else {
            taskRepository.findByTaskListIdAndCompleted(taskListId, completed).map { it.toResponseDTO() }
        }
    }

    /**
     * Updates an existing task if it belongs to the specified user.
     *
     * @param userId The unique identifier of the user attempting to update the task.
     * @param taskId The unique identifier of the task to be updated.
     * @param updateTaskRequestDTO The DTO containing the updated information for the task.
     * @return A [TaskResponseDTO] representing the updated task if the operation is successful, otherwise `null`.
     * @throws AuthorizationException if the user with the given [userId] is not authorized to update the task with the given [taskId].
     */
    @Transactional
    override fun updateTask(
        userId: String,
        taskId: String,
        updateTaskRequestDTO: UpdateTaskRequestDTO
    ): TaskResponseDTO {
        return taskRepository.findByIdOrNull(taskId)?.let { task ->
            if (task.taskList.user.id == userId) {
                updateTaskRequestDTO.name?.let { task.name = it }
                updateTaskRequestDTO.description?.let { task.description = it }
                updateTaskRequestDTO.completed?.let { task.completed = it }
                updateTaskRequestDTO.dueDate?.let { task.dueDate = it }
                updateTaskRequestDTO.priority?.let { task.priority = it }

                val savedTask = taskRepository.save(task)
                savedTask.toResponseDTO()
            } else {
                throw AuthorizationException("User with ID $userId is not authorized to access task with ID $taskId")
            }
        } ?: throw TaskNotFoundException("Task with ID $taskId not found")
    }

    /**
     * Deletes a specific task if it belongs to the specified user.
     *
     * @param userId The unique identifier of the user attempting to delete the task.
     * @param taskId The unique identifier of the task to be deleted.
     * @return `true` if the task was successfully found and deleted, `false` otherwise.
     * @throws AuthorizationException if the user with the given [userId] is not authorized to delete the task with the given [taskId].
     * @throws TaskNotFoundException if the task with the given [taskId] is not found.
     */
    @Transactional
    override fun deleteTask(userId: String, taskId: String): Boolean {
        taskRepository.findByIdOrNull(taskId)?.let { taskToDelete ->
            if (userId == taskToDelete.taskList.user.id) {
                taskRepository.delete(taskToDelete)
                return true
            } else {
                throw AuthorizationException("User with ID $userId is not authorized to access task with ID $taskId")
            }
        }
        return false
    }
}