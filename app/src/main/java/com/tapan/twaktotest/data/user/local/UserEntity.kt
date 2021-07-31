package com.tapan.twaktotest.data.user.local

import androidx.room.*

@Entity(tableName = "USERS")
data class UserEntity(
    @PrimaryKey
    @ColumnInfo(name = "ID")
    var id: Int,
    @ColumnInfo(name = "AVATAR_URL")
    var avatarUrl: String,
    @ColumnInfo(name = "EVENTS_URL")
    var eventsUrl: String,
    @ColumnInfo(name = "FOLLOWERS_URL")
    var followersUrl: String,
    @ColumnInfo(name = "FOLLOWING_URL")
    var followingUrl: String,
    @ColumnInfo(name = "GISTS_URL")
    var gistsUrl: String,
    @ColumnInfo(name = "GRAVATAR_ID")
    var gravatarId: String,
    @ColumnInfo(name = "HTML_URL")
    var htmlUrl: String,
    @ColumnInfo(name = "LOGIN")
    var login: String,
    @ColumnInfo(name = "NODE_ID")
    var nodeId: String,
    @ColumnInfo(name = "ORGANIZATIONS_URL")
    var organizationsUrl: String,
    @ColumnInfo(name = "RECEIVED_EVENTS_URL")
    var receivedEventsUrl: String,
    @ColumnInfo(name = "REPOS_URL")
    var reposUrl: String,
    @ColumnInfo(name = "SITE_ADMIN")
    var siteAdmin: Boolean,
    @ColumnInfo(name = "STARRED_URL")
    var starredUrl: String,
    @ColumnInfo(name = "SUBSCRIPTIONS_URL")
    var subscriptionsUrl: String,
    @ColumnInfo(name = "TYPE")
    var type: String,
    @ColumnInfo(name = "URL")
    var url: String,

    @ColumnInfo(name = "bio")
    var bio: String? = null,
    @ColumnInfo(name = "blog")
    var blog: String? = null,
    @ColumnInfo(name = "company")
    var company: String? = null,
    @ColumnInfo(name = "created_at")
    var createdAt: String? = null,
    @ColumnInfo(name = "email")
    var email: String? = null,
    @ColumnInfo(name = "followers")
    var followers: Int? = null,
    @ColumnInfo(name = "following")
    var following: Int? = null,
    @ColumnInfo(name = "hireable")
    var hireable: String? = null,
    @ColumnInfo(name = "location")
    var location: String? = null,
    @ColumnInfo(name = "name")
    var name: String? = null,
    @ColumnInfo(name = "public_gists")
    var publicGists: Int? = null,
    @ColumnInfo(name = "public_repos")
    var publicRepos: Int? = null,
    @ColumnInfo(name = "twitter_username")
    var twitterUsername: String? = null,
    @ColumnInfo(name = "updated_at")
    var updatedAt: String? = null,
    @ColumnInfo(name = "IS_IMAGE_CACHED",defaultValue = "false")
    var isImageCached: Boolean = false
)

@Entity
data class UserPartialDataEntity(
    @PrimaryKey
    @ColumnInfo(name = "ID")
    var id: Int,
    @ColumnInfo(name = "AVATAR_URL")
    var avatarUrl: String,
    @ColumnInfo(name = "EVENTS_URL")
    var eventsUrl: String,
    @ColumnInfo(name = "FOLLOWERS_URL")
    var followersUrl: String,
    @ColumnInfo(name = "FOLLOWING_URL")
    var followingUrl: String,
    @ColumnInfo(name = "GISTS_URL")
    var gistsUrl: String,
    @ColumnInfo(name = "GRAVATAR_ID")
    var gravatarId: String,
    @ColumnInfo(name = "HTML_URL")
    var htmlUrl: String,
    @ColumnInfo(name = "LOGIN")
    var login: String,
    @ColumnInfo(name = "NODE_ID")
    var nodeId: String,
    @ColumnInfo(name = "ORGANIZATIONS_URL")
    var organizationsUrl: String,
    @ColumnInfo(name = "RECEIVED_EVENTS_URL")
    var receivedEventsUrl: String,
    @ColumnInfo(name = "REPOS_URL")
    var reposUrl: String,
    @ColumnInfo(name = "SITE_ADMIN")
    var siteAdmin: Boolean,
    @ColumnInfo(name = "STARRED_URL")
    var starredUrl: String,
    @ColumnInfo(name = "SUBSCRIPTIONS_URL")
    var subscriptionsUrl: String,
    @ColumnInfo(name = "TYPE")
    var type: String,
    @ColumnInfo(name = "URL")
    var url: String


)
data class UserNoteJoined(
    @Embedded
    var userEntity: UserEntity,
    @Relation(parentColumn = "ID", entityColumn = "USER_ID", entity = NoteEntity::class)
    var note: NoteEntity?
)