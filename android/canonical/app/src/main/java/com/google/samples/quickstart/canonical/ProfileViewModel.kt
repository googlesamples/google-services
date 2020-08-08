package com.google.samples.quickstart.canonical

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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
        var singleRunIDList : MutableLiveData<ArrayList<String>> = MutableLiveData(ArrayList())
    )

    private lateinit var curAppUser: AppUser
    private var timeHMSString: MutableLiveData<String> = MutableLiveData(DEFAULT_TIME)
    private lateinit var runUserDocRef : DocumentReference

    private fun getUid() : String {
        return curAppUser.uid
    }

    private fun getTotalTimeMillisecond() : Long? {
        return curAppUser.totalTimeMillisecond.value
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

    private fun setSingleRunIDList(singleRunIDList : ArrayList<String>) {
        curAppUser.singleRunIDList.value = singleRunIDList
    }

    private fun convertMStoStringHMS(millionSeconds : Long) : String {
        return "%02d:%02d:%02d".format(
            TimeUnit.MILLISECONDS.toHours(millionSeconds),
            TimeUnit.MILLISECONDS.toMinutes(millionSeconds) % TimeUnit.HOURS.toMinutes(1),
            TimeUnit.MILLISECONDS.toSeconds(millionSeconds) % TimeUnit.MINUTES.toSeconds(1)
        )
    }

    private fun setTimeHMSString() {
        val ms = getTotalTimeMillisecond()
        val hms = ms ?.let{convertMStoStringHMS(ms)} ?: run { DEFAULT_TIME }
        timeHMSString.value = hms
        Log.d(PROFILE_VM_TAG, "setTimeHMSString hms: $hms")
    }

    private fun getNewSingleRunningRecordRef(singleRunningTimeMillionSeconds : Long, time: String) {

    }

    private fun syncAppUserStatistic() {
        runUserDocRef.get()
            .addOnSuccessListener {document ->
                Log.d(PROFILE_VM_TAG, "Get doc successfully")
                setTotalDistanceMeters(document.data!![KEY_TOTAL_DIS_M] as Long)
                setTotalEnergyCalories(document.data!![KEY_TOTAL_EN_CAL] as Long)
                setTotalTimeMillisecond(document.data!![KEY_TOTAL_TIME_MS] as Long)
                setSingleRunIDList(document.data!![KEY_SINGLE_RUN_ID_LIST] as ArrayList<String>)
                setTimeHMSString()
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

    fun getTimeHMSStringMutableLiveData() : MutableLiveData<String> {
        return timeHMSString
    }

    fun initAppUser(curFirebaseUser : FirebaseUser) {
        Log.d(PROFILE_VM_TAG, "initAppUser")
        curAppUser = AppUser(curFirebaseUser.displayName ?: "", curFirebaseUser.email ?: "",
            curFirebaseUser.uid, curFirebaseUser.photoUrl.toString())
        runUserDocRef = Firebase.firestore.collection(USER_COLLECTION_NAME).document(getUid())
        syncAppUserStatistic()
    }

    fun uploadNewRecord(singleRunningTimeMillionSeconds : Long, time : String) {

        val newTotalTimeMillisecond = getTotalTimeMillisecond()?.plus(
            singleRunningTimeMillionSeconds
        )
        Log.d(PROFILE_VM_TAG, "newTotalTimeMillisecond $newTotalTimeMillisecond")

//        val singleRunDocRef = Firebase.firestore.collection(RUN_COLLECTION_NAME).document()

        Firebase.firestore.runBatch { batch ->
            // TODO update single run list

            // update total time
            val updateRunUserData = hashMapOf(
                KEY_TOTAL_TIME_MS to newTotalTimeMillisecond,
                KEY_SINGLE_RUN_ID_LIST to FieldValue.arrayUnion("This is test")
            )
            batch.update(runUserDocRef, updateRunUserData)

        }
            .addOnSuccessListener {
                Log.d(PROFILE_VM_TAG, "")
                syncAppUserStatistic()
            }
    }


    companion object {
        const val PROFILE_VM_TAG = "ProfileVM"
        const val USER_COLLECTION_NAME = "RunUser"
        const val RUN_COLLECTION_NAME = "SingleRun"
        const val KEY_USR_NAME = "UserName"
        const val KEY_USR_EMAIL = "Email"
        const val KEY_TOTAL_DIS_M = "TotalDistanceMeters"
        const val KEY_TOTAL_EN_CAL = "TotalEnergyCalories"
        const val KEY_TOTAL_TIME_MS = "TotalTimeMillisecond"
        const val KEY_SINGLE_RUN_ID_LIST = "SingleRunIDList"
        const val DEFAULT_TIME = "00:00:00"
    }
}

class DownloadImageTask(var bmImage: ImageView) :
    AsyncTask<String?, Void?, Bitmap?>() {

    override fun onPostExecute(result: Bitmap?) {
        Log.d(DOWNLOAD_IMAGE_TASK_TAG, "onPostExecute")
        bmImage.setImageBitmap(result)
    }

    override fun doInBackground(vararg urls: String?): Bitmap? {
        val userPhotoUrl = urls[0]
        var userPhotoBitmap: Bitmap? = null
        try {
            val inStream: InputStream = URL(userPhotoUrl).openStream()
            userPhotoBitmap = BitmapFactory.decodeStream(inStream)
        } catch (e: Exception) {
            Log.e(DOWNLOAD_IMAGE_TASK_TAG, e.message!!)
        }
        return userPhotoBitmap
    }

    companion object {
        const val DOWNLOAD_IMAGE_TASK_TAG = "DownloadImageTask"
    }
}