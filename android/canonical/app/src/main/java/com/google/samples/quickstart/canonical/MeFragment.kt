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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


/**
 * A simple [Fragment] subclass.
 * Use the [MeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MeFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_me, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val logoutButton : Button = view.findViewById(R.id.logout_button)
        logoutButton.setOnClickListener {
            // Sign out both Google account and Firebase
            FirebaseAuth.getInstance().signOut()
            (activity as MainActivity).googleSignInClient.signOut().addOnCompleteListener {
                val intent = Intent(context, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }


    override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(this.context)
        account?.let {
            Log.i(ME_TAG, "Already login")
            view?.findViewById<TextView>(R.id.textView)?.text = account.displayName
        } ?.run {
            Log.i(ME_TAG, "No login")
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MeFragment.
         */

        private const val ME_TAG = "MeFragment"

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}