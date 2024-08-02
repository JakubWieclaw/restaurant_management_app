package com.example.restaurantmanagementapp.MealScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.restaurantmanagementapp.TestData
import com.example.restaurantmanagementapp.classes.Opinion
import kotlinx.coroutines.delay


@Preview
@Composable
fun ReviewListPrev() {
    ReviewList(reviews = TestData.opinionsListSample)
}

@Composable
fun StarRating(rating: Int) {
    Row {
        for (i in 1..5) {
            // TODO: być może zmienić ilość gwiazdek na 10
            val starColor = if (i <= rating) {
                Color.Yellow
            } else {
                Color.LightGray
            }
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = starColor
            )
        }
    }
}

@Composable
fun ReviewCard(review: Opinion) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .border(width = 2.dp, color = Color.Black, shape = RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .padding(16.dp)

    ) {
        Row() {
            StarRating(rating = review.stars)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = review.customerName,
                fontSize = 20.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = review.opinionBody, fontSize = 16.sp)
    }
}

@Composable
fun ReviewList(reviews: List<Opinion>) {
    LazyColumn {
        items(reviews.size) { index ->
            var visible by remember{ mutableStateOf(false)}

            LaunchedEffect(Unit) {
                delay(index *100L)
                visible=true
            }

            if(index<8){
                AnimatedVisibility(
                    visible = visible,
                    enter = expandVertically(animationSpec = tween(200)),
                    exit = shrinkVertically(animationSpec = tween(200))
                ) {
                    ReviewCard(review = reviews[index])
                }
            }else{
                ReviewCard(review = reviews[index])
            }
        }
    }
}

