package com.semonemo.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.semonemo.presentation.R
import com.semonemo.presentation.theme.GunMetal
import com.semonemo.presentation.theme.SemonemoTheme
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.theme.White
import com.semonemo.presentation.theme.WhiteGray
import com.semonemo.presentation.util.noRippleClickable

@Composable
fun FilterButton(
    text: String,
    state: Boolean?, // 필터 상태: null, true, false
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // 필터 상태에 따른 색상 설정
    val backgroundColor =
        when (state) {
            null -> WhiteGray
            else -> GunMetal
        }

    val contentColor =
        when (state) {
            null -> GunMetal
            else -> White
        }

    // 화살표가 Y축으로 뒤집힘
    val flipScaleY by animateFloatAsState(
        targetValue = if (state == false) -1f else 1f,
        animationSpec = tween(durationMillis = 300),
        label = "",
    )

    Box(
        modifier =
            modifier
                .height(32.dp)
                .background(backgroundColor, shape = RoundedCornerShape(10.dp))
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .noRippleClickable { onClick() }
                .animateContentSize(),
        contentAlignment = Alignment.Center, // 요소들을 가운데 정렬
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = text,
                style = Typography.labelMedium.copy(fontSize = 13.sp),
                color = contentColor,
            )
            // 아이콘의 가시성을 애니메이션으로 처리
            AnimatedVisibility(
                visible = state != null,
                enter = expandHorizontally(),
                exit = shrinkHorizontally(),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_up),
                    contentDescription = null,
                    tint = contentColor,
                    modifier =
                        Modifier
                            .size(16.dp)
                            .scale(1f, flipScaleY),
                )
            }
        }
    }
}

@Composable
fun CustomStoreFilter(
    modifier: Modifier = Modifier,
    filters: List<StoreFilter>,
    filterStates: Map<StoreFilter, Boolean?>, // 필터 상태를 외부에서 관리
    onFilterChange: (StoreFilter, Boolean?) -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
    ) {
        filters.forEach { filter ->
            val state = filterStates[filter] // 현재 필터의 상태를 가져옴
            FilterButton(
                text = filter.displayName,
                state = state,
                onClick = {
                    // 클릭 시 해당 필터의 상태 변경 로직
                    val newState =
                        state?.let { !it } ?: false // Only toggle between true and false

                    // 상태 업데이트 - 하나만 활성화되도록, 나머지는 모두 false
                    onFilterChange(filter, newState)
                },
            )
        }
    }
}

enum class StoreFilter(
    val displayName: String,
) {
    DATE("날짜순"),
    PRICE("가격순"),
    LIKE("좋아요"),
    SALE("판매량"),
    VIEW("조회수"),
}

// 각 필터 그룹에 대한 상태 관리
val assetFilters =
    listOf(
        StoreFilter.DATE,
        StoreFilter.PRICE,
        StoreFilter.LIKE,
        StoreFilter.SALE,
        StoreFilter.VIEW,
    )

val frameFilters =
    listOf(
        StoreFilter.DATE,
        StoreFilter.PRICE,
        StoreFilter.LIKE,
    )

@Composable
@Preview
fun AssetFilterBarPreview() {
    var filterStates by remember {
        mutableStateOf(StoreFilter.entries.associateWith { null as Boolean? })
    }

    SemonemoTheme {
        CustomStoreFilter(
            filters = assetFilters, // 에셋 필터 사용
            filterStates = filterStates,
            onFilterChange = { selectedFilter, newState ->
                // 선택된 필터만 활성화하고 나머지는 null로
                filterStates =
                    filterStates.mapValues { (filter, _) ->
                        if (filter == selectedFilter) newState else null
                    }
            },
        )
    }
}

@Composable
@Preview
fun FrameFilterBarPreview() {
    var filterStates by remember {
        mutableStateOf(StoreFilter.entries.associateWith { null as Boolean? })
    }

    CustomStoreFilter(
        filters = frameFilters, // 프레임 필터 사용
        filterStates = filterStates,
        onFilterChange = { selectedFilter, newState ->
            // 선택된 필터만 활성화하고 나머지는 null로
            filterStates =
                filterStates.mapValues { (filter, _) ->
                    if (filter == selectedFilter) newState else null
                }
        },
    )
}
