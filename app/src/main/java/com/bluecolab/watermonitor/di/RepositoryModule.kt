package com.bluecolab.watermonitor.di

import com.bluecolab.watermonitor.data.repository.FloodDataRepositoryImpl
import com.bluecolab.watermonitor.data.repository.WaterQualityRepositoryImpl
import com.bluecolab.watermonitor.domain.repository.FloodDataRepository
import com.bluecolab.watermonitor.domain.repository.WaterQualityRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindWaterQualityRepository(
        repositoryImpl: WaterQualityRepositoryImpl
    ): WaterQualityRepository

    @Binds
    @Singleton
    abstract fun bindFloodDataRepository(
        repositoryImpl: FloodDataRepositoryImpl
    ): FloodDataRepository
}
