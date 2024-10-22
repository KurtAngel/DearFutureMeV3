package com.example.dearfutureme.fragments

import android.content.Intent
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dearfutureme.API.RetrofitInstance
import com.example.dearfutureme.API.TokenManager
import com.example.dearfutureme.Activities.MainActivity
import com.example.dearfutureme.Adapter.CapsuleAdapter
import com.example.dearfutureme.Model.LogoutResponse
import com.example.dearfutureme.R
import com.example.dearfutureme.ViewModel.MainViewModel
import com.example.dearfutureme.databinding.FragmentHomeBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel = MainViewModel()
    private lateinit var tokenManager: TokenManager

    // Parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        // Initialize binding
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        initCapsuleList()
        setupListeners()
        tokenManager = TokenManager(requireActivity())
        displayUsername()
        setGradient()

        val intent = requireActivity().intent
        intent.getStringExtra("USERNAME")?.let {
            Log.d("HomeFragment", "Received username: $it")
            binding.usernameView.text = it // Set the username in the TextView
        }
    }

    private fun setupListeners() {
        binding.logoutBtn.setOnClickListener {
            val builder = AlertDialog.Builder(requireActivity())
            builder.setTitle("Logout")
            builder.setMessage("Are you sure you want to logout?")
            builder.setPositiveButton("Yes") { dialog, which -> logoutUser() }
            builder.setNegativeButton("No") { dialog, which -> dialog.dismiss() }
            builder.create().show()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun displayUsername() {
        val username = requireActivity().intent.getStringExtra("USERNAME")
        binding.usernameView.text = username ?: "Guest" // Fallback if username is null
    }

    private fun initCapsuleList() {
        binding.progressBarCapsuleList.visibility = View.VISIBLE

        // Use viewLifecycleOwner to observe LiveData
        viewModel.capsuleList.observe(viewLifecycleOwner, Observer {
            binding.recyclerViewCapsule.layoutManager = LinearLayoutManager(
                requireActivity(),
                LinearLayoutManager.VERTICAL,
                false
            )
            binding.recyclerViewCapsule.adapter = CapsuleAdapter(it.toMutableList())
            binding.progressBarCapsuleList.visibility = View.GONE
        })
        viewModel.loadCapsules()
    }

    private fun logoutUser() {

        RetrofitInstance.instance.logout().enqueue(object : Callback<LogoutResponse> {
            override fun onResponse(call: Call<LogoutResponse>, response: Response<LogoutResponse>) {
                if (response.isSuccessful) {
                    tokenManager.clearToken()
                    Toast.makeText(requireActivity(), response.body()?.message, Toast.LENGTH_SHORT).show()
                    startActivity(Intent(requireActivity(), MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    })
                } else {
                    Toast.makeText(requireActivity(), "Logout failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LogoutResponse>, t: Throwable) {
                Toast.makeText(requireActivity(), "Logout failed: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setGradient() {
        val paint = binding.tvMyCapsule.paint
        val width = paint.measureText(binding.tvMyCapsule.text.toString())
        binding.tvMyCapsule.paint.shader = LinearGradient(
            0f, 0f, width, binding.tvMyCapsule.textSize,
            intArrayOf(Color.parseColor("#6B26D4"), Color.parseColor("#C868FF")),
            null,
            Shader.TileMode.CLAMP
        )
    }
}
