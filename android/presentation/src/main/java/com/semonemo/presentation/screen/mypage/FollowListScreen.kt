package com.semonemo.presentation.screen.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.semonemo.domain.model.User
import com.semonemo.presentation.component.CustomTab
import com.semonemo.presentation.component.NameWithBadge
import com.semonemo.presentation.component.TopAppBar
import com.semonemo.presentation.component.TopAppBarNavigationType
import com.semonemo.presentation.screen.search.UserListItem
import com.semonemo.presentation.theme.SemonemoTheme

@Composable
fun FollowListScreen(
    modifier: Modifier = Modifier,
    popUpBackStack: () -> Unit = {},
    navigateToProfile: (Long) -> Unit = {},
    nickname: String = "나갱",
    followerList: List<User> = emptyList(),
    followingList: List<User> = emptyList(),
) {
    val tabList = listOf("팔로워", "팔로잉")
    var selectedIndex by remember { mutableIntStateOf(0) }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding(),
        ) {
            Spacer(modifier = Modifier.height(15.dp))
            TopAppBar(
                modifier = Modifier,
                title = {
                    NameWithBadge(
                        name = nickname,
                        size = 15,
                    )
                },
                navigationType = TopAppBarNavigationType.Back,
                onNavigationClick = popUpBackStack,
            )
            Spacer(modifier = Modifier.height(15.dp))
            CustomTab(
                tabList = tabList,
                selectedIndex = selectedIndex,
                onTabSelected = { selectedIndex = it },
            )
            Spacer(modifier = Modifier.height(10.dp))
            when (selectedIndex) {
                0 -> {
                    LazyColumn(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .padding(horizontal = 15.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        items(followerList.size) { index ->
                            val follower = followerList[index]
                            UserListItem(
                                modifier = Modifier.fillMaxWidth(),
                                userId = follower.userId,
                                profileImgUrl = follower.profileImage,
                                nickname = follower.nickname,
                                navigateToProfile = {
                                    navigateToProfile(follower.userId)
                                },
                            )
                        }
                    }
                }

                1 -> {
                    LazyColumn(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .padding(horizontal = 15.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        items(followingList.size) { index ->
                            val following = followingList[index]
                            UserListItem(
                                modifier = Modifier.fillMaxWidth(),
                                userId = following.userId,
                                profileImgUrl = following.profileImage,
                                nickname = following.nickname,
                                navigateToProfile = {
                                    navigateToProfile(following.userId)
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun FollowListScreenPreview() {
    SemonemoTheme {
        FollowListScreen(
            modifier = Modifier,
            nickname = "나갱",
        )
    }
}
