package com.example.search.widget

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.search.R
import com.example.search.databinding.FragmentSearchBinding
import com.example.search.widget.utill.Delete_Data_Error
import com.example.search.widget.utill.Load_Error
import com.example.search.widget.utill.Save_Data_Error
import com.example.search.widget.viewModel.SearchViewModel
import com.example.search.widget.widget.SearchBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        addListener()
        observer()
    }

    private fun init() {
        binding.list.layoutManager = LinearLayoutManager(context)
        binding.widgetSearchBar.requestFocusInput()
    }

    private fun addListener() {
        binding.widgetSearchBar.setOnSearchListener(object : SearchBar.OnSearchListener {
            override fun onSearch(query: String) {
                viewModel.onSearch(query)
            }
        })

        binding.btnReSearch.setOnClickListener {
            binding.widgetSearchBar.requestTextClear()
            it.visibility = View.GONE
        }
    }

    private fun observer() {
        viewModel.liveData.observe(viewLifecycleOwner) { action ->
            when (action) {
                is SearchViewModel.Action.Search -> {
                    Log.d(javaClass.name.toString(), "Success to get data ${action.data}")
                }

                is SearchViewModel.Action.DeleteResult -> {
                    Toast.makeText(
                        requireContext(),
                        requireContext().getText(R.string.msg_delete_data_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is SearchViewModel.Action.SaveResult -> {
                    Toast.makeText(
                        requireContext(),
                        requireContext().getText(R.string.msg_save_data_success),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is SearchViewModel.Action.Error -> {
                    when (action.message) {
                        Load_Error -> {
                            binding.btnReSearch.visibility = View.VISIBLE
                            showToastMessage(requireContext().getText(R.string.msg_load_data_error))
                        }

                        Save_Data_Error -> {
                            showToastMessage(requireContext().getText(R.string.msg_save_data_error))
                        }

                        Delete_Data_Error -> {
                            showToastMessage(requireContext().getText(R.string.msg_delete_data_error))
                        }
                    }
                }

            }
        }
    }

    private fun showToastMessage(message: CharSequence) {
        Toast.makeText(
            requireContext(),
            message,
            Toast.LENGTH_SHORT
        ).show()
    }

    companion object {
        fun newInstance() = SearchFragment()
    }
}