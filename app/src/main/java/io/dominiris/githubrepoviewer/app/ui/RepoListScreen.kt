@file:OptIn(ExperimentalSharedTransitionApi::class)

package io.dominiris.githubrepoviewer.app.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer
import io.dominiris.githubrepoviewer.R
import io.dominiris.githubrepoviewer.app.data.ui.RepoListUiItem
import io.dominiris.githubrepoviewer.app.viewmodel.RepoListViewModel
import io.dominiris.githubrepoviewer.app.viewmodel.RepoListViewState
import io.dominiris.githubrepoviewer.ui.theme.GithubRepoViewerTheme

@Composable
fun RepoListScreen(
    viewModel: RepoListViewModel = hiltViewModel()
) {
    val repoItemState = viewModel.viewState.collectAsState()
    val topContributorState = viewModel.topContributorState.collectAsState()
    var showingDetail: Boolean by remember {
        mutableStateOf(false)
    }
    var selectedUiItem: RepoListUiItem? by remember {
        mutableStateOf(null)
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.fetchStarredRepos()
    }

    AnimatedContent(targetState = repoItemState) { targetState ->
        when (targetState.value) {
            is RepoListViewState.Loading -> {
                LazyColumn {
                    items(4) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            color = MaterialTheme.colorScheme.surface,
                            shadowElevation = 2.dp,
                            shape = RoundedCornerShape(5.dp)
                        ) {
                            RepoListItemPlaceholder()
                        }
                    }
                }
            }

            is RepoListViewState.ShowingContent -> {
                val repoList = (repoItemState.value as RepoListViewState.ShowingContent).repoItems
                SharedTransitionLayout {
                    AnimatedContent(targetState = showingDetail) {
                        if (it && selectedUiItem != null) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(color = MaterialTheme.colorScheme.background),
                                contentAlignment = Alignment.Center
                            ) {
                                RepoDetailItem(
                                    imageUrl = selectedUiItem!!.ownerAvatarUrl,
                                    name = selectedUiItem!!.name,
                                    description = selectedUiItem!!.description ?: "",
                                    owner = selectedUiItem!!.ownerName,
                                    topContributor = topContributorState.value,
                                    repoUrl = selectedUiItem!!.httpUrl,
                                    sharedTransitionScope = this@SharedTransitionLayout,
                                    animatedVisibilityScope = this@AnimatedContent,
                                    onClosed = {
                                        showingDetail = false
                                        selectedUiItem = null
                                    }
                                )
                            }
                        } else {
                            LazyColumn {
                                items(repoList) { repoItem ->
                                    Surface(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp, vertical = 8.dp)
                                            .clickable {
                                                selectedUiItem = repoItem
                                                showingDetail = true
                                                viewModel.fetchTopContributor(repoItem.contributorsUrl)
                                            },
                                        color = MaterialTheme.colorScheme.surface,
                                        shadowElevation = 2.dp,
                                        shape = RoundedCornerShape(5.dp)
                                    ) {
                                        RepoListUiItem(
                                            imageUrl = repoItem.ownerAvatarUrl,
                                            name = repoItem.name,
                                            ownerName = repoItem.ownerName,
                                            starCount = repoItem.stars,
                                            animatedVisibilityScope = this@AnimatedContent,
                                            sharedTransition = this@SharedTransitionLayout
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            is RepoListViewState.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.colorScheme.background),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.close),
                        contentDescription = null,
                        modifier = Modifier.size(200.dp)
                    )
                    Text(
                        text = stringResource(id = R.string.list_error),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )
                    Button(onClick = { viewModel.fetchStarredRepos(isRetry = true) }) {
                        Text(
                            text = stringResource(id = R.string.list_error_try_again),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onPrimary,
                        )
                    }
                }
            }
        }
    }
}