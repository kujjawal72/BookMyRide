package com.myelsadev.bookmyride

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.myelsadev.bookmyride.base.BaseFragment
import com.myelsadev.bookmyride.databinding.FragmentMyRidesBinding


class MyRidesFragment : BaseFragment<FragmentMyRidesBinding>() {

    companion object {

        fun newInstance() = MyRidesFragment().apply {
            arguments = bundleOf()
        }
    }

    override fun getLayout(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        attachToParent: Boolean
    ): FragmentMyRidesBinding = FragmentMyRidesBinding.inflate(inflater, parent, attachToParent)


}