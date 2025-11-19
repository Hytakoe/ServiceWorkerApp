package com.example.mobileapp.ui.main_menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapp.R
import com.example.mobileapp.data.model.CarModel
import com.example.mobileapp.databinding.FragmentMainBinding
import kotlin.random.Random

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private val carModels: MutableList<CarModel> = mutableListOf()
    private val images: MutableList<Int> = mutableListOf(
        R.drawable.free_icon_font_checkbox_3917076,
        R.drawable.free_icon_font_exclamation_3917692
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpCarModels()

        val adapter = CarRecyclerViewAdapter(requireContext(), carModels)

        binding.mainRV.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setAdapter(adapter)
        }
    }

    private fun setUpCarModels() {

        val carNames: Array<String> = resources.getStringArray(R.array.car_names)
        val carJobs: Array<String> = resources.getStringArray(R.array.job_names)
        val comments: Array<String> = resources.getStringArray(R.array.comments)
        carModels.clear()

        for (i in carNames.indices) {
            val j = Random.nextInt(0, carJobs.size)
            val imageIndex = Random.nextInt(0, images.size)
            carModels.add(
                CarModel(
                    carNames[i],
                    carJobs[j],
                    comments[j],
                    images[imageIndex]
                )
            )
        }
    }
}