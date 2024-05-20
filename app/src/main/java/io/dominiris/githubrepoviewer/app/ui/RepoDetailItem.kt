@file:OptIn(ExperimentalSharedTransitionApi::class)

package io.dominiris.githubrepoviewer.app.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import io.dominiris.githubrepoviewer.R
import io.dominiris.githubrepoviewer.ui.theme.GithubRepoViewerTheme
import io.ktor.utils.io.concurrent.shared

@Composable
fun RepoDetailItem(
    imageUrl: String,
    name: String,
    description: String,
    owner: String,
    topContributor: String,
    repoUrl: String,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onClosed: () -> Unit
) {
    Surface(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(5.dp),
        shadowElevation = 2.dp
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_close),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 4.dp, start = 4.dp)
                    .size(24.dp)
                    .align(Alignment.Start)
                    .clickable { onClosed() },
                tint = MaterialTheme.colorScheme.onSurface
            )
            with(sharedTransitionScope) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUrl)
                        .crossfade(true)
                        .placeholderMemoryCacheKey("repo_imate_$name")
                        .memoryCacheKey("repo_imate_$name")
                        .build(),
                    contentDescription = null,
                    modifier = Modifier
                        .sharedElement(
                            rememberSharedContentState(key = "repo_imate_$name"),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                        .clip(CircleShape)
                        .size(128.dp)
                )
            }
            GradientDivider(modifier = Modifier.padding(horizontal = 16.dp))
            Text(
                text = name,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = description,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 4.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(id = R.string.repo_detail_owner, owner),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = stringResource(id = R.string.repo_detail_top_contributor, topContributor),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            ClickableLinkText(url = repoUrl, text = stringResource(id = R.string.repo_detail_url))
        }
    }
}

@Composable
fun ClickableLinkText(
    url: String,
    text: String
) {
    val context = LocalContext.current
    // Create an AnnotatedString with the link as an annotation
    val annotatedString = buildAnnotatedString {
        pushStringAnnotation(tag = "URL", annotation = text)
        withStyle(style = SpanStyle(color = Color.Blue, fontWeight = FontWeight.Bold)) {
            append(text)
        }
        pop()
    }

    // Create a ClickableText composable with the annotated string
    ClickableText(
        text = annotatedString,
        onClick = { offset ->
            // Get the annotation at the clicked offset
            annotatedString.getStringAnnotations(tag = "URL", start = offset, end = offset)
                .firstOrNull()?.let { annotation ->
                    // Open the URL in the browser
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    context.startActivity(intent)
                }
        }
    )
}

@Composable
fun GradientDivider(modifier: Modifier = Modifier) {
    // Get the onBackground color from the MaterialTheme
    val onBackgroundColor = MaterialTheme.colorScheme.onBackground

    // Create a gradient brush with the onBackground color and a transparent color
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            onBackgroundColor.copy(alpha = 0f),
            onBackgroundColor.copy(alpha = 1f),
            onBackgroundColor.copy(alpha = 0f)
        )
    )

    // Create a Box with the gradient background
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(2.dp)
            .background(gradientBrush)
    )
}
@Preview
@Composable
private fun RepoDetailItemPreview() {
    GithubRepoViewerTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
        }
    }
}