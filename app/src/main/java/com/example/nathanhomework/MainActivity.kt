package com.example.nathanhomework

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.like.LikeFragment
import com.example.nathanhomework.databinding.ActivityMainBinding
import com.example.search.widget.SearchFragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.viewPager.adapter = Adapter(this)
        TabLayoutMediator(binding.layoutTab, binding.viewPager) { tab, position ->
            tab.text = when (Tab.entries[position]) {
                Tab.SEARCH -> resources.getText(R.string.tab_search)
                Tab.LIKE -> resources.getText(R.string.tab_like)
            }
        }.attach()
    }

    private class Adapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment {
            return when (Tab.entries[position]) {
                Tab.SEARCH -> SearchFragment.newInstance()
                Tab.LIKE -> LikeFragment.newInstance()
            }
        }
    }

    private enum class Tab {
        SEARCH,
        LIKE
    }

}