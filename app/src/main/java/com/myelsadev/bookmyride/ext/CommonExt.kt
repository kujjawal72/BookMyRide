package com.myelsadev.bookmyride.ext

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

fun FragmentActivity.replaceFragment(
    @IdRes idRes: Int, fragment: Fragment,
    backStack: Boolean = false
) {
    val transaction = supportFragmentManager.beginTransaction()
    transaction.replace(idRes, fragment)
    if (backStack) transaction.addToBackStack(null)
    transaction.commit()
}

fun FragmentActivity.addFragment(@IdRes idRes: Int, fragment: Fragment) {
    supportFragmentManager.beginTransaction()
        .add(idRes, fragment).commit()
}