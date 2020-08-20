package com.google.samples.quickstart.canonical

import android.os.SystemClock
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StopwatchViewModel : ViewModel() {

    private var pauseOffset = MutableLiveData<Long>(0L)
    private var fragmentPauseStartTime = MutableLiveData<Long>(0L)
    private var isStopwatchWorking = MutableLiveData<Boolean>(false)
    private var isReadyForUpload = MutableLiveData<Boolean>(false)

    private fun setPauseOffset(pause_offset_value:Long){
        pauseOffset.value = pause_offset_value
    }

    private fun setIsStopwatchWorkingStatus(working_status:Boolean){
        isStopwatchWorking.value = working_status
    }

    private fun setIsReadyForUploadStatus(upload_status:Boolean){
        isReadyForUpload.value = upload_status
    }

    private fun setFragmentPauseStartTime(fragment_pause_start_time:Long){
        fragmentPauseStartTime.value = fragment_pause_start_time
    }

    fun getFragmentPauseStartTime() : Long {
        return fragmentPauseStartTime.value!!
    }

    fun getPauseOffset() : Long {
        return pauseOffset.value!!
    }

    fun getIsStopwatchWorkingMutableLiveData() : MutableLiveData<Boolean> {
        return isStopwatchWorking
    }

    fun getIsStopwatchWorking() : Boolean {
        return isStopwatchWorking.value!!
    }

    fun getIsReadyForUpload() : Boolean {
        return isReadyForUpload.value!!
    }

    fun getActualStartTimeBeforeFragmentPause(): Long {
        return getFragmentPauseStartTime() - getPauseOffset()
    }

    fun pauseStopwatch(curStopwatchBase : Long) {
        setPauseOffset(SystemClock.elapsedRealtime() - curStopwatchBase)
        setIsStopwatchWorkingStatus(false)
    }

    fun startStopwatch() {
        setIsStopwatchWorkingStatus(true)
        setIsReadyForUploadStatus(true)
    }

    fun resetStopwatch() {
        setPauseOffset(0)
        setIsStopwatchWorkingStatus(false)
        setIsReadyForUploadStatus(false)
        setFragmentPauseStartTime(0)
    }

    fun saveStopwatchStatus(curStopwatchBase : Long) {
        setPauseOffset(SystemClock.elapsedRealtime() - curStopwatchBase)
        setFragmentPauseStartTime(SystemClock.elapsedRealtime())
    }
}