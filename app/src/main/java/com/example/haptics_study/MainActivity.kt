package com.example.haptics_study

import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.HapticFeedbackConstants
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
                        modifier = Modifier
                            .padding(innerPadding)
                            .verticalScroll(rememberScrollState())
                    ) {
                        //Android's Vibration Effect is a low level API that gives you more control over the haptic feedback, allowing you to create custom vibration patterns and effects.
                        // It is more flexible and can be used for a wider range of haptic feedback scenarios.
                        Text(
                            text = "Android VibrationEffect",
                            modifier = Modifier.padding(16.dp)
                        )
                        LightImpact()
                        MediumImpact()
                        HeavyImpact()
                        WaveForm()

                        //Haptics library from JetPack compose, closer to the iOS haptics and tied to user interactions (clicks, long clicks, etc).
                        // It is a higher level API with less control over the settings (duration, amplitude, etc)
                        Text(
                            text = "JetPack Compose Haptics",
                            modifier = Modifier.padding(16.dp)
                        )
                        LongClick()
                        Confirm()
                        Reject()

                        // Android's View.performHapticFeedback is an API that provides basic haptic feedback for specific user interactions, such as key presses or long clicks.
                        // From sensory point close to the JetPack compose haptics. Different min API requirements (from API 5 to API 34) and tied to user interactions on views.
                        Text(
                            text = "View Haptics",
                            modifier = Modifier.padding(16.dp)
                        )
                        KeyboardTap()
                        ContextClick()
                        KeyBoardTap()
                    }
                }
            }
        }
    }
}


@Composable
fun KeyboardTap() {
    val view = androidx.compose.ui.platform.LocalView.current
    ButtonModel("Keyboard Tap") {
        view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
    }
}

@Composable
fun ContextClick() {
    val view = androidx.compose.ui.platform.LocalView.current
    ButtonModel("Context Click") {
        view.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
    }
}

@Composable
fun KeyBoardTap() {
    val view = androidx.compose.ui.platform.LocalView.current
    ButtonModel("Keyboard Tap") {
        view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
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
    val context = LocalContext.current
    ButtonModel(
        "Light Impact", {
            val vibrator = context.getSystemService(Vibrator::class.java)
            val effect = VibrationEffect.createOneShot(50L, 50)
            vibrator.vibrate(effect)

        })
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MediumImpact() {
    val context = LocalContext.current
    ButtonModel(
        "Medium Impact", {
            val vibrator = context.getSystemService(Vibrator::class.java)
            val effect = VibrationEffect.createOneShot(50L, 200)
            vibrator.vibrate(effect)
        })
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HeavyImpact() {
    val context = LocalContext.current
    ButtonModel("Heavy Impact", {
        val vibrator = context.getSystemService(Vibrator::class.java)
        val effect = VibrationEffect.createOneShot(50L, 255)
        vibrator.vibrate(effect)
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
            val amplitude =
                (maxAmplitude * (i + 1) / numberOfPulses) // Calculate increasing amplitude
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