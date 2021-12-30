package com.general.files;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.melevicarbrasil.usuario.BookingActivity;
import com.melevicarbrasil.usuario.ChatActivity;
import com.melevicarbrasil.usuario.ConfirmEmergencyTapActivity;
import com.melevicarbrasil.usuario.HistoryDetailActivity;
import com.melevicarbrasil.usuario.MainActivity;
import com.melevicarbrasil.usuario.OnGoingTripDetailsActivity;
import com.melevicarbrasil.usuario.RatingActivity;
import com.melevicarbrasil.usuario.deliverAll.TrackOrderActivity;
import com.utils.Logger;
import com.utils.Utils;
import com.view.GenerateAlertBox;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Created by Admin on 20/03/18.
 */

public class FireTripStatusMsg {

    Context mContext;
    private static String tmp_msg_chk = "";


    public FireTripStatusMsg() {
    }

    public FireTripStatusMsg(Context mContext) {
        this.mContext = mContext;
    }

    public void fireTripMsg(String message) {

        Logger.d("fireTripMsg", ":: called");
        if (message == null || tmp_msg_chk.equals(message)) {
            return;
        }
        tmp_msg_chk = message;

        Logger.e("SocketApp", "::MsgReceived::" + message);
        String finalMsg = message;

        if (!GeneralFunctions.isJsonObj(finalMsg)) {
            try {
                finalMsg = new JSONTokener(message).nextValue().toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (!GeneralFunctions.isJsonObj(finalMsg)) {
                finalMsg = finalMsg.replaceAll("^\"|\"$", "");

                if (!GeneralFunctions.isJsonObj(finalMsg)) {
                    finalMsg = message.replaceAll("\\\\", "");

                    finalMsg = finalMsg.replaceAll("^\"|\"$", "");

                    if (!GeneralFunctions.isJsonObj(finalMsg)) {
                        finalMsg = message.replace("\\\"", "\"").replaceAll("^\"|\"$", "");
                    }

                    finalMsg = finalMsg.replace("\\\\\"", "\\\"");
                }
            }
        }

        if (MyApp.getInstance() == null) {
            if (mContext != null) {
                dispatchNotification(finalMsg);
            }
            return;
        }

        if (MyApp.getInstance().getCurrentAct() != null) {
            mContext = MyApp.getInstance().getCurrentAct();
        }

        if (mContext == null) {
            dispatchNotification(finalMsg);
            return;
        }

        GeneralFunctions generalFunc = MyApp.getInstance().getGeneralFun(mContext);
        JSONObject obj_msg = generalFunc.getJsonObject(finalMsg);
        String tSessionId = generalFunc.getJsonValueStr("tSessionId", obj_msg);

        if (!tSessionId.equals("") && !tSessionId.equals(generalFunc.retrieveValue(Utils.SESSION_ID_KEY))) {
            return;
        }

        if (!GeneralFunctions.isJsonObj(finalMsg)) {
            String passMessage = generalFunc.convertNumberWithRTL(message);
            LocalNotification.dispatchLocalNotification(mContext, passMessage, true);
            generalFunc.showGeneralMessage("", passMessage);
            return;
        }

        boolean isMsgExist = isTripStatusMsgExist(generalFunc, finalMsg, mContext);

        if (isMsgExist == true) {
            return;
        }

        if (mContext instanceof TrackOrderActivity) {
            TrackOrderActivity objAct = (TrackOrderActivity) mContext;
            objAct.pubnubMessage(obj_msg);
        } else if (mContext instanceof Activity) {
            ((Activity) mContext).runOnUiThread(() -> continueDispatchMsg(generalFunc, obj_msg));
        } else {
            dispatchNotification(finalMsg);
        }

    }

    private void continueDispatchMsg(GeneralFunctions generalFunc, JSONObject obj_msg) {
        String messageStr = generalFunc.getJsonValueStr("Message", obj_msg);

        String vTitle = generalFunc.convertNumberWithRTL(generalFunc.getJsonValueStr("vTitle", obj_msg));
        String eType = generalFunc.getJsonValueStr("eType", obj_msg);

        if (messageStr.equals("")) {

            String msgTypeStr = generalFunc.getJsonValueStr("MsgType", obj_msg);
            //   String messageType_str = generalFunc.getJsonValueStr("MessageType", obj_msg);

            if (msgTypeStr.equalsIgnoreCase("CHAT")) {
                LocalNotification.dispatchLocalNotification(mContext, generalFunc.getJsonValueStr("Msg", obj_msg), true);


                if (MyApp.getInstance().getCurrentAct() instanceof ChatActivity == false) {

                    /*Bundle bn = new Bundle();

                    bn.putString("iFromMemberId", generalFunc.getJsonValueStr("iFromMemberId", obj_msg));
                    bn.putString("FromMemberImageName", generalFunc.getJsonValueStr("FromMemberImageName", obj_msg));
                    bn.putString("iTripId", generalFunc.getJsonValueStr("iTripId", obj_msg));
                    bn.putString("FromMemberName", generalFunc.getJsonValueStr("FromMemberName", obj_msg));
                    bn.putString("vBookingNo", generalFunc.getJsonValueStr("vBookingNo", obj_msg));*/


                    Intent chatActInt = new Intent(MyApp.getInstance().getApplicationContext(), ChatActivity.class);
                    if (obj_msg != null) {
                        chatActInt.putExtras(generalFunc.createChatBundle(obj_msg));
                    }
                    chatActInt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    MyApp.getInstance().getApplicationContext().startActivity(chatActInt);
                } else if (MyApp.getInstance() != null && MyApp.getInstance().getCurrentAct() instanceof ChatActivity) {

                       /* Bundle bn = new Bundle();

                        bn.putString("iFromMemberId", generalFunc.getJsonValueStr("iFromMemberId", obj_msg));
                        bn.putString("FromMemberImageName", generalFunc.getJsonValueStr("FromMemberImageName", obj_msg));
                        bn.putString("iTripId", generalFunc.getJsonValueStr("iTripId", obj_msg));
                        bn.putString("FromMemberName", generalFunc.getJsonValueStr("FromMemberName", obj_msg));
                        bn.putString("vBookingNo", generalFunc.getJsonValueStr("vBookingNo", obj_msg));*/

                    ((ChatActivity) MyApp.getInstance().getCurrentAct()).setCurrentTripData(generalFunc.createChatBundle(obj_msg));

                    return;
                }


            }

        } else {

            if (messageStr.equalsIgnoreCase("TripCancelledByDriver")|| messageStr.equalsIgnoreCase("TripCancelled") || messageStr.equalsIgnoreCase("DestinationAdded") || messageStr.equalsIgnoreCase("TripEnd")) {


                if (messageStr.equalsIgnoreCase("TripEnd") || messageStr.equalsIgnoreCase("TripCancelledByDriver") || messageStr.equalsIgnoreCase("TripCancelled")) {
                    generalFunc.storeData(Utils.ISWALLETBALNCECHANGE, "Yes");
                }

                if (eType.equalsIgnoreCase(Utils.CabGeneralType_UberX)) {

                    String iDriverId = generalFunc.getJsonValueStr("iDriverId", obj_msg);
                    String iTripId = generalFunc.getJsonValueStr("iTripId", obj_msg);
                    String showTripFare = generalFunc.getJsonValueStr("ShowTripFare", obj_msg);


                    if (MyApp.getInstance().getCurrentAct() instanceof OnGoingTripDetailsActivity && MyApp.getInstance().onGoingTripDetailsAct != null && MyApp.getInstance().onGoingTripDetailsAct.tripDetail.get("iTripId").equals(iTripId)) {
                        MyApp.getInstance().onGoingTripDetailsAct.pubNubMsgArrived(obj_msg.toString(), true);
                    } else {
                        if (MyApp.getInstance().getCurrentAct() instanceof BookingActivity) {
                            ((BookingActivity) MyApp.getInstance().getCurrentAct()).focusFragment(1);
                        }

                        if (messageStr.equalsIgnoreCase("TripEnd") || showTripFare.equalsIgnoreCase("true")) {
                            showPubnubGeneralMessage(generalFunc, iTripId, vTitle, false, true);
                        } else {

                            if (MyApp.getInstance().getCurrentAct() instanceof ChatActivity || MyApp.getInstance().getCurrentAct() instanceof ConfirmEmergencyTapActivity) {

                                String tripId = "";
                                if (MyApp.getInstance().getCurrentAct() instanceof ChatActivity) {
                                    ChatActivity activity = (ChatActivity) MyApp.getInstance().getCurrentAct();
                                    tripId = activity.data_trip_ada.get("iTripId");
                                } else if (MyApp.getInstance().getCurrentAct() instanceof ConfirmEmergencyTapActivity) {
                                    ConfirmEmergencyTapActivity activity = (ConfirmEmergencyTapActivity) MyApp.getInstance().getCurrentAct();
                                    tripId = activity.iTripId;
                                }

                                if (!tripId.equalsIgnoreCase("") && iTripId.equalsIgnoreCase(tripId)) {
                                    generalFunc.showGeneralMessage("", vTitle, "", generalFunc.retrieveLangLBl("", "LBL_BTN_OK_TXT"),
                                            buttonId -> {
                                                MyApp.getInstance().restartWithGetDataApp();

                                            });
                                } else {
                                    generalFunc.showGeneralMessage("", vTitle);
                                }

                            } else {
                                generalFunc.showGeneralMessage("", vTitle);
                            }
                        }
                    }

                } else if (eType.equalsIgnoreCase(Utils.eType_Multi_Delivery)) {

                    String iDriverId = generalFunc.getJsonValueStr("iDriverId", obj_msg);
                    String iTripId = generalFunc.getJsonValueStr("iTripId", obj_msg);
                    String showTripFare = generalFunc.getJsonValueStr("ShowTripFare", obj_msg);
                    String Is_Last_Delivery = generalFunc.getJsonValueStr("Is_Last_Delivery", obj_msg);


                    if (MyApp.getInstance().getCurrentAct() instanceof OnGoingTripDetailsActivity && MyApp.getInstance().onGoingTripDetailsAct != null && MyApp.getInstance().onGoingTripDetailsAct.tripDetail.get("iTripId").equals(iTripId)) {
                        MyApp.getInstance().onGoingTripDetailsAct.pubNubMsgArrived(obj_msg.toString(), true);
                    } else {
                        if (MyApp.getInstance().getCurrentAct() instanceof BookingActivity) {
                            ((BookingActivity) MyApp.getInstance().getCurrentAct()).focusFragment(1);
                        }

                        if (messageStr.equalsIgnoreCase("TripEnd") || showTripFare.equalsIgnoreCase("true")) {

                            /*Multi Related Condi*/

                            if (Is_Last_Delivery.equalsIgnoreCase("Yes")) {
                                showMultiPubnubGeneralMessage(generalFunc, obj_msg, true);
                            } else {
                                showMultiPubnubGeneralMessage(generalFunc, obj_msg, false);
                            }

                        } else {

                            if (MyApp.getInstance().getCurrentAct() instanceof ChatActivity || MyApp.getInstance().getCurrentAct() instanceof ConfirmEmergencyTapActivity) {
                                String tripId = "";
                                if (MyApp.getInstance().getCurrentAct() instanceof ChatActivity) {
                                    ChatActivity activity = (ChatActivity) MyApp.getInstance().getCurrentAct();
                                    tripId = activity.data_trip_ada.get("iTripId");
                                } else if (MyApp.getInstance().getCurrentAct() instanceof ConfirmEmergencyTapActivity) {
                                    ConfirmEmergencyTapActivity activity = (ConfirmEmergencyTapActivity) MyApp.getInstance().getCurrentAct();
                                    tripId = activity.iTripId;
                                }

                                if (!tripId.equalsIgnoreCase("") && iTripId.equalsIgnoreCase(tripId)) {
                                    generalFunc.showGeneralMessage("", vTitle, "", generalFunc.retrieveLangLBl("", "LBL_BTN_OK_TXT"),
                                            buttonId -> {
                                                MyApp.getInstance().restartWithGetDataApp();

                                            });
                                } else {
                                    generalFunc.showGeneralMessage("", vTitle);
                                }

                            } else {
                                generalFunc.showGeneralMessage("", vTitle);
                            }
                        }
                    }

                } else if (generalFunc.getJsonValueStr("eSystem", obj_msg).equalsIgnoreCase(Utils.eSystem_Type)) {
                    if (messageStr.equalsIgnoreCase("OrderConfirmByRestaurant") || messageStr.equalsIgnoreCase("OrderDeclineByRestaurant") || messageStr.equalsIgnoreCase("OrderPickedup") ||
                            messageStr.equalsIgnoreCase("OrderDelivered") || messageStr.equalsIgnoreCase("OrderCancelByAdmin")) {

                        if (messageStr.equalsIgnoreCase("OrderCancelByAdmin")) {
                            final GenerateAlertBox generateAlert = new GenerateAlertBox(mContext);
                            generateAlert.setCancelable(false);
                            generateAlert.setBtnClickList(btn_id -> MyApp.getInstance().restartWithGetDataApp());
                            generateAlert.setContentMessage("", vTitle);
                            generateAlert.setPositiveBtn(generalFunc.retrieveLangLBl("", "LBL_BTN_OK_TXT"));
                            generateAlert.showAlertBox();
                        } else {
                            generalFunc.showGeneralMessage("", vTitle);
                        }
                    }

                } else {
                    final GenerateAlertBox generateAlert = new GenerateAlertBox(mContext);
                    generateAlert.setCancelable(false);
//                    generateAlert.setSystemAlertWindow(true);
                    generateAlert.setBtnClickList(btn_id -> MyApp.getInstance().restartWithGetDataApp());
                    generateAlert.setContentMessage("", vTitle);
                    generateAlert.setPositiveBtn(generalFunc.retrieveLangLBl("", "LBL_BTN_OK_TXT"));
                    generateAlert.showAlertBox();
                }


                return;
            } else if (messageStr.equalsIgnoreCase("TripStarted") || messageStr.equalsIgnoreCase("DriverArrived")) {
                generalFunc.showGeneralMessage("", vTitle);
            } else {

                //  String vTitle = generalFunc.getJsonValueStr("vTitle", obj_msg);

                if (messageStr.equalsIgnoreCase("OrderConfirmByRestaurant") || messageStr.equalsIgnoreCase("OrderDeclineByRestaurant") || messageStr.equalsIgnoreCase("OrderPickedup") ||
                        messageStr.equalsIgnoreCase("OrderDelivered") || messageStr.equalsIgnoreCase("OrderCancelByAdmin")) {
                    generalFunc.showGeneralMessage("", vTitle);
                }

            }


        }

        if (MyApp.getInstance().mainAct != null && (!eType.equalsIgnoreCase(Utils.CabGeneralType_UberX) || (eType.equalsIgnoreCase(Utils.CabGeneralType_UberX) && messageStr.equalsIgnoreCase("CabRequestAccepted")))) {
            MyApp.getInstance().mainAct.pubNubMsgArrived(obj_msg.toString());
        }
        if (MyApp.getInstance().carWashAct != null && messageStr.equalsIgnoreCase("CabRequestAccepted")) {
            MyApp.getInstance().carWashAct.pubNubMsgArrived(obj_msg.toString());
        }

        if (MyApp.getInstance().onGoingTripDetailsAct != null) {
            MyApp.getInstance().onGoingTripDetailsAct.pubNubMsgArrived(obj_msg.toString(), false);
        }

        if (MyApp.getInstance().uberXAct != null && messageStr.equalsIgnoreCase("CabRequestAccepted")) {
            MyApp.getInstance().uberXAct.pubNubMsgArrived(obj_msg.toString());
        }
    }

    private void dispatchNotification(String message) {
        Context mLocContext = this.mContext;

        if (mLocContext == null && MyApp.getInstance() != null && MyApp.getInstance().getCurrentAct() == null) {
            mLocContext = MyApp.getInstance().getApplicationContext();
        }

//        if (mLocContext != null && MyApp.getInstance().getCurrentAct() == null) {
        if (mLocContext != null) {
            GeneralFunctions generalFunc = MyApp.getInstance().getGeneralFun(mLocContext);

            if (!GeneralFunctions.isJsonObj(message)) {
                LocalNotification.dispatchLocalNotification(mLocContext, message, true);

                return;
            }

            JSONObject obj_msg = generalFunc.getJsonObject(message);

            String message_str = generalFunc.getJsonValueStr("Message", obj_msg);

            if (message_str.equals("")) {
                String msgType_str = generalFunc.getJsonValueStr("MsgType", obj_msg);

                switch (msgType_str) {
                    case "CHAT":
                        generalFunc.storeData("OPEN_CHAT", obj_msg.toString());
                        LocalNotification.dispatchLocalNotification(mLocContext, generalFunc.getJsonValueStr("Msg", obj_msg), false);
                        break;
                }

            } else {
                String pass_msg = generalFunc.convertNumberWithRTL(generalFunc.getJsonValueStr("vTitle", obj_msg));
                switch (message_str) {

                    case "TripCancelledByDriver":
                    case "TripCancelled":
                        generalFunc.saveGoOnlineInfo();
                        LocalNotification.dispatchLocalNotification(mLocContext, pass_msg, false);
                        break;
                    case "DriverArrived":
                    case "DestinationAdded":
                    case "TripStarted":
                    case "TripEnd":
                        LocalNotification.dispatchLocalNotification(mLocContext, pass_msg, false);
                        break;
                    case "OrderDelivered":
                    case "OrderPickedup":
                    case "OrderConfirmByRestaurant":
                    case "OrderDeclineByRestaurant":
                    case "OrderCancelByAdmin":
                        LocalNotification.dispatchLocalNotification(MyApp.getInstance().getApplicationContext(), pass_msg, false);
                        break;
                }
            }
        }
    }

    private boolean isTripStatusMsgExist(GeneralFunctions generalFunc, String msg, Context mContext) {

        JSONObject obj_tmp = generalFunc.getJsonObject(msg);

        if (obj_tmp != null) {

            String message = generalFunc.getJsonValueStr("Message", obj_tmp);

            if (!message.equals("")) {
                String iTripId = "";
                if (generalFunc.getJsonValue("eSystem", msg).equalsIgnoreCase(Utils.eSystem_Type)) {
                    iTripId = generalFunc.getJsonValueStr("iOrderId", obj_tmp);
                } else {
                    iTripId = generalFunc.getJsonValueStr("iTripId", obj_tmp);
                }

                String iTripDeliveryLocationId = generalFunc.getJsonValueStr("iTripDeliveryLocationId", obj_tmp);
                if (!iTripId.equals("")) {
                    String vTitle = generalFunc.convertNumberWithRTL(generalFunc.getJsonValueStr("vTitle", obj_tmp));
                    String time = generalFunc.getJsonValueStr("time", obj_tmp);
                    String key = "";
                    if (generalFunc.getJsonValue("eType", msg).equalsIgnoreCase(Utils.eType_Multi_Delivery)) {
                        key = Utils.TRIP_REQ_CODE_PREFIX_KEY + iTripId + "_" + iTripDeliveryLocationId + "_" + message;
                    } else {
                        key = Utils.TRIP_REQ_CODE_PREFIX_KEY + iTripId + "_" + message;
                    }
                    if (message.equals("DestinationAdded")) {
                        String destKey = key;

                        Long newMsgTime = GeneralFunctions.parseLongValue(0, time);

                        String destKeyValueStr = generalFunc.retrieveValue(destKey, mContext);
                        if (!destKeyValueStr.equals("")) {

                            Long destKeyValue = generalFunc.parseLongValue(0, destKeyValueStr);

                            if (newMsgTime > destKeyValue) {
                                generalFunc.removeValue(destKey);
                            } else {
                                return true;
                            }
                        }
                    }

                    String data = generalFunc.retrieveValue(key);

                    if (data.equals("")) {
                        if (!message.equalsIgnoreCase("TripRequestCancel")) {
                            LocalNotification.dispatchLocalNotification(mContext, vTitle, true);
                        }
                        if (time.equals("")) {
                            generalFunc.storeData(key, "" + System.currentTimeMillis());
                        } else {
                            generalFunc.storeData(key, "" + time);
                        }
                        return false;
                    } else {
                        return true;
                    }
                }
            } else {
                String msgType = generalFunc.getJsonValueStr("MsgType", obj_tmp);
                if (!msgType.equals("") && msgType.equals("TripRequestCancel")) {
                    String iTripId = generalFunc.getJsonValueStr("iTripId", obj_tmp);

                    String key = Utils.TRIP_REQ_CODE_PREFIX_KEY + iTripId + "_" + msgType;
                    String data = generalFunc.retrieveValue(key);
                    if (!data.equals("")) {
                        return true;
                    }
                }

            }

        }

        return false;
    }

    private void showMultiPubnubGeneralMessage(GeneralFunctions generalFunc, final JSONObject msg_Obj, final boolean isMultirate) {
        try {

            String message = generalFunc.getJsonValueStr("vTitle", msg_Obj);

            final GenerateAlertBox generateAlert = new GenerateAlertBox(MyApp.getInstance().getCurrentAct());
            generateAlert.setContentMessage("", message);
            generateAlert.setPositiveBtn(generalFunc.retrieveLangLBl("Ok", "LBL_BTN_OK_TXT"));

            if (generalFunc.getJsonValueStr("eType", msg_Obj).equalsIgnoreCase(Utils.eType_Multi_Delivery) && generalFunc.getJsonValueStr("Is_Last_Delivery", msg_Obj).equalsIgnoreCase("Yes")) {

                generateAlert.setNegativeBtn(generalFunc.retrieveLangLBl("", "LBL_CANCEL_TXT"));

            }

            generateAlert.setBtnClickList(new GenerateAlertBox.HandleAlertBtnClick() {
                @Override
                public void handleBtnClick(int btn_id) {
                    generateAlert.closeAlertBox();

                    if (mContext instanceof MainActivity) {
                        if (((MainActivity) mContext).driverAssignedHeaderFrag != null && ((MainActivity) mContext).driverAssignedHeaderFrag.backImgView != null) {
                            ((MainActivity) mContext).driverAssignedHeaderFrag.backImgView.performClick();
                        }

                    }

                    if (btn_id == 0) {
                        return;
                    } else if (btn_id == 1 && isMultirate) {
                        Bundle bn = new Bundle();
                        bn.putBoolean("isUfx", false);
                        bn.putString("iTripId", generalFunc.getJsonValueStr("iTripId", msg_Obj));

                        if (!Utils.checkText(generalFunc.getJsonValueStr("iTripId", msg_Obj))) {
                            return;
                        }

                        new StartActProcess(mContext).startActForResult(HistoryDetailActivity.class, bn, Utils.MULTIDELIVERY_HISTORY_RATE_CODE);
                    }
                }
            });


            generateAlert.showAlertBox();


        } catch (Exception e) {
            Logger.d("AlertEx", e.toString());
        }
    }

    private void showPubnubGeneralMessage(GeneralFunctions generalFunc, final String iTripId, final String message, final boolean isrestart, final boolean isufxrate) {
        try {

            final GenerateAlertBox generateAlert = new GenerateAlertBox(MyApp.getInstance().getCurrentAct());
            generateAlert.setContentMessage("", message);
            generateAlert.setPositiveBtn(generalFunc.retrieveLangLBl("Ok", "LBL_BTN_OK_TXT"));
            generateAlert.setBtnClickList(btn_id -> {
                generateAlert.closeAlertBox();

                if (isrestart) {
                    MyApp.getInstance().restartWithGetDataApp();
                }

                if (isufxrate) {
                    Bundle bn = new Bundle();
                    bn.putBoolean("isUfx", true);
                    bn.putString("iTripId", iTripId);
                    new StartActProcess(mContext).startActWithData(RatingActivity.class, bn);
                }
            });

            generateAlert.showAlertBox();

        } catch (Exception e) {
            Logger.d("AlertEx", e.toString());
        }
    }
}
