package com.example.dearfutureme.fragments

import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dearfutureme.Adapter.ReceivedCapsuleAdapter
import com.example.dearfutureme.DataRepository.CapsuleCount
import com.example.dearfutureme.Model.ReceivedCapsule
import com.example.dearfutureme.ViewModel.MainViewModel
import com.example.dearfutureme.databinding.FragmentReceivedCapsuleBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ShareCapsuleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ShareCapsuleFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentReceivedCapsuleBinding
    private val viewModel = MainViewModel()
    private lateinit var capsuleAdapter: ReceivedCapsuleAdapter
    private var capsuleList: MutableList<ReceivedCapsule> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        binding = FragmentReceivedCapsuleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initReceivedCapsuleList()
        setGradient()

        capsuleAdapter = ReceivedCapsuleAdapter(capsuleList)
        val counter = capsuleAdapter.countSentCapsules()
        Log.d("Draft Capsule Count", "Count: $counter")
        CapsuleCount.sentCapsule = counter
    }

    private fun setGradient() {
        val paint = binding.tvShared.paint
        val width = paint.measureText(binding.tvShared.text.toString())
        binding.tvShared.paint.shader = LinearGradient(
            0f,0f,width,binding.tvShared.textSize, intArrayOf(
                Color.parseColor("#F25597"),
                Color.parseColor("#FFBDDA")
            ), null, Shader.TileMode.CLAMP
        )
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ShareCapsuleFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ShareCapsuleFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun initReceivedCapsuleList() {

        // Use viewLifecycleOwner to observe LiveData
        viewModel.receivedCapsuleList.observe(viewLifecycleOwner, Observer {
            binding.recyclerViewCapsule.layoutManager = LinearLayoutManager(
                requireActivity(),
                LinearLayoutManager.VERTICAL,
                false
            )
            binding.recyclerViewCapsule.adapter = ReceivedCapsuleAdapter(it.toMutableList())
        })

        viewModel.loadReceivedCapsules()
        viewModel.error.observe(viewLifecycleOwner, Observer{
            binding.tvNoCapsule.text = viewModel.error.value
        })
    }
}