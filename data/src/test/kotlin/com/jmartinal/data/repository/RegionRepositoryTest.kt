package com.jmartinal.data.repository

import com.jmartinal.data.PermissionManager
import com.jmartinal.data.PermissionManager.Permission.COARSE_LOCATION
import com.jmartinal.data.repository.RegionRepository.Companion.DEFAULT_REGION
import com.jmartinal.data.source.LocationDataSource
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RegionRepositoryTest {

    @Mock
    lateinit var locationDataSource: LocationDataSource

    @Mock
    lateinit var permissionManager: PermissionManager

    @Mock
    lateinit var regionRepository: RegionRepository

    @Before
    fun setup() {
        regionRepository = RegionRepository(locationDataSource, permissionManager)
    }

    @Test
    fun `returns default region when permission not granted`() {
        runBlocking {
            whenever(permissionManager.checkPermissions(listOf(COARSE_LOCATION)))
                .thenReturn(false)
            val result = regionRepository.getCurrentRegion()

            assertEquals(DEFAULT_REGION, result)
        }
    }

    @Test
    fun `returns region from location data source when permission granted`() {
        runBlocking {
            whenever(permissionManager.checkPermissions(listOf(COARSE_LOCATION)))
                .thenReturn(true)
            whenever(locationDataSource.getCurrentRegion()).thenReturn("ES")
            val result = regionRepository.getCurrentRegion()

            assertEquals("ES", result)
        }
    }

}