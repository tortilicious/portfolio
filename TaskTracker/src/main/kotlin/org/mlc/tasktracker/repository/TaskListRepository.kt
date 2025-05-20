package org.mlc.tasktracker.repository

import org.mlc.tasktracker.model.TaskList
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


/**
 * Spring Data JPA repository for the [TaskList] entity.
 * This interface provides out-of-the-box CRUD operations and custom query methods.
 */
@Repository
interface TaskListRepository : JpaRepository<TaskList, String> {

    /**
     * Finds all task lists belonging to a specific user, identified by the user's ID.
     *
     * @param userId The user Id to search for.
     * @return A [List] of [TaskList] entities owned by the specified user.
     * Returns an empty list if the user has no task lists or if the user ID does not exist.
     */
    fun findByUserId(userId: String): List<TaskList>

    /**
     * Finds a specific task list by its own ID, but only if it also belongs to the specified user ID.
     *
     * @param id The unique identifier ([TaskList.id]) of the task list to find.
     * @param userId The unique identifier ([User.id]) of the user who must own the task list.
     * @return The [TaskList] entity if found and it belongs to the specified user; otherwise, `null`.
     */
    fun findByIdAndUserId(taskListId: String, userId: String): TaskList?


}