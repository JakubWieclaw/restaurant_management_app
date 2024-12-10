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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
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
import com.example.restaurantmanagementapp.viewmodels.MealsViewModel
import com.example.restaurantmanagementapp.viewmodels.loadImageFromDevice

@Composable
fun RestaurantInfo(images: List<String>, couponsViewModel: CouponsViewModel, mealsViewModel: MealsViewModel,
                   navController: NavController){
    val colScrollState = rememberScrollState()
    val context = LocalContext.current
    val imageBitmap: ImageBitmap = loadImageFromDevice(context = context, filename = "lokalizacja.jpg")
        ?: ImageBitmap.imageResource(id = R.drawable.no_photo)

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
        CouponCarousel(couponsViewModel,mealsViewModel,imageSize =300.dp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = stringResource(id = R.string.localization), style = Typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Image(
            bitmap = imageBitmap,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageCarousel(images: List<String>, imageSize: Dp) {
    val listState = rememberLazyListState(0)
    val context = LocalContext.current
    LazyRow(
        state = listState,
        contentPadding = PaddingValues(horizontal = (imageSize / 5)),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        flingBehavior = rememberSnapFlingBehavior(listState)
    ) {
        items(images) { imageRes ->
            val imageBitmap: ImageBitmap = loadImageFromDevice(context = context, filename = imageRes)
                ?: ImageBitmap.imageResource(id = R.drawable.no_photo)
            Box(
                modifier = Modifier
                    .size(width = imageSize, height = imageSize.times(1.2f))
            ) {
                Image(
                    bitmap = imageBitmap,
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
fun CouponCarousel(couponsViewModel: CouponsViewModel,mealsViewModel: MealsViewModel, imageSize: Dp) {
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
                val imageName = mealsViewModel.findMeal(coupon.meal.id)?.photographUrl
                val imageBitmap: ImageBitmap = loadImageFromDevice(context = context, filename = imageName?: "")
                    ?: ImageBitmap.imageResource(id = R.drawable.no_photo)
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
                        bitmap = imageBitmap,
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