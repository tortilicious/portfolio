package org.mlc.tasktracker.model

import jakarta.persistence.Entity
import jakarta.persistence.Table


@Entity
@Table(name = "users")
data class User(

  val id: String,
  val userName: String,
  val password: String
)