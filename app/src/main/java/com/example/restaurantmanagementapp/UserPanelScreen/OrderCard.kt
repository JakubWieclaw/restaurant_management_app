package com.example.restaurantmanagementapp.UserPanelScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.restaurantmanagementapp.R
import com.example.restaurantmanagementapp.apithings.schemasclasses.Order
import com.example.restaurantmanagementapp.ui.theme.Typography
import com.example.restaurantmanagementapp.viewmodels.MealsViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun OrderCard(order: Order, mealsViewModel: MealsViewModel, toTableRes:Boolean = false) {
        Column(
            modifier = if(!toTableRes){
                Modifier
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(16.dp))  // Tło dla karty
                .border(2.dp, Color.Gray, shape = RoundedCornerShape(16.dp))
                .padding(16.dp) } else {
                    Modifier
                    .fillMaxWidth()}

        ) {
            // Górna sekcja - Typ zamówienia i data
            if(!toTableRes){
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(id = R.string.type) + getOrderType(order.type),
                        fontWeight = FontWeight.Bold,
                        style = Typography.labelMedium
                    )
                    Text(
                        text = formatDateTime(order.dateTime),
                        color = Color.Gray,
                        style = Typography.labelMedium
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Lista posiłków i niechciane składniki
            Text(
                text = stringResource(id = R.string.ordered_meals),
                fontWeight = FontWeight.Bold,
                style = Typography.labelMedium
            )
            Spacer(modifier = Modifier.height(8.dp))  // Odstęp przed posiłkami

            order.mealIds.forEachIndexed { index, meal ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "\t${mealsViewModel.findMeal(meal.mealId)?.name ?: stringResource(id = R.string.unknown_meal) }", style = Typography.labelMedium)
                    Text(text = stringResource(id = R.string.number_of_meals)+" ${meal.quantity}", style = Typography.labelMedium)
                }

                order.unwantedIngredients.find { it.mealIndex == index }?.let { unwanted ->
                    if (unwanted.ingredients.isNotEmpty()) {
                        Text(
                            text = "\t Usunięte składniki: ${unwanted.ingredients.joinToString(", ")}",
                            color = Color.Red,
                            fontStyle = FontStyle.Italic,
                            fontSize = 12.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))  // Odstęp między posiłkami
            }

            // Sekcja dostawy (jeśli dotyczy)
            if (order.type == "DOSTAWA") {
                Spacer(modifier = Modifier.height(10.dp))  // Odstęp przed dostawą
                Text(
                    text = "Dostawa:",
                    fontWeight = FontWeight.Bold
                )
                Text(text = "Adres: ${order.deliveryAddress}")
                Text(text = "Dystans: ${order.deliveryDistance} km")
                Text(text = "Cena dostawy: ${order.deliveryPrice} zł")
            }

            Spacer(modifier = Modifier.height(16.dp))  // Większy odstęp przed statusem i ceną

            // Dolna sekcja - Status zamówienia i cena
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = getOrderStatus(order.status),
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .background(
                            color = getStatusColor(order.status),
                            shape = RoundedCornerShape(50)
                        )
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
                Text(
                    text = stringResource(id = R.string.price)+ ": "+ order.orderPrice+ stringResource(id = R.string.currency),
                    fontWeight = FontWeight.Bold,
                )
            }
        }
}


fun formatDateTime(dateTimeString: String): String {
    // Parsing stringa do LocalDateTime
    val formatterInput = DateTimeFormatter.ISO_DATE_TIME
    val dateTime = LocalDateTime.parse(dateTimeString, formatterInput)

    // Tworzymy format wyjściowy: DD-MM-RRRR GG:MM
    val formatterOutput = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")

    // Zwracamy sformatowaną datę
    return dateTime.format(formatterOutput)
}

fun getOrderType(type:String): String{
    return when (type) {
        "DOSTAWA" -> "dostawa"
        "NA_MIEJSCU" -> "na miejscu"
        else -> "nieznane"
    }
}

fun getOrderStatus(status:String):String{
    return when (status) {
        "OCZEKUJĄCE" -> "OCZEKUJĄCE"
        "W_TRAKCIE_REALIZACJI"-> "W REALIZACJI"
        "GOTOWE"-> "GOTOWE"
        "W_DOSTARCZENIU"-> "W DOSTARCZENIU"
        "DOSTARCZONE"-> "DOSTARCZONE"
        "ODRZUCONE"-> "ODRZUCONE"
        else -> "nieznane"
    }
}

fun getStatusColor(status:String): Color{
    return when (status) {
        "OCZEKUJĄCE" -> Color.Blue
        "W_TRAKCIE_REALIZACJI"-> Color.Yellow
        "GOTOWE"-> Color.Green
        "W_DOSTRACZENIU"-> Color.Yellow
        "DOSTARCZONE"-> Color.Green
        "ODRZUCONE"-> Color.Red
        else -> Color.Transparent
    }
}