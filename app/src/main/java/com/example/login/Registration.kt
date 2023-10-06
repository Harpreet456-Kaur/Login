package com.example.login

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.login.Firebase.Firebase.auth
import com.example.login.Firebase.Firebase.db
import com.example.login.Models.NoteModel
import com.example.login.databinding.FragmentRegistrationBinding
import com.google.firebase.storage.FirebaseStorage
import java.time.LocalDateTime
import java.util.regex.Pattern

class Registration : Fragment() {

    lateinit var binding: FragmentRegistrationBinding
    var storageRef= FirebaseStorage.getInstance()
    private var btmap : Bitmap? = null
    private var imageUri : Uri?=null
    var noteModel= NoteModel()

    var getPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()){
            isGranted->
        if(isGranted){
            Toast.makeText(requireActivity(), "Granted", Toast.LENGTH_SHORT).show()
            getImage.launch("image/*")
        }else{
            Toast.makeText(requireActivity(),"Not Granted", Toast.LENGTH_SHORT).show()
        }
    }

    var getImage = registerForActivityResult(ActivityResultContracts.GetContent()){
        System.out.println("it $it")
        binding.image.setImageURI(it)
        it?.let {
            btmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, it)
            binding.image.setImageBitmap(btmap)
            imageUri = it
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegistrationBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        binding.image.setOnClickListener {
            if(ContextCompat.checkSelfPermission(requireActivity(),
                    READ_EXTERNAL_STORAGE
                )== PackageManager.PERMISSION_GRANTED)
            {
                getImage.launch("image/*")
                Toast.makeText(requireActivity(),"Granted",Toast.LENGTH_LONG).show()
            }
            else
                getPermission.launch(READ_EXTERNAL_STORAGE)
        }

        binding.btn.setOnClickListener {
            if (validations(
                    binding.name1.text.toString(),
                    binding.email1.text.toString(),
                    binding.pass1.text.toString(),
                    binding.confirm1.text.toString()
                )
            ) {

                val date = LocalDateTime.now().toString()
                auth.createUserWithEmailAndPassword(
                    binding.email1.text.toString(),
                    binding.pass1.text.toString()
                )
                    .addOnSuccessListener {
                        val userData = mapOf<String, String>(
                            "userName" to binding.name1.text.toString(),
                            "userEmail" to binding.email1.text.toString(),
                            "createdAt" to date
                        )

                        db.collection("Users").document(auth.currentUser!!.uid).set(userData)
                            .addOnSuccessListener {
                                Toast.makeText(
                                    requireContext(),
                                    "Register Successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }.addOnFailureListener {
                                Toast.makeText(
                                    requireContext(),
                                    "Registration Failed DB",
                                    Toast.LENGTH_SHORT
                                ).show()
                                Log.w("TAG----->",it.message.toString())
                            }
                        findNavController().navigate(R.id.action_registration_to_loginScreen)
                    }.addOnFailureListener {
                        Toast.makeText(requireContext(), "Registration Failed Auth", Toast.LENGTH_SHORT)
                            .show()
                        Log.w("TAG----->",it.message.toString())
                    }
            }
        }

        binding.haveAccount.setOnClickListener {
            findNavController().navigate(R.id.action_registration_to_loginScreen)
        }
        return binding.root
    }

    private fun validations(
        name: String,
        email: String,
        pass: String,
        confirmPass: String
    ): Boolean {
        when {
            name.isEmpty() -> {
                binding.name1.error = "Enter name"
                return false
            }

            email.isEmpty() ||
                    !Pattern.matches(
                        Patterns.EMAIL_ADDRESS.toString(),
                        binding.email1.text.toString().trim()
                    ) -> {
                binding.email1.error = "Enter email"
                return false
            }

            pass.isEmpty() || confirmPass.isEmpty() -> {
                binding.pass1.error = "Enter Password"
                return false
            }

            pass != confirmPass -> {
                binding.confirm1.error = "Enter same password"
                return false
            }

            pass.length < 6 || pass.length > 12 -> {
                binding.pass1.error = "Password must be minimum 6 characters"
                return false
            }
        }
        return true
    }
}