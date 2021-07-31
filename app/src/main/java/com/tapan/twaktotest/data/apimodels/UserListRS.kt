package com.tapan.twaktotest.data.apimodels
import com.google.gson.annotations.SerializedName


class UserListRS : ArrayList<UserListRS.UserApiModel>(){
    data class UserApiModel(
        @SerializedName("avatar_url")
        val avatarUrl: String,
        @SerializedName("events_url")
        val eventsUrl: String,
        @SerializedName("followers_url")
        val followersUrl: String,
        @SerializedName("following_url")
        val followingUrl: String,
        @SerializedName("gists_url")
        val gistsUrl: String,
        @SerializedName("gravatar_id")
        val gravatarId: String,
        @SerializedName("html_url")
        val htmlUrl: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("login")
        val login: String,
        @SerializedName("node_id")
        val nodeId: String,
        @SerializedName("organizations_url")
        val organizationsUrl: String,
        @SerializedName("received_events_url")
        val receivedEventsUrl: String,
        @SerializedName("repos_url")
        val reposUrl: String,
        @SerializedName("site_admin")
        val siteAdmin: Boolean,
        @SerializedName("starred_url")
        val starredUrl: String,
        @SerializedName("subscriptions_url")
        val subscriptionsUrl: String,
        @SerializedName("type")
        val type: String,
        @SerializedName("url")
        val url: String,
        
        @SerializedName("bio")
        val bio: String?,
        @SerializedName("blog")
        val blog: String,
        @SerializedName("company")
        val company: String?,
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("email")
        val email: String?,
        @SerializedName("followers")
        val followers: Int,
        @SerializedName("following")
        val following: Int,
        @SerializedName("hireable")
        val hireable: String?,
        @SerializedName("location")
        val location: String?,
        @SerializedName("name")
        val name: String?,
        @SerializedName("public_gists")
        val publicGists: Int,
        @SerializedName("public_repos")
        val publicRepos: Int,
        @SerializedName("twitter_username")
        val twitterUsername: String?,
        @SerializedName("updated_at")
        val updatedAt: String
    )
}