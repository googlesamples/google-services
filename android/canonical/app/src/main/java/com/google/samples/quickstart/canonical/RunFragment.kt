package com.google.samples.quickstart.canonical

import android.os.Bundle
import android.os.SystemClock
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Chronometer
import kotlinx.android.synthetic.main.fragment_run.*
import org.jetbrains.annotations.Nullable

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_run, container, false)
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
        var pauseOffset = 0L

//        chronometer.format = "00:00:00"
        runBtn.setOnClickListener (object : View.OnClickListener{
            var isWorking = false
            override fun onClick(v: View) {
                isWorking = if (!isWorking) {
                    chronometer.base = SystemClock.elapsedRealtime() - pauseOffset
                    chronometer.start()
                    true
                } else {
                    pauseOffset = SystemClock.elapsedRealtime() - chronometer.base
                    chronometer.stop()
                    false
                }
                run_btn.setText(if (!isWorking) R.string.start else R.string.stop)
            }
        })

    }
}