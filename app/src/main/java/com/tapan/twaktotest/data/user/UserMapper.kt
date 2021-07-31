package com.tapan.twaktotest.data.user

import com.tapan.twaktotest.data.apimodels.DetailsRS
import com.tapan.twaktotest.data.apimodels.UserListRS
import com.tapan.twaktotest.data.user.local.UserEntity
import com.tapan.twaktotest.data.user.local.UserNoteJoined
import com.tapan.twaktotest.data.user.local.UserPartialDataEntity
import com.tapan.twaktotest.domain.model.User


fun UserNoteJoined.toDomainUser() = with(this.userEntity) {
    User(
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
        note?.note,
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

fun UserListRS.UserApiModel.toUserEntity() = with(this) {
    UserPartialDataEntity(
        id,
        avatarUrl,
        eventsUrl,
        followersUrl,
        followingUrl,
        gistsUrl,
        gravatarId,
        htmlUrl,
        login,
        nodeId,
        organizationsUrl,
        receivedEventsUrl,
        reposUrl,
        siteAdmin,
        starredUrl,
        subscriptionsUrl,
        type,
        url
    )
}

object UserMapper {

    fun detailsRsToEntity(entity: UserEntity, detailsRS: DetailsRS) = with(detailsRS) {
        entity.copy(
            bio = bio,
            blog = blog,
            company = company,
            createdAt = createdAt,
            email = email,
            followers = followers,
            following = following,
            hireable = hireable,
            location = location,
            name = name,
            publicGists = publicGists,
            publicRepos = publicRepos,
            twitterUsername = twitterUsername,
            updatedAt = updatedAt,
        )
    }
}