package com.tapan.twaktotest.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tapan.twaktotest.ui.theme.TwakToTestTheme
import com.tapan.twaktotest.ui.userslist.UserListComposeView
import com.tapan.twaktotest.ui.userslist.UserListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainComposeActivity : ComponentActivity() {

    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            TwakToTestTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    TestApp(navController)
                }
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun TestApp(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.USER_LIST.name
    ) {
        composable(Routes.USER_LIST.name) {
            val userListViewModel = hiltViewModel<UserListViewModel>()
            userListViewModel.getAllUsers(0)
            UserListComposeView(userListViewModel.userListLiveData)
        }

    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TwakToTestTheme {
//        TestApp("Android")
    }
}