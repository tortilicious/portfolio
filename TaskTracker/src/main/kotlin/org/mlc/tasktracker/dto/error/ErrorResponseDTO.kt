package org.mlc.tasktracker.dto.error


import java.time.LocalDateTime

data class ErrorResponseDTO(
    val timeStamp: LocalDateTime = LocalDateTime.now(),
    val status: Int,
    val error: String,
    val message: String?,
    val path: String? = null
)
