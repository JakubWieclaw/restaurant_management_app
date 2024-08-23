package com.example.restaurantmanagementapp.MealListScreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.restaurantmanagementapp.classes.Meal
import com.example.restaurantmanagementapp.classes.OrderViewModel
import com.example.restaurantmanagementapp.ui.theme.RestaurantManagementAppTheme
import kotlinx.coroutines.launch

@Preview(
    showBackground = true
)
@Composable
fun MealListPreview() {
    RestaurantManagementAppTheme {
        //MealList(TestData.mealListSample, listOf("Cat0", "Cat1", "Cat2", "Cat3", "Cat4", "Cat5"))
    }
}
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MealList(meals: List<Meal>, categories: List<String>, navController: NavController, orderViewModel: OrderViewModel) {

    val pagerState = rememberPagerState(
        initialPage = 1,
        initialPageOffsetFraction = 0.0f,
        pageCount = { categories.size })
    val coroutineScope = rememberCoroutineScope()
    var searchText by remember { mutableStateOf("") }
    val footerHeight = 70.dp

    Box(modifier = Modifier.fillMaxWidth()){

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(bottom = footerHeight)) {
            Header(navController = navController)
            SearchBar(searchText, onSearchTextChange = {searchText = it})

            // Tabs
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                modifier = Modifier.padding(top = 10.dp)
            ) {
                categories.forEachIndexed { index, name ->
                    Tab(selected = pagerState.currentPage == index, onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                            searchText = ""
                        }
                    }, text = { Text(text = name) })
                }
            }
            //Meal list
            HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { index ->
                val filteredMeals = meals.filter {
                    if (searchText == "") {
                        it.categoryID == index
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
                            text = "Brak dań w tej kategorii lub w wynikach wyszukiwania",
                            fontSize = 18.sp,
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
        Footer(orderViewModel, modifier = Modifier.align(Alignment.BottomCenter).height(footerHeight),navController)
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
            text = "App name", fontSize = 24.sp, modifier = Modifier
                .padding(start = 10.dp)
                .align(Alignment.CenterVertically)
                .weight(0.60f)
        )
        Row(
            horizontalArrangement = Arrangement.Center, modifier = Modifier
                .padding(end = 10.dp)
                .align(Alignment.CenterVertically)
                .weight(0.40f)
        ) {
            Button(onClick = {navController.navigate("loginscreen")}) {
                Icon(
                    imageVector = Icons.Default.AccountBox,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(24.dp)
                        .align(Alignment.CenterVertically),
                    tint = Color.Black
                )
                Text("Zaloguj", fontSize = 20.sp)
            }
        }
    }
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
        label = { Text("Wyszukaj danie") },
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