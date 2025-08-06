package com.bwalshe.describedsky.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.bwalshe.describedsky.ui.theme.DescribedskyTheme

@Composable
fun LoginScreen(
    identity: String = "",
    password: String = "",
    lastLoginFailed:Boolean = false,
    onIdentityChange: (String) -> Unit = {},
    onPasswordChange: (String) -> Unit = {},
    onSubmit: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        TextField(
            value = identity,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                autoCorrectEnabled = false
            ),
            onValueChange = onIdentityChange,
            placeholder = { Text("Enter Username") },
            isError = lastLoginFailed
        )
        TextField(
            value = password,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                autoCorrectEnabled = false
            ),
            visualTransformation = PasswordVisualTransformation(),
            onValueChange = onPasswordChange,
            placeholder = { Text("Enter Password") },
            isError = lastLoginFailed
        )
        OutlinedButton(
            onClick = onSubmit
        ) {
            Text("Submit")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {

    DescribedskyTheme {
        LoginScreen()
    }
}