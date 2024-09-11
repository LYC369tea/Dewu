package com.lyc.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lyc.entity.Shoe;

public interface RedisService {
    void saveShoeInfo(Shoe shoe,String barcode);

    Shoe getShoeInfo(String barcode) ;
}
