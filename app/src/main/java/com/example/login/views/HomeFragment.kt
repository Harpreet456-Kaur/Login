package com.example.login.views

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.login.AddNotes
import com.example.login.Firebase.Firebase.db
import com.example.login.Interface.NoteInterface
import com.example.login.Models.NoteModel
import com.example.login.R
import com.example.login.adapter.NoteAdapter
import com.example.login.databinding.FragmentAddNotesBinding
import com.example.login.databinding.FragmentHomeBinding
import com.google.firebase.firestore.DocumentChange

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    lateinit var noteAdapter: NoteAdapter
    lateinit var noteInterface: NoteInterface
    lateinit var binding1: FragmentAddNotesBinding
    var noteModel = NoteModel()
    private val bundle = Bundle()

    var list = ArrayList<NoteModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding.title.text = arguments?.getString("name").toString()
        arguments?.let {
            //noteModel = it.getSerializable("Notes") as NoteModel
//            val args = this.arguments
//            val inputData = args?.get("data")
//            binding.title.text = inputData.toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        binding1 = FragmentAddNotesBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        noteAdapter = NoteAdapter(list, this)
        binding.recycler.adapter = noteAdapter
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())

        noteAdapter.setOnItemClickListener(object : NoteAdapter.OnItemClickListener {

            fun onClick(it: View, position: Int, noteModel: ArrayList<NoteModel>) {
                Log.d("TAG----item", noteModel[position].id)
            }
//            newDialogBinding.name.setText(list[position].name.toString())
//            newDialogBinding.course.setText(list[position].course.toString())


            override fun onItemClick(position: Int) {
                //Toast.makeText(requireActivity(),"Done",Toast.LENGTH_SHORT).show()
                Log.d("TAG---->", list[position].name.toString())
                Log.d("TAG---->", list[position].note.toString())
                val noteTitle = list[position].name
                val note = list[position].note

                bundle.putString("note_title", noteTitle)
                bundle.putString("note_note", note)

                findNavController().navigate(R.id.action_homeFragment_to_addNotes3, bundle)
            }

            override fun onIconClick(position: Int) {


                val deleteItem = list[position].id
                Log.d("TAG---->", deleteItem)
                db.collection("Notes").document(deleteItem).delete()
                    .addOnSuccessListener {
                        Toast.makeText(requireActivity(), "Deleted", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Log.d("TAG---->", it.toString())
                    }
                list.removeAt(position)
                noteAdapter.notifyDataSetChanged()

            }
        })

        binding.fBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addNotes3)
        }
        // binding.title.text.toSet().toString()


        db.collection("Notes").addSnapshotListener { value, error ->
            if (value != null)
                for (snapshots in value!!.documentChanges) {
                    when (snapshots.type) {
                        DocumentChange.Type.ADDED -> {
                            var noteModel = NoteModel()
                            noteModel = snapshots.document.toObject(noteModel::class.java)
                            noteModel.id = snapshots.document.id ?: ""
                            list.add(noteModel)
                            noteAdapter.notifyDataSetChanged()
                        }

                        DocumentChange.Type.REMOVED -> {
                            var noteModel = NoteModel()
                            noteModel = snapshots.document.toObject(noteModel::class.java)
                            noteModel.id = snapshots.document.id ?: ""
                            for (i in 0..list.size - 1) {
                                if ((snapshots.document.id ?: "").equals(list[i].id)) {
                                    list.removeAt(i)
                                    break
                                }
                            }
                        }

                        else -> {}
                    }
                }
            binding.recycler.clearFocus()
        }
        getNotes()
        return binding.root
        return binding1.root
    }

    private fun getNotes() {
        db.collection("Notes")
            .whereEqualTo("name", true)
            .get().addOnSuccessListener {
                val data = it.documents
                for (i in data.indices) {
                    val noteModel = NoteModel()
                    noteModel.name = data[i].get("name").toString()
                    noteModel.note = data[i].get("note").toString()
                    noteModel.time = data[i].get("time").toString()
                }
            }.addOnFailureListener {
                Log.d("TAG---->", it.message.toString())
            }
    }
//    override fun click(position: Int) {
//        val binding = FragmentAddNotesBinding.inflate(layoutInflater)
//        binding.name.setText(list[position].name)
//        binding.notes.setText(list[position].note)
//
//        findNavController().navigate(R.id.action_homeFragment_to_addNotes3, bundleOf(
//            "Notes" to list[position]
//        ))
//
//        //binding.saveBtn.visibility
//    }


}
