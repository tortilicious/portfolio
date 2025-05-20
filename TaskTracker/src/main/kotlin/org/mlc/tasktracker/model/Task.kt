package org.mlc.tasktracker.model

import jakarta.persistence.*
import java.time.LocalDate

/**
 * Represents an individual task item within a task list.
 * This class is a JPA entity mapped to the "tasks" table in the database.
 *
 * @property id The unique identifier for the task. Typically a UUID string, serving as the primary key.
 * @property name The name or title of the task. Cannot be null.
 * @property description An optional, more detailed description of the task.
 * @property completed A boolean flag indicating if the task is completed (true) or pending (false). Defaults to false.
 * @property dueDate The due date for the task, using [java.time.LocalDate]. This field is mandatory.
 * @property priority The priority of the task, defined by the [TaskPriority] enum. Defaults to [TaskPriority.MID].
 * Stored as a String (e.g., "HIGH", "MID", "LOW") in the database.
 * @property taskList The [TaskList] to which this task belongs. This defines the "many" side of a many-to-one relationship.
 * The `task_list_id` column in the "tasks" table serves as the foreign key.
 * The TaskList object is loaded lazily.
 */
@Entity
@Table(name = "tasks")
data class Task(

  @Id
  @Column(length = 36)
  val id: String,

  @Column(nullable = false, length = 200)
  var name: String,

  @Column(length = 1000)
  var description: String? = null,

  @Column(nullable = false)
  var completed: Boolean = false,

  @Column
  var dueDate: LocalDate? = null,

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 10)
  var priority: TaskPriority = TaskPriority.MID,

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "task_list_id", nullable = false)
  val taskList: TaskList
)
