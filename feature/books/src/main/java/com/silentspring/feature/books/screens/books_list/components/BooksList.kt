package com.silentspring.feature.books.screens.books_list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.silentspring.feature.books.R
import com.silentspring.feature.books.items.FolderItem
import com.silentspring.feature.books.screens.books_list.state.BooksState
import com.silentspring.uikit.components.LiterallyTypography
import com.silentspring.uikit.components.SearchTextField
import com.silentspring.uikit.components.primaryBlack
import com.silentspring.uikit.components.primaryWhite
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.LocalHazeStyle
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import com.silentspring.uikit.R as UiKit


@Composable
fun BooksList(
    state: BooksState,
    onStateChanged: (BooksState) -> Unit
) {
    val hazeState = rememberHazeState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyVerticalGrid(
            modifier = Modifier.hazeSource(hazeState),
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 80.dp, horizontal = 20.dp)
        ) {
            items(state.items, key = { it.title }) { folder ->
                FolderItem(item = folder)
            }
        }
        Box(
            modifier = Modifier
                .height(100.dp)
                .fillMaxWidth()
                .hazeEffect(
                    state = hazeState,
                    style = LocalHazeStyle.current.copy(blurRadius = 5.dp)
                ) {
                    progressive =
                        HazeProgressive.verticalGradient(startIntensity = 1f, endIntensity = 0f)
                }
        )
        Header(state.currentFolder)
        Box(
            modifier = Modifier
                .align(alignment = Alignment.BottomCenter)
                .height(160.dp)
                .fillMaxWidth()
                .hazeEffect(
                    state = hazeState,
                    style = LocalHazeStyle.current.copy(blurRadius = 5.dp)
                ) {
                    progressive =
                        HazeProgressive.verticalGradient(startIntensity = 0f, endIntensity = 1f)
                }
        )
        Box(
            modifier = Modifier
                .height(160.dp)
                .fillMaxWidth()
                .align(alignment = Alignment.BottomCenter)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(primaryBlack.copy(0f), primaryBlack)
                    )
                )
        )
        SearchTextField(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .align(alignment = Alignment.BottomCenter),
            text = state.searchQuery,
            labelText = stringResource(R.string.search_hint),
            trailingIcon = {
                IconButton(onClick = { }) {
                    Icon(
                        painter = painterResource(UiKit.drawable.icon_search),
                        tint = primaryWhite,
                        contentDescription = null
                    )
                }
            },
            onValueChange = {
                onStateChanged(state.copy(searchQuery = it))
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
        )
    }
}

@Composable
fun FolderItem(item: FolderItem) {

    Box(
        modifier = Modifier
            .height(115.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF131313))
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Text(
                text = item.title,
                style = LiterallyTypography.titleMedium.copy(fontSize = 14.sp)
            )
            Text(
                modifier = Modifier.padding(top = 5.dp),
                text = "${item.filesCount} files",
                style = LiterallyTypography.bodySmall.copy(
                    fontSize = 11.sp,
                    color = Color(0xFFA9A9A9)
                )
            )
        }
    }
}


@Composable
fun Header(currentFolder: String) {
    Box(
        modifier = Modifier
            .height(160.dp)
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(primaryBlack, primaryBlack.copy(0f))
                )
            )
    ) {
        Column(
            modifier = Modifier.align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.library).uppercase(),
                style = LiterallyTypography.titleLarge
            )
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = stringResource(R.string.storage_folder, currentFolder),
                style = LiterallyTypography.titleSmall
            )
        }
        Icon(
            modifier = Modifier
                .padding(start = 20.dp, top = 16.dp)
                .align(Alignment.TopStart)
                .size(16.dp)
                .clickable {},
            painter = painterResource(UiKit.drawable.icon_back),
            tint = primaryWhite,
            contentDescription = null
        )
        Icon(
            modifier = Modifier
                .padding(end = 20.dp, top = 16.dp)
                .align(Alignment.TopEnd)
                .size(16.dp)
                .clickable {},
            painter = painterResource(UiKit.drawable.icon_filter),
            tint = primaryWhite,
            contentDescription = null
        )
    }
}