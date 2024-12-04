package com.example.restaurantmanagementapp.MealListScreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.restaurantmanagementapp.HomeScreen.navigateToScreen
import com.example.restaurantmanagementapp.R
import com.example.restaurantmanagementapp.viewmodels.AuthViewModel
import com.example.restaurantmanagementapp.viewmodels.CategoriesViewModel
import com.example.restaurantmanagementapp.apithings.schemasclasses.Meal
import com.example.restaurantmanagementapp.apithings.schemasclasses.Category
import com.example.restaurantmanagementapp.viewmodels.OrderViewModel
import com.example.restaurantmanagementapp.ui.theme.RestaurantManagementAppTheme
import com.example.restaurantmanagementapp.ui.theme.Typography
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MealList(
    meals: List<Meal>,
    navController: NavController,
    orderViewModel: OrderViewModel,
    authViewModel: AuthViewModel,
    categoriesViewModel: CategoriesViewModel
) {
    val categories:List<Category> ?= categoriesViewModel.categoriesState

    val pagerState = rememberPagerState(
        initialPage = 1,
        initialPageOffsetFraction = 0.0f,
        pageCount = { categoriesViewModel.categoriesState!!.size })
    val coroutineScope = rememberCoroutineScope()
    var searchText by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxWidth()){

        Column(modifier = Modifier
            .fillMaxSize()) {
            Header(navController = navController)

            SearchBar(searchText, onSearchTextChange = {searchText = it})

            Spacer(modifier = Modifier.height(10.dp))
            if(categories!=null) {
                for(i in 0..(categories.size/4)){
                    TabRow(
                        selectedTabIndex = if(pagerState.currentPage in 4*i..4*i+3) pagerState.currentPage%4 else 5
                    ) {
                        categories.forEachIndexed { index, category ->
                            if(index in 4*i..4*i+3){
                                Tab(selected = pagerState.currentPage == index, onClick = {
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(index)
                                        searchText = ""
                                    }
                                }, text = { Text(text = category.name, style = Typography.labelMedium, softWrap = false) })
                            }
                        }
                    }
                }
            }
            //Meal list
            HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { index ->
                val filteredMeals = meals.filter {
                    if (searchText == "") {
                        it.categoryId == (categories?.get(index)?.id ?: 0)
                    } else {
                        it.name.contains(searchText, ignoreCase = true)
                    }
                }

                if (filteredMeals.isEmpty()) {
                    // Wyświetlanie komunikatu, gdy brak dań
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = stringResource(id = R.string.no_meals_info) ,
                            style = Typography.labelLarge,
                            color = Color.Gray,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                } else {
                    // Wyświetlanie listy dań
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(filteredMeals) { meal ->
                            MealCard(
                                meal = meal,
                                onAddToOrder = {
                                    orderViewModel.addToOrder(meal)
                                               },
                                modifier = Modifier,
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun Header(navController: NavController){
    Row(
        modifier = Modifier
            .fillMaxWidth(1f)
            .padding(top = 10.dp)
    ) {
        Text(
            text = stringResource(id = R.string.meallist), style = Typography.titleLarge, modifier = Modifier
                .padding(start = 10.dp)
                .align(Alignment.CenterVertically)
                .weight(0.50f)
        )
        Row(
            horizontalArrangement = Arrangement.Center, modifier = Modifier
                .padding(end = 10.dp)
                .align(Alignment.CenterVertically)
                .weight(0.50f)
        ) {
            Button(onClick = {navigateToScreen("favourites",navController)}) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(24.dp)
                        .align(Alignment.CenterVertically),
                    tint = Color.Black
                )
                Text(text = stringResource(id = R.string.favourite), style = Typography.labelLarge)
            }
        }
    }
    Divider(
        color = Color.Gray,
        modifier = Modifier.padding(vertical = 8.dp).padding(horizontal = 12.dp)
    )
}

@Composable
fun SearchBar(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = searchText,
        onValueChange = onSearchTextChange, // Użycie lambda zamiast bezpośredniej zmiany wartości
        label = { Text(text = stringResource(id = R.string.search_meal), style = Typography.labelMedium) },
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search icon",
                tint = Color.Gray
            )
        },
        trailingIcon = {
            if (searchText.isNotEmpty()) {
                IconButton(onClick = { onSearchTextChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear text icon",
                        tint = Color.Gray
                    )
                }
            }
        },
        singleLine = true
    )
}


@Composable
fun OutlinedText(
    text: String,
    modifier: Modifier = Modifier,
    fillColor: Color = Color.Unspecified,
    outlineColor: Color,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalTextStyle.current,
    outlineDrawStyle: Stroke = Stroke(),
) {
    Box(modifier = modifier) {
        Text(
            text = text,
            modifier = modifier,
            //=modifier = Modifier.semantics { invisibleToUser() },
            color = outlineColor,
            fontSize = fontSize,
            fontStyle = fontStyle,
            fontWeight = fontWeight,
            fontFamily = fontFamily,
            letterSpacing = letterSpacing,
            textDecoration = null,
            textAlign = textAlign,
            lineHeight = lineHeight,
            overflow = overflow,
            softWrap = softWrap,
            maxLines = maxLines,
            minLines = minLines,
            onTextLayout = onTextLayout,
            style = style.copy(
                shadow = null,
                drawStyle = Stroke(width = 10f),
            ),
        )

        Text(
            text = text,
            color = fillColor,
            fontSize = fontSize,
            fontStyle = fontStyle,
            fontWeight = fontWeight,
            fontFamily = fontFamily,
            letterSpacing = letterSpacing,
            textDecoration = textDecoration,
            textAlign = textAlign,
            lineHeight = lineHeight,
            overflow = overflow,
            softWrap = softWrap,
            maxLines = maxLines,
            minLines = minLines,
            onTextLayout = onTextLayout,
            style = style,
        )
    }
}