package com.example.dearfutureme.fragments

import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dearfutureme.API.TokenManager
import com.example.dearfutureme.DataRepository.UserRepository
import com.example.dearfutureme.Adapter.CapsuleAdapter
import com.example.dearfutureme.DataRepository.CapsuleCount
import com.example.dearfutureme.Model.Capsules
import com.example.dearfutureme.R
import com.example.dearfutureme.ViewModel.CapsuleViewModel
import com.example.dearfutureme.ViewModel.MainViewModel
import com.example.dearfutureme.databinding.FragmentHomeBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

//interface OnLogoutListener {
//    fun onLogoutRequested()
//}

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel = MainViewModel()
    private lateinit var capsuleAdapter: CapsuleAdapter
    private var capsuleList: MutableList<Capsules> = mutableListOf()
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
        displayUsername()
        setGradient()
        initNavDrawer()


        capsuleAdapter = CapsuleAdapter(capsuleList)
        val counter = capsuleAdapter.countDrafts()
        Log.d("Draft Capsule Count", "Count: $counter")
        CapsuleCount.draftCapsule = counter
        binding.recyclerViewCapsule.adapter = capsuleAdapter

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                capsuleAdapter.filter(newText ?: "")
                return true
            }
        })
    }

    private fun initNavDrawer() {
        val drawerLayout = requireActivity().findViewById<DrawerLayout>(R.id.drawer_layout)
        binding.settingsBtn.setOnClickListener {
            drawerLayout?.openDrawer(GravityCompat.START)
        }
    }


    private fun displayUsername() {
        val username = UserRepository.username
        binding.userNameView.text = username.toString()
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


    private fun initCapsuleList() {

        viewModel.capsuleList.observe(viewLifecycleOwner, Observer {
            binding.recyclerViewCapsule.layoutManager = LinearLayoutManager(
                requireActivity(),
                LinearLayoutManager.VERTICAL,
                false
            )
            binding.recyclerViewCapsule.adapter = CapsuleAdapter(it.toMutableList())
        })
        viewModel.loadCapsules()

        viewModel.error.observe(viewLifecycleOwner, Observer {
            binding.tvCapsuleFound.text = viewModel.error.value
        })
            binding.progressBarCapsuleList.visibility = View.GONE
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
