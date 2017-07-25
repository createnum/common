#ifndef __STATISTICS_LOG_H__
#define __STATISTICS_LOG_H__
#include <string>
#include <time.h>

class StatisticsLog{
public:
    static StatisticsLog* getInstance(){
        return m_inst;
    }
    StatisticsLog();
    void init(const std::string &serverPath, const std::string &baseInfo, const std::string &verifyInfo);
    ~StatisticsLog();
    void sendLogData(const char* action, const char* parameter1="", const char* parameter2="");
    void getVerifyKey(time_t nowTime, char *output);
private:
    static StatisticsLog *m_inst;
    bool m_inited;
    std::string m_serverPath;
    std::string m_baseInfo;
    std::string m_verifyInfo;
};

#endif 

