package com.example.aquaintel

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aquaintel.ui.theme.CreamDark
import com.example.aquaintel.ui.theme.GrayLight
import com.example.aquaintel.ui.theme.LeafGreen


@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    isPassword: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyType: KeyboardType = KeyboardType.Text,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value         = value,
        onValueChange = onValueChange,
        modifier      = modifier.fillMaxWidth(),
        placeholder   = { Text(placeholder, fontSize = 13.sp, color = GrayLight) },
        leadingIcon   = if (leadingIcon != null) ({
            Icon(leadingIcon, null, tint = GrayLight)
        }) else null,
        trailingIcon  = trailingIcon,
        visualTransformation = if (isPassword) PasswordVisualTransformation()
        else            VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyType),
        singleLine    = true,
        shape         = RoundedCornerShape(12.dp),
        colors        = OutlinedTextFieldDefaults.colors(
            focusedBorderColor   = LeafGreen,
            unfocusedBorderColor = CreamDark,
            cursorColor          = LeafGreen
        )
    )
}