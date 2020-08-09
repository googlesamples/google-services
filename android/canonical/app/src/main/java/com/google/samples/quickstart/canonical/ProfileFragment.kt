package com.google.samples.quickstart.canonical

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.databinding.DataBindingUtil
import com.google.samples.quickstart.canonical.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {

    private val signInVM: SignInViewModel by activityViewModels()
    private val profileVM : ProfileViewModel by activityViewModels()
    private lateinit var observer : Observer<Boolean>
    private lateinit var binding : FragmentProfileBinding

    private fun downloadPhotoAndSetView(view: View) {
        val url = profileVM.getUserPhotoURL()
        if (url != "") {
            Log.d(PROFILE_TAG, url)
            DownloadImageTask(view.findViewById(R.id.usr_img))
                .execute(url)
        }
    }

    private fun setRunHistory() {
        val runHistoryListForView : ArrayList<ProfileViewModel.SingleRun>
                = profileVM.getRunHistoryListForView()
        val runHistoryAdapter = RunHistoryAdapter(requireContext(), runHistoryListForView)
        val runHistoryListView = view?.findViewById<ListView>(R.id.run_history_list_view)
        runHistoryListView?.adapter = runHistoryAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Add login status change listener.
        // When firebaseUser is null, signing out successfully
        observer = Observer {
            when (it) {
                true -> {
                    Log.d(PROFILE_TAG, "firebaseUser is not null")
                }

                false -> {
                    Log.d(PROFILE_TAG, "firebaseUser is null")
                    val intent = Intent(context, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                }
            }
        }
        // set LifeCycle owner with MeFragment. Observe will be destroyed when MeFragment is destroyed
        signInVM.getFirebaseAuthLogStatusLiveData().observe(this, observer)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        binding.lifecycleOwner = this
        binding.profileViewModel = profileVM
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val logoutButton : Button = view.findViewById(R.id.logout_button)
        logoutButton.setOnClickListener {
            // Sign out both Google account and Firebase
            signInVM.signOut()
        }
        // update UI
        downloadPhotoAndSetView(view)
        setRunHistory()
    }

    companion object {
        private const val PROFILE_TAG = "ProfileFragment"
    }
}
