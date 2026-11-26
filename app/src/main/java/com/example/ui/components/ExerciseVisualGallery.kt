package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.ZoomIn
import androidx.compose.material.icons.outlined.ZoomOutMap
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.catalog.ExerciseGalleryCatalog
import com.example.domain.model.GalleryFrameCategory
import com.example.domain.model.MovementDetail
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ExercisePoseThumbnail(
    movement: MovementDetail,
    accentColor: Color = disciplineColor(movement.discipline),
    modifier: Modifier = Modifier
) {
    val gallery = remember(movement.id) { ExerciseGalleryCatalog.galleryFor(movement) }
    val previewFrame = gallery.frames.getOrNull(3) ?: gallery.frames.first()
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.28f))
    ) {
        ExercisePoseCanvas(
            spec = gallery,
            progressT = previewFrame.progressT,
            accentColor = accentColor,
            compact = true,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun ExerciseVisualGallery(
    movement: MovementDetail,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    val gallery = remember(movement.id) { ExerciseGalleryCatalog.galleryFor(movement) }
    val frames = gallery.frames
    val pagerState = rememberPagerState(pageCount = { frames.size })
    val thumbListState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    var isAutoPlaying by remember { mutableStateOf(false) }
    var showFullscreen by remember { mutableStateOf(false) }

    LaunchedEffect(pagerState.currentPage) {
        thumbListState.animateScrollToItem(pagerState.currentPage)
    }

    LaunchedEffect(isAutoPlaying) {
        if (isAutoPlaying) {
            var step = pagerState.currentPage.coerceAtMost(4)
            while (isAutoPlaying) {
                delay(1150L)
                step = (step + 1) % 5 // Cycle frames 1..5 to demonstrate complete movement loop
                pagerState.animateScrollToPage(step)
            }
        }
    }

    val activeFrame = frames[pagerState.currentPage]

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("exercise_visual_gallery"),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceContainer,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            // Top bar: Title + counter + motion preview + enlarge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "विज़ुअल अभ्यास गाइड",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "स्वाइप करें या थंबनेल चुनें (${pagerState.currentPage + 1}/${frames.size})",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FilledTonalButton(
                        onClick = { isAutoPlaying = !isAutoPlaying },
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .testTag("gallery_autoplay_button")
                    ) {
                        Icon(
                            imageVector = if (isAutoPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                            contentDescription = if (isAutoPlaying) "रोकें" else "गति देखें",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = if (isAutoPlaying) "रोकें" else "मोशन देखें",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                    IconButton(
                        onClick = {
                            isAutoPlaying = false
                            showFullscreen = true
                        },
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .testTag("gallery_expand_button")
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ZoomOutMap,
                            contentDescription = "बड़ी स्क्रीन में देखें",
                            tint = accentColor
                        )
                    }
                }
            }

            // Main swipeable HorizontalPager
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("gallery_pager")
            ) { page ->
                val frame = frames[page]
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            isAutoPlaying = false
                            showFullscreen = true
                        },
                    shape = MaterialTheme.shapes.medium,
                    border = BorderStroke(1.dp, accentColor.copy(alpha = 0.25f))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                    ) {
                        ExercisePoseCanvas(
                            spec = gallery,
                            progressT = frame.progressT,
                            accentColor = accentColor,
                            isAltAngle = frame.isAltAngle,
                            isDetailZoom = frame.isDetailZoom,
                            isReturning = frame.category == GalleryFrameCategory.RETURN_MOVEMENT,
                            compact = false,
                            modifier = Modifier.fillMaxSize()
                        )

                        // Angle pill top-left
                        Surface(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(10.dp),
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.88f),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                        ) {
                            Text(
                                text = frame.angleLabel,
                                style = MaterialTheme.typography.labelSmall,
                                color = accentColor,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        // Step badge + Zoom hint top-right
                        Surface(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(10.dp),
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.88f),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.ZoomIn,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "${page + 1}/${frames.size}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            // Active frame caption & key focus cue
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.small,
                color = MaterialTheme.colorScheme.surfaceContainerHigh
            ) {
                Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = activeFrame.title,
                            style = MaterialTheme.typography.titleSmall,
                            color = accentColor
                        )
                        Surface(
                            color = accentColor.copy(alpha = 0.14f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = activeFrame.focusHighlight,
                                style = MaterialTheme.typography.labelSmall,
                                color = accentColor,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                            )
                        }
                    }
                    Text(
                        text = activeFrame.instructionCue,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Lazy-loaded thumbnail strip
            LazyRow(
                state = thumbListState,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("gallery_thumbnail_strip")
            ) {
                itemsIndexed(frames, key = { idx, f -> "${movement.id}_${f.category.name}_$idx" }) { idx, frame ->
                    val selected = idx == pagerState.currentPage
                    Surface(
                        modifier = Modifier
                            .width(74.dp)
                            .height(58.dp)
                            .testTag("gallery_thumb_$idx")
                            .clickable {
                                isAutoPlaying = false
                                scope.launch { pagerState.animateScrollToPage(idx) }
                            },
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(
                            width = if (selected) 2.dp else 1.dp,
                            color = if (selected) accentColor else MaterialTheme.colorScheme.outlineVariant
                        ),
                        color = MaterialTheme.colorScheme.surfaceContainerLow
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            ExercisePoseCanvas(
                                spec = gallery,
                                progressT = frame.progressT,
                                accentColor = accentColor,
                                isAltAngle = frame.isAltAngle,
                                isDetailZoom = frame.isDetailZoom,
                                compact = true,
                                modifier = Modifier.fillMaxSize()
                            )
                            Surface(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(3.dp),
                                shape = RoundedCornerShape(4.dp),
                                color = if (selected) accentColor else MaterialTheme.colorScheme.surface.copy(alpha = 0.85f)
                            ) {
                                Text(
                                    text = "${idx + 1}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showFullscreen) {
        FullscreenExerciseGalleryModal(
            movement = movement,
            initialPage = pagerState.currentPage,
            accentColor = accentColor,
            onDismiss = { lastPage ->
                showFullscreen = false
                scope.launch { pagerState.scrollToPage(lastPage) }
            }
        )
    }
}

@Composable
private fun FullscreenExerciseGalleryModal(
    movement: MovementDetail,
    initialPage: Int,
    accentColor: Color,
    onDismiss: (Int) -> Unit
) {
    val gallery = remember(movement.id) { ExerciseGalleryCatalog.galleryFor(movement) }
    val frames = gallery.frames
    val pagerState = rememberPagerState(initialPage = initialPage, pageCount = { frames.size })
    val scope = rememberCoroutineScope()

    Dialog(
        onDismissRequest = { onDismiss(pagerState.currentPage) },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .testTag("fullscreen_gallery_dialog"),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .systemBarsPadding()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = movement.name,
                            style = MaterialTheme.typography.titleLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "पिंच या डबल-टैप से ज़ूम करें • स्वाइप से फ़्रेम बदलें",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(
                        onClick = { onDismiss(pagerState.currentPage) },
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .testTag("close_fullscreen_gallery")
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "बंद करें")
                    }
                }

                Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxSize()
                    ) { page ->
                        val frame = frames[page]
                        var scale by remember(page) { mutableFloatStateOf(1f) }
                        var offsetX by remember(page) { mutableFloatStateOf(0f) }
                        var offsetY by remember(page) { mutableFloatStateOf(0f) }
                        val animatedScale by animateFloatAsState(targetValue = scale, label = "zoomScale")

                        Surface(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(MaterialTheme.shapes.large)
                                .pointerInput(page) {
                                    detectTapGestures(
                                        onDoubleTap = {
                                            if (scale > 1.2f) {
                                                scale = 1f
                                                offsetX = 0f
                                                offsetY = 0f
                                            } else {
                                                scale = 2f
                                            }
                                        }
                                    )
                                }
                                .pointerInput(page) {
                                    detectTransformGestures { _, pan, zoom, _ ->
                                        val nextScale = (scale * zoom).coerceIn(1f, 3f)
                                        scale = nextScale
                                        if (nextScale > 1f) {
                                            offsetX = (offsetX + pan.x).coerceIn(-250f, 250f)
                                            offsetY = (offsetY + pan.y).coerceIn(-250f, 250f)
                                        } else {
                                            offsetX = 0f
                                            offsetY = 0f
                                        }
                                    }
                                },
                            shape = MaterialTheme.shapes.large,
                            border = BorderStroke(1.dp, accentColor.copy(alpha = 0.35f))
                        ) {
                            ExercisePoseCanvas(
                                spec = gallery,
                                progressT = frame.progressT,
                                accentColor = accentColor,
                                isAltAngle = frame.isAltAngle,
                                isDetailZoom = frame.isDetailZoom,
                                isReturning = frame.category == GalleryFrameCategory.RETURN_MOVEMENT,
                                compact = false,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .graphicsLayer(
                                        scaleX = animatedScale,
                                        scaleY = animatedScale,
                                        translationX = offsetX,
                                        translationY = offsetY
                                    )
                            )
                        }
                    }

                    // Prev / Next Floating Pill Controls
                    if (pagerState.currentPage > 0) {
                        IconButton(
                            onClick = { scope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) } },
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(8.dp)
                                .size(48.dp)
                                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.85f), CircleShape)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "पिछला फ़्रेम")
                        }
                    }
                    if (pagerState.currentPage < frames.lastIndex) {
                        IconButton(
                            onClick = { scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) } },
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(8.dp)
                                .size(48.dp)
                                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.85f), CircleShape)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "अगला फ़्रेम")
                        }
                    }
                }

                val currentFrame = frames[pagerState.currentPage]
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.surfaceContainer,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(currentFrame.title, style = MaterialTheme.typography.titleMedium, color = accentColor)
                            Text(
                                "${currentFrame.angleLabel} • ${pagerState.currentPage + 1}/${frames.size}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(currentFrame.instructionCue, style = MaterialTheme.typography.bodyMedium)
                        Text(
                            "मुख्य ध्यान: ${currentFrame.focusHighlight}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    itemsIndexed(frames) { idx, frame ->
                        val selected = idx == pagerState.currentPage
                        FilterChip(
                            selected = selected,
                            onClick = { scope.launch { pagerState.animateScrollToPage(idx) } },
                            label = { Text("${idx + 1}. ${frame.category.title.substringBefore(" ")}") }
                        )
                    }
                }
            }
        }
    }
}
