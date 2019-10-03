package com.example.android.cardemulation;

public interface AIDInfo {
    String SAMPLE_LOYALTY_CARD_AID = "F111111111";
    String SAMPLE_TEST_AID = "E000000000";
    String ANDROID_TEST_AID = "F0010203040506";
    String CMD_SUCCESS = "9000";
    String CMD_UNKNOWN = "0000";
    String CMD_CARD_BROKEN = "6E87";
    String CMD_CUSTOMER_STATUS_CODE = "9487";
    String SELECT_APDU_HEADER_CLA_INS_P1_P2 = "00A40400"; //00A40400
    String UPDATE_APDU_HEADER_CLA_INS_P1_P2 = "00B40400";
    String TEST_APDU_HEADER_CLA_INS_P1_P2 =   "00A40500";
    String ON_DUTY_TIME = "ON_DUTY_TIME";
//    String OFF_DUTY_TIME = "OFF_DUTY_TIME";
}
