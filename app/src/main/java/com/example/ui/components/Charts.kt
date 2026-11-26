package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

data class ChartBarPoint(
    val label: String,
    val value: Float
)

@Composable
fun SimpleBarChart(
    points: List<ChartBarPoint>,
    barColor: Color = MaterialTheme.colorScheme.primary,
    unitSuffix: String = "",
    modifier: Modifier = Modifier
) {
    if (points.isEmpty()) return
    val maxVal = (points.maxOfOrNull { it.value } ?: 1f).coerceAtLeast(1f)
    val trackColor = MaterialTheme.colorScheme.surfaceContainerHigh

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
        ) {
            val count = points.size
            val slotW = size.width / count
            val barW = (slotW * 0.55f).coerceIn(14f, 52f)
            points.forEachIndexed { idx, pt ->
                val left = idx * slotW + (slotW - barW) / 2f
                val ratio = (pt.value / maxVal).coerceIn(0f, 1f)
                val barH = size.height * ratio
                drawRoundRect(
                    color = trackColor,
                    topLeft = Offset(left, 0f),
                    size = Size(barW, size.height),
                    cornerRadius = CornerRadius(10f, 10f)
                )
                drawRoundRect(
                    color = barColor,
                    topLeft = Offset(left, size.height - barH),
                    size = Size(barW, barH),
                    cornerRadius = CornerRadius(10f, 10f)
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            points.forEach { pt ->
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (pt.value % 1f == 0f) "${pt.value.toInt()}$unitSuffix" else "${"%.1f".format(pt.value)}$unitSuffix",
                        style = MaterialTheme.typography.labelSmall,
                        color = barColor
                    )
                    Text(
                        text = pt.label,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun SimpleTrendLineChart(
    values: List<Float>,
    labels: List<String>,
    lineColor: Color = MaterialTheme.colorScheme.secondary,
    modifier: Modifier = Modifier
) {
    if (values.isEmpty()) return
    val minVal = (values.minOrNull() ?: 0f)
    val maxVal = (values.maxOrNull() ?: 10f).let { if (it <= minVal) minVal + 1f else it }
    val gridColor = MaterialTheme.colorScheme.outlineVariant

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        ) {
            // Horizontal reference lines
            listOf(0.25f, 0.5f, 0.75f).forEach { frac ->
                val y = size.height * frac
                drawLine(gridColor.copy(alpha = 0.45f), Offset(0f, y), Offset(size.width, y), strokeWidth = 1.5f)
            }

            if (values.size == 1) {
                val y = size.height * 0.5f
                drawCircle(lineColor, radius = 9f, center = Offset(size.width * 0.5f, y))
            } else {
                val stepX = size.width / (values.size - 1)
                val path = Path()
                val pts = values.mapIndexed { idx, v ->
                    val norm = ((v - minVal) / (maxVal - minVal)).coerceIn(0f, 1f)
                    val x = idx * stepX
                    val y = size.height * (0.85f - norm * 0.7f)
                    Offset(x, y)
                }
                pts.forEachIndexed { idx, pt ->
                    if (idx == 0) path.moveTo(pt.x, pt.y) else path.lineTo(pt.x, pt.y)
                }
                drawPath(path, color = lineColor, style = Stroke(width = 5f, cap = StrokeCap.Round))
                pts.forEach { drawCircle(lineColor, radius = 7f, center = it) }
            }
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(labels.firstOrNull() ?: "", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(labels.lastOrNull() ?: "", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
