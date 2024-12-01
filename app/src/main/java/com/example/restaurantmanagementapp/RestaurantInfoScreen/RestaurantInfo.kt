package com.example.restaurantmanagementapp.RestaurantInfoScreen

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.restaurantmanagementapp.HomeScreen.CustomBackground
import com.example.restaurantmanagementapp.R
import com.example.restaurantmanagementapp.TestData
import com.example.restaurantmanagementapp.ui.theme.Typography
import com.example.restaurantmanagementapp.viewmodels.CouponsViewModel

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
fun RestaurantInfo(images: List<Int>, couponsViewModel: CouponsViewModel, navController: NavController){
    val colScrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .verticalScroll(colScrollState)
            .padding(10.dp))
    {
        //TypographyPreview()
        Text(text =stringResource(id = R.string.restaurant_name), style = Typography.titleLarge)
        Divider(
            color = Color.Gray,
            modifier = Modifier.padding(vertical = 8.dp).padding(horizontal = 12.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = stringResource(id = R.string.about_us), style = Typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = TestData.restaurantDescription, style = Typography.bodyLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = stringResource(id = R.string.gallery), style = Typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        ImageCarousel(images = images, imageSize = 300.dp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = stringResource(id = R.string.today_coupons), style = Typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        CouponCarousel(couponsViewModel,imageSize =300.dp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = stringResource(id = R.string.localization), style = Typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Image(
            painter = painterResource(id = images[0]),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
        )
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
                    .size(width = imageSize, height = imageSize.times(1.2f))
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
                        .size(width = imageSize, height = imageSize.times(1.2f))
                        .clickable {
                            couponsViewModel.selectCoupon(coupon.code)
                            Toast
                                .makeText(context, "Skopiowano kod do koszyka!", Toast.LENGTH_LONG)
                                .show()
                        }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.test_meal_picture_1),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Column(
                        modifier = Modifier
                            .background(
                                Color.White,
                                shape = RoundedCornerShape(bottomEnd = 20.dp)
                            )
                            .align(Alignment.TopStart)
                    ) {
                        Text(
                            text = coupon.meal.name,
                            style = Typography.headlineMedium,
                            modifier = Modifier.padding(6.dp)
                        )
                        Text(
                            text = stringResource(id = R.string.code) + coupon.code,
                            style = Typography.headlineMedium,
                            modifier = Modifier.padding(6.dp)
                        )
                    }
                    Column(
                        modifier = Modifier
                            .background(
                                Color.White,
                                shape = RoundedCornerShape(topStart = 20.dp)
                            )
                            .align(Alignment.BottomEnd)
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


@Composable
fun TypographyPreview() {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = "Display Large", style = Typography.displayLarge)
                Text(text = "Display Medium", style = Typography.displayMedium)
                Text(text = "Display Small", style = Typography.displaySmall)

                Text(text = "Headline Large", style = Typography.headlineLarge)
                Text(text = "Headline Medium", style = Typography.headlineMedium)
                Text(text = "Headline Small", style = Typography.headlineSmall)

                Text(text = "Title Large", style = Typography.titleLarge)
                Text(text = "Title Medium", style = Typography.titleMedium)
                Text(text = "Title Small", style = Typography.titleSmall)

                Text(text = "Body Large", style = Typography.bodyLarge)
                Text(text = "Body Medium", style = Typography.bodyMedium)
                Text(text = "Body Small", style = Typography.bodySmall)

                Text(text = "Label Large", style = Typography.labelLarge)
                Text(text = "Label Medium", style = Typography.labelMedium)
                Text(text = "Label Small", style = Typography.labelSmall)
            }
}