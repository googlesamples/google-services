package com.google.samples.quickstart.canonical

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.URL
import java.util.concurrent.TimeUnit


class ProfileViewModel : ViewModel() {
    data class AppUser (
        var userName : String,
        var email : String,
        var uid : String,
        var googleAccountProfileUrl : String,
        var totalDistanceMeters : MutableLiveData<Long> = MutableLiveData(0),
        var totalEnergyCalories : MutableLiveData<Long> = MutableLiveData(0),
        var totalTimeMillisecond : MutableLiveData<Long> = MutableLiveData(0),
        var runHistoryList : ArrayList<HashMap<String, Any>> =ArrayList()
    )

    data class SingleRun (
        var time : String,
        var dateTime : String
    )

    private lateinit var runUserDocRef : DocumentReference
    private lateinit var curAppUser: AppUser
    private var runHistoryListForView: ArrayList<SingleRun> = ArrayList()
    private var totalEnergyCaloriesMutableLiveDataString : MutableLiveData<String> = MutableLiveData("")
    private var totalTimeMutableLiveDataString : MutableLiveData<String> = MutableLiveData("")


    private fun getUid() : String {
        return curAppUser.uid
    }

    private fun getTotalTimeMillisecond() : Long? {
        return curAppUser.totalTimeMillisecond.value
    }

    private fun getRunHistoryList() : ArrayList<HashMap<String, Any>> {
        val reversedRunHistory = curAppUser.runHistoryList
        reversedRunHistory.reverse()
        Log.d(PROFILE_VM_TAG, "size ${reversedRunHistory.size}")
        return reversedRunHistory
    }

    private fun setTotalDistanceMeters(totalDistanceMeters : Long) {
        curAppUser.totalDistanceMeters.value = totalDistanceMeters
    }

    private fun setTotalEnergyCalories(totalEnergyCalories : Long) {
        curAppUser.totalEnergyCalories.value = totalEnergyCalories
    }

    private fun setTotalTimeMillisecond(totalTimeMillisecond : Long) {
        curAppUser.totalTimeMillisecond.value = totalTimeMillisecond
    }

    private fun setTotalEnergyCaloriesString() {
        totalEnergyCaloriesMutableLiveDataString.value = curAppUser.totalEnergyCalories.value.toString()
    }

    private fun setTotalTimeString() {
        totalTimeMutableLiveDataString.value = convertMStoStringHMS(curAppUser.totalTimeMillisecond.value ?: 0)
    }

    private fun setRunHistoryList(runHistoryList : ArrayList<HashMap<String, Any>>) {
        curAppUser.runHistoryList = runHistoryList
    }

    private fun setRunHistoryListForView() {
        runHistoryListForView.clear()
        val runHistoryListOfHashMap = getRunHistoryList()
        for (singleRun in runHistoryListOfHashMap) {
            val singleRunTime : String = convertMStoStringHMS(singleRun[KEY_SINGLE_RUN_TIME] as Long)
            val singleRunTimestamp : String = singleRun[KEY_SINGLE_RUN_TIMESTAMP] as String
            runHistoryListForView.add(SingleRun(singleRunTime, singleRunTimestamp))
        }
    }

    private fun convertMStoStringHMS(millionSeconds : Long) : String {
        return "%02d:%02d:%02d".format(
            TimeUnit.MILLISECONDS.toHours(millionSeconds),
            TimeUnit.MILLISECONDS.toMinutes(millionSeconds) % TimeUnit.HOURS.toMinutes(1),
            TimeUnit.MILLISECONDS.toSeconds(millionSeconds) % TimeUnit.MINUTES.toSeconds(1)
        )
    }

    private fun calculateCalories(millionSeconds : Long) : Long {
        return millionSeconds.div(4500)
    }

    private fun syncAppUserStatistic(adapter: RunHistoryAdapter? = null) {
        runUserDocRef.get()
            .addOnSuccessListener {document ->
                if (document != null) {
                    Log.d(PROFILE_VM_TAG, "Get doc successfully")
                    setTotalDistanceMeters(document.data!![KEY_TOTAL_DIS_M] as Long)
                    setTotalEnergyCalories(document.data!![KEY_TOTAL_EN_CAL] as Long)
                    setTotalTimeMillisecond(document.data!![KEY_TOTAL_TIME_MS] as Long)
                    setRunHistoryList(document.data!![KEY_RUN_HISTORY] as ArrayList<HashMap<String, Any>>)
                    setRunHistoryListForView()
                    setTotalEnergyCaloriesString()
                    setTotalTimeString()
                    adapter?.notifyDataSetChanged()
                } else {
                    Log.d(PROFILE_VM_TAG, "No such user")
                }
            }
            .addOnFailureListener {
                Log.w(PROFILE_VM_TAG, "Get doc Failed")
            }
    }

    fun getUserName() : String {
        return curAppUser.userName
    }

    fun getUserEmail() : String {
        return curAppUser.email
    }

    fun getUserPhotoURL() : String {
        return curAppUser.googleAccountProfileUrl
    }

    fun getTimeHMSMutableLiveData() : MutableLiveData<String> {
        return totalTimeMutableLiveDataString
    }

    fun getTotalEnergyCaloriesMutableLiveData() : MutableLiveData<String> {
        return totalEnergyCaloriesMutableLiveDataString
    }

    fun getRunHistoryListForView(): ArrayList<SingleRun> {
        return runHistoryListForView
    }

    fun initAppUser(userName: String, email: String, uid: String, photoURL: String,
                    userCollectionName : String = USER_COLLECTION_NAME) {
        Log.d(PROFILE_VM_TAG, "initAppUser")
        curAppUser = AppUser(userName, email, uid, photoURL)
        runUserDocRef = Firebase.firestore.collection(userCollectionName).document(getUid())
        syncAppUserStatistic()
    }

    fun refreshUser(adapter: RunHistoryAdapter) {
        syncAppUserStatistic(adapter)
    }

    fun uploadNewRecord(singleRunningTimeMillionSeconds : Long, timestamp : String) {

        val singleRunningCalories = calculateCalories(singleRunningTimeMillionSeconds ?: 0)

        Log.d(PROFILE_VM_TAG, "newTotalTimeMillisecond $singleRunningTimeMillionSeconds")

        val singleRunData = hashMapOf(
            KEY_SINGLE_RUN_TIME to singleRunningTimeMillionSeconds,
            KEY_SINGLE_RUN_TIMESTAMP to timestamp
        )

        val updateRunUserData = hashMapOf(
            KEY_TOTAL_TIME_MS to FieldValue.increment(singleRunningTimeMillionSeconds),
            KEY_TOTAL_EN_CAL to FieldValue.increment(singleRunningCalories),
            KEY_RUN_HISTORY to FieldValue.arrayUnion(singleRunData)
        )


        Firebase.firestore.runBatch { batch ->
            batch.update(runUserDocRef, updateRunUserData as Map<String, Any>)

        }
            .addOnSuccessListener {
                Log.d(PROFILE_VM_TAG, "Upload record successfully")
                syncAppUserStatistic()
            }
    }

    suspend fun downloadImage(url: String): Bitmap? {
        var userPhotoBitmap: Bitmap? = null
        try {
            Log.d(PROFILE_VM_TAG, url)
            withContext(Dispatchers.IO) {
                val inStream: InputStream = URL(url).openStream()
                userPhotoBitmap = BitmapFactory.decodeStream(inStream)
            }
        } catch (e: Exception) {
            Log.e(PROFILE_VM_TAG, e.message!!)
        }
        return userPhotoBitmap
    }


    companion object {
        const val PROFILE_VM_TAG = "ProfileVM"

        const val USER_COLLECTION_NAME = "RunUser"
        const val KEY_USR_NAME = "UserName"
        const val KEY_USR_EMAIL = "Email"
        const val KEY_TOTAL_DIS_M = "TotalDistanceMeters"
        const val KEY_TOTAL_EN_CAL = "TotalEnergyCalories"
        const val KEY_TOTAL_TIME_MS = "TotalTimeMillisecond"
        const val KEY_RUN_HISTORY = "RunHistory"

        const val KEY_SINGLE_RUN_TIME = "Time"
        const val KEY_SINGLE_RUN_TIMESTAMP = "Timestamp"

        const val DEFAULT_TIME = "00:00:00"
    }
}