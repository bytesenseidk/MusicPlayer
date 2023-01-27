package io.github.larsrosenkilde.musicplayer.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import io.github.larsrosenkilde.musicplayer.services.groove.Song
import io.github.larsrosenkilde.musicplayer.services.radio.PlaybackPosition
import io.github.larsrosenkilde.musicplayer.ui.helpers.Routes
import io.github.larsrosenkilde.musicplayer.ui.helpers.ViewContext
import io.github.larsrosenkilde.musicplayer.ui.helpers.navigate

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun NowPlayingBottomBar(context: ViewContext) {
    var currentPlayingSong by remember {
        mutableStateOf(context.musicPlayer.radio.queue.currentPlayingSong)
    }
    var isPlaying by remember { mutableStateOf(context.musicPlayer.radio.isPlaying) }
    val showMiniPlayerExtendedControls = context.musicPlayer.settings.getMiniPlayerExtendedControls()

    EventerEffect(context.musicPlayer.radio.onUpdate) {
        currentPlayingSong = context.musicPlayer.radio.queue.currentPlayingSong
        isPlaying = context.musicPlayer.radio.isPlaying
    }

    AnimatedVisibility(
        visible = currentPlayingSong != null,
        enter = slideIn {
            IntOffset(0, it.height / 2)
        } + fadeIn(),
        exit = slideOut {
            IntOffset(0, it.height / 2)
        } + fadeOut()
    ) {
        currentPlayingSong?.let { currentSong ->
            Column(
                modifier = Modifier
                    .height(2.dp)
                    .fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .height(2.dp)
                        .fillMaxWidth()
                ) {
                    var duration by remember {
                        mutableStateOf(
                            context.musicPlayer.radio.currentPlaybackPosition ?: PlaybackPosition.zero
                        )
                    }
                    EventerEffect(context.musicPlayer.radio.onPlaybackPositionUpdate) {
                        duration = it
                    }
                    Box(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.primary.copy(0.3f))
                            .fillMaxWidth()
                            .fillMaxHeight()
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .background(MaterialTheme.colorScheme.primary)
                            .fillMaxWidth(duration.ratio)
                            .fillMaxHeight()
                    )
                }
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    shape = RectangleShape,
                    onClick = {
                        context.navController.navigate(Routes.NowPlaying)
                    }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(0.dp, 8.dp)
                    ) {
                        AnimatedContent(
                            modifier = Modifier.weight(1f),
                            targetState = currentSong,
                            transitionSpec = {
                                fadeIn(
                                    animationSpec = tween(220, delayMillis = 90)
                                ) + scaleIn(
                                    initialScale = 0.99f,
                                    animationSpec = tween(220, delayMillis = 90)
                                ) with fadeOut(animationSpec = tween(90))
                            },
                        ) { song ->
                            BoxWithConstraints {
                                val cardWidthPx = constraints.maxWidth
                                var offsetX by remember { mutableStateOf(0f) }
                                val cardOffsetX = animateIntAsState(offsetX.toInt())
                                val cardOpacity = animateFloatAsState(
                                    if (offsetX != 0f) 0.7f else 1f
                                )
                                Box(
                                    modifier = Modifier
                                        .alpha(cardOpacity.value)
                                        .absoluteOffset {
                                            IntOffset(cardOffsetX.value.div(2), 0)
                                        }
                                        .pointerInput(Unit) {
                                            detectHorizontalDragGestures(
                                                onDragEnd = {
                                                    val thresh = cardWidthPx / 4
                                                    offsetX = when {
                                                        -offsetX > thresh -> {
                                                            val changed =
                                                                context.musicPlayer.radio.shorty.skip()
                                                            if (changed) -cardWidthPx.toFloat() else 0f
                                                        }
                                                        offsetX > thresh -> {
                                                            val changed =
                                                                context.musicPlayer.radio.shorty.previous()
                                                            if (changed) cardWidthPx.toFloat() else 0f
                                                        }
                                                        else -> 0f
                                                    }
                                                },
                                                onDragCancel = {
                                                    offsetX = 0f
                                                },
                                                onHorizontalDrag = { _, dragAmount ->
                                                    offsetX += dragAmount
                                                }
                                            )
                                        }
                                    ) {
                                    NowPlayingBottomBarContent(context = context, song = song)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.width(15.dp))
                        if (showMiniPlayerExtendedControls) {
                            IconButton(
                                onClick = { context.musicPlayer.radio.shorty.previous() }
                            ) {
                                Icon(Icons.Default.SkipPrevious, null)
                            }
                        }
                        IconButton(
                            onClick = { context.musicPlayer.radio.shorty.playPause() }
                        ) {
                            Icon(
                                if (!isPlaying) Icons.Default.PlayArrow
                                else Icons.Default.Pause,
                                null
                            )
                        }
                        if (showMiniPlayerExtendedControls) {
                            IconButton(
                                onClick = { context.musicPlayer.radio.shorty.skip() }
                            ) {
                                Icon(Icons.Default.SkipNext, null)
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun NowPlayingBottomBarContent(context: ViewContext, song: Song) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Spacer(modifier = Modifier.width(12.dp))
        AsyncImage(
            song.createArtworkImageRequest(context.musicPlayer).build(),
            null,
            modifier = Modifier
                .size(45.dp)
                .clip(RoundedCornerShape(10.dp))
        )
        Spacer(modifier = Modifier.width(15.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                song.title,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            song.artistName?.let { artistName ->
                Text(
                    artistName,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}