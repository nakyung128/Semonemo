package com.semonemo.presentation.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.semonemo.presentation.R

sealed class BottomNavItem(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    @DrawableRes val iconSelected: Int,
    val route: String,
) {
    data object Shop : BottomNavItem(
        title = R.string.shop_title,
        icon = R.drawable.ic_bot_nav_outline_shop,
        iconSelected = R.drawable.ic_bot_nav_fill_shop,
        route = ScreenDestinations.Shop.route,
    )

    data object Auction : BottomNavItem(
        title = R.string.auction_title,
        icon = R.drawable.ic_bot_nav_outline_auction,
        iconSelected = R.drawable.ic_bot_nav_fill_auction,
        route = ScreenDestinations.Auction.route,
    )

    data object Wallet : BottomNavItem(
        title = R.string.wallet_title,
        icon = R.drawable.ic_bot_nav_outline_wallet,
        iconSelected = R.drawable.ic_bot_nav_fill_wallet,
        route = ScreenDestinations.Wallet.route,
    )

    data object MyPage : BottomNavItem(
        title = R.string.my_title,
        icon = R.drawable.ic_bot_nav_outline_profile,
        iconSelected = R.drawable.ic_bot_nav_fill_profile,
        route = ScreenDestinations.MyPage.createRoute(-1),
    )
}
