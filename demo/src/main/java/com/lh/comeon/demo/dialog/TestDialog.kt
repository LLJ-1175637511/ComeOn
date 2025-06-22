package com.lh.comeon.demo.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.comeon.android.demo.databinding.FragmentDialogTestBinding
import com.comeon.android.library.dialog.FullScreenDialogFragment

class TestDialog: FullScreenDialogFragment() {

    private lateinit var binding: FragmentDialogTestBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentDialogTestBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}