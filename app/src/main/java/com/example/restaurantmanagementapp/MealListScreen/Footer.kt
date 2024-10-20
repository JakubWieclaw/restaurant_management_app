package com.example.restaurantmanagementapp.MealListScreen

//@Composable
//fun Footer(
//    orderViewModel: OrderViewModel,
//    modifier: Modifier = Modifier,
//    navController: NavController
//){
//
//    Row(modifier = Modifier
//        .fillMaxWidth()
//        .then(modifier), horizontalArrangement = Arrangement.End){
//
//        Button(
//            onClick = {
//                navigateToScreen("cart",navController)
//            },
//            modifier = modifier.padding(12.dp)
//        ) {
//            Row(modifier = Modifier.align(Alignment.CenterVertically)){
//                Icon(
//                    imageVector = Icons.Default.ShoppingCart,
//                    contentDescription = null,
//                    modifier = Modifier
//                        .padding(end = 8.dp)
//                        .size(32.dp)
//                        .align(Alignment.CenterVertically),
//                    tint = Color.Black
//                )
//                Text("${orderViewModel.getSize()}: ${"%.2f".format(orderViewModel.getOrderTotal())} ZŁ", textAlign = TextAlign.Center, modifier = Modifier.align(
//                    Alignment.CenterVertically))
//            }
//
//        }
//    }
//}