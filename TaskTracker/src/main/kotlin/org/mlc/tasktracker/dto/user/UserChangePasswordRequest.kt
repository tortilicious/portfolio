package org.mlc.tasktracker.dto.user

data class UserChangePasswordRequest(
    var oldPassword: String,
    var newPassword: String
)
