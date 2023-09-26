package com.example.project.api

import com.example.project.viewmodels.BarterItemData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface BarterService {

    // 전체 목록 API
    @POST("barterItems/all")
    suspend fun getAllBarterItems(
        @Body filter: BarterFilterDTO,
    ): Response<BarterResponse>

    // 내가 등록한 물물 목록 API
    @GET("api/barter/{userIdx}")
    suspend fun getMyBarterItems(
        @Path("userIdx") userIdx: Long,
    ): Response<BarterResponse>

    // 물물교환 등록 API
    @POST("barterItems/regist")
    suspend fun createBarterItem(
        @Body filter: BarterCreateDTO
    ): Response<CreateBarterResponse>

    // 물물교환 수정 API
    @PATCH("api/barter/{barterIdx}")
    suspend fun updateBarterItem(
        @Path("barterIdx") auctionIdx: Long,
        @Body updateData: UpdateBarterDTO
    ): Response<Void>

    // 물물교환 삭제 API
    @DELETE("api/barter/{barterIdx}")
    suspend fun deleteBarterItem(
        @Path("barterIdx") barterIdx: Long
    ): Response<Void>

    // 물물교환 상세보기 API
    @GET("api/barter/detail/{barterIdx}")
    suspend fun getBarterDetail(
        @Path("barterIdx") barterIdx: Long
    ): Response<BarterDetailResponseDTO>

    // 물물교환 거래제안 API
    @POST("api/barter/trade/{barterIdx}")
    suspend fun proposeBarterTrade(
        @Path("barterIdx") barterIdx: Long,
        @Body tradeRequest: TradeBarterRequestDTO
    ): Response<Void>

}

// 목록, 검색 요청 DTO
data class BarterFilterDTO(
    val mainCategory: Int,    // 대분류
    val subCategory: Int,    // 소분류
    val status: Int,           // 상태
    val searchQuery: String? = null, // 검색 쿼리. 검색 API 사용 시에만 값이 있음.
    val page: Int                 // 페이지 정보
)

// 목록, 검색 응답 DTO
data class BarterResponse(
    val totalNum: Int,
    val items: List<BarterItemData>
)

// 물물교환 등록 요청 DTO
data class BarterCreateDTO(
    val mainCategory: Int,
    val subCategory: Int,
    val barterName: String,
    val barterText: String,
    val barterEndDate: String,
    val selectedItemIndices: List<Long>,
    val userIdx: Long,
)

// 물물교환 등록 응답 DTO
data class CreateBarterResponse(
    val barterIdx: Long, // 생성된 물물교환 게시글의 idx주세요
)

// 물물교환 수정 요청 DTO
// 물물교환 수정 응답 DTO = http 형식
data class UpdateBarterDTO(
    val mainCategory: Int,
    val subCategory: Int,
    val barterName: String,
    val barterText: String,
    val barterEndDate: String,
)


// 물물교환 삭제 요청 DTO = Path형식
// 물물교환 삭제 응답 DTO = http 형식


// 물물교환 상세보기 요청 DTO = Path형식
// 물물교환 상세보기 응답 DTO
data class BarterDetailResponseDTO(
    val barterName: String,                // 게시글 제목
    val barterText: String,              // 게시글 내용
    val barterUserIdx: Long,           // 게시글 유저 idx
    val barterUserProfileUrl: String,   // 게시글 유저 사진
    val barterUserDeposit: Long,        // 게시글 유저 보증금
    val barterUserNickname: String,    // 게시글 유저 닉네임
)

// 물물교환 거래제안 요청 DTO
// 물물교환 거래제안 응답 DTO = http형식
data class TradeBarterRequestDTO(
    val selectedItemIndices: List<Long>
)