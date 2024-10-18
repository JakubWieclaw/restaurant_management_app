package com.example.restaurantmanagementapp.RestaurantInfoScreen

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.restaurantmanagementapp.HomeScreen.navigateToScreen
import com.example.restaurantmanagementapp.R
import com.example.restaurantmanagementapp.TestData
import com.example.restaurantmanagementapp.classes.CouponsViewModel

//@Preview
//@Composable
//fun RestaurantInfo2Preview(){
//    RestaurantInfo2(listOf(
//        R.drawable.test_meal_picture_1,
//        R.drawable.test_meal_picture_1,
//        R.drawable.test_meal_picture_1,
//        R.drawable.test_meal_picture_1,
//        R.drawable.test_meal_picture_1
//        ))
//}

@Composable
fun RestaurantInfo(images: List<Int>,couponsViewModel: CouponsViewModel ,navController: NavController){
    val colScrollState = rememberScrollState()

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

        Column(
            modifier = Modifier.verticalScroll(colScrollState).padding(10.dp))
        {
            Text("Restauracja: Słoneczne Smaki")
            Text("About us")
            Text(text = TestData.restaurantDescription)
            Text("Galeria")
            ImageCarousel(images = images, imageSize = 300.dp)
            Text("Dzisiejsze kupowny")
            CouponCarousel(couponsViewModel,imageSize =300.dp)
            Text("localization")
            Image(
                painter = painterResource(id = images[0]),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CouponCarousel(couponsViewModel: CouponsViewModel, imageSize: Dp) {
    val listState = rememberLazyListState(0)
    val context = LocalContext.current
    LazyRow(
        state = listState,
        contentPadding = PaddingValues(horizontal = (imageSize / 5)),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        flingBehavior = rememberSnapFlingBehavior(listState)
    ) {
        if(couponsViewModel.coupons!=null){
            items(couponsViewModel.coupons!!) { coupon ->
                Box(
                    modifier = Modifier
                        .size(imageSize).clickable {
                            couponsViewModel.selectCoupon(coupon.code)
                            Toast.makeText(context,"Skopiowano kod do koszyka!",Toast.LENGTH_LONG).show()
                        }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.test_meal_picture_1),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Column(
                        modifier = Modifier.background(
                            Color.White,
                            shape = RoundedCornerShape(bottomEnd = 20.dp)
                        ).align(Alignment.TopStart)
                    ) {
                        Text(
                            text = coupon.meal.name,
                            fontSize = 22.sp,
                            modifier = Modifier.padding(6.dp)
                        )
                        Text(
                            text = "KOD: " + coupon.code,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(6.dp)
                        )
                    }
                    Column(
                        modifier = Modifier.background(
                            Color.White,
                            shape = RoundedCornerShape(topStart = 20.dp)
                        ).align(Alignment.BottomEnd)
                    ) {
                        Text(
                            text = "-" + coupon.discountPercentage + "%",
                            fontSize = 24.sp,
                            modifier = Modifier.padding(6.dp)
                        )
                    }
                }
            }
        }
    }
}