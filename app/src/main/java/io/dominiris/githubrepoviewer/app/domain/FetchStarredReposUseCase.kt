package io.dominiris.githubrepoviewer.app.domain

import io.dominiris.githubrepoviewer.app.GithubRepoRepository
import io.dominiris.githubrepoviewer.app.data.ui.RepoListUiItem
import javax.inject.Inject

class FetchStarredReposUseCase @Inject constructor(
    private val repository: GithubRepoRepository,
    private val mapper: RepoMapper
) {

    suspend fun fetchRepoItems(): Result<List<RepoListUiItem>> {
        val result = repository.fetchGithubRepos()
        return result.map { repos -> repos.map(mapper::mapFromApiToUi) }
    }
}