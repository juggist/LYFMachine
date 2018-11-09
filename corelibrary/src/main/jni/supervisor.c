#include <errno.h>
#include <fcntl.h>
#include <limits.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <unistd.h>
#include <android/log.h>

static const char *TAG = "supervisor";
#define LOGI(fmt, args...) __android_log_print(ANDROID_LOG_INFO,  TAG, fmt, ##args)
#define LOGD(fmt, args...) __android_log_print(ANDROID_LOG_DEBUG, TAG, fmt, ##args)
#define LOGE(fmt, args...) __android_log_print(ANDROID_LOG_ERROR, TAG, fmt, ##args)

#define BUFFER_LENGTH 1024
#define SLEEP_INTERVAL 10   // every x seconds to check if process is running
int isProcessExist(char* busyboxPath, char *processName);
void runProcess(char* packageName, char* serviceName);

int main(int argc, char **argv) {
	if (argc < 6) {
		LOGE(
				"Usage:%s <sleepSeconds> <processName> <activityName> <busyboxpath> <workingpath>\n",
				argv[0]);
		return -1;
	}
	// check if self already running.
	int ret = -1;
	FILE * g_lockfile = NULL;

	int sleepSeconds = atoi(argv[1]);
	char* processName = argv[2];
	char* activityName = argv[3];
	char* busyboxpath = argv[4];
	char* workingpath = argv[5];
	char flagFile[BUFFER_LENGTH];
	char appStopFlagFile[BUFFER_LENGTH];
	sprintf(flagFile, "%s/tmp.lock", workingpath);
	sprintf(appStopFlagFile, "%s/stop.flag", workingpath);

	//启动时删除停止标志文件
	remove(appStopFlagFile);

	if (sleepSeconds <= 0) {
		LOGD("sleepSeconds is %d", sleepSeconds);
		sleepSeconds = SLEEP_INTERVAL;
	}

	// 检查是否已经有一个supervisor进程在运行
	g_lockfile = fopen(flagFile, "a+");
	if (g_lockfile == NULL) {
		LOGE("fopen(%s) failed:%s!\n", flagFile, strerror(errno));
		return -1;
	}

	ret = flock(fileno(g_lockfile), LOCK_EX | LOCK_NB);
	if (ret != 0) {
		LOGE("flock() failed:%s!\n", strerror(errno));
		LOGD("this program already running\n");
		return -1;
	}

	// 循环检测android进程是否在运行
	while (1) {
		LOGD("supervisor is running...");

		// 判断程序是否已被卸载，如果已经被卸载，则退出supervisor
		if (access(busyboxpath, 0) == -1) {
			LOGD("file not exist. file:%s\n", busyboxpath);
			exit(-1);
		}
		// 判断程序是否已经正常退出
		if (access(appStopFlagFile, 0) == -1) {
			// 判断android进程是否在运行，如果不在运行，则运行之
			LOGD("file not exist. file:%s\n", appStopFlagFile);
			if (!isProcessExist(busyboxpath, processName)) {
				runProcess(processName, activityName);
			}
		}

		sleep(sleepSeconds);
	}

	return 0;
}

// 使用busybox的pidof命令来检测目标进程是否在运行
int isProcessExist(char* busyboxPath, char *processName) {
	char buf[BUFFER_LENGTH];
	char command[BUFFER_LENGTH];
	FILE *fp;
	int ret = 0;
	sprintf(command, "%s pidof %s", busyboxPath, processName);

	if ((fp = popen(command, "r")) == NULL) {
		LOGD("popen failed\n");
		exit(1);
	}

	if ((fgets(buf, BUFFER_LENGTH, fp)) != NULL) {
		ret = 1;
		LOGD("pid is:%s\n", buf);
	}

	pclose(fp);
	return ret;
}
//使用am命令来启动android程序
void runProcess(char* packageName, char* serviceName) {
	FILE *fp;
	char command[BUFFER_LENGTH];
	char buf[BUFFER_LENGTH];
	sprintf(command, "am start -n %s/%s", packageName, serviceName);
	//sprintf(command, "am startservice -n %s/%s", packageName, serviceName);
	LOGD("run cmd: %s\n", command);
	if ((fp = popen(command, "r")) == NULL) {
		LOGE("popen failed\n");
		exit(1);
	}

	while ((fgets(buf, BUFFER_LENGTH, fp)) != NULL) {
		LOGD("cmd result is:%s\n", buf);
	}

	pclose(fp);
}
