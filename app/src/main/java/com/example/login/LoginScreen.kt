package com.example.login

import android.icu.util.LocaleData
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.navigation.fragment.findNavController
import com.example.login.Firebase.Firebase.auth
import com.example.login.Firebase.Firebase.db
import com.example.login.databinding.FragmentLoginScreenBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.util.regex.Pattern

class LoginScreen : Fragment() {

    lateinit var binding: FragmentLoginScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (auth.currentUser != null) {
            findNavController().navigate(R.id.action_loginScreen_to_getData)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginScreenBinding.inflate(layoutInflater)

        val date = LocalDateTime.now().toString()
        val data = mapOf(
            "lastLogin" to date
        )

        // Inflate the layout for this fragment
        binding.btnLogin.setOnClickListener {

            if (validations(
                    binding.email1.text.toString(),
                    binding.email1.text.toString(),
                    binding.pass1.text.toString()
                )
            ) {
                auth.signInWithEmailAndPassword(
                    binding.email1.text.toString(),
                    binding.pass1.text.toString()
                ).addOnSuccessListener {
                    Log.d("TAG---->", "Login")
                    db.collection("Users")
//                        .where(binding.email1.text.toString() == )
                        .document(auth.currentUser!!.uid)
                        .update(data).addOnSuccessListener {
                            Toast.makeText(requireContext(), "Login Successfully", Toast.LENGTH_SHORT)
                                .show()
                            findNavController().navigate(R.id.action_loginScreen_to_getData)
                        }.addOnFailureListener {
                            Toast.makeText(requireContext(), "Login Failed", Toast.LENGTH_SHORT)
                                .show()
                        }
                }.addOnFailureListener {
                    Log.d("TAG---->", it.message.toString())
                }

            } else {
                Toast.makeText(requireContext(), "Login Failure", Toast.LENGTH_SHORT)
                    .show()
            }

        }

        binding.account.setOnClickListener {
            findNavController().navigate(R.id.action_loginScreen_to_registration)
        }

        return binding.root
    }

    private fun validations(email: String, email1: String, pass: String): Boolean {

        when {
            binding.email1.text.toString().isNullOrEmpty() -> {
                binding.email1.error = "Enter email"
                return false
            }

            !Pattern.matches(Patterns.EMAIL_ADDRESS.toString(), binding.email1.text.toString().trim()) -> {
                binding.email1.error = "Enter correct email"
                return false
            }

            binding.pass1.text.toString().isNullOrEmpty() -> {
                binding.pass1.error = "Enter Password"
                return false
            }

            binding.pass1.length() < 6 || binding.pass1.length() > 12 -> {
                binding.pass1.error = "Password must be minimum 6 characters"
                return false
            }
        }
        return true
    }
}