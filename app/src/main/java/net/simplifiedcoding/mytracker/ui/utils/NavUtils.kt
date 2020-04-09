package net.simplifiedcoding.mytracker.ui.utils

import android.view.View
import androidx.annotation.IdRes
import androidx.navigation.findNavController

fun popBackStackAndNavigate(view: View, @IdRes destinationID: Int) {
    if (view.findNavController().currentDestination?.id != destinationID)
        view.findNavController().also {
            it.popBackStack()
            it.navigate(destinationID)
        }
}

fun navigate(view: View, @IdRes destinationID: Int) {
    if (view.findNavController().currentDestination?.id != destinationID)
        view.findNavController().navigate(destinationID)
}