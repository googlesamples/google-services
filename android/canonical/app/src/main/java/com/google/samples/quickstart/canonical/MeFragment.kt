package com.google.samples.quickstart.canonical

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer


class MeFragment : Fragment() {

    private val signInVM: SignInViewModel by activityViewModels()
    private lateinit var observer : Observer<Boolean>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Add login status change listener
        observer = Observer {
            when (it) {
                true -> {
                    Log.d(ME_TAG, "firebaseUser is not null")
                }

                false -> {
                    Log.d(ME_TAG, "firebaseUser is null")
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
        return inflater.inflate(R.layout.fragment_me, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val logoutButton : Button = view.findViewById(R.id.logout_button)
        logoutButton.setOnClickListener {
            // Sign out both Google account and Firebase
            signInVM.signOut()
        }
        // update UI
        view.findViewById<TextView>(R.id.textView)?.text = signInVM.getFirebaseAuthCurUser()?.email
    }

    companion object {
        private const val ME_TAG = "MeFragment"
    }
}