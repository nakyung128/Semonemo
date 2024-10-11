package com.semonemo.presentation.screen.picture

import android.net.Uri
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.semonemo.presentation.navigation.ScreenDestinations
import com.semonemo.presentation.screen.moment.MomentRoute
import com.semonemo.presentation.screen.picture.camera.CameraRoute
import com.semonemo.presentation.screen.picture.camera.CameraViewModel
import com.semonemo.presentation.screen.picture.main.PictureMainRoute
import com.semonemo.presentation.screen.picture.select.PictureSelectRoute

fun NavGraphBuilder.PictureGraph(
    modifier: Modifier,
    navController: NavController,
    graphRoute: String,
    onErrorSnackBar: (String) -> Unit,
    actionWithSnackBar: (Uri) -> Unit,
) {
    navigation(startDestination = ScreenDestinations.Moment.route, route = graphRoute) {
        composable(
            route = ScreenDestinations.Moment.route,
        ) {
            MomentRoute(
                modifier = modifier,
                navigateToAiAsset = { navController.navigate(ScreenDestinations.AiAsset.route) },
                navigateToImageAsset = { navController.navigate(ScreenDestinations.ImageAsset.route) },
                navigateToFrame = { navController.navigate(ScreenDestinations.Frame.route) },
                navigateToPicture = { navController.navigate(ScreenDestinations.PictureMain.route) },
            )
        }

        composable(
            route = ScreenDestinations.PictureMain.route,
        ) {
            val viewModel =
                hiltViewModel<CameraViewModel>(
                    navController.getBackStackEntry(graphRoute),
                )
            PictureMainRoute(
                modifier = modifier,
                navigateToCamera = {
                    navController.navigate(
                        ScreenDestinations.Camera.createRoute(it),
                    )
                },
                popBackStack = navController::popBackStack,
                viewModel = viewModel,
                onShowSnackBar = onErrorSnackBar,
            )
        }
        composable(
            route = ScreenDestinations.Camera.route,
            arguments = ScreenDestinations.Camera.arguments,
        ) {
            val type = it.arguments?.getInt("type") ?: 1
            val viewModel =
                hiltViewModel<CameraViewModel>(
                    navController.getBackStackEntry(graphRoute),
                )

            CameraRoute(
                modifier = modifier,
                frameIdx = type,
                popUpBackStack = navController::popBackStack,
                onShowSnackBar = onErrorSnackBar,
                viewModel = viewModel,
                navigateToSelect = { type ->
                    navController.navigate(ScreenDestinations.PictureSelect.createRoute(type)) {
                        popUpTo(ScreenDestinations.PictureMain.route) { inclusive = false }
                    }
                },
            )
        }

        composable(
            route = ScreenDestinations.PictureSelect.route,
            arguments = ScreenDestinations.PictureSelect.arguments,
        ) {
            val type = it.arguments?.getInt("type") ?: 1
            val viewModel =
                hiltViewModel<CameraViewModel>(
                    navController.getBackStackEntry(graphRoute),
                )
            PictureSelectRoute(
                modifier = modifier,
                popUpBackStack = navController::popBackStack,
                viewModel = viewModel,
                onShowSnackBar = onErrorSnackBar,
                type = type,
                actionWithSnackBar = actionWithSnackBar,
            )
        }
    }
}
