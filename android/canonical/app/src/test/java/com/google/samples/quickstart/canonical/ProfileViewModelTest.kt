//package com.google.samples.quickstart.canonical
//
//import android.content.Context
//import androidx.arch.core.executor.testing.InstantTaskExecutorRule
//import androidx.test.core.app.ApplicationProvider
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import com.google.firebase.FirebaseApp
//import kotlinx.coroutines.android.awaitFrame
//import kotlinx.coroutines.awaitAll
//import org.junit.Test
//
//import org.junit.Assert.*
//import org.junit.Rule
//import org.junit.runner.RunWith
//
//@RunWith(AndroidJUnit4::class)
//class ProfileViewModelTest {
//
//    private val profileViewModelInstance : ProfileViewModel = ProfileViewModel()
//    @get:Rule
//    var instantExecutorRule = InstantTaskExecutorRule()
//
//    @Test
//    fun initAppUserTest() {
//        FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext())
//        profileViewModelInstance.initAppUser("Yang Li", "sjtuly1996@gmail.com",
//            "2FqSoO9f16ccTtPiXUlXYmjfXrI3",
//            "https://lh5.googleusercontent.com/-veHtMiUHDEQ/AAAAAAAAAAI/AAAAAAAAAAA/AMZuucnUbcZ3ffeXsGqfBhNJqgMbd1EWEw/s96-c/photo.jpg")
//        assertEquals(profileViewModelInstance.getTotalEnergyCaloriesMutableLiveData().value, "3")
//
//
//    }
//
//    @Test
//    fun refreshUser() {
//    }
//
//    @Test
//    fun uploadNewRecord() {
//    }
//}