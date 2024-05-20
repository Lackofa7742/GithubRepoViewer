@file:OptIn(ExperimentalSharedTransitionApi::class)

package io.dominiris.githubrepoviewer.app.ui

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.valentinilk.shimmer.shimmer
import io.dominiris.githubrepoviewer.R

@Composable
fun RepoListUiItem(
    modifier: Modifier = Modifier,
    imageUrl: String,
    name: String,
    ownerName: String,
    starCount: Int,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransition: SharedTransitionScope
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(128.dp)
            .padding(start = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        with (sharedTransition) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .placeholderMemoryCacheKey("repo_imate_$name")
                    .memoryCacheKey("repo_imate_$name")
                    .build(),
                contentDescription = null,
                placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
                modifier = Modifier
                    .sharedElement(
                        rememberSharedContentState(key = "repo_image_$name"),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                    .clip(CircleShape)
                    .size(64.dp)
            )
        }
        Column(
            modifier = Modifier.padding(start = 16.dp)
        ) {
            Text(
                text = name,
                modifier = Modifier.padding(bottom = 8.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = ownerName,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Column(
            modifier = Modifier.padding(end = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_star),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = "$starCount",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun RepoListItemPlaceholder(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(128.dp)
            .padding(horizontal = 8.dp)
            .shimmer(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(color = MaterialTheme.colorScheme.primary)
        )
        Column {
            Box(
                modifier = Modifier
                    .padding(start = 16.dp, bottom = 8.dp)
                    .height(24.dp)
                    .width(100.dp)
                    .background(color = MaterialTheme.colorScheme.primary)
            )
            Box(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .height(24.dp)
                    .width(100.dp)
                    .background(color = MaterialTheme.colorScheme.primary)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(color = MaterialTheme.colorScheme.primary)
                .padding(end = 16.dp)
        )
    }
}