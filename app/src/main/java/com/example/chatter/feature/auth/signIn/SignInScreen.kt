package com.example.chatter.feature.auth.signIn

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun SignInScreen(navController: NavController){

    val viewModel:SignInViewModel = hiltViewModel()
    val uiState = viewModel.state.collectAsState()

    var email by remember { mutableStateOf("") }

    var password by remember { mutableStateOf("") }

    val context = LocalContext.current
    LaunchedEffect(key1 = uiState.value) {
        when(uiState.value){
             is SignInState.Error -> {
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
            }
           is SignInState.Success -> {
                navController.navigate("home")
            }
            else -> {}
        }
    }


    Scaffold(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = email,
                onValueChange = {email =it},
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = "Email")
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {password =it},
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = "Password")
                },
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(16.dp))


            if(uiState.value == SignInState.Loading){
                CircularProgressIndicator()
            }else {
                Button(
                    onClick = {
                        viewModel.signIn(email, password)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = email.isNotEmpty() && password.isNotEmpty() && (uiState.value == SignInState.Nothing || uiState.value == SignInState.Error)
                ) {
                    Text(text = "SignIn")
                }

                TextButton(onClick = {
                    navController.navigate("signUp")
                }) {
                    Text(text = "Don't Have Account? Sign Up")
                }
            }
        }

    }
}
