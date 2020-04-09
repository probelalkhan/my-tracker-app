package net.simplifiedcoding.mytracker.ui.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun io(work: suspend (() -> Unit)) =
    CoroutineScope(Dispatchers.IO).launch {
        work()
    }