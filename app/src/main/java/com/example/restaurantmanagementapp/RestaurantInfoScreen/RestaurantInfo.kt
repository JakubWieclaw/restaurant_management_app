package com.example.restaurantmanagementapp.RestaurantInfoScreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.restaurantmanagementapp.R

@Preview
@Composable
fun RestaurantInfo2Preview(){
    RestaurantInfo2(listOf(
        R.drawable.test_meal_picture_1,
        R.drawable.test_meal_picture_1,
        R.drawable.test_meal_picture_1,
        R.drawable.test_meal_picture_1,
        R.drawable.test_meal_picture_1
        ))
}

@Composable
fun RestaurantInfo2(images: List<Int>){
    val colScrollState = rememberScrollState()
    val rowScrollState = rememberScrollState()

    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = Color.White)){
        Image(
            painter = painterResource(id = R.drawable.test_meal_picture_1),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.2f)
        )

        Column(modifier = Modifier.verticalScroll(colScrollState)){
            Text("Restaurant name")
            Text("Who we are?")
            Text("blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah ")
            Text("Gallery")
            ImageCarousel(images = images, imageSize = 300.dp)
            Text("localization")
            Image(
                painter = painterResource(id = images[0]),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            )
        }

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageCarousel(images: List<Int>, imageSize: Dp) {
    val listState = rememberLazyListState(0)
    LazyRow(
        state = listState,
        contentPadding = PaddingValues(horizontal = (imageSize / 5)),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        flingBehavior = rememberSnapFlingBehavior(listState)
    ) {
        items(images) { imageRes ->
            Box(
                modifier = Modifier
                    .size(imageSize)
            ) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}