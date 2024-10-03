package com.semonemo.presentation.navigation

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.semonemo.domain.model.User

/*
 추가 하는 방법
    data object sample : ScreenDestination(route = "화면이름"){
        // 넘기는 인자 있어야 하는 경우
        val arguments = listOf(
            navArgument(name = "id") { type = NavType.StringType },
            navArgument(name = "number") { type = NavType.IntType },
        )
        fun createRoute(
            id : String,
            number : Int
        ) = this.route/id/$number
    }
 */

sealed class ScreenDestinations(
    open val route: String,
) {
    data object Login : ScreenDestinations(route = "login")

    data object Register : ScreenDestinations(route = "register") {
        override val route: String
            get() = "register/{walletAddress}"
        val arguments =
            listOf(
                navArgument(name = "walletAddress") { type = NavType.StringType },
            )

        fun createRoute(walletAddress: String) = "register/$walletAddress"
    }

    data object Shop : ScreenDestinations(route = "shop")

    data object Auction : ScreenDestinations(route = "auction")

    data object Moment : ScreenDestinations(route = "moment")

    data object Wallet : ScreenDestinations(route = "wallet")

    data object MyPage : ScreenDestinations(route = "mypage") {
        override val route: String
            get() = "mypage/{userId}"
        val arguments =
            listOf(
                navArgument(name = "userId") { type = NavType.LongType },
            )

        fun createRoute(userId: Long) = "mypage/$userId"
    }

    data object Setting : ScreenDestinations(route = "setting")

    data object FollowList : ScreenDestinations(route = "followList") {
        override val route: String
            get() = "followList/{nickname}/{followerList}/{followingList}"
        val arguments =
            listOf(
                navArgument(name = "nickname") { type = NavType.StringType },
                navArgument(name = "followerList") { type = UserListNavType() },
                navArgument(name = "followingList") { type = UserListNavType() },
            )

        fun createRoute(
            nickname: String,
            followerList: List<User>,
            followingList: List<User>,
        ) = "followList/$nickname/${
            Uri.encode(Gson().toJson(followerList))
        }/${Uri.encode(Gson().toJson(followingList))}"
    }

    data object AiAsset : ScreenDestinations(route = "aiAsset")

    data object ImageAsset : ScreenDestinations(route = "imageAsset")

    data object Select : ScreenDestinations(route = "select") {
        override val route: String
            get() = "select/{selectedImg}"
        val arguments =
            listOf(
                navArgument(name = "selectedImg") { type = NavType.StringType },
            )

        fun createRoute(selectedImg: String) = "select/$selectedImg"
    }

    data object AssetDone : ScreenDestinations(route = "assetDone") {
        override val route: String
            get() = "assetDone/{assetUrl}"
        val arguments =
            listOf(
                navArgument(name = "assetUrl") { type = NavType.StringType },
            )

        fun createRoute(assetUrl: String) = "assetDone/$assetUrl"
    }

    data object PictureMain : ScreenDestinations(route = "pictureMain")

    data object DrawAsset : ScreenDestinations(route = "drawAsset")

    data object PromptAsset : ScreenDestinations(route = "promptAsset")

    data object Detail : ScreenDestinations(route = "detail") {
        override val route: String
            get() = "detail/{imgUrl}"
        val arguments =
            listOf(
                navArgument(name = "imgUrl") { type = NavType.StringType },
            )

        fun createRoute(imgUrl: String) = "detail/$imgUrl"
    }

    data object Frame : ScreenDestinations(route = "frame")

    data object AuctionProcess : ScreenDestinations(route = "auctionProcess") {
        override val route: String
            get() = "auctionProcess/{auctionId}"
        val arguments =
            listOf(
                navArgument("auctionId") { type = NavType.StringType },
            )

        fun createRoute(auctionId: String) = "auctionProcess/$auctionId"
    }

    data object StoreFullView : ScreenDestinations(route = "storeFullView") {
        override val route: String
            get() = "storeFullView/{isFrame}"
        val arguments =
            listOf(
                navArgument(name = "isFrame") { type = NavType.BoolType },
            )

        fun createRoute(isFrame: Boolean) = "storeFullView/$isFrame"
    }

    data object FrameDone : ScreenDestinations(route = "frameDone")

    data object Search : ScreenDestinations(route = "search")

    data object Camera : ScreenDestinations(route = "camera") {
        override val route: String
            get() = "camera/{amount}"
        val arguments =
            listOf(
                navArgument(name = "amount") { type = NavType.IntType },
            )

        fun createRoute(amount: Int) = "camera/$amount"
    }

    data object PictureSelect : ScreenDestinations(route = "pictureSelect")
}

// 팔로워 / 팔로잉 목록 전달 위한 NavType 정의
class UserListNavType : NavType<List<User>>(isNullableAllowed = false) {
    override fun get(
        bundle: Bundle,
        key: String,
    ): List<User>? = bundle.getParcelableArrayList(key)

    override fun parseValue(value: String): List<User> {
        val listType = object : TypeToken<List<User>>() {}.type
        return Gson().fromJson(value, listType)
    }

    override fun put(
        bundle: Bundle,
        key: String,
        value: List<User>,
    ) {
        bundle.putParcelableArrayList(key, ArrayList(value))
    }
}
