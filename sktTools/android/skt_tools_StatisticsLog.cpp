#include "platform/android/jni/JniHelper.h"
#include <jni.h>
#include "../statisticsLog.h"

using namespace cocos2d;

extern "C" {
    JNIEXPORT void Java_skt_tools_StatisticsLog_nativeInit(JNIEnv* env, jobject obj, jstring serverPath, jstring baseInfo, jstring verifyInfo){
        StatisticsLog *log = new StatisticsLog();
        log->init(JniHelper::jstring2string(serverPath), JniHelper::jstring2string(baseInfo), JniHelper::jstring2string(verifyInfo));
    }

    JNIEXPORT void Java_skt_tools_StatisticsLog_nativeSendLog(JNIEnv* env, jobject obj, jstring action, jstring value1, jstring value2){
        char *actionStr = (char*)env->GetStringUTFChars(action, NULL); 
        char *value1Str = (char*)env->GetStringUTFChars(value1, NULL); 
        char *value2Str = (char*)env->GetStringUTFChars(value2, NULL); 

        StatisticsLog::getInstance()->sendLogData(actionStr, value1Str, value2Str);

        env->ReleaseStringUTFChars(action, actionStr);
        env->ReleaseStringUTFChars(value1, value1Str);
        env->ReleaseStringUTFChars(value2, value2Str);
    }
}
