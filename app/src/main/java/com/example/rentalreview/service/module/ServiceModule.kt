package com.example.rentalreview.service.module

import com.example.rentalreview.service.AccountService
import com.example.rentalreview.service.StorageService
import com.example.rentalreview.service.UploadImageService
import com.example.rentalreview.service.impl.AccountServiceImpl
import com.example.rentalreview.service.impl.StorageServiceImpl
import com.example.rentalreview.service.impl.UploadImageImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {
    @Binds
    abstract fun provideAccountService(impl: AccountServiceImpl): AccountService

    @Binds
    abstract fun provideStorageService(impl: StorageServiceImpl): StorageService

    @Binds
    abstract fun provideUploadImageService(impl: UploadImageImpl): UploadImageService
}