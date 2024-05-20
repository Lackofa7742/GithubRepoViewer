package io.dominiris.githubrepoviewer.app.data.ui

data class RepoListUiItem(
    val id: Long,
    val name: String,
    val description: String?,
    val ownerName: String,
    val ownerAvatarUrl: String,
    val stars: Int,
    val httpUrl: String,
    val contributorsUrl: String
)
