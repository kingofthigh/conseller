package com.conseller.conseller.store;

import com.conseller.conseller.entity.Store;
import com.conseller.conseller.store.dto.request.ModifyStoreRequest;
import com.conseller.conseller.store.dto.request.RegistStoreRequest;
import com.conseller.conseller.store.dto.request.StoreListRequest;
import com.conseller.conseller.store.dto.response.DetailStoreResponse;
import org.springframework.data.domain.Page;

public interface StoreService {
    public Page<Store> getStoreList(StoreListRequest request);

    public void registStore(RegistStoreRequest request);

    public DetailStoreResponse detailStore(Long storeIdx);

    public void modifyStore(Long storeIdx , ModifyStoreRequest storeRequest);

    public void deleteStore(Long storeIdx);

    public void storeStatusPermute(Long storeIdx);

}
