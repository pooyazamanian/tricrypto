package com.example.tradeapp.ui.pages

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.tradeapp.R
import com.example.tradeapp.ui.tools.MainCard
import com.example.tradeapp.ui.tools.MainImportantButton
import com.example.tradeapp.ui.tools.RowContent
import com.example.tradeapp.utils.NamePage
import com.example.tradeapp.viewmodel.ProfileViewModel
import kotlinx.serialization.json.Json
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun ProfilePage(
    navigation: NavHostController,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {

    val state by profileViewModel.state.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(0.8f), verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.test),
                contentDescription = "profile",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
            )
            Spacer(Modifier.size(20.dp))
            Column(modifier = Modifier) {
                Column(modifier = Modifier) {
                    Text(
                        text = "ایمیل",
                        color = MaterialTheme.colorScheme.background,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        overflow = TextOverflow.Ellipsis
                    )
                    state.user?.email?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.background.copy(alpha = 0.6f),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Light,
                            textAlign = TextAlign.End,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
        Spacer(Modifier.size(20.dp))

        MainCard {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                state.user?.phone?.let { RowContent("شماره تلفن", it) }
                Spacer(
                    Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(MaterialTheme.colorScheme.primary)
                )
                state.profile?.full_name?.let { RowContent("نام کامل", it) }
            }
        }
        Spacer(Modifier.size(20.dp))

        MainCard {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                state.profile?.national_id?.let { RowContent("کد ملی", it) }
                Spacer(
                    Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(MaterialTheme.colorScheme.primary)
                )
                state.profile?.username?.let { RowContent("نام کاربری", it) }
            }
        }
        if(!state.isLoading && state.profile != null && state.user != null){
            MainImportantButton(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                text = "ویرایش پروفایل"
            ) {
                val profileJson = Json.encodeToString(state.profile)
                val profile = URLEncoder.encode(profileJson, StandardCharsets.UTF_8.toString())
                val userJson = Json.encodeToString(state.user)
                val user = URLEncoder.encode(userJson, StandardCharsets.UTF_8.toString())
                navigation.navigate(
                    "${NamePage.PROFILE_EDITOR}/${profile}/${user}"
                )
            }
        }

    }
}