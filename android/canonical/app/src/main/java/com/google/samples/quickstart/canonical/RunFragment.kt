package com.google.samples.quickstart.canonical

import android.app.AlertDialog
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.samples.quickstart.canonical.databinding.FragmentRunBinding
import kotlinx.android.synthetic.main.fragment_run.*
import java.text.SimpleDateFormat
import java.util.*


class RunFragment : Fragment() {

    private val stopwatchVM: StopwatchViewModel by activityViewModels()
    private val profileViewModel : ProfileViewModel by activityViewModels()
    private lateinit var binding : FragmentRunBinding

    private fun pauseStopwatch() {
        running_chronometer.stop()
        stopwatchVM.pauseStopwatch(running_chronometer.base)
    }

    private fun startStopwatch() {
        running_chronometer.base = SystemClock.elapsedRealtime() - stopwatchVM.getPauseOffset()
        running_chronometer.start()
        stopwatchVM.startStopwatch()
    }

    private fun resetStopwatch() {
        running_chronometer.base = SystemClock.elapsedRealtime()
        running_chronometer.stop()
        stopwatchVM.resetStopwatch()
    }

    private fun startOrPauseStopwatch() {
        if (!stopwatchVM.getIsStopwatchWorking()) {
            // pause/init status -> start status
            startStopwatch()
        } else {
            // start status -> pause/init status
            pauseStopwatch()
        }
    }

    private fun submitRecord() {
        if (!stopwatchVM.getIsReadyForUpload()) {
            Toast.makeText(context, getString(R.string.submit_illegal), Toast.LENGTH_SHORT).show()
            return
        }

        // pause stopwatch if stopwatch is working before user make a confirmation
        if (stopwatchVM.getIsStopwatchWorking()) {
            pauseStopwatch()
        }

        // build alert dialog
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setMessage(getString(R.string.submit_confirm_dialog_message))
            .setCancelable(false)
            // user confirm submission
            .setPositiveButton(getString(R.string.dialog_confirm_button)) { dialog, id ->
                Toast.makeText(context, "Submitted", Toast.LENGTH_SHORT).show()
                profileViewModel.uploadNewRecord(stopwatchVM.getPauseOffset(), getCurDateAndTime())
                resetStopwatch()
            }
            // user cancel submission
            .setNegativeButton(getString(R.string.dialog_cancel_button)) { dialog, id ->
                dialog.cancel()
            }

        // submit alert show
        val alert = dialogBuilder.create()
        alert.setTitle(getString(R.string.submit_confirm_dialog_title))
        alert.show()
    }

    private fun getCurDateAndTime() : String {
        val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(Date())
        Log.d(RUN_FRAGMENT_TAG,"date $date")
        return date
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.startPauseBtn.setOnClickListener {
            startOrPauseStopwatch()
        }

        binding.resetBtn.setOnClickListener {
            resetStopwatch()
        }

        binding.submitBtn.setOnClickListener {
            submitRecord()
        }

    }

    override fun onResume() {
        super.onResume()
        if (stopwatchVM.getIsStopwatchWorking()) {
            running_chronometer.base = stopwatchVM.getActualStartTimeBeforeFragmentPause()
            running_chronometer.start()
        } else {
            running_chronometer.base = SystemClock.elapsedRealtime() - stopwatchVM.getPauseOffset()
            running_chronometer.stop()
        }
    }

    override fun onPause() {
        super.onPause()
        if (stopwatchVM.getIsStopwatchWorking()) {
            stopwatchVM.saveStopwatchStatus(running_chronometer.base)
        }
    }

    companion object {
        const val RUN_FRAGMENT_TAG = "RunFragment"
    }
}