package com.example.project

import FormattedDateDot
import SelectButton
import UserDetailDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.project.api.myGifticon
import com.example.project.ui.theme.BrandColor1
import com.example.project.ui.theme.logocolor
import com.example.project.viewmodels.BarterViewModel
import kotlinx.coroutines.delay
@Composable
fun BarterConfirmPage(navController: NavHostController, index: String?) {
    val barterViewModel: BarterViewModel = hiltViewModel()
    val confirmDetail by barterViewModel.barterConfirm.collectAsState()
    val barterConfirmNavi by barterViewModel.barterConfirmNavi.collectAsState()
    val error by barterViewModel.error.collectAsState()
    val scrollState = rememberScrollState()


    var showConfirmDialog by remember { mutableStateOf(false) }
    var showCancleDialog by remember { mutableStateOf(false) }
    var showUserDetailDialog by remember { mutableStateOf(false) } // 유저 자세히보기

    var showSnackbar by remember { mutableStateOf(false) } // 에러처리스낵바
    var snackbarText by remember { mutableStateOf("") }

    var selectedBuyUserIdx by remember { mutableStateOf<Long?>(null) } // 선택한 수락의 유저 인덱
    var selectedBuyUserNickname by remember { mutableStateOf<String?>(null) } // 유저 닉네임
    var selectedBuyUserImage by remember { mutableStateOf<String?>(null) } // 유저 이미지

    LaunchedEffect(Unit) {
        index?.toLongOrNull()?.let {
            barterViewModel.fetchBarterConfirmItems(it)
        }
    }
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
    LaunchedEffect(barterConfirmNavi) {
        if (barterConfirmNavi == true) {
            navController.navigate("Home")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxSize()
            .background(Color.White)
            .padding(4.dp)
    ) {
        if (showSnackbar) {
            Snackbar(
                modifier = Modifier.align(Alignment.TopCenter)
            ) {
                Text(
                    text = snackbarText,
                    style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .border(2.dp, Color.Gray, RoundedCornerShape(4.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(8.dp)
            ) {
                confirmDetail?.barterTradeAllList?.forEach { tradeList ->
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("${tradeList.buyUserNickName}님의 제안입니다.",fontSize=20.sp,color=Color.DarkGray,fontWeight=FontWeight.Bold)
                        tradeList.barterTradeList.forEachIndexed { index, item ->
                            ShowTradeGifticon(item.gifticonEndDate,item.gifticonName,item.gifticonDataImageName)
                            Divider()
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        // 버튼
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            SelectButton(
                                text = "수락",
                                onClick = {
                                    showConfirmDialog = true
                                    selectedBuyUserIdx = tradeList.buyUserIdx
                                }
                            )
                            Spacer(modifier = Modifier.width(24.dp))
                            SelectButton(
                                text = "거절",
                                onClick = {
                                    showCancleDialog = true
                                    selectedBuyUserIdx = tradeList.buyUserIdx
                                }
                            )
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        Divider(color = Color.Gray, thickness = 2.dp)
                    }
                }
            }
        }

        if (showConfirmDialog) {
            AlertDialog(
                onDismissRequest = {
                    showConfirmDialog = false
                },
                title = {
                    Text(text = "교환 확정")
                },
                text = {
                    Text("교환 하시겠습니까?")
                },
                dismissButton = {
                    SelectButton(
                        text = "네",
                        onClick = {
                            barterViewModel.barterConfirm(
                                index!!.toLong(),
                                selectedBuyUserIdx!!,
                                true
                            )
                            showConfirmDialog = false
                        }
                    )
                },
                confirmButton = {
                    SelectButton(
                        text = "아니오",
                        onClick = {
                            showConfirmDialog = false
                        }
                    )
                }
            )
        }
        if (showCancleDialog) {
            AlertDialog(
                onDismissRequest = {
                    showCancleDialog = false
                },
                title = {
                    Text(text = "거부하기")
                },
                text = {
                    Text("교환을 거부하시겠습니까?")
                },
                dismissButton = {
                    SelectButton(
                        text = "네",
                        onClick = {
                            barterViewModel.barterConfirm(
                                index!!.toLong(),
                                selectedBuyUserIdx!!,
                                false
                            )
                            showCancleDialog = false
                        }
                    )
                },
                confirmButton = {
                    SelectButton(
                        text = "아니오",
                        onClick = {
                            showCancleDialog = false
                        }
                    )
                }
            )
        }
        // 구매자 상세보기
        if (showUserDetailDialog) {
            UserDetailDialog(
                userImageUrl = selectedBuyUserImage,
                userNickname = selectedBuyUserNickname,
                userDeposit = 0,
                onDismiss = { showUserDetailDialog = false },
                onReportClick = {
                    navController.navigate("Inquiry/$selectedBuyUserIdx")
                },
                onMessageClick = {
                    // 1:1 채팅 미완성
                }
            )
        }
    }
}


@Composable
fun ShowTradeGifticon(
    gifticonDate:String,
    gifticonName:String,
    gifticonImage:String,
) {

    // gifticonEndDate 앞 8글자 추출
    val rawDate = gifticonDate.substring(0, 8)

    // 날짜 형식으로 변환 (예: "20230927" -> "2023-09-27")
    val formattedDate =
        "${rawDate.substring(0, 4)}-${rawDate.substring(4, 6)}-${rawDate.substring(6, 8)}"


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.White),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            contentAlignment = Alignment.CenterStart, modifier = Modifier.size(130.dp)
        ) {
            val imagePainter = rememberAsyncImagePainter(model = gifticonImage)
            Image(
                painter = imagePainter,
                contentDescription = null,
                modifier = Modifier.size(125.dp),
                contentScale = ContentScale.Crop,
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = gifticonName,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Spacer(modifier = Modifier.height(2.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "~${formattedDate}", fontSize = 22.sp)
            }
        }
    }
}

