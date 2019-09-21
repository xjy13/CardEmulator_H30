/*
 * Copyright (C) 2013 The Android Open Source Project
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

package com.example.android.cardemulation;

import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;

import com.example.android.Utils.Utils;
import com.example.android.common.logger.Log;

import java.util.Arrays;

/**
 * This is a sample APDU Service which demonstrates how to interface with the card emulation support
 * added in Android 4.4, KitKat.
 *
 * <p>This sample replies to any requests sent with the string "Hello World". In real-world
 * situations, you would need to modify this code to implement your desired communication
 * protocol.
 *
 * <p>This sample will be invoked for any terminals selecting AIDs of 0xF11111111, 0xF22222222, or
 * 0xF33333333. See src/main/res/xml/aid_list.xml for more details.
 *
 * <p class="note">Note: This is a low-level interface. Unlike the NdefMessage many developers
 * are familiar with for implementing Android Beam in apps, card emulation only provides a
 * byte-array based communication channel. It is left to developers to implement higher level
 * protocol support as needed.
 */
public class CardService extends HostApduService {
    private static final String TAG = "CardService";
    // AID for our loyalty card service.
//    private static final String SAMPLE_LOYALTY_CARD_AID = "F111111111"; //F222222222
//    private static final String SAMPLE_TEST_AID = "E000000000";
//    private static final String ANDROID_TEST_AID = "F0010203040506";
    // ISO-DEP command HEADER for selecting an AID.
    // Format: [Class | Instruction | Parameter 1 | Parameter 2]
//    private static final String SELECT_APDU_HEADER_CLA_INS_P1_P2 = "00A40400"; //00A40400
//    private static final String UPDATE_APDU_HEADER_CLA_INS_P1_P2 = "00B40400";
//    private static final String TEST_APDU_HEADER_CLA_INS_P1_P2 = "00C40400";
    // "OK" status word sent in response to SELECT AID command (0x9000)
//    private static final byte[] SELECT_OK_SW = Utils.textToByteArray("9000");
    // "UNKNOWN" status word sent in response to invalid APDU command (0x0000)
//    private static final byte[] UNKNOWN_CMD_SW = Utils.textToByteArray("0000");
//    private static final byte[] TEST_RAPDU_STATUSCODE = Utils.textToByteArray("9487");
//    private static final byte[] SELECT_APDU = BuildSelectApdu(SAMPLE_LOYALTY_CARD_AID);
//    private static final byte[] TEST_APDU = BuildSelectApdu(SAMPLE_TEST_AID);
//    private static final byte[] ANDROID_APDU = BuildSelectApdu(ANDROID_TEST_AID);





    /**
     * Called if the connection to the NFC card is lost, in order to let the application know the
     * cause for the disconnection (either a lost link, or another AID being selected by the
     * reader).
     *
     * @param reason Either DEACTIVATION_LINK_LOSS or DEACTIVATION_DESELECTED
     */
    @Override
    public void onDeactivated(int reason) {
        Log.d(TAG, "De-Activate: " + reason);
    }

    /**
     * This method will be called when a command APDU has been received from a remote device. A
     * response APDU can be provided directly by returning a byte-array in this method. In general
     * response APDUs must be sent as quickly as possible, given the fact that the user is likely
     * holding his device over an NFC reader when this method is called.
     *
     * <p class="note">If there are multiple services that have registered for the same AIDs in
     * their meta-data entry, you will only get called if the user has explicitly selected your
     * service, either as a default or just for the next tap.
     *
     * <p class="note">This method is running on the main thread of your application. If you
     * cannot return a response APDU immediately, return null and use the {@link
     * #sendResponseApdu(byte[])} method later.
     *
     * @param commandApdu The APDU that received from the remote device
     * @param extras      A bundle containing extra data. May be null.
     * @return a byte-array containing the response APDU, or null if no response APDU can be sent
     * at this point.
     */
    // BEGIN_INCLUDE(processCommandApdu)
    @Override
    public byte[] processCommandApdu(byte[] commandApdu, Bundle extras) {
        // Log.d(TAG,"extras: "+extras.toString());
        if (extras != null) {
            Log.i(TAG, "extras: " + extras.toString());
        }
        Log.i(TAG, "Received APDU: " + Utils.byte2hex(commandApdu));
        byte[] rapduForReader;
        rapduForReader = RAPDUExecutor.testRAPDU(commandApdu,CardService.this);
        Log.d(TAG,"RAPDU: "+Utils.byte2hex(rapduForReader));
        return rapduForReader;
    }
    // END_INCLUDE(processCommandApdu)

}
