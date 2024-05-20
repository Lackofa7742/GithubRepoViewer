package io.dominiris.githubrepoviewer.app.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.dominiris.githubrepoviewer.app.GithubRepoRepository
import io.dominiris.githubrepoviewer.app.domain.FetchStarredReposUseCase
import io.dominiris.githubrepoviewer.app.domain.FetchTopContributorUseCase
import io.dominiris.githubrepoviewer.app.domain.RepoMapper
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.url
import io.ktor.http.auth.AuthScheme
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideHttpClient(): HttpClient {
        return HttpClient(Android) {
            install(JsonFeature) {
                serializer = KotlinxSerializer(
                    Json {
                        ignoreUnknownKeys = true
                        isLenient = true
                    }
                )
            }
        }
    }

    @Provides
    fun provideGithubRepoRepository(httpClient: HttpClient): GithubRepoRepository {
        return GithubRepoRepository(httpClient)
    }

    @Provides
    fun provideRepoMapper() = RepoMapper()

    @Provides
    fun provideFetchStarredReposUseCase(
        githubRepoRepository: GithubRepoRepository,
        repoMapper: RepoMapper
    ): FetchStarredReposUseCase {
        return FetchStarredReposUseCase(githubRepoRepository, repoMapper)
    }

    @Provides
    fun provideFetchTopContributorUseCase(
        githubRepoRepository: GithubRepoRepository
    ): FetchTopContributorUseCase {
        return FetchTopContributorUseCase(githubRepoRepository)
    }
}