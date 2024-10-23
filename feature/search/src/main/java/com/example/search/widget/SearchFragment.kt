package com.example.search.widget

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.domain.model.Router
import com.example.domain.model.SearchResult
import com.example.search.R
import com.example.search.databinding.FragmentSearchBinding
import com.example.search.widget.utill.Delete_Data_Error
import com.example.search.widget.utill.Load_Error
import com.example.search.widget.utill.Save_Data_Error
import com.example.search.widget.utill.Save_Data_Null
import com.example.search.widget.viewModel.SearchViewModel
import com.example.search.widget.widget.SearchAdapter
import com.example.search.widget.widget.SearchBar
import com.example.search.widget.widget.SearchFeedItemDecoration
import com.example.search.widget.widget.SearchListPagingStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModels()
    private val adapter by lazy { SearchAdapter(Glide.with(this), viewModel::onChangeLike , viewModel::onDetailClick) }

    @Inject
    lateinit var router : Router
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
        binding.list.addItemDecoration(SearchFeedItemDecoration())
        adapter.addLoadStateListener {
            showProgress(it.refresh is LoadState.Loading)
            if(it.source.refresh is LoadState.Error){
                showErrorView(true)
            }
            showEmptyView(it.append.endOfPaginationReached && adapter.itemCount == 0)
        }
        binding.list.adapter = adapter.withLoadStateHeaderAndFooter(
            SearchListPagingStateAdapter{ clickRetry() },
            SearchListPagingStateAdapter{clickRetry()}
        )
        binding.btnReSearch.setOnClickListener { clickRetry() }
        binding.list.adapter = adapter
        binding.widgetSearchBar.requestFocusInput()
    }
    private fun clickRetry() {
        adapter.retry()
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
                is SearchViewModel.Action.SaveResult -> {
                    handleRefreshChangedItems(action.data)
                    Toast.makeText(
                        requireContext(),
                        requireContext().getText(R.string.msg_save_data_success),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is SearchViewModel.Action.GetDeviceData -> {
                    action.data.toChangeData()
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
                        Save_Data_Null ->{
                            changeIsLikeFalse()
                        }
                    }
                }

                is SearchViewModel.Action.ShowDetail -> router.navigateToDetail(action.data , action.data.isLike)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.items.collect { pagingData ->
                    adapter.submitData(pagingData)
                    binding.list.scrollToPosition(0)
                }
            }
        }
    }

    private fun handleRefreshChangedItems(changedItems: SearchResult) {
        val list = adapter.snapshot().items
        list.forEachIndexed { index, item ->
            if (item.id == changedItems.id) {
                item.isLike = changedItems.isLike
                adapter.notifyItemChanged(index)
            }
        }
    }

    private fun changeIsLikeFalse(){
        val list = adapter.snapshot().items
        list.forEachIndexed{ index , item ->
            if(item.isLike){
                item.isLike = false
                adapter.notifyItemChanged(index)
            }
        }
    }

    private fun List<Pair<Int, Boolean>>.toChangeData() {
        val data = adapter.snapshot()
        this.forEach { (index, isLike) ->
            (data[index] as? SearchResult)?.isLike = isLike
            adapter.notifyItemChanged(index)
        }
    }

    private fun showToastMessage(message: CharSequence) {
        Toast.makeText(
            requireContext(),
            message,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun showErrorView(isVisible: Boolean) {
        binding.btnReSearch.isVisible = isVisible
        showToastMessage(requireContext().getText(R.string.msg_load_data_error))
    }

    private fun showProgress(isVisible: Boolean) {
        binding.progress.isVisible = isVisible
    }

    private fun showEmptyView(isVisible: Boolean) {
        with(binding) {
            if (isVisible) {
                showToastMessage(requireContext().getText(R.string.msg_load_data_error))
                list.isInvisible = true
            } else {
                list.isInvisible = false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.onCheckDataChange(adapter.snapshot().items)
    }

    companion object {
        fun newInstance() = SearchFragment()
    }
}