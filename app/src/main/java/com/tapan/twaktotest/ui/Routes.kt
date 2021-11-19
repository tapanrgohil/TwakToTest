package com.tapan.twaktotest.ui

import androidx.compose.runtime.Composable

sealed class Routes(val name: String) {
    object USER_LIST: Routes("USER_LIST")
}
