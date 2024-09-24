package com.example.shoppinglistapp

import android.R.attr.checked
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shoppinglistapp.ui.theme.ShoppingListAppTheme


data class ShoppingItem(val name: String, val quantity: String, val isChecked: Boolean = false)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShoppingListAppTheme {
                ShoppingListScreen()
            }
        }
    }
}

@Composable
fun ShoppingListScreen() {
    var itemName by remember { mutableStateOf(TextFieldValue("")) }
    var itemQuantity by remember { mutableStateOf(TextFieldValue(""))}
    val shoppingList = remember { mutableStateListOf<ShoppingItem>() }
    var showErrorDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                Row {
                    TextField(
                        value = itemName,
                        onValueChange = { itemName = it },
                        label = { Text("Item Name") },
                        modifier = Modifier.width(260.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    TextField(
                        value = itemQuantity,
                        onValueChange = { itemQuantity = it },
                        label = { Text("Quantity") },
                        modifier = Modifier.width(110.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    if (itemName.text.isNotBlank() && itemQuantity.text.isNotBlank()) {
                        if (itemQuantity.text.toIntOrNull() != null){
                            shoppingList.add(ShoppingItem(itemName.text, itemQuantity.text))
                            itemName = TextFieldValue("")
                            itemQuantity = TextFieldValue("")
                        }
                        else{
                            showErrorDialog = true
                        }
                    }
                }) {
                    Text("Add Item")
                }
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn {
                    items(shoppingList.size) { index ->
                        val item = shoppingList[index]
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "${item.name} - ${item.quantity}")
                            Checkbox(
                                checked = item.isChecked,
                                onCheckedChange = {
                                    shoppingList[index] = item.copy(isChecked = it)
                                }
                            )
                        }
                    }
                }
            }
        }
    )
    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            confirmButton = {
                TextButton(onClick = { showErrorDialog = false }) {
                    Text("OK")
                }
            },
            title = { Text("Input Error") },
            text = { Text("Please enter a valid number for the quantity.") }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ShoppingListScreenPreview() {
    ShoppingListAppTheme {
        ShoppingListScreen()
    }
}