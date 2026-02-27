package com.eslamdev.weathroza.core.receiver

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eslamdev.weathroza.core.helpers.AppColors
import com.eslamdev.weathroza.ui.theme.WeathrozaTheme
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

class FullScreenAlertActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val alertId = intent.getLongExtra(AlertReceiver.EXTRA_ALERT_ID, -1L)
        val alertName = intent.getStringExtra(AlertReceiver.EXTRA_ALERT_NAME) ?: "Weather Alert"
        val endMillis = intent.getLongExtra(AlertReceiver.EXTRA_END_MILLIS, -1L)
        Log.d("FullScreenAlert", "Activity launched! alertName=$alertName")

        setShowWhenLocked(true)
        setTurnScreenOn(true)

        setContent {
            WeathrozaTheme {
                FullScreenAlertScreen(
                    alertName = alertName,
                    endMillis = endMillis,
                    onDismiss = {
                        sendBroadcast(Intent(AlertReceiver.ACTION_DISMISS).apply {
                            setClass(
                                this@FullScreenAlertActivity,
                                AlertReceiver::class.java
                            )
                            putExtra(AlertReceiver.EXTRA_NOTIFICATION_ID, alertId.toInt())
                        })
                        finish()
                    }
                )
            }
        }
    }
}

@Composable
fun FullScreenAlertScreen(
    alertName: String,
    endMillis: Long,
    onDismiss: () -> Unit,
) {
    var remainingMillis by remember {
        mutableLongStateOf((endMillis - System.currentTimeMillis()).coerceAtLeast(0))
    }
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { visible = true }

    LaunchedEffect(endMillis) {
        while (remainingMillis > 0) {
            delay(1000)
            remainingMillis = (endMillis - System.currentTimeMillis()).coerceAtLeast(0)
        }
        if (endMillis > 0) onDismiss()
    }

    val minutes = TimeUnit.MILLISECONDS.toMinutes(remainingMillis)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(remainingMillis) % 60
    val progress =
        if (endMillis > 0) remainingMillis.toFloat() / (endMillis - System.currentTimeMillis() + remainingMillis) else 1f

    // Pulsing glow animation
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    val contentAlpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(600),
        label = "contentAlpha"
    )
    val contentSlide by animateFloatAsState(
        targetValue = if (visible) 1f else 0.85f,
        animationSpec = tween(600),
        label = "contentSlide"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.sheetBg),
    ) {
        // Background glow blobs
        Box(
            modifier = Modifier
                .size(320.dp)
                .align(Alignment.TopCenter)
                .blur(80.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            AppColors.error.copy(alpha = 0.25f),
                            Color.Transparent
                        )
                    ),
                    CircleShape
                )
        )
        Box(
            modifier = Modifier
                .size(240.dp)
                .align(Alignment.BottomStart)
                .blur(60.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            AppColors.primary.copy(alpha = 0.15f),
                            Color.Transparent
                        )
                    ),
                    CircleShape
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .scale(contentSlide)
                .padding(horizontal = 32.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top label
            Text(
                text = "ACTIVE ALERT",
                color = AppColors.error.copy(alpha = contentAlpha),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 4.sp
            )

            // Center content
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Pulsing warning icon
                Box(contentAlignment = Alignment.Center) {
                    // Outer glow ring
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .scale(pulseScale)
                            .background(
                                AppColors.error.copy(alpha = pulseAlpha * 0.2f),
                                CircleShape
                            )
                    )
                    // Inner ring
                    Box(
                        modifier = Modifier
                            .size(88.dp)
                            .background(
                                AppColors.error.copy(alpha = 0.15f),
                                CircleShape
                            )
                    )
                    Text("⚠️", fontSize = 44.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = alertName,
                    color = AppColors.white.copy(alpha = contentAlpha),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    lineHeight = 36.sp
                )

                Text(
                    text = "Weather conditions require your attention",
                    color = AppColors.lightGray.copy(alpha = contentAlpha),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )

                // Countdown
                if (endMillis > 0) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .background(
                                AppColors.cardBg,
                                RoundedCornerShape(16.dp)
                            )
                            .padding(horizontal = 28.dp, vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "ENDS IN",
                                color = AppColors.label,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 2.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TimeUnit(value = "%02d".format(minutes), label = "MIN")
                                Text(
                                    ":",
                                    color = AppColors.error,
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                TimeUnit(value = "%02d".format(seconds), label = "SEC")
                            }
                        }
                    }
                }
            }

            // Dismiss button
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = AppColors.error),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = "Dismiss Alert",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AppColors.white,
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}

@Composable
private fun TimeUnit(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            color = AppColors.white,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 40.sp
        )
        Text(
            text = label,
            color = AppColors.label,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 1.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FullScreenAlertScreenPrev() {
    FullScreenAlertScreen("aaaaaaa", 342423423423) { }
}