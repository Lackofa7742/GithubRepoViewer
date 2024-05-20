package io.dominiris.githubrepoviewer.app.domain

import io.dominiris.githubrepoviewer.app.GithubRepoRepository
import javax.inject.Inject

class FetchTopContributorUseCase @Inject constructor(
    private val repository: GithubRepoRepository
) {

    suspend fun fetchTopContributor(url: String): Result<String> {
        val result = repository.fetchContributors(url)

        if (result.isSuccess) {
            val contributor = result.getOrThrow()
                .sortedByDescending { it.contributions }
                .map { it.login }
                .first()
            return Result.success(contributor)
        } else {
            return Result.failure(result.exceptionOrNull() ?: RuntimeException())
        }
    }
}