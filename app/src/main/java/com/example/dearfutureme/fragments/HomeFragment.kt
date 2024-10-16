package com.example.dearfutureme.fragments

import android.content.Intent
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dearfutureme.API.RetrofitInstance
import com.example.dearfutureme.API.TokenManager
import com.example.dearfutureme.Activities.CreateCapsule
import com.example.dearfutureme.Activities.MainActivity
import com.example.dearfutureme.Adapter.CapsuleAdapter
import com.example.dearfutureme.Model.Capsules
import com.example.dearfutureme.Model.LogoutResponse
import com.example.dearfutureme.R
import com.example.dearfutureme.ViewModel.MainViewModel
import com.example.dearfutureme.databinding.ActivityMyCapsuleListBinding
import com.example.dearfutureme.databinding.FragmentHomeBinding
import com.example.dearfutureme.fragments.HomeFragment
import com.example.dearfutureme.fragments.NotificationFragment
import com.example.dearfutureme.fragments.ShareCapsuleFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Response
import retrofit2.Call
import retrofit2.Callback

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
//class HomeFragment : Fragment() {
//
//    private val viewModel = MainViewModel()
//    private val capsules = mutableListOf<Capsules>()
//
//    lateinit var tokenManager: TokenManager
//    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null
//    private lateinit var binding: FragmentHomeBinding
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
//
//        displayUsername()
//        initCapsuleList()
//        addCapsuleBtn()
//        setGradient()
//        logoutBtn()
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_home, container, false)
//    }
//
//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment HomeFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            HomeFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
//        private fun displayUsername() {
//        val username = requireActivity().intent.getStringExtra("USERNAME")
//        if (username != null) {
//            binding.usernameView.text = username // Display the username
//        }
//    }
//
//    private fun initCapsuleList() {
//
//        binding.progressBarCapsuleList.visibility = View.VISIBLE
//        viewModel.capsuleList.observe(this, Observer{
//            binding.recyclerViewCapsule.layoutManager =
//                LinearLayoutManager(
//                    requireActivity(),
//                    LinearLayoutManager.VERTICAL,
//                    false
//                )
//            binding.recyclerViewCapsule.adapter = CapsuleAdapter(capsules.toMutableList())
//            binding.progressBarCapsuleList.visibility = View.GONE
//        })
//        viewModel.loadCapsules()
//    }
//
//    private fun logoutBtn() {
//        binding.logoutBtn.setOnClickListener {
//            val builder = AlertDialog.Builder(requireActivity())
//            builder.setTitle("Logout")
//            builder.setMessage("Are you sure you want to logout?")
//
//            builder.setPositiveButton("Yes") { dialog, which ->
//                logoutUser()
//            }
//
//            builder.setNegativeButton("No") { dialog, which ->
//                dialog.dismiss()
//            }
//
//            val dialog = builder.create()
//            dialog.show()
//        }
//    }
//
//    private fun logoutUser() {
//        tokenManager = TokenManager(requireActivity())
//
//        RetrofitInstance.instance.logout().enqueue(object : Callback<LogoutResponse> {
//            override fun onResponse(
//                call: Call<LogoutResponse>,
//                response: Response<LogoutResponse>
//            ) {
//                if (response.isSuccessful) {
//                    // Clear the token from SharedPreferences
//                    val logoutResponse = response.body()?.message
//                    tokenManager.clearToken()
//                    // Handle successful logout (e.g., show a success message)
//                    Toast.makeText(requireActivity(), "$logoutResponse", Toast.LENGTH_SHORT).show()
//
//                    // Redirect to the Login Activity
//                    val intent = Intent(requireActivity(), MainActivity::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                    startActivity(intent)
////                    finish()
//                } else {
//                    // Handle the error response
//                    Toast.makeText(requireActivity(), "Logout failed: ${response.message()}", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onFailure(call: Call<LogoutResponse>, t: Throwable) {
//                // Handle the failure case (e.g., network issues)
//                Toast.makeText(requireActivity(), "Logout failed: ${t.message}", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }
//
//    private fun setGradient() {
//        val paint = binding.tvMyCapsule.paint
//        val width = paint.measureText(binding.tvMyCapsule.text.toString())
//        binding.tvMyCapsule.paint.shader = LinearGradient(
//            0f,0f,width,binding.tvMyCapsule.textSize, intArrayOf(
//                Color.parseColor("#6B26D4"),
//                Color.parseColor("#C868FF")
//            ), null, Shader.TileMode.CLAMP
//        )
//    }
//
//    private fun addCapsuleBtn() {
//        binding.addCapsuleBtn.setOnClickListener {
//            startActivity(Intent(requireActivity(), CreateCapsule::class.java))
//        }
//    }
//}
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Initialize binding
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        displayUsername()
        initCapsuleList()
        addCapsuleBtn()
        setGradient()
        logoutBtn()
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

    private fun logoutBtn() {
        binding.logoutBtn.setOnClickListener {
            val builder = AlertDialog.Builder(requireActivity())
            builder.setTitle("Logout")
            builder.setMessage("Are you sure you want to logout?")
            builder.setPositiveButton("Yes") { dialog, which -> logoutUser() }
            builder.setNegativeButton("No") { dialog, which -> dialog.dismiss() }
            builder.create().show()
        }
    }

    private fun logoutUser() {
        tokenManager = TokenManager(requireActivity())
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

    private fun addCapsuleBtn() {
        binding.addCapsuleBtn.setOnClickListener {
            startActivity(Intent(requireActivity(), CreateCapsule::class.java))
        }
    }
}
