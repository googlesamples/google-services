package com.google.samples.quickstart.canonical

import android.os.Bundle
import android.os.SystemClock
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.google.samples.quickstart.canonical.databinding.FragmentRunBinding
import kotlinx.android.synthetic.main.fragment_run.*


class RunFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val stopwatchVM: StopwatchViewModel by activityViewModels()
    private lateinit var binding : FragmentRunBinding

    private fun startStopTimer(chronometer : Chronometer) {
        if (!stopwatchVM.isWorking.value!!) {
            chronometer.base = SystemClock.elapsedRealtime() - stopwatchVM.pauseOffset.value!!
            chronometer.start()
            chronometer.showContextMenu()
            stopwatchVM.setWorkingStatus(true)
        } else {
            chronometer.stop()
            stopwatchVM.setPauseOffset(SystemClock.elapsedRealtime() - chronometer.base)
            stopwatchVM.setWorkingStatus(false)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_run, container, false)
        binding.lifecycleOwner = this
        binding.stopwatchViewModel = stopwatchVM
        return binding.root
    }

    companion object {
        const val RUN_FRAGMENT_TAG = "RunFragment"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.runBtn.setOnClickListener {
            startStopTimer(binding.chronometer)
        }

    }

    override fun onResume() {
        super.onResume()
        if (stopwatchVM.isWorking.value!!) {
            chronometer?.base = stopwatchVM.fragmentPauseStartTime.value!! - stopwatchVM.pauseOffset.value!!
            chronometer?.start()
        } else {
            chronometer?.base = SystemClock.elapsedRealtime() - stopwatchVM.pauseOffset.value!!
            chronometer?.stop()
        }
    }

    override fun onPause() {
        super.onPause()
        if (stopwatchVM.isWorking.value!!) {
            stopwatchVM.setPauseOffset(SystemClock.elapsedRealtime() - chronometer.base)
            stopwatchVM.setFragmentPauseStartTime(SystemClock.elapsedRealtime())
        }
    }
}