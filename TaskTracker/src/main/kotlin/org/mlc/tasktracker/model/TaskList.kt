package org.mlc.tasktracker.model

import jakarta.persistence.*

/**
 * Represents a list that groups tasks, belonging to a specific user.
 * This class is a JPA entity mapped to the "task_lists" table in the database.
 *
 * @property id The unique identifier for the task list. Typically a UUID string, serving as the primary key.
 * @property title The title or name of the task list. Cannot be null.
 * @property user The [User] who owns this task list. This defines the "many" side of a many-to-one relationship.
 * The `user_id` column in the "task_lists" table serves as the foreign key.
 * The User object is loaded lazily.
 * @property tasks The collection of [Task] entities belonging to this task list.
 * This defines the "one" side of a one-to-many relationship.
 * Operations on a TaskList (e.g., deletion) will cascade to its associated Tasks.
 * Tasks are loaded lazily.
 */
@Entity
@Table(name = "task_lists")
data class TaskList(

  @Id
  @Column(length = 36)
  val id: String,

  @Column(nullable = false, length = 100)
  val title: String,

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  val user: User,

  @OneToMany(
    mappedBy = "taskList",
    cascade = [CascadeType.ALL],
    orphanRemoval = true,
    fetch = FetchType.LAZY
  )
  val tasks: List<Task> = mutableListOf()
)
