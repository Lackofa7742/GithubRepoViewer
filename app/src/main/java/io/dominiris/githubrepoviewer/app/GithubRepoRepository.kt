package io.dominiris.githubrepoviewer.app

import io.dominiris.githubrepoviewer.app.data.api.Contributor
import io.dominiris.githubrepoviewer.app.data.api.GithubRepo
import io.dominiris.githubrepoviewer.app.data.api.RepoResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import javax.inject.Inject

class GithubRepoRepository @Inject constructor(private val client: HttpClient) {

    suspend fun fetchGithubRepos(): Result<List<GithubRepo>> {
        return try {
            val response = client.get<RepoResponse>("https://api.github.com/search/repositories?q=stars:%3E0")
            Result.success(response.items)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun fetchContributors(url: String): Result<List<Contributor>> {
        return try {
            val response = client.get<List<Contributor>>(url)
            return Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}