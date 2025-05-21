package org.mlc.tasktracker.controller

import org.mlc.tasktracker.dto.task.CreateTaskRequestDTO
import org.mlc.tasktracker.dto.task.TaskResponseDTO
import org.mlc.tasktracker.dto.task.UpdateTaskRequestDTO
import org.mlc.tasktracker.service.TaskService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/lists")
class TaskController (private val taskService: TaskService) {

    /**
     * Creates a new task within a specific task list for a given user.
     *
     * This endpoint handles HTTP POST requests to `/api/lists/{listId}/tasks`.
     * The user ID is extracted from the `X-User-Id` request header, and the task list ID
     * from the path variable, to ensure the task is created under the correct ownership.
     *
     * @param userId The ID of the user creating the task, obtained from the "X-User-Id" header.
     * @param listId The ID of the task list where the new task will be added, from the path variable.
     * @param createTaskRequestDTO The [CreateTaskRequestDTO] containing the details for the new task.
     * @return A [ResponseEntity] containing the [TaskResponseDTO] of the newly created task
     * and an HTTP status of [HttpStatus.CREATED].
     * @see TaskService.createTask
     * @see CreateTaskRequestDTO
     * @see TaskResponseDTO
     */
    @PostMapping("/{listId}/tasks")
    fun createTask(
        @RequestHeader("X-User-Id") userId: String,
        @PathVariable listId: String,
        @RequestBody createTaskRequestDTO: CreateTaskRequestDTO,
    ): ResponseEntity<TaskResponseDTO> {
        val createdTask = taskService.createTask(userId, listId, createTaskRequestDTO)
        return ResponseEntity(createdTask, HttpStatus.CREATED)
    }

    /**
     * Fetches tasks from a specific task list for a given user, with optional filtering by completion status.
     *
     * This endpoint handles HTTP GET requests to `/api/lists/{listId}/tasks`.
     * Tasks can be filtered by adding a `?completed=true` or `?completed=false` query parameter.
     * If the `completed` parameter is not provided, all tasks in the list are returned.
     *
     * @param userId The ID of the user requesting the tasks, obtained from the "X-User-Id" header.
     * @param listId The ID of the task list whose tasks are to be fetched, from the path variable.
     * @param completed An optional boolean query parameter to filter tasks by their completion status.
     * `true` for completed tasks, `false` for pending tasks, `null` for all tasks.
     * @return A [ResponseEntity] containing a [List] of [TaskResponseDTO]s
     * and an HTTP status of [HttpStatus.OK].
     * @see TaskService.getTasksByTaskListId
     * @see TaskResponseDTO
     */
    @GetMapping("/{listId}/tasks")
    fun fetchTasks(
        @RequestHeader("X-User-Id") userId: String,
        @PathVariable listId: String,
        @RequestParam(required = false) completed: Boolean?
    ): ResponseEntity<List<TaskResponseDTO>> {
        val list = taskService.getTasksByTaskListId(userId, listId, completed)
        return ResponseEntity(list, HttpStatus.OK)
    }

    /**
     * Retrieves a specific task by its ID for a given user.
     *
     * This endpoint handles HTTP GET requests to `/api/lists/{listId}/tasks/{taskId}`.
     * It ensures that only the owner of the task (via its associated task list) can access it.
     *
     * @param userId The ID of the user requesting the task, obtained from the "X-User-Id" header.
     * @param listId The ID of the task list to which the task belongs, from the path variable.
     * @param taskId The unique ID of the task to fetch, extracted from the path variable.
     * @return A [ResponseEntity] containing the [TaskResponseDTO] of the requested task
     * and an HTTP status of [HttpStatus.OK].
     * @see TaskService.getTaskById
     * @see TaskResponseDTO
     */
    @GetMapping("/{listId}/tasks/{taskId}")
    fun fetchTaskById(
        @RequestHeader("X-User-Id") userId: String,
        @PathVariable listId: String,
        @PathVariable taskId: String
    ): ResponseEntity<TaskResponseDTO> {
        val task = taskService.getTaskById(userId, taskId)
        return ResponseEntity(task, HttpStatus.OK)
    }

    /**
     * Updates an existing task for a specific user.
     *
     * This endpoint handles HTTP PUT requests to `/api/lists/{listId}/tasks/{taskId}`.
     * It ensures that only the owner of the task can update it.
     * The `taskId` identifies the task to be updated, and `updateTaskRequestDTO` provides
     * the new details.
     *
     * @param userId The ID of the user updating the task, obtained from the "X-User-Id" header.
     * @param listId The ID of the task list to which the task belongs, from the path variable.
     * @param taskId The unique ID of the task to update, extracted from the path variable.
     * @param updateTaskRequestDTO The [UpdateTaskRequestDTO] containing the updated information for the task.
     * @return A [ResponseEntity] containing the [TaskResponseDTO] of the updated task
     * and an HTTP status of [HttpStatus.OK].
     * @see TaskService.updateTask
     * @see UpdateTaskRequestDTO
     * @see TaskResponseDTO
     */
    @PutMapping("/{listId}/tasks/{taskId}")
    fun updateTask(
        @RequestHeader("X-User-Id") userId: String,
        @PathVariable listId: String,
        @PathVariable taskId: String,
        @RequestBody updateTaskRequestDTO: UpdateTaskRequestDTO
    ): ResponseEntity<TaskResponseDTO> {
        val updatedTask = taskService.updateTask(userId, taskId, updateTaskRequestDTO)
        return ResponseEntity(updatedTask, HttpStatus.OK)
    }

    /**
     * Deletes a specific task for a given user.
     *
     * This endpoint handles HTTP DELETE requests to `/api/lists/{listId}/tasks/{taskId}`.
     * It ensures that only the owner of the task can delete it.
     *
     * @param userId The ID of the user deleting the task, obtained from the "X-User-Id" header.
     * @param listId The ID of the task list to which the task belongs, from the path variable.
     * @param taskId The unique ID of the task to delete, extracted from the path variable.
     * @return A [ResponseEntity] with an HTTP status of [HttpStatus.NO_CONTENT] upon successful deletion.
     * The body typically contains `true` indicating success, though for `NO_CONTENT` the body is often empty.
     * @see TaskService.deleteTask
     */
    @DeleteMapping("/{listId}/tasks/{taskId}")
    fun deleteTask(
        @RequestHeader("X-User-Id") userId: String,
        @PathVariable listId: String,
        @PathVariable taskId: String
    ): ResponseEntity<Boolean> {
        val deleted = taskService.deleteTask(userId, taskId)
        return ResponseEntity(deleted, HttpStatus.NO_CONTENT)
    }
}