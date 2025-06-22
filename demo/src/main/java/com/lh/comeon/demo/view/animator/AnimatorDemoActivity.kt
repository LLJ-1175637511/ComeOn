package com.lh.comeon.demo.view.animator

import androidx.appcompat.app.AppCompatActivity
import com.comeon.android.demo.databinding.ActivityTestAnimatorBinding

class AnimatorDemoActivity: AppCompatActivity() {

    private val binding by lazy {
        ActivityTestAnimatorBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.btAnimatorTest.setOnClickListener {
            binding.enterView.start(false)
            binding.enterViewNew.start(true)
        }
    }

}