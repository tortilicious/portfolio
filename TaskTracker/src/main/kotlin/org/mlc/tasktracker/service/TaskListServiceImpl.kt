package org.mlc.tasktracker.service

import org.mlc.tasktracker.dto.tasklist.CreateTaskListRequestDTO
import org.mlc.tasktracker.dto.tasklist.TaskListResponseDTO
import org.mlc.tasktracker.dto.tasklist.UpdateTaskListRequestDTO
import org.mlc.tasktracker.exception.AuthorizationException
import org.mlc.tasktracker.exception.TaskListNotFoundException
import org.mlc.tasktracker.exception.UserNotFoundException
import org.mlc.tasktracker.model.TaskList
import org.mlc.tasktracker.repository.TaskListRepository
import org.mlc.tasktracker.repository.UserRepository
import org.mlc.tasktracker.service.mapper.toResponseDTO
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*


@Service
class TaskListServiceImpl(
    private val taskListRepository: TaskListRepository,
    private val userRepository: UserRepository
) : TaskListService {

    @Transactional
    override fun createTaskList(userId: String, createRequestDTO: CreateTaskListRequestDTO): TaskListResponseDTO {
        val user =
            userRepository.findByIdOrNull(userId) ?: throw UserNotFoundException("User with ID $userId not found")

        val newTaskList = TaskList(
            id = UUID.randomUUID().toString(),
            title = createRequestDTO.title,
            user = user,
        )
        val savedTaskList = taskListRepository.save(newTaskList)
        return savedTaskList.toResponseDTO()
    }


    @Transactional(readOnly = true)
    override fun getTaskListsByUserId(userId: String): List<TaskListResponseDTO> {
        val user = userRepository.findByIdOrNull(userId)
            ?: throw UserNotFoundException("User with ID $userId not found")

        val savedTasksList = user.taskLists.map { it.toResponseDTO() }
        return savedTasksList
    }

    @Transactional(readOnly = true)
    override fun getTaskListByIdAndUserId(taskListId: String, userId: String): TaskListResponseDTO {
        val taskListEntity = taskListRepository.findByIdAndUserId(taskListId, userId)
            ?: throw TaskListNotFoundException("Task list with ID $taskListId not found for user $userId, or user is not authorized.")
        return taskListEntity.toResponseDTO()
    }

    @Transactional
    override fun updateTaskList(
        userId: String,
        taskListId: String,
        updateRequestDTO: UpdateTaskListRequestDTO
    ): TaskListResponseDTO {
        val user =
            userRepository.findByIdOrNull(userId) ?: throw UserNotFoundException("User with ID $userId not found")
        taskListRepository.findByIdOrNull(taskListId)?.let { taskList ->
            if (taskList.user.id == user.id) {
                taskList.title = updateRequestDTO.title
                val updatedTaskList = taskListRepository.save(taskList)
                return updatedTaskList.toResponseDTO()

            } else {
                throw AuthorizationException("User with ID $userId not authorized to $taskListId task list.")
            }
        }
        throw TaskListNotFoundException("Task list with ID $taskListId not found")
    }


    @Transactional
    override fun deleteTaskList(userId: String, taskListId: String): Boolean {
        val user =
            userRepository.findByIdOrNull(userId) ?: throw UserNotFoundException("User with ID $userId not found")
        taskListRepository.findByIdOrNull(taskListId)?.let { taskList ->
            if (taskList.user.id == user.id) {
                val deletedTaskList = taskListRepository.delete(taskList)
                return true
            } else {
                throw AuthorizationException("User with ID $userId not authorized to $taskListId task list.")
            }
        }
        throw TaskListNotFoundException("Task list with ID $taskListId not found")
    }
}