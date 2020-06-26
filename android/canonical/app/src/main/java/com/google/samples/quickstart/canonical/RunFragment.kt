package com.google.samples.quickstart.canonical

import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Chronometer
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.google.samples.quickstart.canonical.databinding.FragmentRunBinding
import kotlinx.android.synthetic.main.fragment_run.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RunFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RunFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var stopwatchViewModel: StopwatchView
    private var pauseOffset = 0L
    private var isWorking = false
    private var fragmentPauseStartTime = 0L

    private fun startStopTimer(chronometer : Chronometer) {
        isWorking = if (!isWorking) {
            chronometer.base = SystemClock.elapsedRealtime() - pauseOffset
            chronometer.start()
            chronometer.showContextMenu()
            stopwatchViewModel.setWorkingStatus(true)
            true
        } else {
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.base
            chronometer.stop()
            stopwatchViewModel.setPauseOffset(SystemClock.elapsedRealtime() - chronometer.base)
            stopwatchViewModel.setWorkingStatus(false)
            false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        stopwatchViewModel = activity?.run {
            ViewModelProviders.of(this)[StopwatchView::class.java]
        } ?: throw Exception("Null Activity")

        pauseOffset = stopwatchViewModel.pauseOffset.value!!
        isWorking = stopwatchViewModel.isWorking.value!!
        fragmentPauseStartTime = stopwatchViewModel.fragmentPauseStartTime.value!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : FragmentRunBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_run, container, false)
        binding.lifecycleOwner = this
        binding.stopwatchViewModel = stopwatchViewModel
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RunFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RunFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val chronometer = view.findViewById<Chronometer>(R.id.chronometer)
        val runBtn = view.findViewById<Button>(R.id.run_btn)

        runBtn.setOnClickListener {
            startStopTimer(chronometer)
        }

    }

    override fun onResume() {
        super.onResume()
        if (isWorking) {
            chronometer?.base = fragmentPauseStartTime - pauseOffset
            chronometer?.start()
        } else {
            chronometer?.base = SystemClock.elapsedRealtime() - pauseOffset
            chronometer?.stop()
        }
    }

    override fun onPause() {
        super.onPause()
        if (isWorking) {
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.base
            fragmentPauseStartTime = SystemClock.elapsedRealtime()
            stopwatchViewModel.setPauseOffset(pauseOffset)
            stopwatchViewModel.setFragmentPauseStartTime(fragmentPauseStartTime)
        }
    }
}