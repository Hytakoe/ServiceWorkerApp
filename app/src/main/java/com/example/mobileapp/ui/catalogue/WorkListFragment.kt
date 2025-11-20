package com.example.mobileapp.ui.catalogue

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapp.databinding.FragmentMainBinding
import com.example.mobileapp.ui.workList.TaskAdapter
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class WorkListFragment: Fragment() {

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
        setupRecyclerView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //setupSearchView()
        //setupSpinner()

        //observeViewModel()
    }

    private fun setupRecyclerView() {
        Log.d("CatalogueFragment", "Starting setupRecyclerView")
        val rv: RecyclerView = binding.mainRV
        Log.d("CatalogueFragment", "Setting layout manager")
        val gridLayoutManager = GridLayoutManager(requireContext(), 2)

        // создаем адаптер с колбеками для кнопок +/-
        myAdapter = TaskAdapter()

        rv.apply {
            layoutManager = gridLayoutManager
            Log.d("CatalogueFragment", "Setting adapter: ${myAdapter != null}")
            adapter = myAdapter
            setHasFixedSize(true)
            Log.d("CatalogueFragment", "Adapter item count: ${adapter?.itemCount}")
        }

        Log.d("CatalogueFragment", "setupRecyclerView completed")
    }

    /*private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(p0: String?): Boolean {
                viewModel.onSearchQueryChanged(p0 ?: "")
                return false
            }

            override fun onQueryTextSubmit(p0: String?): Boolean {
                // не обрабатывать отдельно, так как текст меняется при каждом вводе
                return false
            }
        })

        binding.searchView.setOnCloseListener {
            viewModel.onSearchQueryChanged("")
            false // с false SearchView сам очистит текст
        }
    }

    private fun setupSpinner() {
        // создание адаптера для Spinner с фильтрами
        val filterAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.product_filters,
            android.R.layout.simple_spinner_item
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        binding.spinner.adapter = filterAdapter

        // обработка вызова фильтра
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val selectedFilter = when (p2) {
                    0 -> ProductFilter.NONE
                    1 -> ProductFilter.NAME_A_TO_Z
                    2 -> ProductFilter.NAME_Z_TO_A
                    3 -> ProductFilter.PRICE_LOW_TO_HIGH
                    4 -> ProductFilter.PRICE_HIGH_TO_LOW
                    else -> ProductFilter.NONE
                }
                viewModel.onFilterSelected(selectedFilter)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                // ничего
            }

        }
    }

    private fun observeViewModel() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            // обновляем адаптер
            myAdapter.submitList(state.workListUiItems)

            // показываем/скрываем индикатор загрузки
            //binding.progressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE

            state.errorMessage?.let { error ->
                showError(error)
            }

            binding.textView.visibility = if (state.workListUiItems.isEmpty()) View.VISIBLE else View.GONE
        }
    }*/

    private fun showError(error: String) {
        Snackbar.make(binding.root, error, Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // предотвращаем утечки памяти
    }
}
