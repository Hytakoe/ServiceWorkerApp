package com.example.mobileapp.ui.main_menu

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapp.databinding.FragmentMainBinding
import com.example.mobileapp.ui.work_list.WorkListViewModel
import com.example.mobileapp.ui.workList.TaskAdapter
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class MainFragment : Fragment() {
    private val viewModel: WorkListViewModel by viewModel()
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var myAdapter: TaskAdapter

    /*private lateinit var binding: FragmentMainBinding
    private var tasks: MutableList<TaskInWorkList> = mutableListOf()
    private val images: MutableList<Int> = mutableListOf(
        R.drawable.free_icon_font_checkbox_3917076,
        R.drawable.free_icon_font_exclamation_3917692
    )*/

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //binding = FragmentMainBinding.inflate(inflater, container, false)
        //return binding.root
        //return inflater.inflate(R.layout.fragment_main, container, false)
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        setupRecyclerView()
        return binding.root
    }
    private fun setupRecyclerView() {
        Log.d("CatalogueFragment", "Starting setupRecyclerView")
        val rv: RecyclerView = binding.mainRV
        Log.d("CatalogueFragment", "Setting layout manager")
        val linearLayoutManager = LinearLayoutManager(requireContext())

        // создаем адаптер с колбеками для кнопок +/-
        myAdapter = TaskAdapter()

        rv.apply {
            layoutManager = linearLayoutManager
            Log.d("CatalogueFragment", "Setting adapter: ${myAdapter != null}")
            adapter = myAdapter
            setHasFixedSize(true)
            Log.d("CatalogueFragment", "Adapter item count: ${adapter?.itemCount}")
        }

        Log.d("CatalogueFragment", "setupRecyclerView completed")
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
        /*setUpCarTasks()

        val adapter = CarRecyclerViewAdapter(requireContext(), tasks)

        binding.mainRV.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setAdapter(adapter)
        }*/
    }

    /*private fun setUpCarTasks() {
        val taskRepository: WorkListRepositoryImpl
        TaskResult
        tasks = taskRepository.getWorkListItems(0)
        /*val carNames: Array<String> = resources.getStringArray(R.array.car_names)
        val carJobs: Array<String> = resources.getStringArray(R.array.job_names)
        val comments: Array<String> = resources.getStringArray(R.array.comments)
        tasks.clear()

        for (i in carNames.indices) {
            val j = Random.nextInt(0, carJobs.size)
            val imageIndex = Random.nextInt(0, images.size)
            tasks.add(
                CarModel(
                    carNames[i],
                    carJobs[j],
                    comments[j],
                    images[imageIndex]
                )
            )
        }*/
    }*/
    private fun observeViewModel() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            // обновляем адаптер
            myAdapter.submitList(state.workListUiItems)

            // показываем/скрываем индикатор загрузки
            //binding.progressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE

            state.errorMessage?.let { error ->
                showError(error)
            }

            //binding.tv.visibility = if (state.catalogueUiItems.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun showError(error: String) {
        Snackbar.make(binding.root, error, Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // предотвращаем утечки памяти
    }
}