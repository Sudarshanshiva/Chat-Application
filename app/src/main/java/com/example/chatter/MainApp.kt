package com.example.chatter

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.chatter.chat.ChatScreen
import com.example.chatter.feature.auth.signIn.SignInScreen
import com.example.chatter.feature.auth.signUp.SignUpScreen
import com.example.chatter.home.HomeScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun MainApp(){
    Surface(modifier = Modifier.fillMaxSize()) {
        val navController = rememberNavController()
        val currentUser = FirebaseAuth.getInstance().currentUser

         val start = if(currentUser != null) "home" else "signIn"

        NavHost(navController = navController, startDestination = start) {
            
            composable("signIn"){
                SignInScreen(navController = navController)
            }
            composable("signUp"){
                SignUpScreen(navController = navController)
            }
            composable("home"){
                HomeScreen(navController = navController)
            }

            composable("chat/{channelId}", arguments = listOf(
                navArgument("channelId"){
                    type = NavType.StringType
                }
            )){
                val channelId = it.arguments?.getString("channelId")
                ChatScreen(navController = navController, channelId = channelId!!)
            }
            
        }
    }
}