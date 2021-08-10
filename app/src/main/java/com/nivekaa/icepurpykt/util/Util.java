package com.nivekaa.icepurpykt.util;

import android.content.Context;
import android.graphics.Typeface;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Util {
    public static final String PENDO_API_KEY = "eyJhbGciOiJSUzI1NiIsImtpZCI6IiIsInR5cCI6IkpXVCJ9.eyJkYXRhY2VudGVyIjoidXMiLCJrZXkiOiI4N2VkOThkNjkxNTk2Mzg4ZmMwOGNiNDliMDMyZWE3YzEzYzYzNzQyMmE1M2IyNzllYzVkZGNkODU2OGQzYWM3YmRlN2JhZjg0M2EzNTIzNmI3ZjM0YmRlY2M3ZDJjYTQ3ZGU4YTk1YWRkNTY4YzliNGVlNWRiNmMxM2YzN2Q0MDc4Y2I4YWJiMDkxZWE4M2U4YzliOTA3NzEzMjY1MjM2LjgxYTRiYWNlYTJiZGJmMTJjZDg3MzM5OWZiYjIxNjcwLmZkOGU3NTI2ZmM3MjU2MTVjM2Q0NTExYmMyN2VmMjNkOWVlODAzMjA2ZTdiOThmOGUwY2M3MmUwYjQzZTAyZDYifQ.K7Psx-2i6wIrgA1zDtWqwJ3aPAw-4emtuw_w--4ja8oe3nzrlPtrMr5nSmHEf6lLDvO8BwztqZjyC_3VFlWvX8GydXxBVzioqUGZIQL_tfKFvMDR6yqRVLtjyJg51WnPcH5t6Tw1XHDu_R-fpoVkiEYcUXs5DLP3YqKff53HiZc";
    public static final String ATTRIBUTE_TTF_KEY = "ttf_name";
    public static final String ATTRIBUTE_SCHEMA = "http://schemas.android.com/apk/lib/com.nivekaa.ecommerce.util";
    public static final long DELIVERY_PRICE = 100L;
    private static final Map<String, Typeface> TYPEFACE = new HashMap<String, Typeface>();


    // --- METHODS ---- //

    public static Typeface getFonts(Context context, String fontName) {
        Typeface typeface = TYPEFACE.get(fontName);
        if (typeface == null) {
            typeface = Typeface.createFromAsset(context.getAssets(), "font/"
                    + fontName);
            TYPEFACE.put(fontName, typeface);
        }
        return typeface;
    }

    public static Float avoidNullable(Float currentValue) {
        if (currentValue == null)
            return 0F;
        else
            return currentValue;
    }

    public static Float getFloatValAvoidingNullable(BigDecimal val) {
        if (val == null)
            return 0F;
        val.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        return val.floatValue();
    }
}
