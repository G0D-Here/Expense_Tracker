package com.example.expensetracker.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.expensetracker.data.Resource
import com.example.expensetracker.data.utils.Profile
import com.example.expensetracker.nav.AllExpense
import com.example.expensetracker.nav.LoginScreen
import com.example.expensetracker.nav.ProfileScreen
import com.example.expensetracker.ui_constants.backgroundColor
import com.example.expensetracker.viewmodel.AuthViewModel

@Composable
fun ViewProfile(viewModel: AuthViewModel = hiltViewModel(), navController: NavController) {
    val profile = viewModel.getProfile.collectAsState().value
    LaunchedEffect(Unit) {
        viewModel.viewProfile()
    }
    profile?.let {
        when (it) {
            is Resource.Failure -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Profile is MT")
                    Button(onClick = {
//                        viewModel.logout()
                        navController.navigate(ProfileScreen)
                    }) {
                        Text(text = "Update Profile")
                    }
                }
            }

            Resource.Loading -> Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }

            is Resource.Success -> {
                Column(
                    Modifier
                        .fillMaxSize()
                        .background(Color.White)
                ) {
                    ProfilePage(
                        it.result,
                        navController,
                        viewModel
                    )
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun ProfilePage(
    profile: Profile? = null,
    navController: NavController = NavController(LocalContext.current),
    viewModel: AuthViewModel = hiltViewModel()
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(start = 20.dp, top = 40.dp, end = 20.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Back",
                Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .padding(2.dp)
                    .clickable { navController.popBackStack() })
            Row(
                Modifier.padding(start = 20.dp, top = 35.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Profile",
                    Modifier.size(60.dp),
                    tint = Color.Black
                )
                Text(
                    text = "Profile",
                    Modifier.padding(start = 10.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Surface(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 6.dp, top = 20.dp, bottom = 50.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(backgroundColor),
                color = backgroundColor
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .padding(start = 10.dp, top = 20.dp, end = 20.dp, bottom = 30.dp)
                ) {
                    Text(text = "Name   ${profile?.name.toString()}", Modifier)

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "Age   ${profile?.age.toString()}")
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "Income   ${profile?.income.toString()}")
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "Bio   ${profile?.bio.toString()}")

                    Spacer(modifier = Modifier.height(19.dp))
                }
            }

        }
        Row(
            Modifier
                .fillMaxWidth()
                .align(Alignment.BottomEnd)
                .padding(bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = { navController.navigate(AllExpense) },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor
                )
            ) {
                Text(text = "Edit")
            }
            Spacer(modifier = Modifier.width(6.dp))
            Button(
                onClick = {
                    viewModel.logout()
                    navController.navigate(LoginScreen)
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor
                )
            ) {
                Text(text = "LogOut")
            }
        }

    }
}