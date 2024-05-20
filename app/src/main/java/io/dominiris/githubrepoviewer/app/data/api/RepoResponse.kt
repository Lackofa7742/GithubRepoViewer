package io.dominiris.githubrepoviewer.app.data.api

import kotlinx.serialization.Serializable

@Serializable
data class RepoResponse(
    val total_count: Int,
    val incomplete_results: Boolean,
    val items: List<GithubRepo>
)
