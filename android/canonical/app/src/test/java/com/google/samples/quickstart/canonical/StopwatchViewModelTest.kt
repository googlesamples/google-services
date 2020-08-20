package com.google.samples.quickstart.canonical

import android.os.SystemClock
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(manifest=Config.NONE)
class StopwatchViewModelTest {

    private val stopwatchViewModelInstance : StopwatchViewModel = StopwatchViewModel()

    @Test
    fun pauseStopwatchTestSpecificOffset() {
        stopwatchViewModelInstance.pauseStopwatch(SystemClock.elapsedRealtime() - 100)
        assertEquals(stopwatchViewModelInstance.getIsStopwatchWorking(), false)
        assertEquals(stopwatchViewModelInstance.getPauseOffset(), 100L)
    }

    @Test
    fun pauseStopwatchTestZeroOffset() {
        stopwatchViewModelInstance.pauseStopwatch(SystemClock.elapsedRealtime())
        assertEquals(stopwatchViewModelInstance.getIsStopwatchWorking(), false)
        assertEquals(stopwatchViewModelInstance.getPauseOffset(), 0L)
    }

    @Test
    fun startStopwatch() {
        stopwatchViewModelInstance.startStopwatch()
        assertEquals(stopwatchViewModelInstance.getIsStopwatchWorking(), true)
        assertEquals(stopwatchViewModelInstance.getIsReadyForUpload(), true)
    }

    @Test
    fun resetStopwatch() {
        stopwatchViewModelInstance.resetStopwatch()
        assertEquals(stopwatchViewModelInstance.getIsStopwatchWorking(), false)
        assertEquals(stopwatchViewModelInstance.getIsReadyForUpload(), false)
        assertEquals(stopwatchViewModelInstance.getPauseOffset(), 0L)
        assertEquals(stopwatchViewModelInstance.getFragmentPauseStartTime(), 0L)
    }

    @Test
    fun saveStopwatchStatusSpecificOffset() {
        stopwatchViewModelInstance.saveStopwatchStatus(SystemClock.elapsedRealtime() - 100)
        assertEquals(stopwatchViewModelInstance.getPauseOffset(), 100L)
        assertEquals(stopwatchViewModelInstance.getFragmentPauseStartTime(), SystemClock.elapsedRealtime())
    }

    @Test
    fun saveStopwatchStatusZeroOffset() {
        stopwatchViewModelInstance.saveStopwatchStatus(SystemClock.elapsedRealtime())
        assertEquals(stopwatchViewModelInstance.getPauseOffset(), 0)
        assertEquals(stopwatchViewModelInstance.getFragmentPauseStartTime(), SystemClock.elapsedRealtime())
    }
}