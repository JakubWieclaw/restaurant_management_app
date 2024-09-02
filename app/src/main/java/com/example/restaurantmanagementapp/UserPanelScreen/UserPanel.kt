package com.example.restaurantmanagementapp.UserPanelScreen

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource

import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.restaurantmanagementapp.HomeScreen.navigateToScreen
import com.example.restaurantmanagementapp.classes.AuthViewModel

data class SettingItemData(val label: String, val value: String, val editable: Boolean = false, val isDropDown:Boolean = false, val dropDownList:List<String> = listOf())
val settingItems = listOf(
    SettingItemData(label = "Full name", value = "Ron Weasley"),
    SettingItemData(label = "Email", value = "email@poczta.com"),
    SettingItemData(label = "Phone", value = "+48 293 329 923",editable=true),
    SettingItemData(label = "Address", value = "The Burrow"),
)

val settingItems2 = listOf(
    SettingItemData(label = "card number", value = "1323 1233 1233 1233"),
    )

val settingItems3 = listOf(
    SettingItemData(label = "Language", value = "English", isDropDown = true, dropDownList = listOf("Polish","English","Spanish")),
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController,authViewModel: AuthViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TopAppBar(
            title = {
                Text(text = "Profile info", color = Color.Black, modifier = Modifier.align(Alignment.CenterHorizontally))
            }
        )

        Column(){
            SettingList(label = "Profile Settings", icon = android.R.drawable.ic_menu_info_details, settingItemsData = settingItems, foldable = false)
            SettingList(label = "Card Settings", icon = android.R.drawable.ic_btn_speak_now, settingItemsData = settingItems2, foldable = true)
            SettingList(label = "Settings", icon = android.R.drawable.ic_menu_manage, settingItemsData =settingItems3, foldable = false)
        }
    }
}



@Composable
fun SettingItem(label: String, value: String, editable: Boolean, isDropDown: Boolean, dropDownList: List<String>) {
    var editMode by remember { mutableStateOf(false) }
    var itemValue by remember { mutableStateOf(value) }
    var itemValueOld by remember { mutableStateOf(value) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.CenterVertically))

        Row() {
            if (editMode && editable && !isDropDown) {
                TextField(
                    value = itemValue,
                    onValueChange = {itemValue = it  },
                    modifier = Modifier.align(Alignment.CenterVertically),
                    singleLine = true,
                    leadingIcon ={
                        IconButton(onClick = { editMode=false;itemValueOld = itemValue}) {
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
                    ExposedDropdownMenuBox(dropDownList, onValueChange = {selected -> itemValue = selected})
                }else{
                    Text(text = itemValue, color = Color.Gray, modifier = Modifier.align(Alignment.CenterVertically))
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
    foldable: Boolean = false
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
            Text(text = label, fontSize = 18.sp, fontWeight = FontWeight.Bold)
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
                    .padding(start = 12.dp, end = 12.dp)
            ) {
                Divider(modifier = Modifier.height(2.dp), color = Color.Black)
                settingItemsData.forEach { item ->
                    SettingItem(label = item.label, value = item.value, editable = item.editable, isDropDown = item.isDropDown, dropDownList = item.dropDownList)
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExposedDropdownMenuBox(itemList:List<String>, onValueChange: (String)->Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(itemList[0]) }

    Box(
        modifier = Modifier.width(180.dp)
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            OutlinedTextField(
                value = selectedText,
                onValueChange = {onValueChange(it)},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor().background(color = Color.White),
                maxLines = 1
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                itemList.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item) },
                        onClick = {
                            selectedText = item
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}




//@Preview(showBackground = true)
//@Composable
//fun PreviewSettingList() {
//    SettingsScreen(authViewModel)
//}