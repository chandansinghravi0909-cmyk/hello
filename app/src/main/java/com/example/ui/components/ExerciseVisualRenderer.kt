package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.util.lerp
import com.example.domain.model.*
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

private data class P2(val x: Float, val y: Float) {
    fun toOffset(w: Float, h: Float) = Offset(x * w, y * h)
}

private fun lerpP2(a: P2, b: P2, t: Float) = P2(lerp(a.x, b.x, t), lerp(a.y, b.y, t))

private data class ArticulatedPose(
    val head: P2,
    val shoulderNear: P2,
    val elbowNear: P2,
    val handNear: P2,
    val shoulderFar: P2,
    val elbowFar: P2,
    val handFar: P2,
    val hipNear: P2,
    val kneeNear: P2,
    val ankleNear: P2,
    val hipFar: P2,
    val kneeFar: P2,
    val ankleFar: P2,
    val focusPoint: P2 = hipNear,
    val arrowFrom: P2? = null,
    val arrowTo: P2? = null,
    val propAnchorA: P2? = null,
    val propAnchorB: P2? = null
)

private fun lerpPose(a: ArticulatedPose, b: ArticulatedPose, t: Float): ArticulatedPose = ArticulatedPose(
    head = lerpP2(a.head, b.head, t),
    shoulderNear = lerpP2(a.shoulderNear, b.shoulderNear, t),
    elbowNear = lerpP2(a.elbowNear, b.elbowNear, t),
    handNear = lerpP2(a.handNear, b.handNear, t),
    shoulderFar = lerpP2(a.shoulderFar, b.shoulderFar, t),
    elbowFar = lerpP2(a.elbowFar, b.elbowFar, t),
    handFar = lerpP2(a.handFar, b.handFar, t),
    hipNear = lerpP2(a.hipNear, b.hipNear, t),
    kneeNear = lerpP2(a.kneeNear, b.kneeNear, t),
    ankleNear = lerpP2(a.ankleNear, b.ankleNear, t),
    hipFar = lerpP2(a.hipFar, b.hipFar, t),
    kneeFar = lerpP2(a.kneeFar, b.kneeFar, t),
    ankleFar = lerpP2(a.ankleFar, b.ankleFar, t),
    focusPoint = lerpP2(a.focusPoint, b.focusPoint, t),
    arrowFrom = if (a.arrowFrom != null && b.arrowFrom != null) lerpP2(a.arrowFrom, b.arrowFrom, t) else b.arrowFrom ?: a.arrowFrom,
    arrowTo = if (a.arrowTo != null && b.arrowTo != null) lerpP2(a.arrowTo, b.arrowTo, t) else b.arrowTo ?: a.arrowTo,
    propAnchorA = if (a.propAnchorA != null && b.propAnchorA != null) lerpP2(a.propAnchorA, b.propAnchorA, t) else b.propAnchorA ?: a.propAnchorA,
    propAnchorB = if (a.propAnchorB != null && b.propAnchorB != null) lerpP2(a.propAnchorB, b.propAnchorB, t) else b.propAnchorB ?: a.propAnchorB
)

@Composable
fun ExercisePoseCanvas(
    spec: ExerciseVisualGallerySpec,
    progressT: Float,
    accentColor: Color,
    isAltAngle: Boolean = false,
    isDetailZoom: Boolean = false,
    isReturning: Boolean = false,
    compact: Boolean = false,
    modifier: Modifier = Modifier
) {
    val bgTop = MaterialTheme.colorScheme.surfaceContainerLowest
    val bgBottom = MaterialTheme.colorScheme.surfaceContainer
    val bodyPrimary = MaterialTheme.colorScheme.onSurface
    val bodyFar = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.55f)
    val gridColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.55f)
    val propColor = MaterialTheme.colorScheme.secondary
    val highlightColor = accentColor
    val arrowColor = MaterialTheme.colorScheme.tertiary

    Box(
        modifier = modifier.background(
            Brush.verticalGradient(listOf(bgTop, bgBottom))
        )
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val floorY = h * 0.86f
            val scaleFactor = if (compact) 0.52f else 1f

            // Studio grid & floor
            if (!compact) {
                val dash = PathEffect.dashPathEffect(floatArrayOf(8f, 8f), 0f)
                drawLine(gridColor.copy(alpha = 0.35f), Offset(w * 0.5f, h * 0.08f), Offset(w * 0.5f, floorY), strokeWidth = 1.5f, pathEffect = dash)
                drawLine(gridColor.copy(alpha = 0.25f), Offset(w * 0.1f, h * 0.48f), Offset(w * 0.9f, h * 0.48f), strokeWidth = 1.2f, pathEffect = dash)
            }
            drawLine(gridColor, Offset(w * 0.08f, floorY), Offset(w * 0.92f, floorY), strokeWidth = 3f * scaleFactor, cap = StrokeCap.Round)

            val (startPose, endPose, altPose) = posePairFor(spec.poseFamily)
            val basePose = if (isAltAngle) altPose else lerpPose(startPose, endPose, progressT.coerceIn(0f, 1f))
            val pose = if (isDetailZoom) zoomAroundPoint(basePose, basePose.focusPoint, 1.38f) else basePose

            // Draw equipment props behind/with athlete
            drawProp(spec.prop, spec.poseFamily, pose, w, h, floorY, propColor, gridColor, scaleFactor)

            // Muscle highlight glow
            if (!compact) {
                drawMuscleHighlights(spec.highlights, pose, w, h, highlightColor)
            }

            // Far limbs (depth layering)
            val limbStroke = (if (compact) 6.5f else 13f)
            val torsoStroke = (if (compact) 8f else 16f)
            drawSegment(pose.hipFar, pose.kneeFar, w, h, bodyFar, limbStroke * 0.92f)
            drawSegment(pose.kneeFar, pose.ankleFar, w, h, bodyFar, limbStroke * 0.82f)
            drawSegment(pose.shoulderFar, pose.elbowFar, w, h, bodyFar, limbStroke * 0.85f)
            drawSegment(pose.elbowFar, pose.handFar, w, h, bodyFar, limbStroke * 0.78f)

            // Torso & Pelvis
            val midShoulder = lerpP2(pose.shoulderNear, pose.shoulderFar, 0.35f)
            val midHip = lerpP2(pose.hipNear, pose.hipFar, 0.35f)
            drawSegment(midHip, midShoulder, w, h, bodyPrimary.copy(alpha = 0.88f), torsoStroke)

            // Near limbs (bright foreground)
            drawSegment(pose.hipNear, pose.kneeNear, w, h, bodyPrimary, limbStroke)
            drawSegment(pose.kneeNear, pose.ankleNear, w, h, bodyPrimary, limbStroke * 0.88f)
            // Foot
            drawLine(
                color = bodyPrimary,
                start = pose.ankleNear.toOffset(w, h),
                end = Offset(pose.ankleNear.x * w + (18f * scaleFactor), pose.ankleNear.y * h),
                strokeWidth = limbStroke * 0.72f,
                cap = StrokeCap.Round
            )

            // Head
            val headCenter = pose.head.toOffset(w, h)
            val headRadius = if (compact) 7.5f else 15.5f
            drawCircle(color = bodyPrimary, radius = headRadius, center = headCenter)
            drawCircle(color = highlightColor, radius = headRadius, center = headCenter, style = Stroke(width = if (compact) 1.5f else 2.5f))

            // Near arm
            drawSegment(pose.shoulderNear, pose.elbowNear, w, h, bodyPrimary, limbStroke * 0.9f)
            drawSegment(pose.elbowNear, pose.handNear, w, h, bodyPrimary, limbStroke * 0.82f)

            // Joint caps
            val jointR = if (compact) 2.2f else 4.4f
            listOf(pose.shoulderNear, pose.elbowNear, pose.hipNear, pose.kneeNear, pose.ankleNear).forEach { pt ->
                val c = pt.toOffset(w, h)
                drawCircle(color = highlightColor, radius = jointR + 1.5f, center = c)
                drawCircle(color = bgBottom, radius = jointR * 0.55f, center = c)
            }

            // Motion arrow & Technique spotlight
            if (!compact) {
                val from = if (isReturning) pose.arrowTo else pose.arrowFrom
                val to = if (isReturning) pose.arrowFrom else pose.arrowTo
                if (from != null && to != null && progressT in 0.05f..0.95f && !isAltAngle) {
                    drawMotionArrow(from.toOffset(w, h), to.toOffset(w, h), arrowColor)
                }
                if (isDetailZoom) {
                    val focusOffset = pose.focusPoint.toOffset(w, h)
                    drawCircle(
                        color = arrowColor,
                        radius = 44f,
                        center = focusOffset,
                        style = Stroke(width = 3f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 6f), 0f))
                    )
                }
            }
        }
    }
}

private fun zoomAroundPoint(pose: ArticulatedPose, center: P2, scale: Float): ArticulatedPose {
    fun z(p: P2) = P2(
        (0.5f + (p.x - center.x) * scale).coerceIn(0.06f, 0.94f),
        (0.5f + (p.y - center.y) * scale).coerceIn(0.08f, 0.92f)
    )
    return pose.copy(
        head = z(pose.head),
        shoulderNear = z(pose.shoulderNear),
        elbowNear = z(pose.elbowNear),
        handNear = z(pose.handNear),
        shoulderFar = z(pose.shoulderFar),
        elbowFar = z(pose.elbowFar),
        handFar = z(pose.handFar),
        hipNear = z(pose.hipNear),
        kneeNear = z(pose.kneeNear),
        ankleNear = z(pose.ankleNear),
        hipFar = z(pose.hipFar),
        kneeFar = z(pose.kneeFar),
        ankleFar = z(pose.ankleFar),
        focusPoint = z(pose.focusPoint),
        propAnchorA = pose.propAnchorA?.let(::z),
        propAnchorB = pose.propAnchorB?.let(::z)
    )
}

private fun DrawScope.drawSegment(a: P2, b: P2, w: Float, h: Float, color: Color, stroke: Float) {
    drawLine(color = color, start = a.toOffset(w, h), end = b.toOffset(w, h), strokeWidth = stroke, cap = StrokeCap.Round)
}

private fun DrawScope.drawMuscleHighlights(
    highlights: List<MuscleHighlight>,
    pose: ArticulatedPose,
    w: Float,
    h: Float,
    color: Color
) {
    val glow = color.copy(alpha = 0.28f)
    highlights.forEach { region ->
        when (region) {
            MuscleHighlight.QUADS -> drawCircle(glow, 28f, lerpP2(pose.hipNear, pose.kneeNear, 0.55f).toOffset(w, h))
            MuscleHighlight.GLUTES -> drawCircle(glow, 26f, pose.hipNear.toOffset(w, h))
            MuscleHighlight.HAMSTRINGS -> drawCircle(glow, 26f, lerpP2(pose.hipNear, pose.kneeNear, 0.45f).toOffset(w, h))
            MuscleHighlight.CHEST, MuscleHighlight.SHOULDERS -> drawCircle(glow, 26f, pose.shoulderNear.toOffset(w, h))
            MuscleHighlight.LATS_BACK -> drawCircle(glow, 28f, lerpP2(pose.shoulderNear, pose.hipNear, 0.35f).toOffset(w, h))
            MuscleHighlight.ARMS -> drawCircle(glow, 22f, lerpP2(pose.shoulderNear, pose.elbowNear, 0.55f).toOffset(w, h))
            MuscleHighlight.CORE -> drawCircle(glow, 26f, lerpP2(pose.shoulderNear, pose.hipNear, 0.65f).toOffset(w, h))
            MuscleHighlight.CALVES_ANKLES -> drawCircle(glow, 22f, lerpP2(pose.kneeNear, pose.ankleNear, 0.55f).toOffset(w, h))
            MuscleHighlight.CARDIO_LUNGS -> drawCircle(glow, 32f, lerpP2(pose.shoulderNear, pose.hipNear, 0.4f).toOffset(w, h))
            MuscleHighlight.FULL_BODY -> drawCircle(glow.copy(alpha = 0.16f), 48f, lerpP2(pose.shoulderNear, pose.hipNear, 0.5f).toOffset(w, h))
        }
    }
}

private fun DrawScope.drawMotionArrow(from: Offset, to: Offset, color: Color) {
    val dash = PathEffect.dashPathEffect(floatArrayOf(10f, 6f), 0f)
    drawLine(color = color, start = from, end = to, strokeWidth = 4f, pathEffect = dash, cap = StrokeCap.Round)
    val angle = atan2((to.y - from.y).toDouble(), (to.x - from.x).toDouble())
    val headLen = 14.0
    val left = Offset(
        (to.x - headLen * cos(angle - PI / 6)).toFloat(),
        (to.y - headLen * sin(angle - PI / 6)).toFloat()
    )
    val right = Offset(
        (to.x - headLen * cos(angle + PI / 6)).toFloat(),
        (to.y - headLen * sin(angle + PI / 6)).toFloat()
    )
    val p = Path().apply {
        moveTo(to.x, to.y)
        lineTo(left.x, left.y)
        moveTo(to.x, to.y)
        lineTo(right.x, right.y)
    }
    drawPath(p, color = color, style = Stroke(width = 4f, cap = StrokeCap.Round))
}

private fun DrawScope.drawProp(
    prop: EquipmentProp,
    family: PoseFamily,
    pose: ArticulatedPose,
    w: Float,
    h: Float,
    floorY: Float,
    propColor: Color,
    gridColor: Color,
    s: Float
) {
    when (prop) {
        EquipmentProp.BARBELL -> {
            val anchor = pose.propAnchorA ?: pose.handNear
            val c = anchor.toOffset(w, h)
            if (family == PoseFamily.SQUAT) {
                drawCircle(propColor, 14f * s, c)
                drawCircle(gridColor, 6f * s, c)
            } else {
                drawLine(propColor, Offset(c.x - 46f * s, c.y), Offset(c.x + 46f * s, c.y), strokeWidth = 5f * s, cap = StrokeCap.Round)
                drawCircle(propColor, 13f * s, Offset(c.x - 40f * s, c.y))
                drawCircle(propColor, 13f * s, Offset(c.x + 40f * s, c.y))
            }
        }
        EquipmentProp.DUMBBELL, EquipmentProp.KETTLEBELL -> {
            val c = pose.handNear.toOffset(w, h)
            drawRoundRect(
                color = propColor,
                topLeft = Offset(c.x - 10f * s, c.y - 6f * s),
                size = Size(20f * s, 14f * s),
                cornerRadius = CornerRadius(4f * s)
            )
        }
        EquipmentProp.BENCH -> {
            val y = h * 0.66f
            drawRoundRect(propColor.copy(alpha = 0.7f), Offset(w * 0.24f, y), Size(w * 0.52f, 12f * s), CornerRadius(4f * s))
            drawLine(propColor.copy(alpha = 0.6f), Offset(w * 0.30f, y + 10f * s), Offset(w * 0.30f, floorY), strokeWidth = 6f * s)
            drawLine(propColor.copy(alpha = 0.6f), Offset(w * 0.70f, y + 10f * s), Offset(w * 0.70f, floorY), strokeWidth = 6f * s)
        }
        EquipmentProp.PULLUP_BAR, EquipmentProp.RINGS -> {
            val barY = h * 0.14f
            drawLine(propColor, Offset(w * 0.2f, barY), Offset(w * 0.8f, barY), strokeWidth = 6f * s, cap = StrokeCap.Round)
            if (prop == EquipmentProp.RINGS) {
                val hx = pose.handNear.x * w
                val hy = pose.handNear.y * h
                drawLine(propColor.copy(alpha = 0.7f), Offset(hx, barY), Offset(hx, hy), strokeWidth = 3f * s)
                drawCircle(propColor, 10f * s, Offset(hx, hy), style = Stroke(3.5f * s))
            }
        }
        EquipmentProp.WALL -> {
            val wx = w * 0.74f
            drawLine(propColor.copy(alpha = 0.5f), Offset(wx, h * 0.1f), Offset(wx, floorY), strokeWidth = 8f * s)
        }
        EquipmentProp.AB_WHEEL -> {
            val c = pose.handNear.toOffset(w, h)
            drawCircle(propColor, 12f * s, c, style = Stroke(4f * s))
        }
        EquipmentProp.CYCLE_ROWER -> {
            drawCircle(propColor.copy(alpha = 0.65f), 20f * s, Offset(w * 0.66f, floorY - 22f * s), style = Stroke(5f * s))
            drawLine(propColor.copy(alpha = 0.6f), Offset(w * 0.3f, floorY - 12f * s), Offset(w * 0.66f, floorY - 22f * s), strokeWidth = 5f * s)
        }
        EquipmentProp.CONES_BOX -> {
            drawRoundRect(propColor.copy(alpha = 0.55f), Offset(w * 0.6f, floorY - 34f * s), Size(w * 0.18f, 34f * s), CornerRadius(4f * s))
        }
        EquipmentProp.BLOCKS_MAT -> {
            drawLine(propColor.copy(alpha = 0.6f), Offset(w * 0.16f, floorY - 2f), Offset(w * 0.84f, floorY - 2f), strokeWidth = 6f * s, cap = StrokeCap.Round)
        }
        EquipmentProp.NONE -> Unit
    }
}

private fun posePairFor(family: PoseFamily): Triple<ArticulatedPose, ArticulatedPose, ArticulatedPose> {
    val standing = ArticulatedPose(
        head = P2(0.50f, 0.21f),
        shoulderNear = P2(0.49f, 0.32f), elbowNear = P2(0.45f, 0.45f), handNear = P2(0.48f, 0.54f),
        shoulderFar = P2(0.51f, 0.32f), elbowFar = P2(0.54f, 0.45f), handFar = P2(0.52f, 0.54f),
        hipNear = P2(0.49f, 0.54f), kneeNear = P2(0.50f, 0.70f), ankleNear = P2(0.49f, 0.85f),
        hipFar = P2(0.51f, 0.54f), kneeFar = P2(0.52f, 0.70f), ankleFar = P2(0.51f, 0.85f),
        focusPoint = P2(0.50f, 0.66f),
        arrowFrom = P2(0.32f, 0.40f), arrowTo = P2(0.32f, 0.62f)
    )
    return when (family) {
        PoseFamily.SQUAT -> Triple(
            standing.copy(handNear = P2(0.47f, 0.30f), elbowNear = P2(0.42f, 0.38f), propAnchorA = P2(0.48f, 0.30f)),
            ArticulatedPose(
                head = P2(0.48f, 0.37f),
                shoulderNear = P2(0.45f, 0.47f), elbowNear = P2(0.41f, 0.55f), handNear = P2(0.45f, 0.46f),
                shoulderFar = P2(0.48f, 0.47f), elbowFar = P2(0.48f, 0.55f), handFar = P2(0.48f, 0.46f),
                hipNear = P2(0.37f, 0.68f), kneeNear = P2(0.58f, 0.65f), ankleNear = P2(0.49f, 0.85f),
                hipFar = P2(0.39f, 0.68f), kneeFar = P2(0.60f, 0.65f), ankleFar = P2(0.51f, 0.85f),
                focusPoint = P2(0.56f, 0.68f),
                arrowFrom = P2(0.32f, 0.42f), arrowTo = P2(0.32f, 0.66f),
                propAnchorA = P2(0.44f, 0.45f)
            ),
            ArticulatedPose(
                head = P2(0.50f, 0.37f),
                shoulderNear = P2(0.41f, 0.47f), elbowNear = P2(0.36f, 0.54f), handNear = P2(0.38f, 0.45f),
                shoulderFar = P2(0.59f, 0.47f), elbowFar = P2(0.64f, 0.54f), handFar = P2(0.62f, 0.45f),
                hipNear = P2(0.44f, 0.67f), kneeNear = P2(0.35f, 0.68f), ankleNear = P2(0.40f, 0.85f),
                hipFar = P2(0.56f, 0.67f), kneeFar = P2(0.65f, 0.68f), ankleFar = P2(0.60f, 0.85f),
                focusPoint = P2(0.36f, 0.72f)
            )
        )
        PoseFamily.SPLIT_SQUAT_LUNGE -> Triple(
            standing.copy(ankleNear = P2(0.58f, 0.85f), ankleFar = P2(0.38f, 0.85f), kneeNear = P2(0.56f, 0.70f), kneeFar = P2(0.40f, 0.71f)),
            ArticulatedPose(
                head = P2(0.49f, 0.35f),
                shoulderNear = P2(0.48f, 0.45f), elbowNear = P2(0.47f, 0.58f), handNear = P2(0.48f, 0.68f),
                shoulderFar = P2(0.51f, 0.45f), elbowFar = P2(0.52f, 0.58f), handFar = P2(0.52f, 0.68f),
                hipNear = P2(0.47f, 0.65f), kneeNear = P2(0.61f, 0.66f), ankleNear = P2(0.59f, 0.85f),
                hipFar = P2(0.47f, 0.65f), kneeFar = P2(0.42f, 0.81f), ankleFar = P2(0.31f, 0.84f),
                focusPoint = P2(0.60f, 0.72f),
                arrowFrom = P2(0.30f, 0.46f), arrowTo = P2(0.30f, 0.66f)
            ),
            standing.copy(hipNear = P2(0.45f, 0.64f), kneeNear = P2(0.36f, 0.66f), ankleNear = P2(0.38f, 0.85f), hipFar = P2(0.55f, 0.64f), kneeFar = P2(0.62f, 0.72f), ankleFar = P2(0.62f, 0.85f), focusPoint = P2(0.37f, 0.70f))
        )
        PoseFamily.HIP_HINGE_DEADLIFT -> Triple(
            standing.copy(arrowFrom = P2(0.66f, 0.74f), arrowTo = P2(0.66f, 0.48f), propAnchorA = P2(0.52f, 0.56f)),
            ArticulatedPose(
                head = P2(0.68f, 0.38f),
                shoulderNear = P2(0.58f, 0.42f), elbowNear = P2(0.56f, 0.58f), handNear = P2(0.55f, 0.76f),
                shoulderFar = P2(0.60f, 0.42f), elbowFar = P2(0.58f, 0.58f), handFar = P2(0.57f, 0.76f),
                hipNear = P2(0.36f, 0.51f), kneeNear = P2(0.49f, 0.68f), ankleNear = P2(0.48f, 0.85f),
                hipFar = P2(0.38f, 0.51f), kneeFar = P2(0.51f, 0.68f), ankleFar = P2(0.50f, 0.85f),
                focusPoint = P2(0.42f, 0.50f),
                arrowFrom = P2(0.66f, 0.76f), arrowTo = P2(0.66f, 0.52f),
                propAnchorA = P2(0.55f, 0.77f)
            ),
            standing.copy(head = P2(0.64f, 0.35f), shoulderNear = P2(0.56f, 0.40f), hipNear = P2(0.38f, 0.49f), handNear = P2(0.54f, 0.68f), propAnchorA = P2(0.54f, 0.68f), focusPoint = P2(0.45f, 0.47f))
        )
        PoseFamily.HORIZONTAL_PUSH_BENCH -> Triple(
            ArticulatedPose(
                head = P2(0.30f, 0.58f),
                shoulderNear = P2(0.38f, 0.61f), elbowNear = P2(0.39f, 0.45f), handNear = P2(0.39f, 0.31f),
                shoulderFar = P2(0.40f, 0.61f), elbowFar = P2(0.41f, 0.45f), handFar = P2(0.41f, 0.31f),
                hipNear = P2(0.58f, 0.62f), kneeNear = P2(0.71f, 0.64f), ankleNear = P2(0.71f, 0.85f),
                hipFar = P2(0.60f, 0.62f), kneeFar = P2(0.73f, 0.64f), ankleFar = P2(0.73f, 0.85f),
                focusPoint = P2(0.39f, 0.50f), arrowFrom = P2(0.28f, 0.30f), arrowTo = P2(0.28f, 0.54f), propAnchorA = P2(0.39f, 0.31f)
            ),
            ArticulatedPose(
                head = P2(0.30f, 0.58f),
                shoulderNear = P2(0.38f, 0.61f), elbowNear = P2(0.41f, 0.73f), handNear = P2(0.42f, 0.55f),
                shoulderFar = P2(0.40f, 0.61f), elbowFar = P2(0.43f, 0.73f), handFar = P2(0.44f, 0.55f),
                hipNear = P2(0.58f, 0.62f), kneeNear = P2(0.71f, 0.64f), ankleNear = P2(0.71f, 0.85f),
                hipFar = P2(0.60f, 0.62f), kneeFar = P2(0.73f, 0.64f), ankleFar = P2(0.73f, 0.85f),
                focusPoint = P2(0.42f, 0.62f), arrowFrom = P2(0.28f, 0.54f), arrowTo = P2(0.28f, 0.30f), propAnchorA = P2(0.42f, 0.54f)
            ),
            ArticulatedPose(
                head = P2(0.50f, 0.38f),
                shoulderNear = P2(0.41f, 0.50f), elbowNear = P2(0.31f, 0.58f), handNear = P2(0.34f, 0.44f),
                shoulderFar = P2(0.59f, 0.50f), elbowFar = P2(0.69f, 0.58f), handFar = P2(0.66f, 0.44f),
                hipNear = P2(0.45f, 0.68f), kneeNear = P2(0.42f, 0.78f), ankleNear = P2(0.42f, 0.85f),
                hipFar = P2(0.55f, 0.68f), kneeFar = P2(0.58f, 0.78f), ankleFar = P2(0.58f, 0.85f),
                focusPoint = P2(0.32f, 0.56f)
            )
        )
        PoseFamily.PUSHUP_FLOOR, PoseFamily.PLANK_ANTI_EXT -> Triple(
            ArticulatedPose(
                head = P2(0.71f, 0.49f),
                shoulderNear = P2(0.65f, 0.54f), elbowNear = P2(0.66f, 0.69f), handNear = P2(0.67f, 0.84f),
                shoulderFar = P2(0.67f, 0.54f), elbowFar = P2(0.68f, 0.69f), handFar = P2(0.69f, 0.84f),
                hipNear = P2(0.45f, 0.64f), kneeNear = P2(0.31f, 0.74f), ankleNear = P2(0.18f, 0.84f),
                hipFar = P2(0.46f, 0.64f), kneeFar = P2(0.32f, 0.74f), ankleFar = P2(0.19f, 0.84f),
                focusPoint = P2(0.50f, 0.62f), arrowFrom = P2(0.78f, 0.50f), arrowTo = P2(0.78f, 0.72f)
            ),
            ArticulatedPose(
                head = P2(0.73f, 0.66f),
                shoulderNear = P2(0.66f, 0.71f), elbowNear = P2(0.59f, 0.72f), handNear = P2(0.67f, 0.84f),
                shoulderFar = P2(0.68f, 0.71f), elbowFar = P2(0.61f, 0.72f), handFar = P2(0.69f, 0.84f),
                hipNear = P2(0.45f, 0.76f), kneeNear = P2(0.31f, 0.80f), ankleNear = P2(0.18f, 0.84f),
                hipFar = P2(0.46f, 0.76f), kneeFar = P2(0.32f, 0.80f), ankleFar = P2(0.19f, 0.84f),
                focusPoint = P2(0.62f, 0.74f), arrowFrom = P2(0.78f, 0.72f), arrowTo = P2(0.78f, 0.50f)
            ),
            ArticulatedPose(
                head = P2(0.71f, 0.54f),
                shoulderNear = P2(0.64f, 0.58f), elbowNear = P2(0.65f, 0.72f), handNear = P2(0.67f, 0.84f),
                shoulderFar = P2(0.66f, 0.58f), elbowFar = P2(0.67f, 0.72f), handFar = P2(0.69f, 0.84f),
                hipNear = P2(0.44f, 0.66f), kneeNear = P2(0.30f, 0.75f), ankleNear = P2(0.18f, 0.84f),
                hipFar = P2(0.45f, 0.66f), kneeFar = P2(0.31f, 0.75f), ankleFar = P2(0.19f, 0.84f),
                focusPoint = P2(0.44f, 0.66f)
            )
        )
        PoseFamily.DIP_SUPPORT -> Triple(
            ArticulatedPose(
                head = P2(0.50f, 0.22f),
                shoulderNear = P2(0.48f, 0.32f), elbowNear = P2(0.49f, 0.44f), handNear = P2(0.50f, 0.56f),
                shoulderFar = P2(0.52f, 0.32f), elbowFar = P2(0.52f, 0.44f), handFar = P2(0.52f, 0.56f),
                hipNear = P2(0.48f, 0.55f), kneeNear = P2(0.47f, 0.71f), ankleNear = P2(0.44f, 0.82f),
                hipFar = P2(0.50f, 0.55f), kneeFar = P2(0.49f, 0.71f), ankleFar = P2(0.46f, 0.82f),
                focusPoint = P2(0.49f, 0.38f), arrowFrom = P2(0.66f, 0.30f), arrowTo = P2(0.66f, 0.52f)
            ),
            ArticulatedPose(
                head = P2(0.54f, 0.36f),
                shoulderNear = P2(0.52f, 0.46f), elbowNear = P2(0.42f, 0.48f), handNear = P2(0.49f, 0.56f),
                shoulderFar = P2(0.55f, 0.46f), elbowFar = P2(0.45f, 0.48f), handFar = P2(0.51f, 0.56f),
                hipNear = P2(0.48f, 0.66f), kneeNear = P2(0.47f, 0.77f), ankleNear = P2(0.43f, 0.84f),
                hipFar = P2(0.50f, 0.66f), kneeFar = P2(0.49f, 0.77f), ankleFar = P2(0.45f, 0.84f),
                focusPoint = P2(0.46f, 0.48f), arrowFrom = P2(0.66f, 0.52f), arrowTo = P2(0.66f, 0.30f)
            ),
            standing.copy(shoulderNear = P2(0.45f, 0.42f), shoulderFar = P2(0.55f, 0.42f), elbowNear = P2(0.39f, 0.48f), elbowFar = P2(0.61f, 0.48f), handNear = P2(0.42f, 0.56f), handFar = P2(0.58f, 0.56f), focusPoint = P2(0.42f, 0.48f))
        )
        PoseFamily.VERTICAL_PUSH_OHP -> Triple(
            standing.copy(elbowNear = P2(0.54f, 0.41f), handNear = P2(0.50f, 0.31f), elbowFar = P2(0.55f, 0.41f), handFar = P2(0.51f, 0.31f), propAnchorA = P2(0.50f, 0.30f), arrowFrom = P2(0.34f, 0.34f), arrowTo = P2(0.34f, 0.14f)),
            standing.copy(elbowNear = P2(0.50f, 0.22f), handNear = P2(0.50f, 0.12f), elbowFar = P2(0.52f, 0.22f), handFar = P2(0.52f, 0.12f), propAnchorA = P2(0.50f, 0.12f), focusPoint = P2(0.50f, 0.24f), arrowFrom = P2(0.34f, 0.14f), arrowTo = P2(0.34f, 0.34f)),
            standing.copy(elbowNear = P2(0.40f, 0.22f), handNear = P2(0.40f, 0.12f), elbowFar = P2(0.60f, 0.22f), handFar = P2(0.60f, 0.12f), focusPoint = P2(0.50f, 0.32f))
        )
        PoseFamily.HANDSTAND_INVERSION -> Triple(
            ArticulatedPose(
                head = P2(0.44f, 0.66f),
                shoulderNear = P2(0.47f, 0.64f), elbowNear = P2(0.49f, 0.74f), handNear = P2(0.50f, 0.84f),
                shoulderFar = P2(0.49f, 0.64f), elbowFar = P2(0.51f, 0.74f), handFar = P2(0.52f, 0.84f),
                hipNear = P2(0.46f, 0.44f), kneeNear = P2(0.62f, 0.58f), ankleNear = P2(0.68f, 0.78f),
                hipFar = P2(0.48f, 0.44f), kneeFar = P2(0.58f, 0.46f), ankleFar = P2(0.64f, 0.54f),
                focusPoint = P2(0.50f, 0.80f), arrowFrom = P2(0.64f, 0.56f), arrowTo = P2(0.52f, 0.22f)
            ),
            ArticulatedPose(
                head = P2(0.50f, 0.73f),
                shoulderNear = P2(0.49f, 0.64f), elbowNear = P2(0.49f, 0.74f), handNear = P2(0.50f, 0.84f),
                shoulderFar = P2(0.51f, 0.64f), elbowFar = P2(0.51f, 0.74f), handFar = P2(0.52f, 0.84f),
                hipNear = P2(0.49f, 0.43f), kneeNear = P2(0.49f, 0.28f), ankleNear = P2(0.49f, 0.14f),
                hipFar = P2(0.51f, 0.43f), kneeFar = P2(0.51f, 0.28f), ankleFar = P2(0.51f, 0.14f),
                focusPoint = P2(0.50f, 0.72f), arrowFrom = P2(0.36f, 0.20f), arrowTo = P2(0.36f, 0.42f)
            ),
            ArticulatedPose(
                head = P2(0.50f, 0.73f),
                shoulderNear = P2(0.45f, 0.64f), elbowNear = P2(0.44f, 0.74f), handNear = P2(0.44f, 0.84f),
                shoulderFar = P2(0.55f, 0.64f), elbowFar = P2(0.56f, 0.74f), handFar = P2(0.56f, 0.84f),
                hipNear = P2(0.46f, 0.43f), kneeNear = P2(0.47f, 0.28f), ankleNear = P2(0.47f, 0.14f),
                hipFar = P2(0.54f, 0.43f), kneeFar = P2(0.53f, 0.28f), ankleFar = P2(0.53f, 0.14f),
                focusPoint = P2(0.44f, 0.80f)
            )
        )
        PoseFamily.PULL_UP_HANG -> Triple(
            ArticulatedPose(
                head = P2(0.50f, 0.34f),
                shoulderNear = P2(0.49f, 0.40f), elbowNear = P2(0.49f, 0.27f), handNear = P2(0.49f, 0.14f),
                shoulderFar = P2(0.51f, 0.40f), elbowFar = P2(0.51f, 0.27f), handFar = P2(0.51f, 0.14f),
                hipNear = P2(0.49f, 0.58f), kneeNear = P2(0.50f, 0.72f), ankleNear = P2(0.49f, 0.84f),
                hipFar = P2(0.51f, 0.58f), kneeFar = P2(0.52f, 0.72f), ankleFar = P2(0.51f, 0.84f),
                focusPoint = P2(0.50f, 0.36f), arrowFrom = P2(0.68f, 0.54f), arrowTo = P2(0.68f, 0.30f)
            ),
            ArticulatedPose(
                head = P2(0.50f, 0.13f),
                shoulderNear = P2(0.48f, 0.23f), elbowNear = P2(0.43f, 0.30f), handNear = P2(0.49f, 0.15f),
                shoulderFar = P2(0.52f, 0.23f), elbowFar = P2(0.57f, 0.30f), handFar = P2(0.51f, 0.15f),
                hipNear = P2(0.48f, 0.43f), kneeNear = P2(0.50f, 0.58f), ankleNear = P2(0.48f, 0.72f),
                hipFar = P2(0.51f, 0.43f), kneeFar = P2(0.52f, 0.58f), ankleFar = P2(0.50f, 0.72f),
                focusPoint = P2(0.50f, 0.24f), arrowFrom = P2(0.68f, 0.30f), arrowTo = P2(0.68f, 0.54f)
            ),
            ArticulatedPose(
                head = P2(0.50f, 0.16f),
                shoulderNear = P2(0.43f, 0.26f), elbowNear = P2(0.36f, 0.30f), handNear = P2(0.38f, 0.14f),
                shoulderFar = P2(0.57f, 0.26f), elbowFar = P2(0.64f, 0.30f), handFar = P2(0.62f, 0.14f),
                hipNear = P2(0.46f, 0.46f), kneeNear = P2(0.46f, 0.62f), ankleNear = P2(0.46f, 0.74f),
                hipFar = P2(0.54f, 0.46f), kneeFar = P2(0.54f, 0.62f), ankleFar = P2(0.54f, 0.74f),
                focusPoint = P2(0.50f, 0.28f)
            )
        )
        PoseFamily.ROW_PULL -> Triple(
            ArticulatedPose(
                head = P2(0.68f, 0.36f),
                shoulderNear = P2(0.59f, 0.40f), elbowNear = P2(0.58f, 0.55f), handNear = P2(0.57f, 0.69f),
                shoulderFar = P2(0.61f, 0.40f), elbowFar = P2(0.60f, 0.55f), handFar = P2(0.59f, 0.69f),
                hipNear = P2(0.37f, 0.48f), kneeNear = P2(0.47f, 0.67f), ankleNear = P2(0.46f, 0.85f),
                hipFar = P2(0.39f, 0.48f), kneeFar = P2(0.49f, 0.67f), ankleFar = P2(0.48f, 0.85f),
                focusPoint = P2(0.52f, 0.44f), arrowFrom = P2(0.58f, 0.68f), arrowTo = P2(0.48f, 0.46f), propAnchorA = P2(0.57f, 0.70f)
            ),
            ArticulatedPose(
                head = P2(0.68f, 0.36f),
                shoulderNear = P2(0.59f, 0.40f), elbowNear = P2(0.46f, 0.36f), handNear = P2(0.49f, 0.49f),
                shoulderFar = P2(0.61f, 0.40f), elbowFar = P2(0.48f, 0.36f), handFar = P2(0.51f, 0.49f),
                hipNear = P2(0.37f, 0.48f), kneeNear = P2(0.47f, 0.67f), ankleNear = P2(0.46f, 0.85f),
                hipFar = P2(0.39f, 0.48f), kneeFar = P2(0.49f, 0.67f), ankleFar = P2(0.48f, 0.85f),
                focusPoint = P2(0.52f, 0.40f), arrowFrom = P2(0.48f, 0.46f), arrowTo = P2(0.58f, 0.68f), propAnchorA = P2(0.49f, 0.49f)
            ),
            standing.copy(head = P2(0.65f, 0.38f), shoulderNear = P2(0.57f, 0.41f), elbowNear = P2(0.47f, 0.38f), handNear = P2(0.50f, 0.52f), hipNear = P2(0.38f, 0.49f), focusPoint = P2(0.50f, 0.42f))
        )
        PoseFamily.HOLLOW_DRAGON_LEVER -> Triple(
            ArticulatedPose(
                head = P2(0.24f, 0.78f),
                shoulderNear = P2(0.33f, 0.79f), elbowNear = P2(0.27f, 0.74f), handNear = P2(0.22f, 0.79f),
                shoulderFar = P2(0.34f, 0.79f), elbowFar = P2(0.28f, 0.74f), handFar = P2(0.23f, 0.79f),
                hipNear = P2(0.52f, 0.79f), kneeNear = P2(0.64f, 0.64f), ankleNear = P2(0.60f, 0.49f),
                hipFar = P2(0.53f, 0.79f), kneeFar = P2(0.65f, 0.64f), ankleFar = P2(0.61f, 0.49f),
                focusPoint = P2(0.48f, 0.76f), arrowFrom = P2(0.66f, 0.50f), arrowTo = P2(0.76f, 0.74f)
            ),
            ArticulatedPose(
                head = P2(0.24f, 0.78f),
                shoulderNear = P2(0.33f, 0.79f), elbowNear = P2(0.27f, 0.74f), handNear = P2(0.22f, 0.79f),
                shoulderFar = P2(0.34f, 0.79f), elbowFar = P2(0.28f, 0.74f), handFar = P2(0.23f, 0.79f),
                hipNear = P2(0.52f, 0.74f), kneeNear = P2(0.67f, 0.71f), ankleNear = P2(0.81f, 0.68f),
                hipFar = P2(0.53f, 0.74f), kneeFar = P2(0.68f, 0.71f), ankleFar = P2(0.82f, 0.68f),
                focusPoint = P2(0.52f, 0.74f), arrowFrom = P2(0.76f, 0.74f), arrowTo = P2(0.66f, 0.50f)
            ),
            ArticulatedPose(
                head = P2(0.25f, 0.77f),
                shoulderNear = P2(0.34f, 0.78f), elbowNear = P2(0.28f, 0.73f), handNear = P2(0.23f, 0.78f),
                shoulderFar = P2(0.35f, 0.78f), elbowFar = P2(0.29f, 0.73f), handFar = P2(0.24f, 0.78f),
                hipNear = P2(0.52f, 0.76f), kneeNear = P2(0.66f, 0.72f), ankleNear = P2(0.78f, 0.67f),
                hipFar = P2(0.53f, 0.76f), kneeFar = P2(0.67f, 0.72f), ankleFar = P2(0.79f, 0.67f),
                focusPoint = P2(0.45f, 0.76f)
            )
        )
        PoseFamily.LEG_RAISE_LSIT -> Triple(
            ArticulatedPose(
                head = P2(0.45f, 0.54f),
                shoulderNear = P2(0.45f, 0.64f), elbowNear = P2(0.46f, 0.74f), handNear = P2(0.46f, 0.84f),
                shoulderFar = P2(0.47f, 0.64f), elbowFar = P2(0.48f, 0.74f), handFar = P2(0.48f, 0.84f),
                hipNear = P2(0.45f, 0.82f), kneeNear = P2(0.58f, 0.82f), ankleNear = P2(0.71f, 0.82f),
                hipFar = P2(0.47f, 0.82f), kneeFar = P2(0.60f, 0.82f), ankleFar = P2(0.73f, 0.82f),
                focusPoint = P2(0.47f, 0.75f), arrowFrom = P2(0.72f, 0.80f), arrowTo = P2(0.72f, 0.64f)
            ),
            ArticulatedPose(
                head = P2(0.44f, 0.48f),
                shoulderNear = P2(0.45f, 0.58f), elbowNear = P2(0.46f, 0.71f), handNear = P2(0.46f, 0.84f),
                shoulderFar = P2(0.47f, 0.58f), elbowFar = P2(0.48f, 0.71f), handFar = P2(0.48f, 0.84f),
                hipNear = P2(0.46f, 0.76f), kneeNear = P2(0.60f, 0.75f), ankleNear = P2(0.74f, 0.74f),
                hipFar = P2(0.48f, 0.76f), kneeFar = P2(0.62f, 0.75f), ankleFar = P2(0.76f, 0.74f),
                focusPoint = P2(0.48f, 0.74f), arrowFrom = P2(0.72f, 0.64f), arrowTo = P2(0.72f, 0.80f)
            ),
            ArticulatedPose(
                head = P2(0.46f, 0.48f),
                shoulderNear = P2(0.45f, 0.58f), elbowNear = P2(0.46f, 0.71f), handNear = P2(0.46f, 0.84f),
                shoulderFar = P2(0.47f, 0.58f), elbowFar = P2(0.48f, 0.71f), handFar = P2(0.48f, 0.84f),
                hipNear = P2(0.47f, 0.76f), kneeNear = P2(0.60f, 0.66f), ankleNear = P2(0.73f, 0.56f),
                hipFar = P2(0.49f, 0.76f), kneeFar = P2(0.62f, 0.66f), ankleFar = P2(0.75f, 0.56f),
                focusPoint = P2(0.46f, 0.64f)
            )
        )
        PoseFamily.SPRINT_DRIVE, PoseFamily.CARDIO_RUN_WALK -> Triple(
            ArticulatedPose(
                head = P2(0.55f, 0.23f),
                shoulderNear = P2(0.51f, 0.34f), elbowNear = P2(0.41f, 0.42f), handNear = P2(0.46f, 0.35f),
                shoulderFar = P2(0.53f, 0.34f), elbowFar = P2(0.61f, 0.41f), handFar = P2(0.66f, 0.34f),
                hipNear = P2(0.46f, 0.53f), kneeNear = P2(0.60f, 0.62f), ankleNear = P2(0.54f, 0.84f),
                hipFar = P2(0.46f, 0.53f), kneeFar = P2(0.38f, 0.70f), ankleFar = P2(0.28f, 0.80f),
                focusPoint = P2(0.52f, 0.56f), arrowFrom = P2(0.55f, 0.46f), arrowTo = P2(0.75f, 0.46f)
            ),
            ArticulatedPose(
                head = P2(0.62f, 0.25f),
                shoulderNear = P2(0.56f, 0.35f), elbowNear = P2(0.66f, 0.39f), handNear = P2(0.71f, 0.31f),
                shoulderFar = P2(0.54f, 0.35f), elbowFar = P2(0.43f, 0.44f), handFar = P2(0.39f, 0.37f),
                hipNear = P2(0.47f, 0.54f), kneeNear = P2(0.65f, 0.54f), ankleNear = P2(0.62f, 0.72f),
                hipFar = P2(0.47f, 0.54f), kneeFar = P2(0.37f, 0.71f), ankleFar = P2(0.26f, 0.85f),
                focusPoint = P2(0.62f, 0.54f), arrowFrom = P2(0.55f, 0.46f), arrowTo = P2(0.75f, 0.46f)
            ),
            ArticulatedPose(
                head = P2(0.58f, 0.24f),
                shoulderNear = P2(0.53f, 0.34f), elbowNear = P2(0.63f, 0.39f), handNear = P2(0.68f, 0.32f),
                shoulderFar = P2(0.51f, 0.34f), elbowFar = P2(0.41f, 0.43f), handFar = P2(0.44f, 0.36f),
                hipNear = P2(0.46f, 0.54f), kneeNear = P2(0.61f, 0.56f), ankleNear = P2(0.57f, 0.76f),
                hipFar = P2(0.46f, 0.54f), kneeFar = P2(0.39f, 0.70f), ankleFar = P2(0.32f, 0.85f),
                focusPoint = P2(0.55f, 0.56f)
            )
        )
        PoseFamily.JUMP_PLYO -> Triple(
            standing.copy(head = P2(0.50f, 0.34f), shoulderNear = P2(0.48f, 0.44f), elbowNear = P2(0.36f, 0.50f), handNear = P2(0.28f, 0.48f), hipNear = P2(0.42f, 0.62f), kneeNear = P2(0.55f, 0.68f), ankleNear = P2(0.49f, 0.85f), arrowFrom = P2(0.50f, 0.60f), arrowTo = P2(0.50f, 0.24f)),
            ArticulatedPose(
                head = P2(0.52f, 0.14f),
                shoulderNear = P2(0.50f, 0.24f), elbowNear = P2(0.56f, 0.16f), handNear = P2(0.60f, 0.08f),
                shoulderFar = P2(0.52f, 0.24f), elbowFar = P2(0.58f, 0.16f), handFar = P2(0.62f, 0.08f),
                hipNear = P2(0.50f, 0.44f), kneeNear = P2(0.54f, 0.58f), ankleNear = P2(0.49f, 0.72f),
                hipFar = P2(0.51f, 0.44f), kneeFar = P2(0.55f, 0.58f), ankleFar = P2(0.50f, 0.72f),
                focusPoint = P2(0.52f, 0.54f), arrowFrom = P2(0.50f, 0.30f), arrowTo = P2(0.50f, 0.62f)
            ),
            standing.copy(head = P2(0.50f, 0.28f), shoulderNear = P2(0.48f, 0.38f), hipNear = P2(0.45f, 0.58f), kneeNear = P2(0.53f, 0.68f), focusPoint = P2(0.50f, 0.76f))
        )
        PoseFamily.LATERAL_SKATER_AGILITY -> Triple(
            standing.copy(head = P2(0.36f, 0.28f), shoulderNear = P2(0.35f, 0.38f), hipNear = P2(0.33f, 0.58f), kneeNear = P2(0.37f, 0.71f), ankleNear = P2(0.34f, 0.85f), kneeFar = P2(0.28f, 0.74f), ankleFar = P2(0.24f, 0.82f), arrowFrom = P2(0.38f, 0.50f), arrowTo = P2(0.64f, 0.50f)),
            standing.copy(head = P2(0.66f, 0.28f), shoulderNear = P2(0.65f, 0.38f), hipNear = P2(0.65f, 0.58f), kneeNear = P2(0.68f, 0.71f), ankleNear = P2(0.66f, 0.85f), kneeFar = P2(0.56f, 0.74f), ankleFar = P2(0.50f, 0.81f), focusPoint = P2(0.66f, 0.70f), arrowFrom = P2(0.62f, 0.50f), arrowTo = P2(0.36f, 0.50f)),
            standing.copy(head = P2(0.50f, 0.26f), shoulderNear = P2(0.49f, 0.36f), hipNear = P2(0.49f, 0.56f), kneeNear = P2(0.51f, 0.70f), ankleNear = P2(0.50f, 0.85f), kneeFar = P2(0.40f, 0.66f), ankleFar = P2(0.42f, 0.76f), focusPoint = P2(0.50f, 0.72f))
        )
        PoseFamily.MARTIAL_STRIKE_KICK -> Triple(
            standing.copy(elbowNear = P2(0.54f, 0.40f), handNear = P2(0.56f, 0.30f), elbowFar = P2(0.58f, 0.38f), handFar = P2(0.61f, 0.28f), ankleNear = P2(0.56f, 0.85f), ankleFar = P2(0.42f, 0.85f), arrowFrom = P2(0.52f, 0.52f), arrowTo = P2(0.76f, 0.34f)),
            ArticulatedPose(
                head = P2(0.38f, 0.25f),
                shoulderNear = P2(0.39f, 0.35f), elbowNear = P2(0.45f, 0.42f), handNear = P2(0.46f, 0.30f),
                shoulderFar = P2(0.41f, 0.35f), elbowFar = P2(0.36f, 0.43f), handFar = P2(0.41f, 0.30f),
                hipNear = P2(0.41f, 0.54f), kneeNear = P2(0.58f, 0.45f), ankleNear = P2(0.74f, 0.35f),
                hipFar = P2(0.41f, 0.54f), kneeFar = P2(0.42f, 0.70f), ankleFar = P2(0.41f, 0.85f),
                focusPoint = P2(0.60f, 0.42f), arrowFrom = P2(0.74f, 0.35f), arrowTo = P2(0.52f, 0.54f)
            ),
            standing.copy(elbowNear = P2(0.60f, 0.34f), handNear = P2(0.74f, 0.32f), focusPoint = P2(0.56f, 0.34f))
        )
        PoseFamily.YOGA_DOWNDOG_FOLD -> Triple(
            standing.copy(arrowFrom = P2(0.54f, 0.28f), arrowTo = P2(0.68f, 0.72f)),
            ArticulatedPose(
                head = P2(0.60f, 0.62f),
                shoulderNear = P2(0.58f, 0.56f), elbowNear = P2(0.64f, 0.70f), handNear = P2(0.70f, 0.84f),
                shoulderFar = P2(0.59f, 0.56f), elbowFar = P2(0.65f, 0.70f), handFar = P2(0.71f, 0.84f),
                hipNear = P2(0.44f, 0.32f), kneeNear = P2(0.34f, 0.58f), ankleNear = P2(0.25f, 0.84f),
                hipFar = P2(0.46f, 0.32f), kneeFar = P2(0.36f, 0.58f), ankleFar = P2(0.27f, 0.84f),
                focusPoint = P2(0.45f, 0.36f), arrowFrom = P2(0.48f, 0.30f), arrowTo = P2(0.40f, 0.24f)
            ),
            ArticulatedPose(
                head = P2(0.58f, 0.62f),
                shoulderNear = P2(0.56f, 0.56f), elbowNear = P2(0.63f, 0.70f), handNear = P2(0.69f, 0.84f),
                shoulderFar = P2(0.58f, 0.56f), elbowFar = P2(0.64f, 0.70f), handFar = P2(0.70f, 0.84f),
                hipNear = P2(0.44f, 0.34f), kneeNear = P2(0.35f, 0.59f), ankleNear = P2(0.26f, 0.84f),
                hipFar = P2(0.46f, 0.34f), kneeFar = P2(0.37f, 0.59f), ankleFar = P2(0.28f, 0.84f),
                focusPoint = P2(0.56f, 0.56f)
            )
        )
        PoseFamily.YOGA_BACKBEND_WHEEL -> Triple(
            ArticulatedPose(
                head = P2(0.25f, 0.79f),
                shoulderNear = P2(0.34f, 0.79f), elbowNear = P2(0.30f, 0.69f), handNear = P2(0.27f, 0.83f),
                shoulderFar = P2(0.35f, 0.79f), elbowFar = P2(0.31f, 0.69f), handFar = P2(0.28f, 0.83f),
                hipNear = P2(0.51f, 0.79f), kneeNear = P2(0.64f, 0.66f), ankleNear = P2(0.72f, 0.84f),
                hipFar = P2(0.52f, 0.79f), kneeFar = P2(0.65f, 0.66f), ankleFar = P2(0.73f, 0.84f),
                focusPoint = P2(0.46f, 0.62f), arrowFrom = P2(0.48f, 0.76f), arrowTo = P2(0.48f, 0.44f)
            ),
            ArticulatedPose(
                head = P2(0.33f, 0.58f),
                shoulderNear = P2(0.36f, 0.46f), elbowNear = P2(0.32f, 0.65f), handNear = P2(0.29f, 0.84f),
                shoulderFar = P2(0.38f, 0.46f), elbowFar = P2(0.34f, 0.65f), handFar = P2(0.31f, 0.84f),
                hipNear = P2(0.52f, 0.39f), kneeNear = P2(0.67f, 0.56f), ankleNear = P2(0.72f, 0.84f),
                hipFar = P2(0.54f, 0.39f), kneeFar = P2(0.68f, 0.56f), ankleFar = P2(0.73f, 0.84f),
                focusPoint = P2(0.46f, 0.42f), arrowFrom = P2(0.48f, 0.44f), arrowTo = P2(0.48f, 0.76f)
            ),
            ArticulatedPose(
                head = P2(0.35f, 0.58f),
                shoulderNear = P2(0.38f, 0.48f), elbowNear = P2(0.34f, 0.66f), handNear = P2(0.31f, 0.84f),
                shoulderFar = P2(0.40f, 0.48f), elbowFar = P2(0.36f, 0.66f), handFar = P2(0.33f, 0.84f),
                hipNear = P2(0.53f, 0.42f), kneeNear = P2(0.66f, 0.58f), ankleNear = P2(0.72f, 0.84f),
                hipFar = P2(0.55f, 0.42f), kneeFar = P2(0.67f, 0.58f), ankleFar = P2(0.73f, 0.84f),
                focusPoint = P2(0.38f, 0.48f)
            )
        )
        PoseFamily.YOGA_BALANCE_CROW -> Triple(
            standing.copy(head = P2(0.50f, 0.44f), shoulderNear = P2(0.49f, 0.54f), elbowNear = P2(0.53f, 0.68f), handNear = P2(0.56f, 0.84f), hipNear = P2(0.40f, 0.68f), kneeNear = P2(0.50f, 0.68f), ankleNear = P2(0.45f, 0.85f), arrowFrom = P2(0.44f, 0.64f), arrowTo = P2(0.62f, 0.54f)),
            ArticulatedPose(
                head = P2(0.68f, 0.50f),
                shoulderNear = P2(0.60f, 0.56f), elbowNear = P2(0.56f, 0.69f), handNear = P2(0.56f, 0.84f),
                shoulderFar = P2(0.62f, 0.56f), elbowFar = P2(0.58f, 0.69f), handFar = P2(0.58f, 0.84f),
                hipNear = P2(0.40f, 0.52f), kneeNear = P2(0.52f, 0.60f), ankleNear = P2(0.34f, 0.64f),
                hipFar = P2(0.42f, 0.52f), kneeFar = P2(0.54f, 0.60f), ankleFar = P2(0.36f, 0.64f),
                focusPoint = P2(0.56f, 0.68f), arrowFrom = P2(0.62f, 0.54f), arrowTo = P2(0.44f, 0.64f)
            ),
            ArticulatedPose(
                head = P2(0.66f, 0.52f),
                shoulderNear = P2(0.58f, 0.57f), elbowNear = P2(0.55f, 0.70f), handNear = P2(0.55f, 0.84f),
                shoulderFar = P2(0.60f, 0.57f), elbowFar = P2(0.57f, 0.70f), handFar = P2(0.57f, 0.84f),
                hipNear = P2(0.40f, 0.54f), kneeNear = P2(0.51f, 0.62f), ankleNear = P2(0.35f, 0.66f),
                hipFar = P2(0.42f, 0.54f), kneeFar = P2(0.53f, 0.62f), ankleFar = P2(0.37f, 0.66f),
                focusPoint = P2(0.56f, 0.80f)
            )
        )
        PoseFamily.YOGA_TWIST_WARRIOR -> Triple(
            standing.copy(ankleNear = P2(0.62f, 0.85f), ankleFar = P2(0.36f, 0.85f), handNear = P2(0.64f, 0.34f), handFar = P2(0.34f, 0.34f), arrowFrom = P2(0.50f, 0.45f), arrowTo = P2(0.50f, 0.62f)),
            ArticulatedPose(
                head = P2(0.50f, 0.31f),
                shoulderNear = P2(0.49f, 0.41f), elbowNear = P2(0.62f, 0.40f), handNear = P2(0.74f, 0.39f),
                shoulderFar = P2(0.49f, 0.41f), elbowFar = P2(0.36f, 0.40f), handFar = P2(0.24f, 0.39f),
                hipNear = P2(0.49f, 0.61f), kneeNear = P2(0.65f, 0.64f), ankleNear = P2(0.66f, 0.85f),
                hipFar = P2(0.49f, 0.61f), kneeFar = P2(0.39f, 0.72f), ankleFar = P2(0.30f, 0.85f),
                focusPoint = P2(0.65f, 0.66f), arrowFrom = P2(0.50f, 0.62f), arrowTo = P2(0.50f, 0.45f)
            ),
            standing.copy(hipNear = P2(0.49f, 0.61f), kneeNear = P2(0.63f, 0.64f), ankleNear = P2(0.64f, 0.85f), kneeFar = P2(0.39f, 0.72f), ankleFar = P2(0.31f, 0.85f), focusPoint = P2(0.49f, 0.60f))
        )
        PoseFamily.MOBILITY_SPLIT_STRETCH -> Triple(
            ArticulatedPose(
                head = P2(0.50f, 0.38f),
                shoulderNear = P2(0.49f, 0.48f), elbowNear = P2(0.52f, 0.62f), handNear = P2(0.54f, 0.75f),
                shoulderFar = P2(0.51f, 0.48f), elbowFar = P2(0.48f, 0.62f), handFar = P2(0.46f, 0.75f),
                hipNear = P2(0.48f, 0.66f), kneeNear = P2(0.62f, 0.68f), ankleNear = P2(0.64f, 0.85f),
                hipFar = P2(0.48f, 0.66f), kneeFar = P2(0.35f, 0.82f), ankleFar = P2(0.22f, 0.84f),
                focusPoint = P2(0.48f, 0.68f), arrowFrom = P2(0.64f, 0.82f), arrowTo = P2(0.78f, 0.82f)
            ),
            ArticulatedPose(
                head = P2(0.50f, 0.50f),
                shoulderNear = P2(0.49f, 0.60f), elbowNear = P2(0.52f, 0.72f), handNear = P2(0.53f, 0.84f),
                shoulderFar = P2(0.51f, 0.60f), elbowFar = P2(0.48f, 0.72f), handFar = P2(0.47f, 0.84f),
                hipNear = P2(0.49f, 0.79f), kneeNear = P2(0.66f, 0.82f), ankleNear = P2(0.82f, 0.84f),
                hipFar = P2(0.49f, 0.79f), kneeFar = P2(0.32f, 0.82f), ankleFar = P2(0.16f, 0.84f),
                focusPoint = P2(0.52f, 0.80f), arrowFrom = P2(0.78f, 0.82f), arrowTo = P2(0.64f, 0.82f)
            ),
            ArticulatedPose(
                head = P2(0.50f, 0.52f),
                shoulderNear = P2(0.46f, 0.61f), elbowNear = P2(0.44f, 0.72f), handNear = P2(0.44f, 0.84f),
                shoulderFar = P2(0.54f, 0.61f), elbowFar = P2(0.56f, 0.72f), handFar = P2(0.56f, 0.84f),
                hipNear = P2(0.47f, 0.79f), kneeNear = P2(0.32f, 0.82f), ankleNear = P2(0.18f, 0.84f),
                hipFar = P2(0.53f, 0.79f), kneeFar = P2(0.68f, 0.82f), ankleFar = P2(0.82f, 0.84f),
                focusPoint = P2(0.50f, 0.79f)
            )
        )
        PoseFamily.MOBILITY_HIP_90_90 -> Triple(
            ArticulatedPose(
                head = P2(0.44f, 0.44f),
                shoulderNear = P2(0.44f, 0.54f), elbowNear = P2(0.40f, 0.66f), handNear = P2(0.36f, 0.80f),
                shoulderFar = P2(0.48f, 0.54f), elbowFar = P2(0.54f, 0.66f), handFar = P2(0.58f, 0.80f),
                hipNear = P2(0.45f, 0.78f), kneeNear = P2(0.61f, 0.76f), ankleNear = P2(0.64f, 0.84f),
                hipFar = P2(0.45f, 0.78f), kneeFar = P2(0.32f, 0.76f), ankleFar = P2(0.24f, 0.84f),
                focusPoint = P2(0.48f, 0.76f), arrowFrom = P2(0.48f, 0.46f), arrowTo = P2(0.64f, 0.58f)
            ),
            ArticulatedPose(
                head = P2(0.59f, 0.54f),
                shoulderNear = P2(0.55f, 0.62f), elbowNear = P2(0.60f, 0.72f), handNear = P2(0.65f, 0.83f),
                shoulderFar = P2(0.57f, 0.62f), elbowFar = P2(0.54f, 0.72f), handFar = P2(0.52f, 0.83f),
                hipNear = P2(0.45f, 0.78f), kneeNear = P2(0.62f, 0.77f), ankleNear = P2(0.65f, 0.84f),
                hipFar = P2(0.45f, 0.78f), kneeFar = P2(0.32f, 0.77f), ankleFar = P2(0.24f, 0.84f),
                focusPoint = P2(0.56f, 0.74f), arrowFrom = P2(0.64f, 0.58f), arrowTo = P2(0.48f, 0.46f)
            ),
            ArticulatedPose(
                head = P2(0.50f, 0.44f),
                shoulderNear = P2(0.44f, 0.54f), elbowNear = P2(0.38f, 0.66f), handNear = P2(0.34f, 0.82f),
                shoulderFar = P2(0.56f, 0.54f), elbowFar = P2(0.62f, 0.66f), handFar = P2(0.66f, 0.82f),
                hipNear = P2(0.47f, 0.78f), kneeNear = P2(0.63f, 0.76f), ankleNear = P2(0.66f, 0.84f),
                hipFar = P2(0.53f, 0.78f), kneeFar = P2(0.36f, 0.76f), ankleFar = P2(0.26f, 0.84f),
                focusPoint = P2(0.50f, 0.76f)
            )
        )
        PoseFamily.OLYMPIC_LIFT -> Triple(
            ArticulatedPose(
                head = P2(0.62f, 0.38f),
                shoulderNear = P2(0.55f, 0.44f), elbowNear = P2(0.54f, 0.59f), handNear = P2(0.54f, 0.74f),
                shoulderFar = P2(0.57f, 0.44f), elbowFar = P2(0.56f, 0.59f), handFar = P2(0.56f, 0.74f),
                hipNear = P2(0.38f, 0.55f), kneeNear = P2(0.51f, 0.68f), ankleNear = P2(0.49f, 0.85f),
                hipFar = P2(0.40f, 0.55f), kneeFar = P2(0.53f, 0.68f), ankleFar = P2(0.51f, 0.85f),
                focusPoint = P2(0.53f, 0.62f), arrowFrom = P2(0.68f, 0.74f), arrowTo = P2(0.68f, 0.24f), propAnchorA = P2(0.54f, 0.75f)
            ),
            ArticulatedPose(
                head = P2(0.50f, 0.34f),
                shoulderNear = P2(0.48f, 0.44f), elbowNear = P2(0.48f, 0.30f), handNear = P2(0.48f, 0.16f),
                shoulderFar = P2(0.51f, 0.44f), elbowFar = P2(0.51f, 0.30f), handFar = P2(0.51f, 0.16f),
                hipNear = P2(0.41f, 0.64f), kneeNear = P2(0.56f, 0.66f), ankleNear = P2(0.49f, 0.85f),
                hipFar = P2(0.43f, 0.64f), kneeFar = P2(0.58f, 0.66f), ankleFar = P2(0.51f, 0.85f),
                focusPoint = P2(0.49f, 0.30f), arrowFrom = P2(0.68f, 0.24f), arrowTo = P2(0.68f, 0.52f), propAnchorA = P2(0.49f, 0.16f)
            ),
            standing.copy(handNear = P2(0.48f, 0.14f), elbowNear = P2(0.48f, 0.24f), propAnchorA = P2(0.49f, 0.14f), focusPoint = P2(0.49f, 0.42f))
        )
        PoseFamily.CARDIO_CYCLE_ROW -> Triple(
            ArticulatedPose(
                head = P2(0.40f, 0.34f),
                shoulderNear = P2(0.43f, 0.44f), elbowNear = P2(0.50f, 0.48f), handNear = P2(0.57f, 0.47f),
                shoulderFar = P2(0.45f, 0.44f), elbowFar = P2(0.52f, 0.48f), handFar = P2(0.59f, 0.47f),
                hipNear = P2(0.38f, 0.64f), kneeNear = P2(0.49f, 0.62f), ankleNear = P2(0.57f, 0.77f),
                hipFar = P2(0.40f, 0.64f), kneeFar = P2(0.52f, 0.70f), ankleFar = P2(0.58f, 0.82f),
                focusPoint = P2(0.48f, 0.64f), arrowFrom = P2(0.58f, 0.44f), arrowTo = P2(0.42f, 0.44f)
            ),
            ArticulatedPose(
                head = P2(0.36f, 0.34f),
                shoulderNear = P2(0.38f, 0.44f), elbowNear = P2(0.35f, 0.52f), handNear = P2(0.44f, 0.50f),
                shoulderFar = P2(0.40f, 0.44f), elbowFar = P2(0.37f, 0.52f), handFar = P2(0.46f, 0.50f),
                hipNear = P2(0.38f, 0.64f), kneeNear = P2(0.52f, 0.72f), ankleNear = P2(0.60f, 0.80f),
                hipFar = P2(0.40f, 0.64f), kneeFar = P2(0.50f, 0.64f), ankleFar = P2(0.58f, 0.76f),
                focusPoint = P2(0.44f, 0.58f), arrowFrom = P2(0.42f, 0.44f), arrowTo = P2(0.58f, 0.44f)
            ),
            standing.copy(head = P2(0.42f, 0.35f), shoulderNear = P2(0.44f, 0.45f), hipNear = P2(0.42f, 0.65f), kneeNear = P2(0.53f, 0.67f), ankleNear = P2(0.59f, 0.79f), focusPoint = P2(0.48f, 0.58f))
        )
        PoseFamily.ROTATION_CARRY -> Triple(
            standing.copy(elbowNear = P2(0.46f, 0.44f), handNear = P2(0.46f, 0.56f), arrowFrom = P2(0.52f, 0.46f), arrowTo = P2(0.70f, 0.46f)),
            standing.copy(shoulderNear = P2(0.52f, 0.32f), elbowNear = P2(0.62f, 0.34f), handNear = P2(0.72f, 0.34f), ankleNear = P2(0.56f, 0.85f), ankleFar = P2(0.42f, 0.85f), focusPoint = P2(0.50f, 0.46f), arrowFrom = P2(0.70f, 0.46f), arrowTo = P2(0.52f, 0.46f)),
            standing.copy(elbowNear = P2(0.62f, 0.35f), handNear = P2(0.72f, 0.35f), focusPoint = P2(0.50f, 0.48f))
        )
        PoseFamily.BREATHING_POSE -> Triple(
            ArticulatedPose(
                head = P2(0.50f, 0.36f),
                shoulderNear = P2(0.44f, 0.47f), elbowNear = P2(0.40f, 0.61f), handNear = P2(0.46f, 0.68f),
                shoulderFar = P2(0.56f, 0.47f), elbowFar = P2(0.60f, 0.61f), handFar = P2(0.54f, 0.68f),
                hipNear = P2(0.46f, 0.76f), kneeNear = P2(0.32f, 0.80f), ankleNear = P2(0.44f, 0.84f),
                hipFar = P2(0.54f, 0.76f), kneeFar = P2(0.68f, 0.80f), ankleFar = P2(0.56f, 0.84f),
                focusPoint = P2(0.50f, 0.62f), arrowFrom = P2(0.50f, 0.62f), arrowTo = P2(0.50f, 0.50f)
            ),
            ArticulatedPose(
                head = P2(0.50f, 0.35f),
                shoulderNear = P2(0.44f, 0.46f), elbowNear = P2(0.39f, 0.61f), handNear = P2(0.46f, 0.67f),
                shoulderFar = P2(0.56f, 0.46f), elbowFar = P2(0.61f, 0.61f), handFar = P2(0.54f, 0.67f),
                hipNear = P2(0.46f, 0.76f), kneeNear = P2(0.32f, 0.80f), ankleNear = P2(0.44f, 0.84f),
                hipFar = P2(0.54f, 0.76f), kneeFar = P2(0.68f, 0.80f), ankleFar = P2(0.56f, 0.84f),
                focusPoint = P2(0.50f, 0.60f), arrowFrom = P2(0.50f, 0.50f), arrowTo = P2(0.50f, 0.64f)
            ),
            ArticulatedPose(
                head = P2(0.24f, 0.77f),
                shoulderNear = P2(0.33f, 0.78f), elbowNear = P2(0.38f, 0.74f), handNear = P2(0.48f, 0.74f),
                shoulderFar = P2(0.34f, 0.78f), elbowFar = P2(0.39f, 0.81f), handFar = P2(0.48f, 0.82f),
                hipNear = P2(0.52f, 0.79f), kneeNear = P2(0.64f, 0.66f), ankleNear = P2(0.72f, 0.84f),
                hipFar = P2(0.53f, 0.79f), kneeFar = P2(0.65f, 0.66f), ankleFar = P2(0.73f, 0.84f),
                focusPoint = P2(0.46f, 0.76f)
            )
        )
    }
}
