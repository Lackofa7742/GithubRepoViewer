package io.dominiris.githubrepoviewer.app.domain

import io.dominiris.githubrepoviewer.app.data.api.GithubRepo
import io.dominiris.githubrepoviewer.app.data.ui.RepoListUiItem

class RepoMapper {
    fun mapFromApiToUi(apiModel: GithubRepo): RepoListUiItem {
        return RepoListUiItem(
            id = apiModel.id,
            name = apiModel.name,
            description = apiModel.description,
            ownerName = apiModel.owner.login,
            ownerAvatarUrl = apiModel.owner.avatar_url,
            stars = apiModel.stargazers_count,
            httpUrl = apiModel.html_url,
            contributorsUrl = apiModel.contributors_url
        )
    }
}