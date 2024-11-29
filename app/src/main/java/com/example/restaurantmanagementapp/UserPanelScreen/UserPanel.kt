package com.example.restaurantmanagementapp.UserPanelScreen

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration

import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.restaurantmanagementapp.HomeScreen.CustomBackground
import com.example.restaurantmanagementapp.HomeScreen.navigateToScreen
import com.example.restaurantmanagementapp.apithings.RequestClasses.RegisterRequest
import com.example.restaurantmanagementapp.ui.theme.Typography
import com.example.restaurantmanagementapp.viewmodels.AuthViewModel
import com.example.restaurantmanagementapp.viewmodels.OrderHistoryViewModel

data class SettingItemData(val label: String, var value: String, val editable: Boolean = false, val isDropDown:Boolean = false, val dropDownList:List<String> = listOf())



val settingItems2 = listOf(
    SettingItemData(label = "card number", value = "1323 1233 1233 1233"),
    )

val settingItems3 = listOf(
    SettingItemData(label = "Language", value = "English", isDropDown = true, dropDownList = listOf("Polish","English","Spanish")),
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController,orderHistoryViewModel: OrderHistoryViewModel,authViewModel: AuthViewModel) {
    val customer = authViewModel.customerData!!

    val settingItems by remember {mutableStateOf(listOf(
        SettingItemData(label = "Imię", value = customer.customerName,editable=true),
        SettingItemData(label = "Nazwisko", value = customer.customerSurname,editable=true),
        SettingItemData(label = "Email", value = customer.customerEmail,editable=false),
        SettingItemData(label = "Telefon", value =  if(authViewModel.phone!=null) authViewModel.phone!! else "123456789",editable=false)
    ))}
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LoyaltyHeader(points = 10,authViewModel = authViewModel,navController=navController)
        Button(
            onClick={
            orderHistoryViewModel.fetchOrderHistory(customer.customerId,customer.token, onComplete = {})
            navigateToScreen("orderhistory",navController)
        },
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
            ){
            //TODO: zmienić by vył R.string
            Text(text=  "Moje zamówienia")
        }
        Column(){
            SettingList(label = "Profile Settings", icon = android.R.drawable.ic_menu_info_details, settingItemsData = settingItems, foldable = false,
                onValueChange={ index,newValue->
                    settingItems[index].value = newValue
                    val registerRequest = RegisterRequest(
                        name = settingItems[0].value,
                        surname = settingItems[1].value,
                        email = settingItems[2].value,
                        phone = settingItems[3].value,
                        password = "",
                    )
                    authViewModel.updateCustomer(registerRequest)
                })
            //SettingList(label = "Card Settings", icon = android.R.drawable.ic_btn_speak_now, settingItemsData = settingItems2, foldable = true)
            //SettingList(label = "Settings", icon = android.R.drawable.ic_menu_manage, settingItemsData =settingItems3, foldable = false)
        }
        Row() {
            Text("Masz uwagi, sugestie, pytania?")
            Text("Wciśnij tutaj.",color = Color.Blue, textDecoration = TextDecoration.Underline, modifier = Modifier.clickable { navigateToScreen("suggestionform",navController)  })
        }
    }
}



@Composable
fun SettingItem(label: String, value: String, editable: Boolean, isDropDown: Boolean, dropDownList: List<String>,onValueChange: (String) -> Unit) {
    var editMode by remember { mutableStateOf(false) }
    var itemValue by remember { mutableStateOf(value) }
    var itemValueOld by remember { mutableStateOf(value) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontWeight = FontWeight.Bold, fontSize = 18.sp,style = Typography.labelLarge, modifier = Modifier.align(Alignment.CenterVertically))

        Row() {
            if (editMode && editable && !isDropDown) {
                TextField(
                    value = itemValue,
                    onValueChange = {itemValue = it  },
                    modifier = Modifier.align(Alignment.CenterVertically),
                    singleLine = true,
                    leadingIcon ={
                        IconButton(onClick = { editMode=false;itemValueOld = itemValue;onValueChange(itemValue)}) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Clear text icon",
                                tint = Color.Gray
                            )
                        }
                    } ,
                    trailingIcon = {
                        IconButton(onClick = { editMode=false; itemValue = itemValueOld}) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear text icon",
                                tint = Color.Gray
                            )
                        }
                    }

                )
            } else {
                if(isDropDown){
                    ExposedDropdownMenuBox(dropDownList, onValueChange = {selected -> itemValue = selected}, modifier = Modifier.width(180.dp))
                }else{
                    Text(text = itemValue, color = Color.Gray, style = Typography.labelLarge ,fontSize = 18.sp, modifier = Modifier.align(Alignment.CenterVertically))
                    if(editable){
                        IconButton(onClick = { editMode = !editMode }, modifier = Modifier.align(Alignment.CenterVertically)) {
                            Icon(imageVector = Icons.Default.Create, contentDescription = null)
                        }
                    }else{
                        Spacer(modifier = Modifier.width(40.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun SettingList(
    label: String,
    icon: Int,
    settingItemsData: List<SettingItemData>,
    foldable: Boolean = false,
    onValueChange: (Int,String) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    val rotationAngle by animateFloatAsState(if (isExpanded) 180f else 0f, label = "")
    //val rotationAngle = if (isExpanded) 180f else 0f
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(shape = RoundedCornerShape(18.dp))
            .background(color = Color.LightGray)
            .animateContentSize()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isExpanded = !isExpanded }
                .padding(8.dp)
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(8.dp))
            Text(text = label, style = Typography.titleMedium)
            Spacer(modifier = Modifier.weight(1f))
            if(foldable){
                Icon(
                    painter = painterResource(id = android.R.drawable.arrow_down_float),
                    contentDescription = null,
                    modifier = Modifier.rotate(rotationAngle)
                )
            }
        }

        if (isExpanded || !foldable) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
            ) {
                Divider(modifier = Modifier.height(2.dp), color = Color.Black)
                settingItemsData.forEachIndexed {index, item ->
                    SettingItem(label = item.label, value = item.value, editable = item.editable, isDropDown = item.isDropDown, dropDownList = item.dropDownList, onValueChange = {newValue->onValueChange(index,newValue)})
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExposedDropdownMenuBox(itemList:List<String>, onValueChange: (String)->Unit, modifier: Modifier) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(itemList[0]) }

    Box(
        modifier = Modifier.then(modifier)
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            OutlinedTextField(
                value = selectedText,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .background(color = Color.White),
                maxLines = 1
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                itemList.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item, style = Typography.labelMedium) },
                        onClick = {
                            selectedText = item
                            expanded = false
                            onValueChange(selectedText)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun LoyaltyHeader(points: Int,authViewModel:AuthViewModel,navController:NavController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left line
            Column(modifier = Modifier.weight(1f)){
                Text(text =  "Twój profil", style = Typography.labelLarge, fontSize = 18.sp,modifier = Modifier.align(Alignment.CenterHorizontally))
                Divider(thickness = 2.dp)
                Text(text = " ", fontSize = 18.sp)
            }


            // User icon in the center
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "User Icon",
                modifier = Modifier
                    .size(80.dp)
                    .padding(horizontal = 16.dp),
                tint = Color.Black
            )

            // Right line
            //TODO: uncomment after loyalty points functionality will be added
            Column(modifier = Modifier.weight(1f)){
                Button(modifier = Modifier.align(Alignment.CenterHorizontally),onClick = {authViewModel.logout();navigateToScreen("loginscreen", navController)}){
                    Text(text = "Wyloguj się")
                }

                //Text(text = "Loyalty points:", fontSize = 18.sp,modifier = Modifier.align(Alignment.CenterHorizontally))
                //Divider(thickness = 2.dp)
                //Text(text = "$points", fontSize = 18.sp,modifier = Modifier.align(Alignment.CenterHorizontally))
            }
        }




    }
}



//@Preview(showBackground = true)
//@Composable
//fun PreviewSettingList() {
//    SettingsScreen(authViewModel)
//}