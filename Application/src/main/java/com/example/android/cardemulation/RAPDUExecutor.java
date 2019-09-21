package com.example.android.cardemulation;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.android.Utils.TimeUtils;
import com.example.android.Utils.Utils;
import com.example.android.common.logger.Log;

import java.util.Arrays;

import static com.example.android.cardemulation.AIDInfo.ANDROID_TEST_AID;
import static com.example.android.cardemulation.AIDInfo.SAMPLE_LOYALTY_CARD_AID;
import static com.example.android.cardemulation.AIDInfo.SAMPLE_TEST_AID;
import static com.example.android.cardemulation.AIDInfo.SELECT_APDU_HEADER_CLA_INS_P1_P2;
import static com.example.android.cardemulation.AIDInfo.TEST_APDU_HEADER_CLA_INS_P1_P2;
import static com.example.android.cardemulation.AIDInfo.UPDATE_APDU_HEADER_CLA_INS_P1_P2;

public class RAPDUExecutor {
    private static final String TAG = "RAPDUExecutor";
    private static String isOnDuty = "Off Duty";
    private static long punchTime = -1;

    private static SharedPreferences punchTimeCache;

    static synchronized byte[] testRAPDU(byte[] commandApdu, Context context) {
        punchTimeCache = PreferenceManager.getDefaultSharedPreferences(context);
        if(punchTimeCache == null){
            return ConcatArrays("CardBroken".getBytes(),Utils.textToByteArray(String.valueOf(AIDInfo.CMD_CARD_BROKEN)));
        }
        Log.d(TAG, "commandApdu: " + Utils.byte2hex(commandApdu));
        if (Arrays.equals(BuildSelectApdu(SAMPLE_LOYALTY_CARD_AID), commandApdu)) {
            String account = AccountStorage.GetAccount(context);
            byte[] accountBytes = account.getBytes();
            Log.i(TAG, "Sending account number: " + account);
            return ConcatArrays(accountBytes, Utils.textToByteArray(String.valueOf(AIDInfo.CMD_SUCCESS)));
        } else if (Arrays.equals(BuildSelectApdu(SAMPLE_TEST_AID), commandApdu)) {

            punchTime = punchTimeCache.getLong(AIDInfo.ON_DUTY_TIME, -1);
            if (punchTime != -1) {
                long currentTime = TimeUtils.timeStamp();
                long workingTime = currentTime - punchTime;
                if (workingTime < 28800000) {
                    isOnDuty = "Go back to your cube!!";
                    return ConcatArrays(isOnDuty.getBytes(), Utils.textToByteArray(String.valueOf(AIDInfo.CMD_CUSTOMER_STATUS_CODE)));
                } else {
                    isOnDuty = "Off Duty";
                    String offDutyTime = TimeUtils.getTime(TimeUtils.TimeType.YMDHm);
                    String payload = isOnDuty + offDutyTime + "Chihyuj";
                    punchTimeCache.edit().clear().apply();
                    punchTime = 0;
                    return ConcatArrays(payload.getBytes(), Utils.textToByteArray(String.valueOf(AIDInfo.CMD_SUCCESS)));
                }
            } else {
                punchTimeCache.edit().putLong(AIDInfo.ON_DUTY_TIME, TimeUtils.timeStamp()).apply();
                isOnDuty = "Punch Time";
                String punchTime = TimeUtils.getTime(TimeUtils.TimeType.YMDHm);
                String payload = isOnDuty + punchTime + "Chinyuj";
                return ConcatArrays(payload.getBytes(), Utils.textToByteArray(String.valueOf(AIDInfo.CMD_SUCCESS)));
            }
            //return ConcatArrays("haha".getBytes(),Utils.textToByteArray(String.valueOf(AIDInfo.CMD_SUCCESS)));
        } else {
            String txt = "no_this_aid";
            return ConcatArrays(txt.getBytes(), Utils.textToByteArray(AIDInfo.CMD_UNKNOWN));
        }
    }


    /**
     * Utility method to concatenate two byte arrays.
     *
     * @param first First array
     * @param rest  Any remaining arrays
     * @return Concatenated copy of input arrays
     */
    private static byte[] ConcatArrays(byte[] first, byte[]... rest) {
        int totalLength = first.length;
        for (byte[] array : rest) {
            totalLength += array.length;
        }
        byte[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;
        for (byte[] array : rest) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        //  Log.d(TAG, "contact array: " + Utils.byte2hex(result));
        return result;
    }


    /**
     * Build APDU for SELECT AID command. This command indicates which service a reader is
     * interested in communicating with. See ISO 7816-4.
     *
     * @param aid Application ID (AID) to select
     * @return APDU for SELECT AID command
     */
    private static byte[] BuildSelectApdu(String aid) {
        // Format: [CLASS | INSTRUCTION | PARAMETER 1 | PARAMETER 2 | LENGTH | DATA]
        Log.d(TAG, "build select apdu: " + aid);
        if (aid == null) {
            return null;
        }
        String cmd = "";

        switch (aid) {
            case SAMPLE_LOYALTY_CARD_AID:
                cmd = TEST_APDU_HEADER_CLA_INS_P1_P2 + String.format("%02X", aid.length() / 2) + aid;
                break;
            case SAMPLE_TEST_AID:
                cmd = SELECT_APDU_HEADER_CLA_INS_P1_P2 + String.format("%02X", aid.length() / 2) + aid;
                break;
            case ANDROID_TEST_AID:
                cmd = UPDATE_APDU_HEADER_CLA_INS_P1_P2 + String.format("%02X", aid.length() / 2) + aid;
                break;
            default:
                break;
        }
        return Utils.textToByteArray(cmd);
    }

}
