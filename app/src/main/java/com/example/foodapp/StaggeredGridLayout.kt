package com.example.foodapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

data class Item(val name: String, val imageRes: Int)

@Composable
fun StaggeredGridScreenContent() {
    val randomSizedPhotos = listOf(
       Item("Italian", R.drawable.italian),
        Item("Mexican", R.drawable.mexican),
       Item("Chinese", R.drawable.chinese),
       Item("Indian", R.drawable.indianfood),
       Item("Japanese", R.drawable.japenese),
        Item("Italian", R.drawable.italian),
        Item("Mexican", R.drawable.mexican),
        Item("Chinese", R.drawable.chinese),
        Item("Indian", R.drawable.indianfood),
        Item("Japanese", R.drawable.japenese),
        Item("Italian", R.drawable.italian),
        Item("Mexican", R.drawable.mexican),
        Item("Chinese", R.drawable.chinese),
        Item("Indian", R.drawable.indianfood),
        Item("Japanese", R.drawable.japenese),
        Item("Italian", R.drawable.italian),
        Item("Mexican", R.drawable.mexican),
        Item("Chinese", R.drawable.chinese),
        Item("Indian", R.drawable.indianfood),
        Item("Japanese", R.drawable.japenese),
        Item("Italian", R.drawable.italian),
        Item("Mexican", R.drawable.mexican),
        Item("Chinese", R.drawable.chinese),
        Item("Indian", R.drawable.indianfood),
        Item("Japanese", R.drawable.japenese),
        Item("Italian", R.drawable.italian),
        Item("Mexican", R.drawable.mexican),
        Item("Chinese", R.drawable.chinese),
        Item("Indian", R.drawable.indianfood),
        Item("Japanese", R.drawable.japenese),
    )

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(3),
        verticalItemSpacing = 4.dp,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(randomSizedPhotos) {Item ->
            val isSpecialImage = Item.name == "Chinese" || Item.name == "Indian"
            Image(
                painter = painterResource(id = Item.imageRes),
                contentDescription = Item.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .apply {
                        if (isSpecialImage) {
                            height(16.dp) // Specify height for special images
                        } else {
                            wrapContentHeight() // Regular images
                        }
                    }
            )
        }
    }
}
