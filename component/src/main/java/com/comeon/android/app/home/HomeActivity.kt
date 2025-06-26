package com.comeon.android.app.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.comeon.android.app.home.discover.DiscoverFragment
import com.comeon.android.app.home.main.MainFragment
import com.comeon.android.app.home.mine.MineFragment
import com.comeon.android.component.databinding.ActivityHomeBinding
import com.google.android.material.tabs.TabLayoutMediator

class HomeActivity : AppCompatActivity() {

    companion object {
        const val TAG = "HomeActivity"
    }

    private lateinit var binding: ActivityHomeBinding

    private val tabNameArray = arrayOf(
        "首页",
        "发现",
        "我的"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityHomeBinding.inflate(layoutInflater).also {
            binding = it
            setContentView(it.root)
        }
        binding.viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = tabNameArray.size

            override fun createFragment(position: Int) = when (position) {
                0 -> MainFragment() // 首页
                1 -> DiscoverFragment() // 发现
                2 -> MineFragment() // 我的
                else -> throw IllegalArgumentException("Invalid position: $position")
            }
        }
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabNameArray[position]
        }.attach()
    }

}