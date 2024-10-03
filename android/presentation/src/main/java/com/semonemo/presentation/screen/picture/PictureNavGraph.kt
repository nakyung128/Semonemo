package com.semonemo.presentation.screen.picture

import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.semonemo.presentation.navigation.ScreenDestinations
import com.semonemo.presentation.screen.picture.camera.CameraRoute
import com.semonemo.presentation.screen.picture.camera.CameraViewModel

fun NavGraphBuilder.PictureGraph(
    modifier: Modifier,
    navController: NavController,
    graphRoute: String,
    onErrorSnackBar: (String) -> Unit,
) {
    navigation(startDestination = ScreenDestinations.PictureMain.route, route = graphRoute) {
        composable(
            route = ScreenDestinations.PictureMain.route,
        ) {
            PictureMainRoute(
                modifier = modifier,
                navigateToCamera = {
                    navController.navigate(
                        ScreenDestinations.Camera.createRoute(it),
                    )
                },
            )
        }
        composable(
            route = ScreenDestinations.Camera.route,
            arguments = ScreenDestinations.Camera.arguments,
        ) {
            val amount = it.arguments?.getInt("amount") ?: -1
            val viewModel =
                hiltViewModel<CameraViewModel>(
                    navController.getBackStackEntry(graphRoute),
                )

            CameraRoute(
                modifier = modifier,
                amount = amount,
                popUpBackStack = navController::popBackStack,
                onShowSnackBar = onErrorSnackBar,
                viewModel = viewModel,
                navigateToSelect = {
                    navController.navigate(ScreenDestinations.PictureSelect.route) {
                        popUpTo(ScreenDestinations.PictureMain.route) { inclusive = false }
                    }
                },
            )
        }

        composable(
            route = ScreenDestinations.PictureSelect.route,
        ) {
            val viewModel =
                hiltViewModel<CameraViewModel>(
                    navController.getBackStackEntry(graphRoute),
                )
            PictureSelectRoute(
                modifier = modifier,
                popUpBackStack = navController::popBackStack,
                viewModel = viewModel,
                onShowSnackBar = onErrorSnackBar,
            )
        }
    }
}
