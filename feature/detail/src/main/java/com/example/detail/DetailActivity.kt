package com.example.detail

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import com.bumptech.glide.Glide
import com.example.detail.databinding.ActivityDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModels()
    private var menuItem: MenuItem? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initActionBar()
        observer()
    }

    private fun initActionBar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
    }
    private fun observer(){
        viewModel.livedata.observe(this){
            setData(it)
        }
    }
    private fun setData(data : DetailViewModel.Action){
        setImage(data.data.thumbNail)
    }
    private fun setImage(url : String){
        Glide.with(this)
            .load(url)
            .fitCenter()
            .into(binding.image)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }

            R.id.action_favorite -> {
                viewModel.onLikeChange()
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        menuItem = menu?.get(0)
        setMenuIcon(viewModel.livedata.value?.isLike ?: false)
        return true
    }
    private fun setMenuIcon(favorite: Boolean) {
        menuItem?.setIcon(
            if (favorite)
                    R.drawable.ic_like_on
            else R.drawable.ic_like_off
        )
    }
}