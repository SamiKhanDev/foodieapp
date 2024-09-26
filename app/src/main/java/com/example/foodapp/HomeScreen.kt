package com.example.foodapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun HomeScreen (navController: NavHostController){

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item {
            Text(
                text = "Foodie",
                style = MaterialTheme.typography.headlineLarge,
            )
        }


        item {
            Image(
                painter = painterResource(id = R.drawable.image_0),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
        }

        item {
            Text(
                text = "Cuisines",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
            )
        }

        item {
            CuisineRow()
        }

        item {
            Text(
                text = "Categories",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding( top = 16.dp)
            )
        }

        item {
            CategoriesGridScreen { category ->
                navController.navigate("categoryDetail/Italian")
            }
        }
    }

}
data class CuisineItem(val name:String,val imagesRes:Int)
data class CategoryItem( val imageRes: Int)

@Composable
fun CuisineRow() {
    val cuisines = remember {
        listOf(
            CuisineItem("Italian", R.drawable.italian),
            CuisineItem("Mexican", R.drawable.mexican),
            CuisineItem("Chinese", R.drawable.chinese),
            CuisineItem("Indian", R.drawable.indianfood),
            CuisineItem("Japanese", R.drawable.japenese)
        )
    }

    LazyRow(
        modifier = Modifier.padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        items(cuisines) { cuisine ->
            CuisineCard(cuisine)
        }
    }
}

@Composable
fun CuisineCard(cuisine: CuisineItem) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(100.dp)
            .padding(4.dp)
    ) {
        Image(
            painter = painterResource(id = cuisine.imagesRes),
            contentDescription = "${cuisine.name} Image",
            modifier = Modifier
                .size(90.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Text(
            text = cuisine.name,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 8.dp),
            color = Color.Black
        )
    }
}

@Composable
fun CategoriesGridScreen(onCategoryClick: (CategoryItem) -> Unit) {
    val categories = remember {
        listOf(
            CategoryItem( R.drawable.image_1),
            CategoryItem( R.drawable.image_0),
            CategoryItem( R.drawable.image_1),
            CategoryItem( R.drawable.image_0),
            CategoryItem( R.drawable.image_1),
            CategoryItem( R.drawable.image_1),
            CategoryItem( R.drawable.image_0),
            CategoryItem( R.drawable.image_1),
            CategoryItem( R.drawable.image_0),
            CategoryItem( R.drawable.image_1),
            CategoryItem( R.drawable.image_1),
            CategoryItem( R.drawable.image_0),
            CategoryItem( R.drawable.image_1),
            CategoryItem( R.drawable.image_0),
            CategoryItem( R.drawable.image_1),
            CategoryItem( R.drawable.image_1),
            CategoryItem( R.drawable.image_0),
            CategoryItem( R.drawable.image_1),
            CategoryItem( R.drawable.image_0),
            CategoryItem( R.drawable.image_1),
            CategoryItem( R.drawable.image_1),
            CategoryItem( R.drawable.image_0),
            CategoryItem( R.drawable.image_1),
            CategoryItem( R.drawable.image_0),
            CategoryItem( R.drawable.image_1)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categories.chunked(3).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (category in rowItems) {
                    CategoryCard(category, onCategoryClick)
                }
            }
        }
    }

}

@Composable
fun CategoryCard(category: CategoryItem, onCategoryClick: (CategoryItem) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onCategoryClick(category) }
            .padding(4.dp)
    ) {
        Image(
            painter = painterResource(id = category.imageRes),
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )


    }
}









