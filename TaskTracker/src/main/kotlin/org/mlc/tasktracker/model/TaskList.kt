package org.mlc.tasktracker.model

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

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

  @Column(nullable = false, length = 100) // Removed unique = true from title for now
  val title: String,

  @ManyToOne(fetch = FetchType.LAZY) // Correct: TaskList belongs to one User
  @JoinColumn(name = "user_id", nullable = false) // Correct: Defines the foreign key column
  val user: User,

  @OneToMany(
    mappedBy = "taskList", // Correct: "taskList" is the property in Task that owns the relationship
    cascade = [CascadeType.ALL],
    orphanRemoval = true,
    fetch = FetchType.LAZY // Correct: Use LAZY for collections
  )
  val tasks: List<Task> = mutableListOf()
)
