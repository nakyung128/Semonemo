package com.semonemo.presentation.screen.picture.camera.subscreen

import android.Manifest
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraPreviewWithPermission(
    modifier: Modifier = Modifier,
    controller: LifecycleCameraController,
) {
    val cameraPermissionState =
        rememberPermissionState(
            Manifest.permission.CAMERA,
        )
    LaunchedEffect(cameraPermissionState.status.isGranted.not()) {
        run { cameraPermissionState.launchPermissionRequest() }
    }

    if (cameraPermissionState.status.isGranted) {
        CameraPreviewView(
            modifier = modifier,
            controller = controller,
        )
    }
}

@Composable
fun CameraPreviewView(
    modifier: Modifier = Modifier,
    controller: LifecycleCameraController,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    AndroidView(
        modifier =
            modifier.clipToBounds(),
        factory = {
            PreviewView(it).apply {
                this.controller = controller
                controller.bindToLifecycle(lifecycleOwner)
            }
        },
        onReset = {
            controller.unbind()
        },
    )
}
