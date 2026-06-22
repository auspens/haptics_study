package com.example.haptics_study

import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.haptics_study.ui.theme.Haptics_studyTheme

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Haptics_studyTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(innerPadding)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text (
                            text = "Android VibrationEffect",
                            modifier = Modifier.padding(16.dp)
                        )
                        LightImpact()
                        MediumImpact()
                        HeavyImpact()
                        WaveForm()

                        Text (
                            text = "JetPack Compose Haptics",
                            modifier = Modifier.padding(16.dp)
                        )
                        LongClick()
                        Confirm()
                        Reject()
                    }
                }
            }
        }
    }
}


@Composable
fun LongClick() {
    val haptic = LocalHapticFeedback.current
    ButtonModel("Long Click", {
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
    })
}

@Composable
fun Confirm() {
    val haptic = LocalHapticFeedback.current
    ButtonModel("Confirm", {
        haptic.performHapticFeedback(HapticFeedbackType.Confirm)
    })
}

@Composable
fun Reject() {
    val haptic = LocalHapticFeedback.current
    ButtonModel("Reject", {
        haptic.performHapticFeedback(HapticFeedbackType.Reject)
    })
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LightImpact() {
    var hapticEngine by remember { mutableStateOf<VibrationEffect?>(null) }
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        val duration = 50L
        val amplitude = 50
        hapticEngine = VibrationEffect.createOneShot(duration, amplitude)
    }
    ButtonModel(
        "Light Impact", {
            hapticEngine?.let {
                val vibrator = context.getSystemService(Vibrator::class.java)
                vibrator.vibrate(it)
            }
        })
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MediumImpact() {
    var hapticEngine by remember { mutableStateOf<VibrationEffect?>(null) }
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        val duration = 50L
        val amplitude = 200
        hapticEngine = VibrationEffect.createOneShot(duration, amplitude)
    }
    ButtonModel(
        "Medium Impact", {
            hapticEngine?.let {
                val vibrator = context.getSystemService(Vibrator::class.java)
                vibrator.vibrate(it)
            }
        })
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HeavyImpact() {
    var hapticEngine by remember { mutableStateOf<VibrationEffect?>(null) }
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        val duration = 50L
        val amplitude = 255
        hapticEngine = VibrationEffect.createOneShot(duration, amplitude)
    }
    ButtonModel("Heavy Impact", {
        hapticEngine?.let {
            val vibrator = context.getSystemService(Vibrator::class.java)
            vibrator.vibrate(it)
        }
    })
}

@Composable
fun ButtonModel(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .defaultMinSize(minHeight = 96.dp)
    ) {
        Text(text)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WaveForm() {
    var hapticEngine by remember { mutableStateOf<VibrationEffect?>(null) }
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        val numberOfPulses = 3 // Number of increasing haptic pulses
        val pulseDuration = 50L // Duration of each pulse in milliseconds
        val spaceBetweenPulses = 100L // Duration of space between pulses in milliseconds
        val maxAmplitude = 255 // Maximum amplitude for the last pulse

        val timings = LongArray(numberOfPulses * 2) // Double the size for on/off
        val amplitudes = IntArray(numberOfPulses * 2)

        for (i in 0 until numberOfPulses) {
            val amplitude = (maxAmplitude * (i + 1) / numberOfPulses) // Calculate increasing amplitude
            timings[i * 2] = spaceBetweenPulses // Space before the pulse
            timings[i * 2 + 1] = pulseDuration // Duration of the pulse
            amplitudes[i * 2] = 0 // Amplitude of the space
            amplitudes[i * 2 + 1] = amplitude // Amplitude of the pulse
        }

        hapticEngine = VibrationEffect.createWaveform(timings, amplitudes, -1)
    }
    ButtonModel(
        "Wave Form", {
            hapticEngine?.let {
                val vibrator = context.getSystemService(Vibrator::class.java)
                vibrator.vibrate(it)
            }
        })
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun ButtonPreview() {
    Haptics_studyTheme {
        HeavyImpact()
    }
}