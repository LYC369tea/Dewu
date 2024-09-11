package com.lyc.service;

import com.lyc.entity.Shoe;

public interface ImageRecognizeService {
    /*Shoe recognizeImage(String imageUrl, String zxingBarcode);*/

    Shoe recognizeImage(String  smallFile, String zxingBarcode);
}
