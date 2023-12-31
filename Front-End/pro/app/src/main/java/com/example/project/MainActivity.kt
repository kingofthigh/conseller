package com.example.project

import InquiryPage
import MypageAuction
import MypageStore
import PermissionRequester
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.project.sharedpreferences.SharedPreferencesUtil
import com.example.project.viewmodels.MygifticonViewModel
import dagger.hilt.android.AndroidEntryPoint

val customBackgroundColor = Color(245, 245, 245)

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val sharedPreferencesUtil by lazy { SharedPreferencesUtil(this) }

    private val myGifticonViewModel: MygifticonViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContent {
            AppNavigation(intent,sharedPreferencesUtil, myGifticonViewModel)
        }
    }
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setContent {
            AppNavigation(intent,sharedPreferencesUtil, myGifticonViewModel)
        }
    }
}

@Composable
fun AppNavigation(intent:Intent?,sharedPreferencesUtil: SharedPreferencesUtil, myGifticonViewModel: MygifticonViewModel) {

    val startDestination = when {
        !sharedPreferencesUtil.isPermissionsChecked() -> "CheckPermission"
        sharedPreferencesUtil.isLoggedIn() -> "Login"
        else -> "TextLoginPage"
    }


    val navController = rememberNavController()
    LaunchedEffect(intent?.action) {
        if (intent?.action == "OPEN_MYPAGE_COMPOSABLE") {
            navController.navigate("MyPage")
        }
    }
    Surface(modifier = Modifier.fillMaxSize(), color = customBackgroundColor) {
        Column {
            val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route
            if (currentDestination != "Login" && currentDestination != "SignUp" &&
                currentDestination != "TextLoginPage" && currentDestination != "MakePatternPage" &&
                currentDestination != "CheckPermission") {
                TopBar(navController)
            }
            Box(modifier = Modifier.weight(1f)) {
                NavHost(navController, startDestination = startDestination) {
                    composable("CheckPermission") { CheckPermission(navController, sharedPreferencesUtil)}
                    composable("PermissionRequesterPage") { PermissionRequester(navController)}
                    composable("Login") { LoginPage(navController, sharedPreferencesUtil) }
                    composable("SignUp") { SignUpPage(navController) }
                    composable("TextLoginPage") { TextLoginPage(navController) }
                    composable("com.example.project.FindIdPage"){ FindIdPage(navController)}
                    composable("FindPwPage"){ FindPwPage(navController)}
                    composable("MakePatternPage"){ MakePatternPage(navController, sharedPreferencesUtil)}
                    // top bar
                    composable("AlertPage") { AlertPage() }
                    // bottom bar
                    composable("Home") { HomePage(navController = navController) }
                    composable("MyPage") { MyPage(navController = navController) }
                    composable("MyPageModify") { MyPageModifyPage(navController = navController) }
                    composable("MyModifyPageValidPage") { MyModifyPageValidPage(navController = navController) }
                    composable("MyDeletePageValidPage") { MyDeletePageValidPage(navController = navController) }
                    composable("MypageCoupon") { MypageCoupon(navController = navController) }
                    composable("MypageSellCoupon") { MypageSellCoupon(navController = navController) }
                    composable("MyPageCouponDetail/{index}") { backStackEntry ->
                        val gifticonIdx = backStackEntry.arguments?.getString("index")
                        MyPageCouponDetail(navController, gifticonIdx)
                    }
                    composable("MypageBarter") { MypageBarter(navController = navController) }
                    composable("MypageStore") { MypageStore(navController = navController) }
                    composable("MypageAuction") { MypageAuction(navController = navController) }
                    composable("MyGifticonAdd") { GifticonAddPage(navController = navController) }
                    composable("MypageDelete") { MypageDelete(navController = navController) }
                    composable("MyGifticonAddDetail") { GifticonAddDetailPage(navController = navController) }
                    composable("SearchPage") { SearchPage() }
                    composable("MySalesPage") { MySalesPage(navController = navController) }
                    composable("MyPurchasePage") { MyPurchasePage(navController = navController) }
                    // 신고하기
                    composable("Inquiry/{opponentIdx}") { backStackEntry ->
                        val opponentIdx = backStackEntry.arguments?.getString("opponentIdx")
                        InquiryPage(navController,opponentIdx)
                    }

                    // 스토어
                    composable("StorePage") { StorePage(navController = navController) }
                    composable("StoreDetailPage/{storeIdx}") { backStackEntry ->
                        val storeIdx = backStackEntry.arguments?.getString("storeIdx")
                        StoreDetailPage(navController, storeIdx)
                    }
                    composable("StoreCreatePage") { StoreCreatePage(navController,myGifticonViewModel) }
                    composable("StoreCreateDetailPage/{selectedItemIndex}") { backStackEntry ->
                        val selectedItemIndex = backStackEntry.arguments?.getString("selectedItemIndex")
                        StoreCreateDetailPage(navController, selectedItemIndex,myGifticonViewModel)
                    }
                    composable("storeUpdate/{storeIdx}") { backStackEntry ->
                        val storeIdx = backStackEntry.arguments?.getString("storeIdx")
                        StoreUpdatePage(navController, storeIdx)
                    }
                    composable("StoreTradePage/{storeIdx}") { backStackEntry ->
                        val storeIdx = backStackEntry.arguments?.getString("storeIdx")
                        StoreTradePage(storeIdx, navController)
                    }
                    composable("storeConfirmPage/{storeIdx}") { backStackEntry ->
                        val storeIdx = backStackEntry.arguments?.getString("storeIdx")
                        StoreConfirmPage(navController, storeIdx)
                    }


                    // 경매
                    composable("AuctionPage") { AuctionPage(navController) }
                    composable("AuctionDetailPage/{index}") { backStackEntry ->
                        val auctionIdx = backStackEntry.arguments?.getString("index")
                        AuctionDetailPage(navController, auctionIdx)
                    }
                    composable("AuctionCreatePage") { AuctionCreatePage(navController,myGifticonViewModel) }
                    composable("AuctionCreateDetailPage/{selectedItemIndex}") { backStackEntry ->
                        val selectedItemIndex = backStackEntry.arguments?.getString("selectedItemIndex")
                        AuctionCreateDetailPage(navController, selectedItemIndex,myGifticonViewModel)
                    }
                    composable("auctionUpdate/{auctionIdx}") { backStackEntry ->
                        val auctionIdx = backStackEntry.arguments?.getString("auctionIdx")
                        AuctionUpdatePage(navController, auctionIdx)
                    }
                    composable("AuctionTradePage/{auctionIdx}") { backStackEntry ->
                        val auctionIdx = backStackEntry.arguments?.getString("auctionIdx")
                        AuctionTradePage(auctionIdx, navController)
                    }
                    composable("AuctionConfirmPage/{auctionIdx}") { backStackEntry ->
                        val auctionIdx = backStackEntry.arguments?.getString("auctionIdx")
                        AuctionConfirmPage(navController, auctionIdx)
                    }
                    composable("AuctionConfirmBuyPage/{auctionIdx}") { backStackEntry ->
                        val auctionIdx = backStackEntry.arguments?.getString("auctionIdx")
                        auctionConfirmBuyPage(navController, auctionIdx)
                    }


                    // 물물
                    composable("BarterPage") { BarterPage(navController) }
                    composable("BarterDetailPage/{barterIdx}") { backStackEntry ->
                        val barterIdx = backStackEntry.arguments?.getString("barterIdx")
                        BarterDetailPage(barterIdx, navController)
                    }
                    composable("BarterCreatePage") { BarterCreatePage(navController,myGifticonViewModel) }
                    composable("BarterCreateDetailPage/{selectedItemIndices}") { backStackEntry ->
                        val selectedItemIndicesString = backStackEntry.arguments?.getString("selectedItemIndices") ?: ""
                        val selectedItemIndicesList = selectedItemIndicesString.split(",").map { it.toLongOrNull() }.filterNotNull()
                        BarterCreateDetailPage(navController, selectedItemIndicesList, myGifticonViewModel)
                    }
                    composable("barterUpdate/{barterIdx}") { backStackEntry ->
                        val barterIdx = backStackEntry.arguments?.getString("barterIdx")
                        BarterUpdatePage(barterIdx, navController)
                    }
                    composable("BarterTradeSelectPage/{barterIdx}") { backStackEntry ->
                        val barterIdx = backStackEntry.arguments?.getString("barterIdx")
                        BarterTradeSelectPage(barterIdx, navController, myGifticonViewModel)
                    }
                    composable("BarterTradePage/{selectedItemIndices}/{barterIdx}") { backStackEntry ->
                        val selectedItemIndicesString = backStackEntry.arguments?.getString("selectedItemIndices") ?: ""
                        val selectedItemIndicesList = selectedItemIndicesString.split(",").map { it.toLongOrNull() }.filterNotNull()
                        val barterIdx = backStackEntry.arguments?.getString("barterIdx")
                        BarterTradePage(navController, selectedItemIndicesList, barterIdx, myGifticonViewModel)
                    }
                    composable("barterConfirmPage/{barterIdx}") { backStackEntry ->
                        val barterIdx = backStackEntry.arguments?.getString("barterIdx")
                        BarterConfirmPage(navController, barterIdx)
                    }

                    // 거래 완료
                    composable("WaitingPage") { WaitingPage(navController) }
                    // 이벤트
                    composable("EventPage") { EventPage(navController = navController) }

                }
            }
            if (currentDestination != "Login" && currentDestination != "SignUp" &&
                currentDestination != "TextLoginPage" && currentDestination != "MakePatternPage" &&
                currentDestination != "CheckPermission") {
                BottomBar(navController)
            }
        }
    }
}


