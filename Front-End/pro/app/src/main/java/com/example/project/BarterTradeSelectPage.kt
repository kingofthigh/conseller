package com.example.project

import GifticonItem
import PaginationControls
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.project.api.myGifticon
import com.example.project.ui.theme.BrandColor1
import com.example.project.viewmodels.MygifticonViewModel
import kotlinx.coroutines.delay

@Composable
fun BarterTradeSelectPage(index: String?, navController: NavHostController, mygifticonViewModel: MygifticonViewModel) {
    val gifticonItems by mygifticonViewModel.gifticonItems.collectAsState() // 내기프티콘
    val totalItems by mygifticonViewModel.totalItems.collectAsState() // 내기프티콘 총페이지
    val error by mygifticonViewModel.error.collectAsState()
    val scrollState = rememberScrollState()

    var currentPage by remember { mutableStateOf(1) }
    val itemsPerPage = 10

    var selectedItemIndices by remember { mutableStateOf(listOf<Long>()) }

    var showSnackbar by remember { mutableStateOf(false) } // 에러처리스낵바
    var snackbarText by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        mygifticonViewModel.getUserGifticons(1)
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

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {
            if (showSnackbar) {
                Snackbar(
                ) {
                    Text(text = snackbarText, style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    )
                }
            }
            Text(
                text = "1. 기프티콘을 선택해주세요.(최대 5개)",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (gifticonItems.isEmpty()) {
                Text(
                    text = "등록할 수 있는 기프티콘이 없습니다",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                )
            } else {
                gifticonItems.forEach { item ->
                    GifticonItem(
                        gifticonData = item,
                        isSelected = selectedItemIndices.contains(item.gifticonIdx),
                        onClick = {
                            // 선택한 항목의 인덱스가 이미 목록에 있는 경우 제거, 그렇지 않으면 추가
                            if (selectedItemIndices.contains(item.gifticonIdx)) {
                                selectedItemIndices = selectedItemIndices - item.gifticonIdx
                            } else {
                                if (selectedItemIndices.size < 5) {
                                    selectedItemIndices = selectedItemIndices + item.gifticonIdx
                                }
                            }
                        }
                    )
                    Divider()
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            PaginationControls(
                totalItems = totalItems,
                currentPage = currentPage,
                itemsPerPage = itemsPerPage
            ) { newPage ->
                currentPage = newPage
                mygifticonViewModel.changePage(newPage)
            }
        }

        // 오른쪽 하단에 위치한 "다음" 버튼
        if (selectedItemIndices.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 16.dp)
            ) {
                Button(onClick = {
                    // 선택된 항목들을 다음 페이지로 전달. "/nextPageRoute/1,2,3"과 같이
                    navController.navigate("BarterTradePage/${selectedItemIndices.joinToString(",")}/${index}"){
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }) {
                    Icon(Icons.Default.ArrowForward, contentDescription = "다음")
                }
            }
        }
    }
}


