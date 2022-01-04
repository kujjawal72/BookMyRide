package com.myelsadev.bookmyride.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding


abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    abstract fun getLayout(inflater: LayoutInflater): VB
    protected lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getLayout(layoutInflater)
        setContentView(binding.root)

        bindViews(savedInstanceState)
    }

    abstract fun bindViews(savedInstanceState: Bundle?)

}