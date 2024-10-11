
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.semonemo.presentation.navigation.BottomNavItem
import com.semonemo.presentation.navigation.ScreenDestinations
import com.semonemo.presentation.theme.Gray01
import com.semonemo.presentation.theme.GunMetal
import com.semonemo.presentation.theme.SemonemoTheme
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.theme.White
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun BottomNavigationBar(
    navController: NavController,
    currentRoute: String?,
    modifier: Modifier = Modifier,
) {
    val items =
        listOf(
            BottomNavItem.Shop,
            BottomNavItem.Auction,
            BottomNavItem.Wallet,
            BottomNavItem.MyPage,
        )

    BottomAppBar(
        modifier =
            modifier
                .fillMaxHeight(0.07f)
                .navigationBarsPadding()
                .shadow(
                    elevation = 20.dp,
                    shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp),
                ), // 그림자 적용
        containerColor = White,
    ) {
        items.forEachIndexed { index, item ->
            val isSelected =
                if (currentRoute == "mypage/{userId}") {
                    item.route == "mypage/-1"
                } else {
                    currentRoute == item.route
                }

            val icon = if (isSelected) item.iconSelected else item.icon

            if (index == 2) {
                Spacer(modifier = Modifier.weight(1f, true))
            }

            NavigationBarItem(
                interactionSource = NoRippleInteractionSource,
                colors =
                    NavigationBarItemDefaults.colors(
                        indicatorColor = Color.Transparent,
                        selectedIconColor = GunMetal,
                        unselectedIconColor = Gray01,
                        selectedTextColor = GunMetal,
                        unselectedTextColor = Gray01,
                    ),
                selected = isSelected,
                label = {
                    Text(
                        text = stringResource(id = item.title),
                        style = if (isSelected) Typography.bodySmall.copy(fontSize = 12.sp) else Typography.labelSmall,
                    )
                },
                onClick = {
                    if (item is BottomNavItem.MyPage) {
                        navController.navigate(ScreenDestinations.MyPage.createRoute(-1))
                    } else {
                        navController.navigate(item.route) {
                            navController.graph.startDestinationRoute?.let {
                                popUpTo(it) { saveState = true }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = stringResource(id = item.title),
                        tint = Color.Unspecified,
                    )
                },
            )
        }
    }
}

/**
 * 클릭시 알약 파장 효과를 제거.
 */
private object NoRippleInteractionSource : MutableInteractionSource {
    override val interactions: Flow<Interaction> = emptyFlow()

    override suspend fun emit(interaction: Interaction) {}

    override fun tryEmit(interaction: Interaction) = true
}

@Preview(showBackground = true)
@Composable
fun MyBottomNavigationPreview() {
    SemonemoTheme {
        val navController = rememberNavController()
        BottomNavigationBar(navController = navController, "moment")
    }
}
