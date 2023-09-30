package com.example.project

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.project.viewmodels.AuctionViewModel
import com.example.project.viewmodels.MyPageViewModel
import kotlinx.coroutines.delay

@Composable
fun HomePage(modifier: Modifier = Modifier, navController: NavHostController) {

    LaunchedEffect(Unit) {
        navController.currentBackStackEntry?.also { entry ->
            navController.popBackStack(entry.id, true)
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item { Spacer(modifier = Modifier.height(10.dp)) }
            item { HomeLayout2() }
            item { Spacer(modifier = Modifier.height(10.dp)) }
            item { HomeLayout3(navController) }
            item { Spacer(modifier = Modifier.height(10.dp)) }
            item { HomeLayout4(navController) }
            item { Spacer(modifier = Modifier.height(10.dp)) }
            item { HomeLayout5() }
            item { Spacer(modifier = Modifier.height(10.dp)) }
            item { HomeLayout6() }
            item { Spacer(modifier = Modifier.height(10.dp)) }
            item { HomeLayout7() }
            item { Spacer(modifier = Modifier.height(10.dp)) }
            item { HomeLayout8() }

        }
    }
}

@Composable
fun HomeLayout2() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .background(Color.White, shape = RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text("Hot")
    }
}

@Composable
fun HomeLayout3(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .background(Color.White, shape = RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.weight(1f))  // 상단에 공간을 주기 위해 weight를 사용
            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.width(16.dp))
                ImageButton("스토어") { navController.navigate("StorePage") }
                ImageButton("경매") { navController.navigate("AuctionPage") }
                ImageButton("물물교환") { navController.navigate("BarterPage") }
                ImageButton("이벤트") { navController.navigate("EventPage") }
                Spacer(modifier = Modifier.width(8.dp))
            }
            Spacer(modifier = Modifier.weight(1f))  // 하단에 공간을 주기 위해 weight를 사용
        }
    }
}

@Composable
fun ImageButton(imageName: String, onClick: () -> Unit) {
    val resourceId = when (imageName) {
        "경매" -> R.drawable.auction
        "물물교환" -> R.drawable.barter
        "스토어" -> R.drawable.store
        "이벤트" -> R.drawable.event
        else -> R.drawable.chatbot
    }

    val interactionSource = remember { MutableInteractionSource() }
    var scale by remember { mutableFloatStateOf(1f) }
    val pressedScale = 0.95f

    Image(
        painter = painterResource(id = resourceId),
        contentDescription = null,
        modifier = Modifier
            .size(88.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onClick() },
                    onPress = {
                        tryAwaitRelease()
                        scale = pressedScale
                        delay(100L)
                        scale = 1f
                    }
                )
            }
    )
}

@Composable
fun HomeLayout4(navController: NavController) {
    val auctionViewModel: AuctionViewModel = hiltViewModel()
    val myPageViewModel: MyPageViewModel = hiltViewModel()
    val error by auctionViewModel.error.collectAsState()
    val myerror by myPageViewModel.error.collectAsState()

    val myAuctionItems by auctionViewModel.myAuctionItems.collectAsState()
    val myBids by myPageViewModel.getMyAuctionBidResponse.collectAsState()

    var selectedTab by remember { mutableStateOf("입찰") } // 초기값은 "입찰"

    var showSnackbar by remember { mutableStateOf(false) } // 에러처리스낵바
    var snackbarText by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        myPageViewModel.getMyAuctionBid()
        auctionViewModel.fetchMyAuctionItems()
    }
    LaunchedEffect(error) {
        if (error != null) {
            showSnackbar = true
            snackbarText = error!!
        }
    }
    LaunchedEffect(myerror) {
        if (myerror != null) {
            showSnackbar = true
            snackbarText = myerror!!
        }
    }
    LaunchedEffect(showSnackbar) {
        if (showSnackbar) {
            delay(5000)
            showSnackbar = false
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth().height(330.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (showSnackbar) {
            Snackbar(
            ) {
                Text(text = snackbarText, style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
                )
            }
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(Color.White, shape = RoundedCornerShape(8.dp)),
        ) {
            Column(
                modifier = Modifier.align(Alignment.TopCenter).padding(top = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    Text("입찰",
                        modifier = Modifier.clickable { selectedTab = "입찰" },
                        fontSize = 18.sp,
                        color = if (selectedTab == "입찰") Color.Black else Color.Gray)
                    Spacer(modifier = Modifier.weight(0.8f))
                    Text("경매",
                        modifier = Modifier.clickable { selectedTab = "경매" },
                        fontSize = 18.sp,
                        color = if (selectedTab == "경매") Color.Black else Color.Gray)
                    Spacer(modifier = Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(10.dp))
                Divider()
            }

            // 선택된 탭에 따른 내용
            when (selectedTab) {
                "입찰" -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        if (myBids.isEmpty()) {
                            Text(text = "등록된 입찰이 없습니다.", fontSize = 24.sp)
                        } else {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(top = 50.dp)
                            ) {
                                myBids.take(3).forEach { item ->
                                    val itemInteractionState = remember { MutableInteractionSource() }
                                    val itemIsPressed by itemInteractionState.collectIsPressedAsState()

                                    Box(
                                        modifier = Modifier.fillMaxWidth()
                                            .height(85.dp)
                                            .padding(horizontal = 10.dp)
                                            .clickable(
                                                interactionSource = itemInteractionState,
                                                indication = rememberRipple(bounded = true),
                                                onClick = {
                                                    navController.navigate("AuctionDetailPage/${item.auctionItemData.auctionIdx}")
                                                }
                                            )
                                            .background(
                                                if (itemIsPressed) Color.LightGray else Color.Transparent
                                            )
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Start,
                                            modifier = Modifier.padding(8.dp)
                                        ) {
                                            val painter =
                                                rememberAsyncImagePainter(model = item.auctionItemData.gifticonDataImageName)
                                            Image(
                                                painter = painter,
                                                contentDescription = null,
                                                modifier = Modifier
                                                    .size(85.dp)
                                                    .align(Alignment.CenterVertically)
                                            )
                                            Spacer(modifier = Modifier.width(25.dp))
                                            Column(
                                                modifier = Modifier.align(Alignment.CenterVertically)
                                            ) {
                                                Text(item.auctionItemData.gifticonName?: "defaultText", fontSize = 18.sp)
                                                Spacer(modifier = Modifier.height(5.dp))
                                                Text(
                                                    "최고 입찰가: ${item.auctionItemData.auctionHighestBid}원 (${if (item.auctionItemData.auctionHighestBid == item.auctionBidPrice) "(본인)" else "(타인)"})",
                                                    fontSize = 18.sp
                                                )
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(10.dp))
                                }
                            }
                        }
                    }
                }
                "경매" -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        if (myAuctionItems.isEmpty()) {
                            Text(text = "등록된 경매가 없습니다.", fontSize = 24.sp)
                        } else {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(top = 50.dp)
                            ) {
                                myAuctionItems.take(3).forEach { item ->
                                    val itemInteractionState =
                                        remember { MutableInteractionSource() }
                                    val itemIsPressed by itemInteractionState.collectIsPressedAsState()

                                    Box(
                                        modifier = Modifier.fillMaxWidth()
                                            .height(85.dp)
                                            .padding(horizontal = 10.dp)
                                            .clickable(
                                                interactionSource = itemInteractionState,
                                                indication = rememberRipple(bounded = true),
                                                onClick = {
                                                    navController.navigate("AuctionDetailPage/${item.auctionIdx}")
                                                }
                                            )
                                            .background(
                                                if (itemIsPressed) Color.LightGray else Color.Transparent
                                            )
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Start,
                                            modifier = Modifier.padding(8.dp)
                                        ) {
                                            val painter =
                                                rememberAsyncImagePainter(model = item.gifticonDataImageName)
                                            Image(
                                                painter = painter,
                                                contentDescription = null,
                                                modifier = Modifier
                                                    .size(85.dp)
                                                    .align(Alignment.CenterVertically)
                                            )
                                            Spacer(modifier = Modifier.width(25.dp))
                                            Column(
                                                modifier = Modifier.align(Alignment.CenterVertically)
                                            ) {
                                                Text("${item.gifticonName}", fontSize = 18.sp)
                                                Spacer(modifier = Modifier.height(5.dp))
                                                Text(
                                                    "최고 입찰가: ${item.auctionHighestBid}원",
                                                    fontSize = 18.sp
                                                )
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(10.dp))
                                }
                            }
                        }
                    }
                }
            }
            Button(onClick = {}, modifier = Modifier.align(Alignment.BottomEnd).padding(10.dp)) {
                Text("더보기", fontSize = 16.sp)
            }
        }
    }
}


@Composable
fun HomeLayout5() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .background(Color.White, shape = RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text("여기부터 알고리즘 인기")
    }
}

@Composable
fun HomeLayout6() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .background(Color.White, shape = RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text("알고리즘 인기 2")
    }
}

@Composable
fun HomeLayout7() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .background(Color.White, shape = RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text("알고리즘 인기 3")
    }
}

@Composable
fun HomeLayout8() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(Color.White, shape = RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text("약관 등등")
    }
}