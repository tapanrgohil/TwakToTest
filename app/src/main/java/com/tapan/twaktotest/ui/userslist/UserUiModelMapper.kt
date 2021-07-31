package com.tapan.twaktotest.ui.userslist

import com.tapan.twaktotest.domain.model.User
import com.tapan.twaktotest.ui.model.UserAdapterModel
import com.tapan.twaktotest.ui.model.UserAdapterModelType
import com.tapan.twaktotest.ui.model.UserUiModel

object UserUiModelMapper {

    fun User.mapUserToUserUi() = with(this) {
        return@with UserUiModel(
            avatarUrl,
            eventsUrl,
            followersUrl,
            followingUrl,
            gistsUrl,
            gravatarId,
            htmlUrl,
            id,
            login,
            nodeId,
            organizationsUrl,
            receivedEventsUrl,
            reposUrl,
            siteAdmin,
            starredUrl,
            subscriptionsUrl,
            type,
            url,
            note,
            bio,
            blog,
            company,
            createdAt,
            email,
            followers,
            following,
            hireable,
            location,
            name,
            publicGists,
            publicRepos,
            twitterUsername
        )
    }


    fun User.mapUserToUserAdapterModel() = with(this) {
        return@with UserAdapterModel(
            mapUserToUserUi(), type = UserAdapterModelType.NORMAL
        )
    }


    fun List<User>.toUserAdapterModelList(): List<UserAdapterModel> = with(this) {
        return@with map {
            val model = it.mapUserToUserAdapterModel()
            model
        }
    }


}