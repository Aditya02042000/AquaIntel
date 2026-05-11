package com.example.aquaintel

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.runtime.Composable
import com.example.aquaintel.ui.theme.ForestGreen


@Composable
fun AddBPDialog(onDismiss: () -> Unit, onSave: (Int, Int, Int) -> Unit) {
    var sys by remember { mutableStateOf("") }
    var dia by remember { mutableStateOf("") }
    var pul by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(24.dp),
        title = { Text("Log BP Reading", fontWeight = FontWeight.Bold) },
        text  = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                AppTextField(sys, { sys = it }, "Systolic (e.g. 120)",  keyType = KeyboardType.Number)
                AppTextField(dia, { dia = it }, "Diastolic (e.g. 80)",  keyType = KeyboardType.Number)
                AppTextField(pul, { pul = it }, "Pulse (e.g. 72)",      keyType = KeyboardType.Number)
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(
                        sys.toIntOrNull() ?: 120,
                        dia.toIntOrNull() ?: 80,
                        pul.toIntOrNull() ?: 72
                    )
                },
                colors = ButtonDefaults.buttonColors(ForestGreen),
                shape  = RoundedCornerShape(12.dp)
            ) { Text("Save") }
        },
        dismissButton = {
            TextButton(onDismiss) { Text("Cancel") }
        }
    )
}