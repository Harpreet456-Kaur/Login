package com.example.login

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.login.Models.NoteModel
import com.example.login.databinding.FragmentAddNotesBinding
import com.example.login.views.HomeFragment
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime

class AddNotes : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var mainActivity: MainActivity
    private lateinit var binding: FragmentAddNotesBinding
    var noteModel = NoteModel()

    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddNotesBinding.inflate(layoutInflater)

        mainActivity = activity as MainActivity

            //isUpdated = it.getBoolean("isUpdate", false)
            val name = arguments?.getSerializable("note_title")
            val note = arguments?.getSerializable("note_note")

            binding.name.setText(name.toString())
            binding.notes.setText(note.toString())

        // Inflate the layout for this fragment
        binding.saveBtn.setOnClickListener {
            Log.d("TAG--->", it.id.toString())
            if (validations(binding.name.text.toString(), binding.notes.text.toString())) {
//                val date = LocalDateTime.now().toString()
                noteModel.name = binding.name.text.toString()
                noteModel.note = binding.notes.text.toString()
                noteModel.time = LocalDateTime.now().toString()

                db.collection("Notes")
                    .document().set(noteModel)
                    .addOnSuccessListener {
                        // Log.d("TAG--->",it.toString())
                        //System.out.println("$ Done")
                        Toast.makeText(requireContext(), "Successful", Toast.LENGTH_SHORT).show()
//                        val bundle = Bundle()
//                        bundle.putString("data",binding.name.text.toString())
//                        mainActivity.passData(binding.name.text.toString())

                        findNavController().navigate(R.id.action_addNotes_to_homeFragment)
                    }
                    .addOnFailureListener {
                        Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(requireContext(), "Failure", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        return binding.root
    }

    private fun validations(name: String, notes: String): Boolean {
        when {
            name.isNullOrEmpty() || notes.isNullOrEmpty() -> {
                binding.name.error = "Enter text"
                return false
            }
        }
        return true
    }
}
