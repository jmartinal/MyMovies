package com.jmartinal.data

interface PermissionManager {

    suspend fun checkPermissions(permissions: List<Permission>): Boolean

    fun requestPermission(permission: String, continuation: (Boolean) -> Unit)

    enum class Permission {
        COARSE_LOCATION
    }
}