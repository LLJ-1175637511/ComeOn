package com.comeon.android.app.home.mine

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.comeon.android.app.home.video.ui.activity.PlayerActivity
import com.comeon.android.component.databinding.FragmentMineBinding
import com.comeon.android.library.kit.MultipleClickActionHelper


class MineFragment: Fragment() {

    companion object {
        const val TAG = "MineFragment"
    }

    private lateinit var binding: FragmentMineBinding

    private val clickActionHelper = MultipleClickActionHelper(3) {
        startActivity(Intent(requireContext(), PlayerActivity::class.java))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentMineBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvVersion.setOnClickListener {
            clickActionHelper.registerClick()
        }
    }
}