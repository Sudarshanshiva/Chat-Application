package com.example.chatter.feature.auth.signUp

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
import com.example.chatter.feature.auth.signIn.SignInState

@Composable
fun SignUpScreen(navController: NavController){

    val viewModel:SignUpViewModel = hiltViewModel()
    val uiState = viewModel.state.collectAsState()

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }

    val context = LocalContext.current
    LaunchedEffect(key1 = uiState.value) {
        when(uiState.value){
            is SignUpState.Success -> {
                navController.navigate("home")
            }
            is SignUpState.Error -> {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
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
                value = name,
                onValueChange = {name =it},
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = "Name")
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

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

            OutlinedTextField(
                value = confirm,
                onValueChange = {confirm =it},
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = "Confirm Password")
                },
                visualTransformation = PasswordVisualTransformation(),
                isError = password.isNotEmpty() && confirm.isNotEmpty() && password != confirm
            )
            Spacer(modifier = Modifier.height(16.dp))

            if(uiState.value == SignUpState.Loading){
                CircularProgressIndicator()
            }else {
                Button(
                    onClick = {
                        viewModel.signUp(name, email, password)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirm.isNotEmpty() && password == confirm
                ) {

                    Text(text = "SignUp")
                }


                TextButton(onClick = {
                    navController.popBackStack()
                }) {
                    Text(text = "Already have an account? Sign In")
                }
            }
        }

    }

}