/*
 * Copyright (C) 2013-2016, The CyanogenMod Project
 * Copyright (C) 2017, The LineageOS Project
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

package com.android.internal.telephony;

import static com.android.internal.telephony.RILConstants.*;

import android.content.Context;
import android.telephony.Rlog;
import android.os.AsyncResult;
import android.os.Message;
import android.os.Parcel;
import android.os.SystemProperties;
import android.telephony.ModemActivityInfo;
import android.telephony.PhoneNumberUtils;
import android.telephony.SignalStrength;
import com.android.internal.telephony.uicc.IccCardApplicationStatus;
import com.android.internal.telephony.uicc.IccCardStatus;
import com.android.internal.telephony.uicc.IccUtils;
import java.util.ArrayList;
import java.util.Collections;

/**
 * RIL customization for Galaxy C5
 *
 * {@hide}
 */
public class C5RIL extends RIL {

    private static final int RIL_REQUEST_DIAL_EMERGENCY_CALL = 10001;
    private static final int RIL_REQUEST_CALL_DEFLECTION = 10002;
    private static final int RIL_REQUEST_MODIFY_CALL_INITIATE = 10003;
    private static final int RIL_REQUEST_MODIFY_CALL_CONFIRM = 10004;
    private static final int RIL_REQUEST_SET_VOICE_DOMAIN_PREF = 10005;
    private static final int RIL_REQUEST_SAFE_MODE = 10006;
    private static final int RIL_REQUEST_SET_TRANSMIT_POWER = 10007;
    private static final int RIL_REQUEST_GET_CELL_BROADCAST_CONFIG = 10008;
    private static final int RIL_REQUEST_GET_PHONEBOOK_STORAGE_INFO = 10009;
    private static final int RIL_REQUEST_GET_PHONEBOOK_ENTRY = 10010;
    private static final int RIL_REQUEST_ACCESS_PHONEBOOK_ENTRY = 10011;
    private static final int RIL_REQUEST_USIM_PB_CAPA = 10012;
    private static final int RIL_REQUEST_LOCK_INFO = 10013;
    private static final int RIL_REQUEST_STK_SIM_INIT_EVENT = 10014;
    private static final int RIL_REQUEST_SET_PREFERRED_NETWORK_LIST = 10015;
    private static final int RIL_REQUEST_GET_PREFERRED_NETWORK_LIST = 10016;
    private static final int RIL_REQUEST_CHANGE_SIM_PERSO = 10017;
    private static final int RIL_REQUEST_ENTER_SIM_PERSO = 10018;
    private static final int RIL_REQUEST_SEND_ENCODED_USSD = 10019;
    private static final int RIL_REQUEST_CDMA_SEND_SMS_EXPECT_MORE = 10020;
    private static final int RIL_REQUEST_REQUEST_HOLD = 10022;
    private static final int RIL_REQUEST_SET_SIM_POWER = 10023;
    private static final int RIL_REQUEST_GET_ACB_INFO = 10024;
    private static final int RIL_REQUEST_UICC_GBA_AUTHENTICATE_BOOTSTRAP = 10025;
    private static final int RIL_REQUEST_UICC_GBA_AUTHENTICATE_NAF = 10026;
    private static final int RIL_REQUEST_GET_INCOMING_COMMUNICATION_BARRING = 10027;
    private static final int RIL_REQUEST_SET_INCOMING_COMMUNICATION_BARRING = 10028;
    private static final int RIL_REQUEST_QUERY_CNAP = 10029;
    private static final int RIL_REQUEST_SET_TRANSFER_CALL = 10030;
    private static final int RIL_REQUEST_GET_DISABLE_2G = 10031;
    private static final int RIL_REQUEST_SET_DISABLE_2G = 10032;
    private static final int RIL_REQUEST_REFRESH_NITZ_TIME = 10033;
    private static final int RIL_REQUEST_ENABLE_UNSOL_RESPONSE = 10034;
    private static final int RIL_REQUEST_CANCEL_TRANSFER_CALL = 10035;

    private static final int RIL_UNSOL_RESPONSE_NEW_CB_MSG = 11000;
    private static final int RIL_UNSOL_RELEASE_COMPLETE_MESSAGE = 11001;
    private static final int RIL_UNSOL_STK_SEND_SMS_RESULT = 11002;
    private static final int RIL_UNSOL_STK_CALL_CONTROL_RESULT = 11003;
    private static final int RIL_UNSOL_ACB_INFO_CHANGED = 11005;
    private static final int RIL_UNSOL_DEVICE_READY_NOTI = 11008;
    private static final int RIL_UNSOL_GPS_NOTI = 11009;
    private static final int RIL_UNSOL_AM = 11010;
    private static final int RIL_UNSOL_SAP = 11013;
    private static final int RIL_UNSOL_UART = 11020;
    private static final int RIL_UNSOL_SIM_PB_READY = 11021;
    private static final int RIL_UNSOL_VE = 11024;
    private static final int RIL_UNSOL_FACTORY_AM = 11026;
    private static final int RIL_UNSOL_MODIFY_CALL = 11028;
    private static final int RIL_UNSOL_CS_FALLBACK = 11030;
    private static final int RIL_UNSOL_VOICE_SYSTEM_ID = 11032;
    private static final int RIL_UNSOL_IMS_RETRYOVER = 11034;
    private static final int RIL_UNSOL_PB_INIT_COMPLETE = 11035;
    private static final int RIL_UNSOL_HYSTERESIS_DCN = 11037;
    private static final int RIL_UNSOL_CP_POSITION = 11038;
    private static final int RIL_UNSOL_HOME_NETWORK_NOTI = 11043;
    private static final int RIL_UNSOL_STK_CALL_STATUS = 11054;
    private static final int RIL_UNSOL_MODEM_CAP = 11056;
    private static final int RIL_UNSOL_SIM_SWAP_STATE_CHANGED = 11057;
    private static final int RIL_UNSOL_SIM_COUNT_MISMATCHED = 11058;
    private static final int RIL_UNSOL_DUN = 11060;
    private static final int RIL_UNSOL_IMS_PREFERENCE_CHANGED = 11061;
    private static final int RIL_UNSOL_SIM_APPLICATION_REFRESH = 11062;
    private static final int RIL_UNSOL_UICC_APPLICATION_STATUS = 11063;
    private static final int RIL_UNSOL_VOICE_RADIO_BEARER_HO_STATUS = 11064;
    private static final int RIL_UNSOL_CLM_NOTI = 11065;
    private static final int RIL_UNSOL_SIM_ICCID_NOTI = 11066;
    private static final int RIL_UNSOL_TIMER_STATUS_CHANGED_NOTI = 11067;


    public C5RIL(Context context, int preferredNetworkType, int cdmaSubscription) {
        this(context, preferredNetworkType, cdmaSubscription, null);
    }

    public C5RIL(Context context, int preferredNetworkType,
            int cdmaSubscription, Integer instanceId) {
        super(context, preferredNetworkType, cdmaSubscription, instanceId);
    }

    @Override
    public void
    dial(String address, int clirMode, UUSInfo uusInfo, Message result) {
        if (PhoneNumberUtils.isEmergencyNumber(address)) {
            dialEmergencyCall(address, clirMode, result);
            return;
        }

        RILRequest rr = RILRequest.obtain(RIL_REQUEST_DIAL, result);

        rr.mParcel.writeString(address);
        rr.mParcel.writeInt(clirMode);
        rr.mParcel.writeInt(0);     // CallDetails.call_type
        rr.mParcel.writeInt(1);     // CallDetails.call_domain
        rr.mParcel.writeString(""); // CallDetails.getCsvFromExtras

        if (uusInfo == null) {
            rr.mParcel.writeInt(0); // UUS information is absent
        } else {
            rr.mParcel.writeInt(1); // UUS information is present
            rr.mParcel.writeInt(uusInfo.getType());
            rr.mParcel.writeInt(uusInfo.getDcs());
            rr.mParcel.writeByteArray(uusInfo.getUserData());
        }

        if (RILJ_LOGD) riljLog(rr.serialString() + "> " + requestToString(rr.mRequest));

        send(rr);
    }

    private void
    dialEmergencyCall(String address, int clirMode, Message result) {
        RILRequest rr;

        rr = RILRequest.obtain(RIL_REQUEST_DIAL_EMERGENCY_CALL, result);
        rr.mParcel.writeString(address);
        rr.mParcel.writeInt(clirMode);
        rr.mParcel.writeInt(0);        // CallDetails.call_type
        rr.mParcel.writeInt(3);        // CallDetails.call_domain
        rr.mParcel.writeString("");    // CallDetails.getCsvFromExtra
        rr.mParcel.writeInt(0);        // Unknown

        if (RILJ_LOGD) riljLog(rr.serialString() + "> " + requestToString(rr.mRequest));

        send(rr);
    }

    @Override
    public void
    acceptCall (Message result) {
        RILRequest rr = RILRequest.obtain(RIL_REQUEST_ANSWER, result);

        rr.mParcel.writeInt(1);
        rr.mParcel.writeInt(0);

        if (RILJ_LOGD) riljLog(rr.serialString() + "> " + requestToString(rr.mRequest));

        send(rr);
    }

    @Override
    protected Object 
    responseCallList(Parcel p) {
        int num = p.readInt();
        ArrayList<DriverCall> response = new ArrayList(num);
        for (int i = 0; i < num; i++) {
            boolean z;
            DriverCall dc = new DriverCall();
            dc.state = DriverCall.stateFromCLCC(p.readInt());
            dc.index = p.readInt();
//            if (CscFeature.getInstance().getEnableStatus("CscFeature_RIL_SupportVolte")) {
//                dc.id = (dc.index >> 8) & 255;
//                dc.index &= 255;
//            }
            dc.TOA = p.readInt();
            dc.isMpty = p.readInt() != 0;
            dc.isMT = p.readInt() != 0;
            dc.als = p.readInt();
            if (p.readInt() == 0) {
                z = false;
            } else {
                z = true;
            }
            dc.isVoice = z;
            int type = p.readInt();
            int domain = p.readInt();
            String extras = p.readString();
//            dc.callDetails = new CallDetails(type, domain, null);
//            dc.callDetails.setExtrasFromCsv(extras);
//            Rlog.d(RILJ_LOG_TAG, "dc.index " + dc.index + " dc.id " + dc.id + " dc.callDetails " + dc.callDetails);
            dc.isVoicePrivacy = p.readInt() != 0;
            dc.number = p.readString();
            dc.numberPresentation = DriverCall.presentationFromCLIP(p.readInt());
            dc.name = p.readString();
            Rlog.d(RILJ_LOG_TAG, "responseCallList dc.name" + dc.name);
            dc.namePresentation = DriverCall.presentationFromCLIP(p.readInt());
            if (p.readInt() == 1) {
                dc.uusInfo = new UUSInfo();
                dc.uusInfo.setType(p.readInt());
                dc.uusInfo.setDcs(p.readInt());
                dc.uusInfo.setUserData(p.createByteArray());
                riljLogv(String.format("Incoming UUS : type=%d, dcs=%d, length=%d", new Object[]{Integer.valueOf(dc.uusInfo.getType()), Integer.valueOf(dc.uusInfo.getDcs()), Integer.valueOf(dc.uusInfo.getUserData().length)}));
                riljLogv("Incoming UUS : data (string)=" + new String(dc.uusInfo.getUserData()));
                riljLogv("Incoming UUS : data (hex): " + IccUtils.bytesToHexString(dc.uusInfo.getUserData()));
            } else {
                riljLogv("Incoming UUS : NOT present!");
            }
            dc.number = PhoneNumberUtils.stringFromStringAndTOA(dc.number, dc.TOA);

            response.add(dc);
            if (dc.isVoicePrivacy) {
                this.mVoicePrivacyOnRegistrants.notifyRegistrants();
                riljLog("InCall VoicePrivacy is enabled");
            } else {
                this.mVoicePrivacyOffRegistrants.notifyRegistrants();
                riljLog("InCall VoicePrivacy is disabled");
            }
        }
        Collections.sort(response);
        if (num == 0 && this.mTestingEmergencyCall.getAndSet(false) && this.mEmergencyCallbackModeRegistrant != null) {
            riljLog("responseCallList: call ended, testing emergency call, notify ECM Registrants");
            this.mEmergencyCallbackModeRegistrant.notifyRegistrant();
        }
        return response;
    }

    @Override
    protected Object
    responseIccCardStatus(Parcel p) {
        IccCardApplicationStatus appStatus;

        IccCardStatus cardStatus = new IccCardStatus();
        cardStatus.setCardState(p.readInt());
        cardStatus.setUniversalPinState(p.readInt());
        cardStatus.mGsmUmtsSubscriptionAppIndex = p.readInt();
        cardStatus.mCdmaSubscriptionAppIndex = p.readInt();
        cardStatus.mImsSubscriptionAppIndex = p.readInt();
        int numApplications = p.readInt();

        // limit to maximum allowed applications
        if (numApplications > IccCardStatus.CARD_MAX_APPS) {
            numApplications = IccCardStatus.CARD_MAX_APPS;
        }
        cardStatus.mApplications = new IccCardApplicationStatus[numApplications];
        for (int i = 0 ; i < numApplications ; i++) {
            appStatus = new IccCardApplicationStatus();
            appStatus.app_type       = appStatus.AppTypeFromRILInt(p.readInt());
            appStatus.app_state      = appStatus.AppStateFromRILInt(p.readInt());
            appStatus.perso_substate = appStatus.PersoSubstateFromRILInt(p.readInt());
            appStatus.aid            = p.readString();
            appStatus.app_label      = p.readString();
            appStatus.pin1_replaced  = p.readInt();
            appStatus.pin1           = appStatus.PinStateFromRILInt(p.readInt());
            appStatus.pin2           = appStatus.PinStateFromRILInt(p.readInt());
            p.readInt(); // pin1_num_retries
            p.readInt(); // puk1_num_retries
            p.readInt(); // pin2_num_retries
            p.readInt(); // puk2_num_retries
            p.readInt(); // perso_unblock_retries
            cardStatus.mApplications[i] = appStatus;
        }
        return cardStatus;
    }

    @Override
    protected RILRequest
    processSolicited (Parcel p, int type) {
        int serial, error;
        boolean found = false;

        int dataPosition = p.dataPosition(); // save off position within the Parcel

        serial = p.readInt();
        error = p.readInt();

        RILRequest rr = null;


        /* Pre-process the reply before popping it */
        synchronized (mRequestList) {
            RILRequest tr = mRequestList.get(serial);
            if (tr != null && tr.mSerial == serial) {
                if (error == 0 || p.dataAvail() > 0) {
                    try {switch (tr.mRequest) {
                            /* Get those we're interested in */
                        case RIL_REQUEST_DIAL_EMERGENCY_CALL:
                        case RIL_REQUEST_CALL_DEFLECTION:
                        case RIL_REQUEST_MODIFY_CALL_INITIATE:
                        case RIL_REQUEST_MODIFY_CALL_CONFIRM:
                        case RIL_REQUEST_SET_VOICE_DOMAIN_PREF:
                        case RIL_REQUEST_SAFE_MODE:
                        case RIL_REQUEST_SET_TRANSMIT_POWER:
                        case RIL_REQUEST_GET_CELL_BROADCAST_CONFIG:
                        case RIL_REQUEST_GET_PHONEBOOK_STORAGE_INFO:
                        case RIL_REQUEST_GET_PHONEBOOK_ENTRY:
                        case RIL_REQUEST_ACCESS_PHONEBOOK_ENTRY:
                        case RIL_REQUEST_USIM_PB_CAPA:
                        case RIL_REQUEST_LOCK_INFO:
                        case RIL_REQUEST_STK_SIM_INIT_EVENT:
                        case RIL_REQUEST_SET_PREFERRED_NETWORK_LIST:
                        case RIL_REQUEST_GET_PREFERRED_NETWORK_LIST:
                        case RIL_REQUEST_CHANGE_SIM_PERSO:
                        case RIL_REQUEST_ENTER_SIM_PERSO:
                        case RIL_REQUEST_SEND_ENCODED_USSD:
                        case RIL_REQUEST_CDMA_SEND_SMS_EXPECT_MORE:
                        case RIL_REQUEST_REQUEST_HOLD:
                        case RIL_REQUEST_SET_SIM_POWER:
                        case RIL_REQUEST_GET_ACB_INFO:
                        case RIL_REQUEST_UICC_GBA_AUTHENTICATE_BOOTSTRAP:
                        case RIL_REQUEST_UICC_GBA_AUTHENTICATE_NAF:
                        case RIL_REQUEST_GET_INCOMING_COMMUNICATION_BARRING:
                        case RIL_REQUEST_SET_INCOMING_COMMUNICATION_BARRING:
                        case RIL_REQUEST_QUERY_CNAP:
                        case RIL_REQUEST_SET_TRANSFER_CALL:
                        case RIL_REQUEST_GET_DISABLE_2G:
                        case RIL_REQUEST_SET_DISABLE_2G:
                        case RIL_REQUEST_REFRESH_NITZ_TIME:
                        case RIL_REQUEST_ENABLE_UNSOL_RESPONSE:
                        case RIL_REQUEST_CANCEL_TRANSFER_CALL:
                            rr = tr;
                            break;
                    }} catch (Throwable thr) {
                        // Exceptions here usually mean invalid RIL responses
                        if (tr.mResult != null) {
                            AsyncResult.forMessage(tr.mResult, null, thr);
                            tr.mResult.sendToTarget();
                        }
                        return tr;
                    }
                }
            }
        }
        if (rr == null) {
            /* Nothing we care about, go up */
            p.setDataPosition(dataPosition);
            // Forward responses that we are not overriding to the super class
            return super.processSolicited(p, type);
        }

        rr = findAndRemoveRequestFromList(serial);

        if (rr == null) {
            return null;
        }

        Object ret = null;

        if (error == 0 || p.dataAvail() > 0) {
            // either command succeeds or command fails but with data payload
            switch (rr.mRequest) {
            /*
 cat libs/telephony/ril_commands.h \
 | egrep "^ *{RIL_" \
 | sed -re 's/\{([^,]+),[^,]+,([^}]+).+/case \1: ret = \2(p); break;/'
             */
            case RIL_REQUEST_DIAL_EMERGENCY_CALL: ret = responseVoid(p); break;
            case RIL_REQUEST_CALL_DEFLECTION: ret = responseVoid(p); break;
            case RIL_REQUEST_MODIFY_CALL_INITIATE: ret = responseFailCause(p);break;
            case RIL_REQUEST_MODIFY_CALL_CONFIRM: ret = responseVoid(p); break;
            case RIL_REQUEST_SET_VOICE_DOMAIN_PREF: ret = responseVoid(p); break;
            case RIL_REQUEST_SAFE_MODE: ret = responseVoid(p); break;
            case RIL_REQUEST_SET_TRANSMIT_POWER: ret = responseVoid(p); break;
            case RIL_REQUEST_GET_CELL_BROADCAST_CONFIG: ret = responseVoid(p); break; //responseCbSettings(p);
            case RIL_REQUEST_GET_PHONEBOOK_STORAGE_INFO: ret = responseInts(p); break;
            case RIL_REQUEST_GET_PHONEBOOK_ENTRY: ret = responseVoid(p); break; //responseSIM_PB(p);
            case RIL_REQUEST_ACCESS_PHONEBOOK_ENTRY: ret = responseInts(p); break;
            case RIL_REQUEST_USIM_PB_CAPA: ret = responseInts(p); break;
            case RIL_REQUEST_LOCK_INFO: ret = responseVoid(p); break; //responseSIM_LockInfo(p);
            case RIL_REQUEST_STK_SIM_INIT_EVENT: ret = responseVoid(p); break;
            case RIL_REQUEST_SET_PREFERRED_NETWORK_LIST: ret = responseVoid(p); break;
            case RIL_REQUEST_GET_PREFERRED_NETWORK_LIST: ret = responseVoid(p); break; //responsePreferredNetworkList(p);
            case RIL_REQUEST_CHANGE_SIM_PERSO: ret = responseInts(p); break;
            case RIL_REQUEST_ENTER_SIM_PERSO: ret = responseInts(p); break;
            case RIL_REQUEST_SEND_ENCODED_USSD: ret = responseVoid(p); break;
            case RIL_REQUEST_CDMA_SEND_SMS_EXPECT_MORE: ret = responseSMS(p); break;
            case RIL_REQUEST_REQUEST_HOLD: ret = responseVoid(p); break;
            case RIL_REQUEST_SET_SIM_POWER: ret = responseVoid(p); break; //responseSimPowerDone(p);
            case RIL_REQUEST_GET_ACB_INFO: ret = responseInts(p); break;
            case RIL_REQUEST_UICC_GBA_AUTHENTICATE_BOOTSTRAP: ret = responseVoid(p); break; //responseBootstrap(p);
            case RIL_REQUEST_UICC_GBA_AUTHENTICATE_NAF: ret = responseVoid(p); break; //responseNaf(p);
            case RIL_REQUEST_GET_INCOMING_COMMUNICATION_BARRING: ret = responseString(p); break;
            case RIL_REQUEST_SET_INCOMING_COMMUNICATION_BARRING: ret = responseVoid(p); break;
            case RIL_REQUEST_QUERY_CNAP: ret = responseInts(p); break;
            case RIL_REQUEST_SET_TRANSFER_CALL: ret = responseVoid(p); break;
            case RIL_REQUEST_GET_DISABLE_2G: ret = responseInts(p); break;
            case RIL_REQUEST_SET_DISABLE_2G: ret = responseVoid(p); break;
            case RIL_REQUEST_REFRESH_NITZ_TIME: ret = responseVoid(p); break;
            case RIL_REQUEST_ENABLE_UNSOL_RESPONSE: ret = responseVoid(p); break;
            case RIL_REQUEST_CANCEL_TRANSFER_CALL: ret = responseVoid(p); break;
            default:
                throw new RuntimeException("Unrecognized solicited response: " + rr.mRequest);
            //break;
           }
        }

        if (RILJ_LOGD) riljLog(rr.serialString() + "< " + requestToString(rr.mRequest)
            + " " + retToString(rr.mRequest, ret));

        if (rr.mResult != null) {
            AsyncResult.forMessage(rr.mResult, ret, null);
            rr.mResult.sendToTarget();
        }

        return rr;
    }


    @Override
    protected void
    processUnsolicited (Parcel p, int type) {
        Object ret;
        int dataPosition = p.dataPosition(); // save off position within the Parcel
        int response = p.readInt();

        switch(response) {
            case RIL_UNSOL_RELEASE_COMPLETE_MESSAGE: ret = responseVoid(p); break; //responseSSReleaseCompleteNotification(p);
            case RIL_UNSOL_STK_SEND_SMS_RESULT: ret = responseInts(p); break;
            case RIL_UNSOL_STK_CALL_CONTROL_RESULT: ret = responseString(p); break;
            case RIL_UNSOL_ACB_INFO_CHANGED: ret = responseInts(p); break;
            case RIL_UNSOL_DEVICE_READY_NOTI: ret = responseVoid(p); break;
            case RIL_UNSOL_GPS_NOTI: ret = responseVoid(p); break;
            case RIL_UNSOL_AM: ret = responseString(p); break;
            case RIL_UNSOL_SAP: ret = responseRaw(p); break;
            case RIL_UNSOL_UART: ret = responseRaw(p); break;
            case RIL_UNSOL_SIM_PB_READY: ret = responseVoid(p); break;
            case RIL_UNSOL_VE: ret = responseRaw(p); break;
            case RIL_UNSOL_MODIFY_CALL: ret = responseVoid(p); break; //responseCallModify(p);
            case RIL_UNSOL_CS_FALLBACK: ret = responseInts(p); break;
            case RIL_UNSOL_VOICE_SYSTEM_ID: ret = responseInts(p); break;
            case RIL_UNSOL_IMS_RETRYOVER: ret = responseVoid(p); break;
            case RIL_UNSOL_PB_INIT_COMPLETE: ret = responseVoid(p); break;
            case RIL_UNSOL_HYSTERESIS_DCN: ret = responseVoid(p); break;
            case RIL_UNSOL_HOME_NETWORK_NOTI: ret = responseVoid(p); break;
            case RIL_UNSOL_STK_CALL_STATUS: ret = responseInts(p); break;
            case RIL_UNSOL_MODEM_CAP: ret = responseRaw(p); break;
            case RIL_UNSOL_SIM_SWAP_STATE_CHANGED: ret = responseInts(p); break;
            case RIL_UNSOL_SIM_COUNT_MISMATCHED: ret = responseInts(p); break;
            case RIL_UNSOL_DUN: ret = responseStrings(p); break;
            case RIL_UNSOL_IMS_PREFERENCE_CHANGED: ret = responseInts(p); break;
            case RIL_UNSOL_SIM_APPLICATION_REFRESH: ret = responseInts(p); break;
            case RIL_UNSOL_VOICE_RADIO_BEARER_HO_STATUS: ret = responseInts(p); break;
            case RIL_UNSOL_SIM_ICCID_NOTI: ret = responseString(p); break;
            case RIL_UNSOL_TIMER_STATUS_CHANGED_NOTI: ret = responseInts(p); break;
            default:
                // Rewind the Parcel
                p.setDataPosition(dataPosition);

                // Forward responses that we are not overriding to the super class
                super.processUnsolicited(p, type);
                return;
        }
    }

// setUiccSubscription???

}
