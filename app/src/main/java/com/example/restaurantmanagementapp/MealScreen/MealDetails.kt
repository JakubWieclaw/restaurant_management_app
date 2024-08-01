package com.example.restaurantmanagementapp.MealScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.restaurantmanagementapp.R
import com.example.restaurantmanagementapp.TestData
import com.example.restaurantmanagementapp.classes.Meal
import kotlin.math.round

@Preview(showBackground = true)
@Composable
fun MealScreenPreview() {
    MealScreen(TestData.mealListSample[0])
}

@Composable
fun MealScreen(meal: Meal) {
    var showReviews by remember { mutableStateOf(false) }
    var isPictureVisible by remember { mutableStateOf(true) }

    val pictureShape = RoundedCornerShape(
        topStart = 0.dp,
        topEnd = 0.dp,
        bottomStart = 26.dp,
        bottomEnd = 26.dp
    )
    val elementsModifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)


    Scaffold(
        bottomBar = {
            MealScreenFooter(meal)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedVisibility(
                visible = isPictureVisible,
                //enter = scaleIn(initialScale = 0.3f, animationSpec = tween(durationMillis = 300)) + fadeIn(animationSpec = tween(durationMillis = 300)),
                enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
                exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.test_meal_picture_1),
                    contentDescription = "Picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(pictureShape)
                )
            }


            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = elementsModifier,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = meal.name, fontSize = 20.sp)
                Text(text = meal.price.toString(), fontSize = 20.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Meal description meal description meal description",
                modifier = elementsModifier
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Allergens and caloric value", modifier = elementsModifier)

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    showReviews = !showReviews
                    isPictureVisible = !isPictureVisible
                },
                modifier = elementsModifier
            ) {
                Text(text = if (showReviews) "Hide opinions" else "Show opinions")
            }

            if (showReviews) {
                ReviewList(reviews = TestData.opinionsListSample)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun MealScreenFooter(meal: Meal) {
    var quantity by remember { mutableIntStateOf(1) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            //.height(200.dp)
            .background(Color.Gray.copy(alpha = 0.1f))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(all = 12.dp)
        ) {
            OutlinedButton(
                onClick = { if (quantity > 1) quantity-- },
                border = BorderStroke(1.dp, Color.Red),
                //shape = RoundedCornerShape(50), // = 50% percent
                shape = CircleShape,
                modifier = Modifier.size(50.dp)
            ) {
                Text(
                    text = "-",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }

            BasicTextField(
                value = TextFieldValue(quantity.toString()),
                onValueChange = { newValue ->
                    newValue.text.toIntOrNull()?.let {
                        quantity = if (it < 1) 1 else if (it > 999) 999 else it
                    }
                },
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 50.sp
                ),
                modifier = Modifier
                    .width(60.dp)
                    .height(50.dp)
                    .background(
                        color = Color.LightGray,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(8.dp)
            )

            OutlinedButton(
                onClick = { if (quantity < 999) quantity++ },
                border = BorderStroke(1.dp, Color.Red),
                //shape = RoundedCornerShape(50), // = 50% percent
                shape = CircleShape,
                modifier = Modifier.size(50.dp)
            ) {
                Text(
                    text = "+",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }

        Button(
            onClick = { /* Handle add to order */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
                .align(Alignment.End)
                .height(60.dp)
        ) {
            Text(text = "Add to order (Total: \$${round(quantity * meal.price * 100) / 100})") // Example total price calculation
        }
    }
}
