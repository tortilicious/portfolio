package org.mlc.tasktracker.controller

import org.mlc.tasktracker.dto.tasklist.CreateTaskListRequestDTO
import org.mlc.tasktracker.dto.tasklist.TaskListResponseDTO
import org.mlc.tasktracker.dto.tasklist.UpdateTaskListRequestDTO
import org.mlc.tasktracker.service.TaskListService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/lists")
class TaskListController(
    private val taskListService: TaskListService
) {

    /**
     * Creates a new task list for a specific user.
     *
     * This endpoint handles HTTP POST requests to `/api/lists`.
     * The user ID is extracted from the `X-User-Id` request header to associate the
     * new task list with the correct user.
     *
     * @param userId The ID of the user creating the task list, obtained from the "X-User-Id" header.
     * @param createRequestDTO The [CreateTaskListRequestDTO] containing the details for the new task list.
     * @return A [ResponseEntity] containing the [TaskListResponseDTO] of the newly created task list
     * and an HTTP status of [HttpStatus.CREATED].
     * @see TaskListService.createTaskList
     * @see CreateTaskListRequestDTO
     * @see TaskListResponseDTO
     */
    @PostMapping
    fun createTaskList(
        @RequestHeader("X-User-Id") userId: String,
        @RequestBody createRequestDTO: CreateTaskListRequestDTO,
    ): ResponseEntity<TaskListResponseDTO> {

        val createdTaskList = taskListService.createTaskList(userId, createRequestDTO)
        return ResponseEntity(createdTaskList, HttpStatus.CREATED)
    }

    /**
     * Fetches a specific task list by its ID for a given user.
     *
     * This endpoint handles HTTP GET requests to `/api/lists/{listId}`.
     * It ensures that only the owner of the task list can access it by comparing
     * the provided `userId` from the header with the task list's owner ID.
     *
     * @param userId The ID of the user requesting the task list, obtained from the "X-User-Id" header.
     * @param listId The unique ID of the task list to fetch, extracted from the path variable.
     * @return A [ResponseEntity] containing the [TaskListResponseDTO] of the requested task list
     * and an HTTP status of [HttpStatus.OK].
     * @see TaskListService.getTaskListByIdAndUserId
     * @see TaskListResponseDTO
     */
    @GetMapping("/{listId}")
    fun fetchTaskListById(
        @RequestHeader("X-User-Id") userId: String,
        @PathVariable listId: String,
    ): ResponseEntity<TaskListResponseDTO> {

        val taskList = taskListService.getTaskListByIdAndUserId(listId, userId)
        return ResponseEntity(taskList, HttpStatus.OK)
    }

    /**
     * Fetches all task lists owned by a specific user.
     *
     * This endpoint handles HTTP GET requests to `/api/lists`.
     * The `userId` from the request header is used to retrieve all task lists
     * associated with that user.
     *
     * @param userId The ID of the user whose task lists are to be fetched, obtained from the "X-User-Id" header.
     * @return A [ResponseEntity] containing a [List] of [TaskListResponseDTO]s
     * and an HTTP status of [HttpStatus.OK].
     * @see TaskListService.getTaskListsByUserId
     * @see TaskListResponseDTO
     */
    @GetMapping
    fun fetchAllTaskLists(
        @RequestHeader("X-User-Id") userId: String,
    ): ResponseEntity<List<TaskListResponseDTO>> {

        val list = taskListService.getTaskListsByUserId(userId)
        return ResponseEntity(list, HttpStatus.OK)
    }

    /**
     * Updates an existing task list for a specific user.
     *
     * This endpoint handles HTTP PUT requests to `/api/lists/{listId}`.
     * It ensures that only the owner of the task list can update it.
     * The `listId` identifies the task list to be updated, and `updateRequestDTO` provides
     * the new details.
     *
     * @param userId The ID of the user updating the task list, obtained from the "X-User-Id" header.
     * @param listId The unique ID of the task list to update, extracted from the path variable.
     * @param updateRequestDTO The [UpdateTaskListRequestDTO] containing the updated details for the task list.
     * @return A [ResponseEntity] containing the [TaskListResponseDTO] of the updated task list
     * and an HTTP status of [HttpStatus.OK].
     * @see TaskListService.updateTaskList
     * @see UpdateTaskListRequestDTO
     * @see TaskListResponseDTO
     */
    @PutMapping("/{listId}")
    fun updateTaskList(
        @RequestHeader("X-User-Id") userId: String,
        @PathVariable listId: String,
        @RequestBody updateRequestDTO: UpdateTaskListRequestDTO,
    ): ResponseEntity<TaskListResponseDTO> {
        val updatedTaskList = taskListService.updateTaskList(userId, listId, updateRequestDTO)
        return ResponseEntity(updatedTaskList, HttpStatus.OK)
    }

    /**
     * Deletes a specific task list for a given user.
     *
     * This endpoint handles HTTP DELETE requests to `/api/lists/{listId}`.
     * It ensures that only the owner of the task list can delete it.
     *
     * @param userId The ID of the user deleting the task list, obtained from the "X-User-Id" header.
     * @param listId The unique ID of the task list to delete, extracted from the path variable.
     * @return A [ResponseEntity] with an HTTP status of [HttpStatus.NO_CONTENT] upon successful deletion.
     * The body contains `true` indicating success, though for `NO_CONTENT` the body is typically empty.
     * @see TaskListService.deleteTaskList
     */
    @DeleteMapping("/{listId}")
    fun deleteTaskList(
        @RequestHeader("X-User-Id") userId: String,
        @PathVariable listId: String,
    ): ResponseEntity<Boolean> {
        val deletedTaskList = taskListService.deleteTaskList(userId, listId)
        return ResponseEntity(deletedTaskList, HttpStatus.NO_CONTENT)
    }
}