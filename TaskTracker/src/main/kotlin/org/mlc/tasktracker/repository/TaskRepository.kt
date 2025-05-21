package org.mlc.tasktracker.repository

import org.mlc.tasktracker.model.Task
import org.mlc.tasktracker.model.User // Keep if User model is indirectly referenced by TaskList
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Spring Data JPA repository for the [Task] entity.
 * This interface provides out-of-the-box CRUD operations and custom query methods.
 */
@Repository
interface TaskRepository : JpaRepository<Task, String> {

    /**
     * Finds all tasks belonging to a specific task list, with a specified sort order.
     *
     * @param taskListId The ID of the TaskList whose tasks are to be retrieved.
     * @param sort The [Sort] object specifying the sorting criteria (field and direction).
     * @return A list of [Task] entities belonging to the specified task list.
     */
    fun findByTaskListId(taskListId: String, sort: Sort): List<Task>

    /**
     * Finds all tasks belonging to a specific task list, filtered by their completion status,
     * and with a specified sort order.
     *
     * @param taskListId The ID of the TaskList.
     * @param completed The completion status to filter by (`true` for completed, `false` for pending).
     * @param sort The [Sort] object specifying the sorting criteria (field and direction).
     * @return A list of [Task] entities matching the criteria.
     */
    fun findByTaskListIdAndCompleted(taskListId: String, completed: Boolean, sort: Sort): List<Task>
}