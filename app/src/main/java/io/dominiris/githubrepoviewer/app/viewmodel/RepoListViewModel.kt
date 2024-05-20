package io.dominiris.githubrepoviewer.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.dominiris.githubrepoviewer.app.data.ui.RepoListUiItem
import io.dominiris.githubrepoviewer.app.domain.FetchStarredReposUseCase
import io.dominiris.githubrepoviewer.app.domain.FetchTopContributorUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepoListViewModel @Inject constructor(
    private val repoListUseCase: FetchStarredReposUseCase,
    private val topContributorUseCase: FetchTopContributorUseCase
) : ViewModel() {
    private val _viewState = MutableStateFlow<RepoListViewState>(RepoListViewState.Loading)
    val viewState = _viewState.asStateFlow()

    private val _topContributorState = MutableStateFlow<String>("")
    val topContributorState = _topContributorState.asStateFlow()

    fun fetchStarredRepos(isRetry: Boolean = false) {
        if (isRetry) {
            _viewState.update { _ ->
                RepoListViewState.Loading
            }
        }
        viewModelScope.launch {
            val result = repoListUseCase.fetchRepoItems()
            _viewState.update { _ ->
                if (result.isSuccess) {
                    RepoListViewState.ShowingContent(result.getOrThrow())
                } else {
                    RepoListViewState.Error
                }
            }
        }
    }

    fun fetchTopContributor(url: String) {
        viewModelScope.launch {
            val result = topContributorUseCase.fetchTopContributor(url)
            _topContributorState.update {
                if (result.isSuccess) {
                    result.getOrThrow()
                } else {
                    ""
                }
            }
        }
    }
}

sealed class RepoListViewState {
    data object Loading : RepoListViewState()
    data class ShowingContent(val repoItems: List<RepoListUiItem>) : RepoListViewState()
    data object Error : RepoListViewState()
}