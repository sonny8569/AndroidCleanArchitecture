package com.example.like

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.domain.model.SearchResult
import com.example.like.databinding.FragmentLikeBinding
import com.example.like.utill.Delete_Data_Error
import com.example.like.utill.Save_Data_Null
import com.example.like.viewModel.LikeViewModel
import com.example.like.widget.FavoriteLikeItemDecoration
import com.example.like.widget.LikeAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LikeFragment : Fragment() {
    private lateinit var binding: FragmentLikeBinding
    private val viewModel: LikeViewModel by viewModels()
    private val adapter by lazy {
        LikeAdapter(
            Glide.with(this),
            viewModel::onChangeLike,
            viewModel::onClickItem
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentLikeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observer()
    }

    private fun initView() {
        with(binding) {
            recyclerviewLike.layoutManager = GridLayoutManager(context, 2)
            recyclerviewLike.adapter = adapter
            recyclerviewLike.addItemDecoration(FavoriteLikeItemDecoration())
        }
    }

    private fun observer() {
        viewModel.liveData.observe(viewLifecycleOwner) { action ->
            when (action) {
                is LikeViewModel.Action.SaveResult -> {
                    requireContext().getText(R.string.msg_save_data_success).showMessage()
                }

                is LikeViewModel.Action.OnClickItem -> {

                }

                is LikeViewModel.Action.Error -> {
                    when (action.message) {
                        Save_Data_Null -> requireContext().getText(R.string.msg_data_null)
                            .showMessage()

                        Delete_Data_Error -> requireContext().getText(R.string.msg_delete_data_error)
                            .showMessage()
                    }
                }

            }
        }
    }


    private fun CharSequence.showMessage() {
        Toast.makeText(requireContext(), this, Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        viewModel.onGetData()
    }

    companion object {
        fun newInstance() = LikeFragment()
    }
}