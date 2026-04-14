package com.example.voicemind.di

import android.content.Context
import androidx.room3.Room
import com.example.voicemind.data.local.VocabDatabase
import com.example.voicemind.data.local.dao.UserProfileDao
import com.example.voicemind.data.repository.AuthRepositoryImpl
import com.example.voicemind.data.repository.UserProfileRepositoryImpl
import com.example.voicemind.domain.repository.AuthRepository
import com.example.voicemind.domain.repository.UserProfileRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideAuthRepository(
        firebaseAuth: FirebaseAuth
    ): AuthRepository = AuthRepositoryImpl(firebaseAuth)

    @Provides
    @Singleton
    fun provideVocabDatabase(@ApplicationContext context: Context): VocabDatabase =
        Room.databaseBuilder(context, VocabDatabase::class.java, "vocabmind.db").build()

    @Provides
    fun provideUserProfileDao(db: VocabDatabase): UserProfileDao = db.userProfileDao()

    @Provides
    @Singleton
    fun provideUserProfileRepository(
        firestore: FirebaseFirestore,
        dao: UserProfileDao
    ): UserProfileRepository = UserProfileRepositoryImpl(firestore, dao)
}