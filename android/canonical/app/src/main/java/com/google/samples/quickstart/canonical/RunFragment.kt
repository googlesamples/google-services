package com.google.samples.quickstart.canonical

import android.os.Bundle
import android.os.SystemClock
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import androidx.databinding.DataBindingUtil
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

    private lateinit var stopwatchVM: StopwatchViewModel
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        stopwatchVM = activity?.run {
            ViewModelProviders.of(this)[StopwatchViewModel::class.java]
        } ?: throw Exception("Null Activity")
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