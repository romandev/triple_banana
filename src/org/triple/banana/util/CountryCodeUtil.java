// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

import org.banana.cake.interfaces.BananaApplicationUtils;

import java.lang.reflect.Method;

/**
 * This class provides a way to get country code from device.
 * Most of code comes from the following link:
 *   - https://stackoverflow.com/questions/35789693/android-get-device-country-code
 */
public class CountryCodeUtil {
    private static final String FALLBACK = "US";
    public static String getCountryCode() {
        Context context = BananaApplicationUtils.get().getApplicationContext();
        TelephonyManager tm =
                (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null) return FALLBACK;

        String countryCode = null;

        // Query first getSimCountryIso()
        countryCode = tm.getSimCountryIso();
        if (countryCode != null && countryCode.length() == 2) return countryCode.toUpperCase();

        if (tm.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA) {
            // Special case for CDMA Devices
            countryCode = getCDMACountryIso();
        } else {
            // For 3G devices (with SIM) query getNetworkCountryIso()
            countryCode = tm.getNetworkCountryIso();
        }
        if (countryCode != null && countryCode.length() == 2) return countryCode.toUpperCase();

        // If network country not available(e.g. tablets), get country code from Locale class
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            countryCode =
                    context.getResources().getConfiguration().getLocales().get(0).getCountry();
        } else {
            countryCode = context.getResources().getConfiguration().locale.getCountry();
        }

        if (countryCode != null && countryCode.length() == 2) return countryCode.toUpperCase();

        return FALLBACK;
    }

    @SuppressLint("PrivateApi")
    private static String getCDMACountryIso() {
        try {
            // Try to get country code from SystemProperties private class
            Class<?> systemProperties = Class.forName("android.os.SystemProperties");
            Method get = systemProperties.getMethod("get", String.class);

            // Get homeOperator that contain MCC + MNC
            String homeOperator =
                    ((String) get.invoke(systemProperties, "ro.cdma.home.operator.numeric"));

            // First 3 chars (MCC) from homeOperator represents the country code
            int mcc = Integer.parseInt(homeOperator.substring(0, 3));

            // Mapping just countries that actually use CDMA networks
            switch (mcc) {
                case 330:
                    return "PR";
                case 310:
                    return "US";
                case 311:
                    return "US";
                case 312:
                    return "US";
                case 316:
                    return "US";
                case 283:
                    return "AM";
                case 460:
                    return "CN";
                case 455:
                    return "MO";
                case 414:
                    return "MM";
                case 619:
                    return "SL";
                case 450:
                    return "KR";
                case 634:
                    return "SD";
                case 434:
                    return "UZ";
                case 232:
                    return "AT";
                case 204:
                    return "NL";
                case 262:
                    return "DE";
                case 247:
                    return "LV";
                case 255:
                    return "UA";
            }
        } catch (Exception e) {
        }

        return null;
    }
}
