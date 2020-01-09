package com.jmartinal.data.source

interface LocationDataSource {
    suspend fun getCurrentRegion(): String?
}