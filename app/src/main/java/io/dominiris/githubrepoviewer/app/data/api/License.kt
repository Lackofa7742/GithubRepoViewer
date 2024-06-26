package io.dominiris.githubrepoviewer.app.data.api

import kotlinx.serialization.Serializable

@Serializable
data class License(
    val key: String,
    val name: String,
    val spdx_id: String,
    val url: String?,
    val node_id: String
)