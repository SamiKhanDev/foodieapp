package com.example.foodapp

import android.app.Activity
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import java.util.Locale

@Composable
fun HomeScreen (navController: NavHostController){



    val context = LocalContext.current
    val allLanguages = listOf(
        Language("en", "English", R.drawable.english),
        Language("ar", "arabic", R.drawable.arabic),
        Language("fr", "French", R.drawable.france)
    )

    var currentLanguage by remember { mutableStateOf(LocaleHelper.getPersistedData(context, Locale.getDefault().language)) }

    val onCurrentLanguageChange: (String) -> Unit = { newLanguage ->
        currentLanguage = newLanguage
        LocaleHelper.setLocale(context, newLanguage)
        (context as Activity).recreate()  // Recreate the activity to apply language change
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            LanguagesDropdown(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(top = 8.dp),
                languagesList = allLanguages,
                currentLanguage = currentLanguage,
                onCurrentLanguageChange = onCurrentLanguageChange
            )
        }

        item {
            Text(text = stringResource(id = R.string.cuisines), style = MaterialTheme.typography.headlineSmall)
        }

        item {
            CuisineRow()
        }

        item {
            Text(text = stringResource(id = R.string.categories), style = MaterialTheme.typography.headlineSmall)
        }

        item {
            CategoriesGridScreen { category ->
                navController.navigate("categoryDetail/Italian")
            }
        }
    }
}


data class CuisineItem(val name:String, val imagesRes: Int)
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
fun LanguagesDropdown(
    modifier: Modifier = Modifier,
    languagesList: List<Language>,
    currentLanguage: String,
    onCurrentLanguageChange: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedItem by remember {
        mutableStateOf(languagesList.firstOrNull { it.code == currentLanguage } ?: languagesList.first())
    }
    Box(
        modifier = modifier
            .padding(end = 16.dp)
            .wrapContentSize(Alignment.TopEnd)
    ) {
        Row(
            modifier = Modifier
                .height(24.dp)
                .clickable { expanded = !expanded }
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LanguageListItem(selectedItem)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            languagesList.forEach { item ->
                DropdownMenuItem(
                    text = { LanguageListItem(selectedItem = item) },
                    onClick = {
                        selectedItem = item
                        expanded = !expanded
                        onCurrentLanguageChange(item.code)  // Call language change function
                    }
                )
            }
        }
    }
}


data class Language(
    val code: String,
    val name: String,
    @DrawableRes val flag: Int
)

@Composable
fun LanguageListItem(selectedItem:Language) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape),
            painter = painterResource(selectedItem.flag),
            contentScale = ContentScale.Crop,
            contentDescription = selectedItem.code
        )

        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = selectedItem.name,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.W500,
                color = MaterialTheme.colorScheme.onBackground,
            )
        )
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









