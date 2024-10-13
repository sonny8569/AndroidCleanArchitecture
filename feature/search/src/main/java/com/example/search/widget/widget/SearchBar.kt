package com.example.search.widget.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import com.example.search.databinding.ItemEdittextSearchBinding

internal class SearchBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: ItemEdittextSearchBinding
    private var onSearchListener: OnSearchListener? = null

    init {
        val inflater = LayoutInflater.from(context)
        binding = ItemEdittextSearchBinding.inflate(inflater, this, true)
        addListener()
    }

    private fun addListener() {
        binding.editSearchContent.addTextChangedListener {
            if (it?.isNotEmpty() == true) {
                binding.imgDeleteIcon.visibility = VISIBLE
            } else {
                binding.imgDeleteIcon.visibility = GONE
            }
        }
        binding.imgDeleteIcon.setOnClickListener {
            binding.editSearchContent.text.clear()
        }
        binding.editSearchContent.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                onSearchListener?.onSearch(v.text.toString())
                hideKeyboard()
                v.clearFocus()
                return@setOnEditorActionListener true
            }
            false
        }
    }

    fun requestFocusInput() {
        binding.root.post {
            binding.editSearchContent.requestFocus()
            showKeyboard()
        }
    }

    fun requestTextClear(){
        binding.editSearchContent.text.clear()
    }
    fun setOnSearchListener(listener: OnSearchListener) {
        onSearchListener = listener
    }

    private fun showKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.editSearchContent, 0)
    }

    private fun hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    interface OnSearchListener {
        fun onSearch(query: String)
    }

}