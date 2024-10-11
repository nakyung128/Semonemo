package com.semonemo.presentation.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.semonemo.domain.model.myFrame.MyFrame
import com.semonemo.presentation.theme.Gray02
import com.semonemo.presentation.theme.GunMetal
import com.semonemo.presentation.theme.Typography
import kotlinx.collections.immutable.toPersistentList

/**
 * CustomTab component
 *
 * @param modifier
 * @param tabList : 탭 목록 작성
 * @param selectedIndex : 선택된 탭 index
 * @param onTabSelected : 탭 선택 시 동작
 */

@Composable
fun CustomTab(
    modifier: Modifier = Modifier,
    tabList: List<String> = listOf(),
    frameList: List<MyFrame> = listOf(),
    selectedIndex: Int = 0,
    onTabSelected: (Int) -> Unit,
) {
    val tabItems = tabList.toPersistentList()

    TabRow(
        selectedTabIndex = selectedIndex,
        containerColor = Color.Unspecified,
        contentColor = GunMetal,
        indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
                modifier = modifier.tabIndicatorOffset(tabPositions[selectedIndex]),
                height = 2.dp,
                color = GunMetal,
            )
        },
    ) {
        tabItems.forEachIndexed { index, title ->
            Tab(
                selected = selectedIndex == index,
                onClick = { onTabSelected(index) },
                selectedContentColor = GunMetal,
                unselectedContentColor = Gray02,
            ) {
                Text(
                    modifier = Modifier.padding(12.dp),
                    text = title,
                    style =
                        if (selectedIndex == index) {
                            Typography.bodySmall.copy(fontSize = 13.sp)
                        } else {
                            Typography.labelMedium.copy(
                                fontSize = 13.sp,
                            )
                        },
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CustomTabPreview() {
    CustomTab(
        tabList = listOf("내 프레임", "에셋"),
        selectedIndex = 0,
        onTabSelected = { },
    )
}
