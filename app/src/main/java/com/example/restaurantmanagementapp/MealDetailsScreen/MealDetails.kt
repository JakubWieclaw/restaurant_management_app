package com.example.restaurantmanagementapp.MealDetailsScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.restaurantmanagementapp.R
import com.example.restaurantmanagementapp.viewmodels.FavMealsViewModel
import com.example.restaurantmanagementapp.apithings.schemasclasses.Meal
import com.example.restaurantmanagementapp.ui.theme.Typography
import com.example.restaurantmanagementapp.viewmodels.AuthViewModel
import com.example.restaurantmanagementapp.viewmodels.OrderViewModel
import com.example.restaurantmanagementapp.viewmodels.loadImageFromDevice
import kotlin.math.round

@Composable
fun MealScreen(
    meal: Meal,
    navController: NavController,
    orderViewModel: OrderViewModel,
    authViewModel: AuthViewModel,
    favMealsViewModel: FavMealsViewModel
) {

    var showReviews by remember { mutableStateOf(false) }
    var isPictureVisible by remember { mutableStateOf(true) }
    val context = LocalContext.current
    val imageBitmap: ImageBitmap = loadImageFromDevice(context = context, filename = meal.photographUrl)
        ?: ImageBitmap.imageResource(id = R.drawable.no_photo)

    val pictureShape = RoundedCornerShape(
        topStart = 0.dp,
        topEnd = 0.dp,
        bottomStart = 26.dp,
        bottomEnd = 26.dp
    )
    val elementsModifier = Modifier
        .fillMaxWidth()
        .padding(start = 16.dp, end = 16.dp, top = 4.dp)


    Scaffold(
        bottomBar = {
            MealScreenFooter(orderViewModel,meal, navController = navController, favMealsViewModel = favMealsViewModel)
        },
        containerColor = Color.Transparent
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
                    bitmap = imageBitmap,
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
                Text(text = meal.name, style = Typography.titleMedium)
                Text(text = meal.price.toString(), style = Typography.titleMedium)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(id = R.string.ingredients) ,
                style = Typography.labelLarge,
                modifier = elementsModifier
            )
            ListElements(meal.ingredients,modifier = elementsModifier)

            Text(
                text = stringResource(id = R.string.allergens) ,
                style = Typography.labelLarge,
                modifier = elementsModifier
            )
            ListElements(names = meal.allergens, modifier = elementsModifier)

            Row(modifier = elementsModifier){
                Text(
                    text = meal.weightOrVolume.toString() + if(meal.unitType=="GRAMY") " g" else " ml",
                    style = Typography.labelLarge
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = meal.calories.toString() +" KCAL",
                    style = Typography.labelLarge
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Button(
                onClick = {
                    showReviews = !showReviews
                    isPictureVisible = !isPictureVisible
                },
                modifier = elementsModifier
            ) {
                Text(text = if (showReviews) stringResource(id = R.string.hide_opinions)  else stringResource(id = R.string.show_opinions), style = Typography.labelLarge)
            }

            if (showReviews) {
                ReviewList(reviews = meal.opinions)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun MealScreenFooter(orderViewModel: OrderViewModel, meal: Meal, navController: NavController, favMealsViewModel: FavMealsViewModel) {
    var quantity by remember { mutableIntStateOf(1) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            //.height(200.dp)
            .background(Color.Gray.copy(alpha = 0.1f))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(all = 10.dp)
                .fillMaxWidth()
        ){

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                //modifier = Modifier.padding(all = 12.dp)
            ) {
                IconButton(
                    onClick = { if (quantity > 1) quantity-- },
                ) {
                    Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = null,modifier = Modifier.size(60.dp).background(color = colorScheme.background, shape = RoundedCornerShape(50)),tint = colorScheme.primary)
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

                IconButton(
                    onClick = { if (quantity < 999) quantity++ },
                    //shape = RoundedCornerShape(50), // = 50% percent
                    //shape = CircleShape,
                ) {
                    Icon(imageVector = Icons.Default.KeyboardArrowUp, contentDescription = null,modifier = Modifier.size(60.dp).background(color = colorScheme.background, shape = RoundedCornerShape(50)),tint = colorScheme.primary)
                }
            }
            if(favMealsViewModel.findMeal(meal)){
                IconButton(onClick = {
                    favMealsViewModel.removeFromFav(meal)
                }) {
                    Icon(imageVector =  Icons.Default.Favorite ,contentDescription = null,modifier = Modifier.size(60.dp).background(color = Color.Black, shape = RoundedCornerShape(50)),tint = colorScheme.primary)
                }
            }else{
                IconButton(onClick = {
                    favMealsViewModel.addToFav(meal)
                }) {
                    Icon(imageVector =  Icons.Default.FavoriteBorder ,contentDescription = null,modifier = Modifier.size(60.dp).background(color = colorScheme.background, shape = RoundedCornerShape(50)),tint = colorScheme.primary)
                }
            }

        }

        Button(modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(start = 12.dp, end = 12.dp, bottom=10.dp),
                onClick = {for(i in 1..quantity){orderViewModel.addToOrder(meal)} },
            ) {
                Text(text = stringResource(id = R.string.add_to_order) +" ${round(quantity * meal.price * 100) / 100}"+ stringResource(id=R.string.currency)+") ", style = Typography.labelMedium)
            }


    }
}

@Composable
fun ListElements(names:List<String>, modifier:Modifier){
    Column(modifier = Modifier.then(modifier)){
        for(i in 1..<names.size){
            if(i%2==1){
                Row(horizontalArrangement = Arrangement.SpaceAround){
                    Row(modifier = Modifier.weight(0.5f)){
                        Spacer(modifier = Modifier.width(20.dp))
                        Text(text = "- ${names[i-1]}" ,style = Typography.labelLarge)
                    }
                    Row(modifier = Modifier.weight(0.5f)){
                        Spacer(modifier = Modifier.width(20.dp))
                        Text(text = "- ${names[i]}",style = Typography.labelLarge)
                    }
                }
            }
        }
        if(names.size%2==1){
            Row(){
                Spacer(modifier = Modifier.width(20.dp))
                Text(text = "- ${names[names.size-1]}",style = Typography.labelLarge)
            }
        }
    }
}