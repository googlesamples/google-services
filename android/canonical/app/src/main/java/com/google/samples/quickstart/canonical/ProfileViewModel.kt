package com.google.samples.quickstart.canonical

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.io.InputStream
import java.net.URL


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

    private fun getUid() : String {
        return curAppUser.uid
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

    fun getUserName() : String {
        return curAppUser.userName
    }

    fun getUserEmail() : String {
        return curAppUser.email
    }

    fun getUserPhotoURL() : String {
        return curAppUser.googleAccountProfileUrl
    }

    fun initAppUser(curFirebaseUser : FirebaseUser) {
        Log.d(PROFILE_VM_TAG, "initAppUser")
        curAppUser = AppUser(curFirebaseUser.displayName ?: "", curFirebaseUser.email ?: "",
            curFirebaseUser.uid, curFirebaseUser.photoUrl.toString())
    }

    fun initAppUserStatistic() {
        val uid = getUid()
        val ref = Firebase.firestore.collection(USER_COLLECTION_NAME).document(uid)
        ref.get()
            .addOnSuccessListener {document ->
                Log.d(PROFILE_VM_TAG, "Get doc successfully")
                setTotalDistanceMeters(document.data!![KEY_TOTAL_DIS_M] as Long)
                setTotalEnergyCalories(document.data!![KEY_TOTAL_EN_CAL] as Long)
                setTotalTimeMillisecond(document.data!![KEY_TOTAL_TIME_MS] as Long)
                setSingleRunIDList(document.data!![KEY_SINGLE_RUN_ID_LIST] as ArrayList<String>)
            }
            .addOnFailureListener {
                Log.w(PROFILE_VM_TAG, "Get doc Failed")
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