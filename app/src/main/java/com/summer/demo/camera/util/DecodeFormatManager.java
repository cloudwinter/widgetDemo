/*
 * Copyright (C) 2010 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.summer.demo.camera.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;

public class DecodeFormatManager {

    // 1D解码
    private static final Set<BarcodeFormat> PRODUCT_FORMATS;
    private static final Set<BarcodeFormat> INDUSTRIAL_FORMATS;
    private static final Set<BarcodeFormat> ONE_D_FORMATS;

    // 二维码解码
    private static final Set<BarcodeFormat> QR_CODE_FORMATS;

    static {
        PRODUCT_FORMATS = EnumSet.of(BarcodeFormat.UPC_A, BarcodeFormat.UPC_E, BarcodeFormat.EAN_13,
                BarcodeFormat.EAN_8, BarcodeFormat.RSS_14, BarcodeFormat.RSS_EXPANDED);
        INDUSTRIAL_FORMATS = EnumSet.of(BarcodeFormat.CODE_39, BarcodeFormat.CODE_93, BarcodeFormat
                .CODE_128, BarcodeFormat.ITF, BarcodeFormat.CODABAR);
        ONE_D_FORMATS = EnumSet.copyOf(PRODUCT_FORMATS);
        ONE_D_FORMATS.addAll(INDUSTRIAL_FORMATS);

        QR_CODE_FORMATS = EnumSet.of(BarcodeFormat.QR_CODE);
    }

    public static Collection<BarcodeFormat> getQrCodeFormats() {
        return QR_CODE_FORMATS;
    }

    public static Collection<BarcodeFormat> getBarCodeFormats() {
        return ONE_D_FORMATS;
    }

    public static Map<DecodeHintType, Object> getHints() {
        Map<DecodeHintType, Object> hints = new EnumMap<DecodeHintType, Object>(DecodeHintType.class);
        Collection<BarcodeFormat> decodeFormats = new ArrayList<BarcodeFormat>();
        decodeFormats.addAll(EnumSet.of(BarcodeFormat.AZTEC));
        decodeFormats.addAll(EnumSet.of(BarcodeFormat.PDF_417));
        decodeFormats.addAll(DecodeFormatManager.getBarCodeFormats());
        decodeFormats.addAll(DecodeFormatManager.getQrCodeFormats());
        hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);
        return hints;
    }


}
