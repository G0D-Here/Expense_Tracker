package com.example.expensetracker.di

import com.example.expensetracker.data.AuthRepository
import com.example.expensetracker.data.AuthRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {
    @Provides
    fun authRepository(impl: AuthRepositoryImpl): AuthRepository = impl

    @Provides
    fun firebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun firebaseFireStore(): FirebaseFirestore = FirebaseFirestore.getInstance()

}