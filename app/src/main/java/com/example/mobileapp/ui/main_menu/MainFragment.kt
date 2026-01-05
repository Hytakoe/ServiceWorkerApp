package com.example.mobileapp.ui.main_menu

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapp.databinding.FragmentMainBinding
import com.example.mobileapp.ui.work_list.WorkListViewModel
import com.example.mobileapp.ui.workList.TaskAdapter
import com.example.mobileapp.ui.work_list.WorkListUiItem
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class MainFragment : Fragment() {
    private val viewModel: WorkListViewModel by viewModel()
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var myAdapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObservers()

        // Загружаем задачи при создании фрагмента
        viewModel.loadTasks()
    }

    private fun setupRecyclerView() {
        Log.d("MainFragment", "Starting setupRecyclerView")
        val rv: RecyclerView = binding.mainRV
        Log.d("MainFragment", "Setting layout manager")
        val linearLayoutManager = LinearLayoutManager(requireContext())

        // Создаем адаптер (без параметров, если ваш TaskAdapter так работает)
        myAdapter = TaskAdapter()

        rv.apply {
            layoutManager = linearLayoutManager
            Log.d("MainFragment", "Setting adapter: ${myAdapter != null}")
            adapter = myAdapter
            setHasFixedSize(true)
            Log.d("MainFragment", "Adapter item count: ${adapter?.itemCount}")
        }

        Log.d("MainFragment", "setupRecyclerView completed")
    }

    private fun setupObservers() {
        // Для StateFlow используем lifecycleScope.collect
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.tasks.collect { tasks ->
                // Преобразуем Task в WorkListUiItem
                val uiItems = tasks.map { task ->
                    WorkListUiItem(task)
                }
                // Обновляем адаптер с полученными задачами
                myAdapter.submitList(uiItems)
                Log.d("MainFragment", "Tasks updated: ${tasks.size}")
            }
        }

        // Наблюдаем за состоянием загрузки
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loading.collect { isLoading ->
                // Показываем/скрываем прогресс (если есть ProgressBar в layout)
                // Если нет ProgressBar - можно не добавлять
                Log.d("MainFragment", "Loading: $isLoading")
            }
        }

        // Наблюдаем за ошибками
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.error.collect { error ->
                error?.let {
                    showError(it)
                }
            }
        }
    }

    private fun showError(error: String) {
        Snackbar.make(binding.root, error, Snackbar.LENGTH_SHORT).show()
        Log.e("MainFragment", "Error: $error")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}