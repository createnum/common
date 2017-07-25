package skt.tools;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public class StatisticsLog{

    private static List<Pair> staticInfo = new ArrayList<Pair>();
    private static String verifyInfo;

    public static String getDeviceKey(Context context){
        String key = PhoneUtils.getIMEI(context);
        if(null == key || key.length() <= 0){
            key = PhoneUtils.getMacAddress(context);
            if(null == key || key.length() <= 0){
                key = PhoneUtils.getBluetoothAddress(context);
            }
        }
        return key;
    }

    public static void init(Context context, String serverPath, String channelId){
        staticInfo.clear();
        String packageName = context.getPackageName();
        verifyInfo = packageName;
        staticInfo.add(new Pair("appId", packageName));
        System.out.println(channelId);
        staticInfo.add(new Pair("channelId", channelId));// 渠道号
        
    	System.out.println("channelId" + channelId);
        staticInfo.add(new Pair("version", PhoneUtils.getAppVersionName(context)));// 版本号
        staticInfo.add(new Pair("phoneBrand", android.os.Build.BRAND));
        staticInfo.add(new Pair("phoneType", android.os.Build.MODEL));// 机型
        staticInfo.add(new Pair("systemVersion", android.os.Build.VERSION.RELEASE));
        staticInfo.add(new Pair("imsi", PhoneUtils.getIMSI(context)));// IMSI
        String key = getDeviceKey(context);
        staticInfo.add(new Pair("key", key));// imei/mac
        verifyInfo += key;
        PhoneUtils.OperatorName curOperator = PhoneUtils.getOperatorName(context);
        staticInfo.add(new Pair("operatorId", null == curOperator ? "-1" : "" + curOperator.ordinal()));
        // action
        nativeInit(serverPath, getBasicInfo(), getVerifyInfo());
    }

    public static void sendLog(String action, String value1, String value2){
        nativeSendLog(action, value1, value2);
    }

    private static native void nativeInit(String serverPath, String baseInfo, String verifyInfo);

    private static native void nativeSendLog(String action, String value1, String value2);

    private static String getBasicInfo(){
        StringBuilder sb = new StringBuilder();
        for(Pair pair : staticInfo){
            sb.append("&").append(pair.key).append("=").append(pair.value);
        }
        String s = sb.toString();
        return s;
    }

    private static String getVerifyInfo(){
        return verifyInfo;
    }

    static class Pair{
        Pair(String key, String value){
            this.key = key;
            this.value = value;
        }

        String key;
        String value;
    }
}
