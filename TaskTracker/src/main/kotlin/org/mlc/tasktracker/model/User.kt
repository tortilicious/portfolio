package org.mlc.tasktracker.model

import jakarta.persistence.*

/**
 * Represents a user of the application.
 * This class is a JPA entity mapped to the "users" table in the database.
 * It holds user credentials and their associated task lists.
 *
 * @property id The unique identifier for the user. Typically a UUID string, serving as the primary key.
 * @property userName The unique username chosen by the user. Stored in the "user_name" column and cannot be null.
 * @property password The hashed password for the user. Cannot be null.
 * @property taskLists The collection of [TaskList] entities associated with this user.
 * This defines the "one" side of a one-to-many relationship.
 * Operations on a User (e.g., deletion) will cascade to their associated TaskLists.
 * TaskLists are loaded lazily to optimize performance.
 */
@Entity
@Table(name = "users")
data class User(

    @Id
    val id: String,

    @Column(name = "user_name", nullable = false, unique = true, length = 50)
    val userName: String,

    @Column(nullable = false, length = 255)
    var password: String,


    @OneToMany(
        mappedBy = "user",
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    val taskLists: List<TaskList>
)