package com.example.foodapp

import LanguageChangeHelper
import android.app.Activity
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.NavHostController
import com.example.foodapp.tooltip.TooltipContent
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.delay

@Composable
fun HomeScreen (navController: NavHostController){
    val context = LocalContext.current

    val languageChangeHelper by lazy {
        LanguageChangeHelper()
    }

    val allLanguages = listOf(
        Language("en", "English", R.drawable.english),
        Language("ar", "Arabic", R.drawable.arabic),
        Language("fr", "French", R.drawable.france),
    )

    val currentLanguageCode: String = languageChangeHelper.getLanguageCode(context)

    var currentLanguage by remember { mutableStateOf(currentLanguageCode) }

    val onCurrentLanguageChange: (String) -> Unit = { newLanguage ->
        currentLanguage = newLanguage
        languageChangeHelper.changeLanguage(context, newLanguage)
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
                    .padding(top = 20.dp),
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
data class CategoryItem( val name: String,val imageRes: Int)

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
    TooltipBoxWithTriangle(tooltipText = "Cuisine: ${cuisine.name}") {
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
}

@Composable
fun CategoriesGridScreen(onCategoryClick: (CategoryItem) -> Unit) {
    val categories = remember {
        listOf(
            CategoryItem("Italian", R.drawable.italian),
            CategoryItem("Mexican", R.drawable.mexican),
            CategoryItem("Chinese", R.drawable.chinese),
            CategoryItem("Indian", R.drawable.indianfood),
            CategoryItem("Japanese", R.drawable.japenese),
            CategoryItem("Italian", R.drawable.italian),
            CategoryItem("Mexican", R.drawable.mexican),
            CategoryItem("Chinese", R.drawable.chinese),
            CategoryItem("Indian", R.drawable.indianfood),
            CategoryItem("Japanese", R.drawable.japenese),
            CategoryItem("Italian", R.drawable.italian),
            CategoryItem("Mexican", R.drawable.mexican),
            CategoryItem("Chinese", R.drawable.chinese),
            CategoryItem("Indian", R.drawable.indianfood),
            CategoryItem("Japanese", R.drawable.japenese),
            CategoryItem("Italian", R.drawable.italian),
            CategoryItem("Mexican", R.drawable.mexican),
            CategoryItem("Chinese", R.drawable.chinese),
            CategoryItem("Indian", R.drawable.indianfood),
            CategoryItem("Japanese", R.drawable.japenese),
            CategoryItem("Italian", R.drawable.italian),
            CategoryItem("Mexican", R.drawable.mexican),
            CategoryItem("Chinese", R.drawable.chinese),
            CategoryItem("Indian", R.drawable.indianfood),
            CategoryItem("Japanese", R.drawable.japenese),

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
fun TooltipBoxWithTriangle(
    tooltipText: String,
    modifier: Modifier = Modifier,
    showOnHover: Boolean = false,
    content: @Composable (Modifier) -> Unit
) {
    var isTooltipVisible by remember { mutableStateOf(false) }
    var tooltipPosition by remember { mutableStateOf(Offset(0f, 0f)) }
    var contentHeight by remember { mutableStateOf(0) }

    // Track the position of the content (image)
    val contentModifier = Modifier.onGloballyPositioned { coordinates ->
        val position = coordinates.positionInRoot()
        tooltipPosition = Offset(position.x + coordinates.size.width / 2f, position.y + coordinates.size.height)
        contentHeight = coordinates.size.height
    }
    LaunchedEffect(isTooltipVisible) {
        if (isTooltipVisible) {
            delay(1000)  // Tooltip stays visible for 3 seconds
            isTooltipVisible = false
        }
    }

    Box(
        modifier = modifier
            .padding(4.dp)
            .clickable(
                indication = null, // Disable ripple effect
                interactionSource = remember { MutableInteractionSource() }
            ) { isTooltipVisible = !isTooltipVisible }
    ) {
        content(contentModifier)

        // When the tooltip is visible, position it below the content (image)
        AnimatedVisibility(
            visible = isTooltipVisible,
            enter = slideInVertically(
                initialOffsetY = { it },  // Slide from below
                animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
            ),
            exit = slideOutVertically(
                targetOffsetY = { it },  // Slide back down
                animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
            )
        ) {
          Popup( properties = PopupProperties(
              focusable = false,
              dismissOnBackPress = true,
              dismissOnClickOutside = true
          )) {
              Box(
                  modifier = Modifier
                      .offset { IntOffset(tooltipPosition.x.toInt() - 50, (tooltipPosition.y + contentHeight).toInt())
                      }
                      .background(color = Color.Transparent)
              ) {
                  Column(horizontalAlignment = Alignment.CenterHorizontally) {
                      // Triangle shape to point towards the content (image)
                      Box(
                          modifier = Modifier
                              .align(Alignment.CenterHorizontally)
                              .padding(top = 4.dp)  // Smaller padding for triangle
                              .size(8.dp)  // Smaller triangle size
                              .background(
                                  color = Color.White,
                                  shape = TriangleShape()
                              )
                      )

                      // Tooltip text box
                      Row(
                          modifier = Modifier
                              .background(
                                  color = Color.White,
                                  shape = RoundedCornerShape(3.dp)
                              )
                              .padding(horizontal = 8.dp, vertical = 4.dp)  // Smaller padding
                      ) {
                          Text(
                              text = tooltipText,
                              modifier = Modifier
                                  .padding(horizontal = 8.dp, vertical = 4.dp),  // Smaller padding for text
                              textAlign = TextAlign.Center,
                              color = Color.Black,
                              style = TextStyle(fontSize = 12.sp)  // Smaller text size
                          )
                      }
                  }
              }
          }  // Tooltip container

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
                        onCurrentLanguageChange(item.code)
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
    TooltipBoxWithTriangle(
        tooltipText = "Category: ${category.name}",
        showOnHover = true
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .clickable { onCategoryClick(category) }
                .padding(4.dp)
        ) {
            Image(
                painter = painterResource(id = category.imageRes),
                contentDescription = "${category.name} Image",
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Text(
                text = category.name,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 8.dp),
                color = Color.Black
            )
        }
    }
}
