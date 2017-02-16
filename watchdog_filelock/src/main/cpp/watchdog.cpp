//
// Created by YaLin on 2016/4/23.
//

#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <time.h>

#include <android/log.h>

#define LOG_TAG ("watchdog_native")
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)

#define COMMON_BUFFER_SIZE  (256)

const char *LOG = "LOG";

int writeLog = 1;
const char *logFile = "/Watchdog/native_log.txt";
const char *logFile2 = "native_log.txt";

void getCurrentTime(char *out) {
    time_t t = time(0);
    struct tm *local;
    local = localtime(&t);
    sprintf(out, "%d-%02d-%02d %02d:%02d:%02d", local->tm_year + 1900, local->tm_mon + 1,
            local->tm_mday,
            local->tm_hour, local->tm_min, local->tm_sec);
//    strftime(out, sizeof(out), "%Y-%m-%D %H:%M:%S",localtime(&t));
    //LOGI(out, LOG);
}

void printLog(const char *str) {
    if (writeLog == 0) {
        return;
    }

    LOGI(str, LOG);
    FILE *fp = fopen(logFile, "a");
    if (!fp) {
        fp = fopen(logFile2, "a");
    }
    if (!fp) {
        LOGI("open log file failed!");
        return;
    }
    char t[128];
    getCurrentTime(t);
    fprintf(fp, "%s %s\r\n", t, str);
    fclose(fp);
}

void execute_command_by_popen(char *command, char *out_result,
                              int resultBufferSize) {
    FILE *fp;
    out_result[resultBufferSize - 1] = '\0';
    fp = popen(command, "r");
    if (fp) {
        fgets(out_result, resultBufferSize - 1, fp);
        out_result[resultBufferSize - 1] = '\0';
        pclose(fp);
    }
}

void check_and_restart_service(char *service, char *action) {
    if (service == NULL) {
        return;
    }

    char cmdline[COMMON_BUFFER_SIZE];
    sprintf(cmdline, "am startservice -n %s -a %s --user 0", service, action);

    char tmp[COMMON_BUFFER_SIZE];
    sprintf(tmp, "watchdog_native: check_and_restart_service: cmd=%s",
            cmdline);
    printLog(tmp);

    execute_command_by_popen(cmdline, tmp, 200);
    printLog(tmp);
}

int main(int argc, char *argv[]) {
//    prctl(PR_SET_NAME, "newname")

    sprintf(argv[0], "SystemService");

    FILE *file;
    char ch;

    char *srvname = NULL;
    char *srvaction = NULL;
    char *lock_file = NULL;
    char *interval = NULL;
    char *outputLog = NULL;
    if (argc >= 6) {
        srvname = argv[1];
        srvaction = argv[2];
        lock_file = argv[3];
        interval = argv[4];
        outputLog = argv[5];
    } else {
        return 1;
    }

    if ((file = fopen(lock_file, "w")) == NULL) {
        printLog("watchdog_native: open file failed");
        return 1;
    }

    fprintf(file, "watchdog native");

    fclose(file);

    int intervalSecond = atoi(interval);
    writeLog = atoi(outputLog);

    char tmc[COMMON_BUFFER_SIZE];
    sprintf(tmc, "watchdog_native: interval=%d", intervalSecond);
    printLog(tmc);

    while (1) {
        sleep(intervalSecond);
        printLog("watchdog_native: native process keep running...");

        file = fopen(lock_file, "r");
        if (file)
            fclose(file);
        else
            break;

        check_and_restart_service(srvname, srvaction);

        sprintf(tmc, "watchdog_native: native process sleep %d seconds.", intervalSecond);
        printLog(tmc);
    }

    printLog("watchdog native, exit...");
    return 0;
}
