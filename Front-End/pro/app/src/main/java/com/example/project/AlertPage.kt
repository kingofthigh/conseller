package com.example.project

import SelectButton
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.project.api.MyNotificationResponseDTO
import com.example.project.viewmodels.MyAuctionViewModel
import formatAlertDate
import kotlinx.coroutines.delay

@Composable
fun AlertPage(myAuctionViewModel: MyAuctionViewModel = hiltViewModel()) {
    val error by myAuctionViewModel.error.collectAsState()

    var showSnackbar by remember { mutableStateOf(false) } // 에러처리스낵바
    var snackbarText by remember { mutableStateOf("") }

    LaunchedEffect(error) {
        if (error != null) {
            showSnackbar = true
            snackbarText = error!!
        }
    }
    LaunchedEffect(showSnackbar) {
        if (showSnackbar) {
            delay(5000)
            showSnackbar = false
        }
    }

    val notifications = myAuctionViewModel.myNotifications.collectAsState().value
        .sortedWith(compareBy({ it.notificationType == "5" || it.notificationType == "6" }, { it.notificationIdx })) // 여기에서 정렬합니다.

    Box(modifier = Modifier.fillMaxSize()) {
        if (showSnackbar) {
            Snackbar(
                modifier = Modifier.align(Alignment.TopCenter)
            ) {
                Text(text = snackbarText, style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
                )
            }
        }
        if (notifications.isEmpty()) {
            Text(
                text = "알림이 없습니다.",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(notifications) { index, notification ->
                    NotificationItem(index + 1, notification, myAuctionViewModel) // 알림 ID를 인덱스 + 1로
                }
            }
        }
    }
}

@Composable
fun NotificationItem(id: Int, notification: MyNotificationResponseDTO, viewModel: MyAuctionViewModel) {
    val isType6 = notification.notificationType == "6"
    val isType5 = notification.notificationType == "5"
    val textColor = if (isType6 || isType5) Color.Gray else Color.Black

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color.White)
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = "번호: $id",
                style = MaterialTheme.typography.bodyMedium.copy(color = textColor, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "날짜: ${formatAlertDate(notification.notificationCreatedDate)}",
                style = MaterialTheme.typography.bodyMedium.copy(color = textColor, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "상태: ${notification.notificationStatus}",
                style = MaterialTheme.typography.bodyMedium.copy(color = textColor, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            )

            // 5나 6이면 회색으로
            if (!isType6&&!isType5) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    SelectButton(
                        text = "Yes",
                        onClick = {
                            viewModel.sendNotificationAnswer(
                                notificationIdx = notification.notificationIdx,
                                notificationType = notification.notificationType,
                                answer = true
                            )
                        }
                    )
                    Spacer(modifier = Modifier.width(40.dp))
                    SelectButton(
                        text = "No",
                        onClick = {
                            viewModel.sendNotificationAnswer(
                                notificationIdx = notification.notificationIdx,
                                notificationType = notification.notificationType,
                                answer = false
                            )
                        }
                    )
                }
            }
        }
    }
    Divider()
}