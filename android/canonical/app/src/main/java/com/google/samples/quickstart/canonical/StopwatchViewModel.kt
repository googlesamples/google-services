package com.google.samples.quickstart.canonical

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StopwatchViewModel : ViewModel() {

    var pauseOffset = MutableLiveData<Long>(0L)
    var isWorking = MutableLiveData<Boolean>(false)
    var fragmentPauseStartTime = MutableLiveData<Long>(0L)

    fun setPauseOffset(pause_offset_value:Long){
        pauseOffset.value = pause_offset_value
    }

    fun setWorkingStatus(working_status:Boolean){
        isWorking.value = working_status
    }

    fun setFragmentPauseStartTime(fragment_pause_start_time:Long){
        fragmentPauseStartTime.value = fragment_pause_start_time
    }
}