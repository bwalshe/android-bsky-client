package com.bwalshe.describedsky

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bwalshe.describedsky.model.DescribedSkyViewModel
import com.bwalshe.describedsky.model.LoginViewModel
import com.bwalshe.describedsky.ui.LoginScreen
import com.bwalshe.describedsky.ui.TimelineScreen
import com.bwalshe.describedsky.ui.theme.DescribedskyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DescribedskyTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val loginViewModel: LoginViewModel = viewModel(factory = LoginViewModel.Factory)
                    val describedSkyViewModel: DescribedSkyViewModel = viewModel(factory = DescribedSkyViewModel.Factory)
                    HomeScreen(
                        loginViewModel,
                        describedSkyViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun HomeScreen(
    loginViewModel: LoginViewModel,
    describedSkyViewModel: DescribedSkyViewModel,
    modifier: Modifier = Modifier) {
    if(loginViewModel.loggedIn) {
       TimelineScreen(
           describedSkyViewModel.timeline,
           refresh = describedSkyViewModel::refreshTimeline,
           logout = {},
           modifier = modifier
       )
    } else {
        LoginScreen(
            loginViewModel.identity,
            loginViewModel.password,
            loginViewModel.lastLoginFailed,
            loginViewModel::updateIdentity,
            loginViewModel::updatePassword,
            onSubmit = {
                loginViewModel.createSession()
                if(loginViewModel.loggedIn) {
                    describedSkyViewModel.refreshTimeline()
                }
            },
            modifier = modifier
        )
    }
}
