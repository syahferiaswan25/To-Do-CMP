package com.aswan.todo.util

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun <T> RequestState<T>.DisplayResult(
    modifier: Modifier = Modifier,
    onIdle: (@Composable () -> Unit)? = null,
    onLoading: @Composable () -> Unit,
    onSuccess: @Composable (T) -> Unit,
    onError: @Composable (String) -> Unit,
    transitionSpec: AnimatedContentTransitionScope<RequestState<T>>.() -> ContentTransform = {
        fadeIn(tween(durationMillis = 300)) + scaleIn(initialScale = 0.9f) togetherWith
                fadeOut(tween(durationMillis = 300)) + scaleOut(targetScale = 0.9f)
    }
) {
    AnimatedContent(
        targetState = this,
        transitionSpec = transitionSpec,
        label = "Content Animation"
    ) { state ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Transparent),
            contentAlignment = Alignment.TopStart
        ) {
            when (state) {
                is RequestState.Idle -> {
                    onIdle?.invoke()
                }

                is RequestState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        onLoading()
                    }
                }

                is RequestState.Success -> {
                    onSuccess(state.getSuccessData())
                }

                is RequestState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        onError(state.getErrorMessage())
                    }
                }
            }
        }
    }
}
