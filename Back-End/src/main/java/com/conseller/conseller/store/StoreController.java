package com.conseller.conseller.store;

import com.conseller.conseller.notification.NotificationService;
import com.conseller.conseller.store.dto.request.ModifyStoreRequest;
import com.conseller.conseller.store.dto.request.RegistStoreRequest;
import com.conseller.conseller.store.dto.request.StoreListRequest;
import com.conseller.conseller.store.dto.response.DetailStoreResponse;
import com.conseller.conseller.store.dto.response.StoreListResponse;
import com.conseller.conseller.store.dto.response.StoreTradeResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/store")
public class StoreController {

    private final StoreService storeService;
    private final NotificationService notificationService;

    private final String SUCCESS = "success";

    // 스토어 목록
    @PostMapping
    public ResponseEntity<StoreListResponse> getStoreList(@RequestBody StoreListRequest request) {
            StoreListResponse response = storeService.getStoreList(request);

        return ResponseEntity.ok()
                .body(response);
    }

    // 스토어 글 등록
    @PostMapping("/regist")
    public ResponseEntity<Object> registStore(@RequestBody RegistStoreRequest request) {
        storeService.registStore(request);

        return ResponseEntity.ok()
                .build();
    }

    // 스토어 글 상세보기
    @GetMapping("/{store_idx}")
    public ResponseEntity<DetailStoreResponse> detailStore(@PathVariable("store_idx") Long storeIdx) {
        DetailStoreResponse detailStoreResponse = storeService.detailStore(storeIdx);

        return ResponseEntity.ok()
                .body(detailStoreResponse);
    }

    // 스토어 글 수정
    @PatchMapping("/{store_idx}")
    public ResponseEntity<Object> modifyStore(@PathVariable("store_idx") Long storeIdx, @RequestBody ModifyStoreRequest storeRequest) {
        storeService.modifyStore(storeIdx, storeRequest);
        return ResponseEntity.ok()
                .build();
    }

    // 스토어 글 삭제
    @DeleteMapping("/{store_idx}")
    public ResponseEntity<Object> deleteStore(@PathVariable("store_idx") Long storeIdx) {
        storeService.deleteStore(storeIdx);
        return ResponseEntity.ok()
                .build();
    }

    // 스토어 거래 진행
    @GetMapping("/trade/{store_idx}/{consumer_idx}")
    public ResponseEntity<StoreTradeResponse> tradeStore(@PathVariable("store_idx") Long storeIdx,
                                                         @PathVariable("consumer_idx") Long consumerIdx) {
        StoreTradeResponse response = storeService.tradeStore(storeIdx, consumerIdx);

        //판매자에게 알림
        notificationService.sendStoreNotification(storeIdx, "스토어 거래 진행", "거래 진행 중입니다.", 2, 2);

        return ResponseEntity.ok()
                .body(response);
    }

    // 스토어 거래 진행 취소
    @PatchMapping("/cancel/{store_idx}")
    public ResponseEntity<Object> cancelStore(@PathVariable("store_idx") Long storeIdx) {
        storeService.cancelStore(storeIdx);

        // 판매자 구매자 알림
        notificationService.sendStoreNotification(storeIdx, "스토어 거래 취소", "스토어 거래 취소입니다.", 1, 2);
        notificationService.sendStoreNotification(storeIdx, "스토어 거래 취소", "스토어 거래 취소입니다.", 2, 2);

        return ResponseEntity.ok()
                .build();
    }

    // 스토어 입금 완료
    @PatchMapping("/complete/{store_idx}")
    public ResponseEntity<Object> completeStore(@PathVariable("store_idx") Long storeIdx) {
        // 판매자에게 알림
        notificationService.sendStoreNotification(storeIdx, "스토어 입금 완료", "입금 완료입니다.", 2, 2);

        return ResponseEntity.ok()
                .build();
    }

    // 스토어 거래 확정
    @PatchMapping("/confirm/{store_idx}")
    public ResponseEntity<Object> confirmStore(@PathVariable("store_idx") Long storeIdx) {
        storeService.confirmStore(storeIdx);

        // 구매자에게 알림
        notificationService.sendStoreNotification(storeIdx, "스토어 거래 완료", "거래가 완료되었습니다.", 1, 2);

        return ResponseEntity.ok()
                .build();
    }


}