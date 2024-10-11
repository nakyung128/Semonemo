package com.semonemo.presentation.screen.frame

import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.semonemo.presentation.navigation.ScreenDestinations
import com.semonemo.presentation.screen.moment.MomentRoute

fun NavGraphBuilder.MomentGraph(
    modifier: Modifier,
    navController: NavController,
    graphRoute: String,
    onErrorSnackBar: (String) -> Unit,
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
                onShowSnackBar = onErrorSnackBar,
            )
        }
        composable(
            route = ScreenDestinations.Frame.route,
        ) {
            val viewModel =
                hiltViewModel<FrameViewModel>(
                    navController.getBackStackEntry(graphRoute),
                )

            FrameRoute(
                modifier = modifier,
                viewModel = viewModel,
                navigateToFrameDone = { navController.navigate(ScreenDestinations.FrameDone.route) },
                onErrorSnackBar = onErrorSnackBar,
                popBackStack = { navController.navigate(ScreenDestinations.Moment.route) },
            )
        }

        composable(
            route = ScreenDestinations.FrameDone.route,
        ) {
            val viewModel =
                hiltViewModel<FrameViewModel>(
                    navController.getBackStackEntry(graphRoute),
                )
            FrameDoneRoute(
                modifier = modifier,
                viewModel = viewModel,
                navigateToMoment = { navController.navigate(ScreenDestinations.Moment.route) },
                onErrorSnackBar = onErrorSnackBar,
            )
        }
    }
}
