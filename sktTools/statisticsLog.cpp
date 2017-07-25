#include "statisticsLog.h"
#include <sstream>
#include <stdio.h>
#include "cocos-ext.h"
#include "md5.h"

using namespace cocos2d;

StatisticsLog * StatisticsLog::m_inst = 0;

StatisticsLog::StatisticsLog(){
    m_serverPath = "";
    m_baseInfo = "";
    m_inited = false;
    m_inst = this;
}

StatisticsLog::~StatisticsLog(){
    m_inst = 0;
}

void StatisticsLog::init(const std::string &serverPath, const std::string &baseInfo, const std::string &verifyInfo){
    m_serverPath = serverPath;
    m_baseInfo = baseInfo;
    m_verifyInfo = verifyInfo;
    m_inited = true;
}

class StatisticsLogHttpRequestCallback:public cocos2d::CCObject{
public:
    void onHttpRequestCompleted(cocos2d::extension::CCHttpClient* client, cocos2d::extension::CCHttpResponse* response);
};
void StatisticsLogHttpRequestCallback::onHttpRequestCompleted(cocos2d::extension::CCHttpClient* client, cocos2d::extension::CCHttpResponse* response)  
{  
    if (!response)    
    {  
        return;    
    }
    int statusCode = response->getResponseCode();
    const char* tag = response->getHttpRequest()->getTag();
    if (!response->isSucceed() || 200 != statusCode)  
    {  
        cocos2d::CCLog("response failed");  
        cocos2d::CCLog("error buffer:%s" ,response->getErrorBuffer());
        return;
    }  
    std::vector<char> *buffer = response->getResponseData();
    int flag = 0;
    for (int i = 0; i < 4; i++)
    {
        flag <<= 8;
        flag = flag | (0x000000FF & (*buffer)[i]);
    }
    CCAssert(0 == flag, "error rtn value");

//     std::ostringstream oss;
//     for (unsigned int i = 0; i < buffer->size(); i ++)  
//     {  
//         oss<<(*buffer)[i]<<",";
//     }  
//     cocos2d::CCLog("Http response,dump data:%s", oss.str().c_str());
}

void StatisticsLog::sendLogData(const char* action, const char* parameter1, const char* parameter2){
    if (m_inited)
    {
        cocos2d::extension::CCHttpRequest* request = new cocos2d::extension::CCHttpRequest();
        request->setUrl(m_serverPath.c_str());
        request->setRequestType(cocos2d::extension::CCHttpRequest::kHttpPost);
        std::ostringstream postData;
        time_t nowtime;
        nowtime = time(NULL); //获取当前时间 
        char verifyKey[33];
        getVerifyKey(nowtime, verifyKey);
        postData<<"verifyKey="<<verifyKey<<"&action=";
        {
            //time,action,value,value;time,action,value,value;
            postData<<nowtime<<","<<action<<","<<parameter1<<","<<parameter2<<";";
        }
        postData<<"&cached="<<"false";
        postData<<m_baseInfo;
        request->setRequestData(postData.str().c_str(), strlen(postData.str().c_str())); 
        request->setTag("POST");
        //request->setUserData(100);
        cocos2d::CCLog("log post: %s", postData.str().c_str());
        static StatisticsLogHttpRequestCallback *back = new StatisticsLogHttpRequestCallback();//note: static object
        using namespace cocos2d;
        request->setResponseCallback(back, httpresponse_selector(StatisticsLogHttpRequestCallback::onHttpRequestCompleted));  
        cocos2d::extension::CCHttpClient::getInstance()->send(request);
        request->release(); 
    }
}

void StatisticsLog::getVerifyKey(time_t nowTime, char *output){
    char str[256];
    sprintf(str, "%s%d", m_verifyInfo.c_str(), nowTime);
    int len = strlen(str);
    md5_byte_t* data = reinterpret_cast<md5_byte_t *>(str);

    md5_state_t pms;
    md5_init(&pms);
    md5_append(&pms, data, len);
    md5_byte_t digest[16];
    md5_finish(&pms, digest);

    for(int j = 0; j < 16; j++ )
    {
        sprintf(output + j * 2, "%02x", ((unsigned char*)digest)[j]);
    }
}
