package com.semonemo.presentation

import BottomNavigationBar
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.semonemo.domain.model.User
import com.semonemo.presentation.navigation.CustomFAB
import com.semonemo.presentation.navigation.ScreenDestinations
import com.semonemo.presentation.screen.aiAsset.AiAssetScreen
import com.semonemo.presentation.screen.aiAsset.AssetDoneRoute
import com.semonemo.presentation.screen.aiAsset.DrawAssetScreen
import com.semonemo.presentation.screen.aiAsset.PromptAssetScreen
import com.semonemo.presentation.screen.auction.AuctionProcessScreen
import com.semonemo.presentation.screen.auction.AuctionScreen
import com.semonemo.presentation.screen.detail.asset.AssetDetailRoute
import com.semonemo.presentation.screen.detail.frame.FrameDetailRoute
import com.semonemo.presentation.screen.frame.MomentGraph
import com.semonemo.presentation.screen.imgAsset.ImageAssetScreen
import com.semonemo.presentation.screen.imgAsset.ImageSelectRoute
import com.semonemo.presentation.screen.login.LoginRoute
import com.semonemo.presentation.screen.mypage.DetailScreen
import com.semonemo.presentation.screen.mypage.FollowListScreen
import com.semonemo.presentation.screen.mypage.MyPageRoute
import com.semonemo.presentation.screen.mypage.setting.SettingRoute
import com.semonemo.presentation.screen.picture.PictureGraph
import com.semonemo.presentation.screen.search.SearchRoute
import com.semonemo.presentation.screen.signup.SignUpRoute
import com.semonemo.presentation.screen.store.StoreFullViewScreen
import com.semonemo.presentation.screen.store.StoreRoute
import com.semonemo.presentation.screen.store.asset.AssetSaleRoute
import com.semonemo.presentation.screen.store.frame.FrameSaleRoute
import com.semonemo.presentation.screen.wallet.WalletScreen
import com.semonemo.presentation.theme.Gray01
import com.semonemo.presentation.theme.GunMetal
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.util.toUriOrDefault
import kotlinx.coroutines.launch

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val coroutineScope = rememberCoroutineScope()
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val (visible, setVisible) =
        remember {
            mutableStateOf(false)
        }
    when (currentRoute) {
        "mypage/{userId}", "shop", "moment", "wallet", "auction", "storeFullView/{isFrame}" ->
            setVisible(
                true,
            )

        else -> setVisible(false)
    }

    val snackBarHostState =
        remember {
            SnackbarHostState()
        }

    val onShowErrorSnackBar: (errorMessage: String) -> Unit = { errorMessage ->
        coroutineScope.launch {
            snackBarHostState.showSnackbar(errorMessage)
        }
    }

    Scaffold(
        modifier = Modifier,
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        bottomBar = {
            if (visible) {
                BottomNavigationBar(navController = navController, currentRoute = currentRoute)
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            if (visible) {
                val isSelected = currentRoute == ScreenDestinations.Moment.route
                CustomFAB(
                    onClick = {
                        navController.navigate(ScreenDestinations.Moment.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    },
                    icon =
                        if (isSelected) {
                            R.drawable.ic_bot_nav_fill_frame
                        } else {
                            R.drawable.ic_bot_nav_outline_frame
                        },
                    iconColor = Color.White,
                    labelColor =
                        if (isSelected) GunMetal else Gray01,
                    labelStyle =
                        if (isSelected) Typography.bodySmall.copy(fontSize = 12.sp) else Typography.labelSmall,
                )
            }
        },
    ) { _ ->
        MainNavHost(
            navController = navController,
            startDestination = ScreenDestinations.Login.route,
            onShowErrorSnackBar = onShowErrorSnackBar,
        )
    }
}

@Composable
fun MainNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String,
    onShowErrorSnackBar: (String) -> Unit,
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(
            route = ScreenDestinations.Login.route,
        ) {
            LoginRoute(
                modifier = modifier,
                popUpBackStack = navController::popBackStack,
                navigateToRegister = { walletAddress ->
                    navController.navigate(ScreenDestinations.Register.createRoute(walletAddress))
                },
                onShowErrorSnackBar = onShowErrorSnackBar,
                navigateToMoment = {
                    navController.navigate(ScreenDestinations.Moment.route) {
                        popUpTo(startDestination) {
                            inclusive = true
                        }
                    }
                },
            )
        }

        composable(
            route = ScreenDestinations.Register.route,
            arguments = ScreenDestinations.Register.arguments,
        ) {
            SignUpRoute(
                modifier = modifier,
                popUpBackStack = navController::popBackStack,
                onShowErrorSnackBar = onShowErrorSnackBar,
            )
        }

        composable(
            route = ScreenDestinations.Shop.route,
        ) {
            StoreRoute(
                modifier = modifier,
                navigateToFullView = { isFrame ->
                    navController.navigate(
                        ScreenDestinations.StoreFullView.createRoute(
                            isFrame,
                        ),
                    )
                },
                navigateToSearch = {
                    navController.navigate(ScreenDestinations.Search.route)
                },
                navigateToAssetSale = {
                    navController.navigate(ScreenDestinations.AssetSale.route)
                },
                navigateToFrameSale = {
                    navController.navigate(ScreenDestinations.FrameSale.route)
                },
                navigateToFrameDetail = { marketId ->
                    navController.navigate(ScreenDestinations.FrameDetail.createRoute(marketId))
                },
                navigateToAssetDetail = { assetSellId ->
                    navController.navigate(ScreenDestinations.AssetDetail.createRoute(assetSellId))
                },
            )
        }

        composable(
            route = ScreenDestinations.Search.route,
        ) {
            SearchRoute(
                navigateToProfile = { userId ->
                    navController.navigate(ScreenDestinations.MyPage.createRoute(userId))
                },
                popUpBackStack = navController::popBackStack,
            )
        }

        composable(
            route = ScreenDestinations.Auction.route,
        ) {
            AuctionScreen(
                modifier = modifier,
                navigateToAuctionProcess = { auctionId ->
                    navController.navigate(
                        ScreenDestinations.AuctionProcess.createRoute(
                            auctionId,
                        ),
                    )
                },
            )
        }

        composable(
            route = ScreenDestinations.Wallet.route,
        ) {
            WalletScreen(modifier = modifier)
        }

        composable(
            route = ScreenDestinations.MyPage.route,
            arguments = ScreenDestinations.MyPage.arguments,
        ) { navBackStackEntry ->
            val userId = navBackStackEntry.arguments?.getLong("userId")

            MyPageRoute(
                modifier = modifier,
                navigateToDetail = { imgUrl ->
                    navController.navigate(
                        ScreenDestinations.Detail.createRoute(
                            imgUrl,
                        ),
                    )
                },
                navigateToFollowList = { nickname, followerList, followingList ->
                    navController.navigate(
                        ScreenDestinations.FollowList.createRoute(
                            nickname = nickname,
                            followerList = followerList,
                            followingList = followingList,
                        ),
                    )
                },
                navigateToSetting = {
                    navController.navigate(ScreenDestinations.Setting.route)
                },
                onErrorSnackBar = onShowErrorSnackBar,
                userId = userId ?: -1,
            )
        }

        composable(
            route = ScreenDestinations.Setting.route,
        ) {
            SettingRoute(
                modifier = modifier,
                popUpBackStack = navController::popBackStack,
                navigateToLogin = {
                    navController.navigate(ScreenDestinations.Login.route)
                },
                onShowSnackBar = onShowErrorSnackBar,
            )
        }

        composable(
            route = ScreenDestinations.FollowList.route,
            arguments = ScreenDestinations.FollowList.arguments,
        ) { navBackStackEntry ->
            val nickname = navBackStackEntry.arguments?.getString("nickname")
            val followerList =
                navBackStackEntry.arguments?.getParcelableArrayList<User>("followerList")
            val followingList =
                navBackStackEntry.arguments?.getParcelableArrayList<User>("followingList")

            FollowListScreen(
                modifier = modifier,
                nickname = nickname ?: "",
                popUpBackStack = navController::popBackStack,
                navigateToProfile = {
                    navController.navigate(ScreenDestinations.MyPage.createRoute(it))
                },
                followerList = followerList ?: emptyList(),
                followingList = followingList ?: emptyList(),
            )
        }

        composable(
            route = ScreenDestinations.ImageAsset.route,
        ) {
            ImageAssetScreen(
                modifier = modifier,
                popUpBackStack = navController::popBackStack,
                navigateToSelect = { selectedImg ->
                    navController.navigate(ScreenDestinations.Select.createRoute(selectedImg))
                },
            )
        }

        composable(
            route = ScreenDestinations.Select.route,
            arguments = ScreenDestinations.Select.arguments,
        ) { navBackStackEntry ->
            val imageUriString = navBackStackEntry.arguments?.getString("selectedImg")
            val imageUri = imageUriString?.toUriOrDefault()
            ImageSelectRoute(
                modifier = modifier,
                popUpBackStack = navController::popBackStack,
                navigateToDone = { assetUrl ->
                    navController.navigate(ScreenDestinations.AssetDone.createRoute(assetUrl)) {
                        popUpTo("imageAsset") { inclusive = true }
                    }
                },
            )
        }

        composable(
            route = ScreenDestinations.AssetDone.route,
            arguments = ScreenDestinations.AssetDone.arguments,
        ) {
            AssetDoneRoute(
                modifier = modifier,
                popUpBackStack = navController::popBackStack,
                navigateToMy = {
                    navController.navigate(ScreenDestinations.MyPage.createRoute(it))
                },
            )
        }

        composable(
            route = ScreenDestinations.AiAsset.route,
        ) {
            AiAssetScreen(
                modifier = modifier,
                navigateToDraw = {
                    navController.navigate(ScreenDestinations.DrawAsset.route)
                },
                navigateToPrompt = {
                    navController.navigate(ScreenDestinations.PromptAsset.route)
                },
            )
        }

        composable(
            route = ScreenDestinations.DrawAsset.route,
        ) {
            DrawAssetScreen(
                modifier = modifier,
                navigateToDone = { assetUrl ->
                    navController.navigate(ScreenDestinations.AssetDone.createRoute(assetUrl))
                },
            )
        }

        composable(
            route = ScreenDestinations.PromptAsset.route,
        ) {
            PromptAssetScreen(
                modifier = modifier,
                navigateToDone = { assetUrl ->
                    navController.navigate(ScreenDestinations.AssetDone.createRoute(assetUrl))
                },
            )
        }

        composable(
            route = ScreenDestinations.Detail.route,
        ) {
            DetailScreen(
                modifier = modifier,
                imgUrl = it.arguments?.getString("imgUrl"),
            )
        }

        MomentGraph(
            modifier = modifier,
            navController = navController,
            graphRoute = "frame_graph",
            onErrorSnackBar = onShowErrorSnackBar,
        )

        composable(
            route = ScreenDestinations.AuctionProcess.route,
            arguments = ScreenDestinations.AuctionProcess.arguments,
        ) {
            AuctionProcessScreen(
                modifier = modifier,
                auctionId = it.arguments?.getString("auctionId") ?: "",
            )
        }

        composable(
            route = ScreenDestinations.StoreFullView.route,
            arguments = ScreenDestinations.StoreFullView.arguments,
        ) { navBackStackEntry ->
            StoreFullViewScreen(
                modifier = modifier,
                isFrame = navBackStackEntry.arguments?.getBoolean("isFrame") ?: false,
            )
        }

        composable(
            route = ScreenDestinations.FrameSale.route,
        ) {
            FrameSaleRoute(
                modifier = modifier,
                popUpBackStack = navController::popBackStack,
                onShowSnackBar = onShowErrorSnackBar,
            )
        }

        PictureGraph(
            modifier = modifier,
            navController = navController,
            onErrorSnackBar = onShowErrorSnackBar,
            graphRoute = "picture_graph",
        )

        composable(
            route = ScreenDestinations.AssetSale.route,
        ) {
            AssetSaleRoute(
                modifier = modifier,
                navigateToStore = {
                    navController.navigate(ScreenDestinations.Shop.route)
                },
                onShowSnackBar = onShowErrorSnackBar,
            )
        }

        composable(
            route = ScreenDestinations.FrameDetail.route,
            arguments = ScreenDestinations.FrameDetail.arguments,
        ) {
            FrameDetailRoute(
                modifier = modifier,
                popUpBackStack = navController::popBackStack,
                onShowSnackBar = onShowErrorSnackBar,
            )
        }

        composable(
            route = ScreenDestinations.AssetDetail.route,
            arguments = ScreenDestinations.AssetDetail.arguments,
        ) {
            AssetDetailRoute(
                modifier = modifier,
                popUpBackStack = navController::popBackStack,
                onShowSnackBar = onShowErrorSnackBar,
            )
        }
    }
}
