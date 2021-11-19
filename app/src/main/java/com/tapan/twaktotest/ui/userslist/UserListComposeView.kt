package com.tapan.twaktotest.ui.userslist

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tapan.twaktotest.data.core.Resource
import com.tapan.twaktotest.ui.model.UserAdapterModel
import com.tapan.twaktotest.ui.model.UserAdapterModelType
import com.tapan.twaktotest.ui.userslist.adapter.UserAdapterView
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@ExperimentalAnimationApi
@Composable
fun UserListComposeView(flow: StateFlow<Resource<List<UserAdapterModel>>>) {
    val isSearch = MutableStateFlow(false)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "User List")
                },
                actions = {
                    val isSearchVIew = isSearch.asStateFlow().collectAsState()
                    if (!isSearchVIew.value) {
                        IconButton(onClick = {
                            isSearch.value = true;
                        }) {
                            Icon(Icons.Filled.Search, null)
                        }

                    } else {
                        TextField(value = "", onValueChange = {

                        },label = {
                                  Text(text = "Search",color = Color.White)
                        }, modifier = Modifier.fillMaxWidth())
                    }

                },
                backgroundColor = Color.Blue,
                contentColor = Color.White,
                elevation = 12.dp,

                )
        }, content = {
            val userList = flow.collectAsState()
            Log.d("flow", userList.value.data.toString())
            LazyColumn {

                userList.value.data.orEmpty().map { model ->

                    item(key = { model.userUiModel?.id ?: model.type.hashCode() }) {
                        if (model.type == UserAdapterModelType.NORMAL) {
                            with(model.userUiModel!!) {
                                UserAdapterView(avatarUrl, login, bio.orEmpty(), note != null)
                            }
                        } else {
                            CircularProgressIndicator(
                                modifier =
                                Modifier.padding(Dp(25f))
                            )
                        }
                    }
                }

            }
        })


}


/*
@Preview(widthDp = 320, showSystemUi = true)
@ExperimentalAnimationApi
@Composable
fun previewUserListComposeView() {
    UserListComposeView(
        userList =
        (1..10).map {
            UserAdapterModel(
                UserUiModel(
                    avatarUrl = "",
                    eventsUrl = "",
                    followersUrl = "",
                    followingUrl = "",
                    gistsUrl = "",
                    gravatarId = "",
                    htmlUrl = "",
                    id = 0,
                    login = "test",
                    nodeId = "",
                    organizationsUrl = "",
                    receivedEventsUrl = "",
                    reposUrl = "",
                    siteAdmin = false,
                    starredUrl = "",
                    subscriptionsUrl = "",
                    type = "",
                    url = "",
                    note = null,
                    bio = null,
                    blog = null,
                    company = null,
                    createdAt = null,
                    email = null,
                    followers = null,
                    following = null,
                    hireable = null,
                    location = null,
                    name = null,
                    publicGists = null,
                    publicRepos = null,
                    twitterUsername = null,
                    updatedAt = null
                ), false
            )
        }

    )

}*/
