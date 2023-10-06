package com.example.login

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.login.Firebase.Firebase.auth
import com.example.login.Firebase.Firebase.db
import com.example.login.Models.MyData
import com.example.login.Retrofit.API
import com.example.login.Retrofit.RetrofitInstance
import com.example.login.databinding.FragmentGetDataBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class GetData : Fragment() {

    lateinit var binding: FragmentGetDataBinding
    private lateinit var name : String
    private lateinit var email : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGetDataBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        val new = db.collection("Users").document(auth.currentUser!!.uid)
            GlobalScope.launch(Dispatchers.IO){
                delay(3000L)
                new.get().addOnSuccessListener {
                val data = it.data
                binding.name.text = data!!["userName"].toString()
                binding.email.text = data["userEmail"].toString()
//            Log.d("TAG---->", data["userName"].toString())
//            Log.d("TAG---->", data["userEmail"].toString())

            }.addOnFailureListener {
                Toast.makeText(requireContext(),"Failure",Toast.LENGTH_SHORT).show()
            }
            }
//            .get().addOnSuccessListener {
//            val data = it.data
//            binding.name.text = data!!["userName"].toString()
//            binding.email.text = data["userEmail"].toString()
////            Log.d("TAG---->", data["userName"].toString())
////            Log.d("TAG---->", data["userEmail"].toString())
//
//        }.addOnFailureListener {
//            Toast.makeText(requireContext(),"Failure",Toast.LENGTH_SHORT).show()
//        }

        //getData()
        binding.btn.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                Log.d("TAG--->","Coroutines")
                getData()
            }
        }
        binding.list.setOnClickListener {
            findNavController().navigate(R.id.action_getData_to_addNotes)
        }
        return binding.root
    }

    private fun getData(){
        RetrofitInstance.retrofitInstance().create(API::class.java).getMeme().enqueue(object : Callback<MyData?> {
            override fun onResponse(call: Call<MyData?>, response: Response<MyData?>) {
               val res = response.body()
                Log.d("TAG---->", res!!.title)
                Log.d("TAG---->", res.postLink)
                Log.d("TAG---->", res.url)
                Log.d("TAG---->", res.author)

                binding.memeName.text = res.title.toString()
                Glide.with(this@GetData).load(res.url).into(binding.image)

            }

            override fun onFailure(call: Call<MyData?>, t: Throwable) {
                Log.d("TAG---->", t.message.toString())
            }
        })
    }
}

