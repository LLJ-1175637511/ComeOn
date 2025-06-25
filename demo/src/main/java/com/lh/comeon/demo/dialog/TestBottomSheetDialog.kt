package com.lh.comeon.demo.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.comeon.android.demo.databinding.FragmentBottomSheetDialogTestBinding
import com.comeon.android.library.container.dialog.FullScreenBottomSheetDialogFragment

class TestBottomSheetDialog: FullScreenBottomSheetDialogFragment() {

    private lateinit var binding: FragmentBottomSheetDialogTestBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentBottomSheetDialogTestBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}