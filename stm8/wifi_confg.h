#ifndef __WIFI_CONFG_H__
#define __WIFI_CONFG_H__
#include "flash_eeprom.h"

#define TxBufferSize1   (countof(RxBuffer1) - 1)
#define KEY_PORTA        GPIOA	/* 定义按键外设所接GPIO端口 */

/* LED灯所接的GPIO引脚定义 */
#define KEY_1     	GPIO_PIN_1
/* Private macro -------------------------------------------------------------*/
#define countof(a)   (sizeof(a) / sizeof(*(a)))
/* Exported functions ------------------------------------------------------- */
uint8_t GetVar_RxCounter1(void);
/***/
uint8_t IncrementVar_RxCounter1(void);

//读取fash的数据 看看是否有保持
uint8_t readFlashDate();
//初始化 AP模式 开启 tcp 服务器 
void setconfigAp();
//发送数据, 最后可能出现问题点的地方
 void putDate();
void getADC();
 //连接路由器
void lengWifi();
//读取fash的数据 看看是否有数据
//int readFlashDate();
void Delayms(unsigned int time) ;  //毫秒级延时程序--
//---  微秒级延时--------------------------   
void Delayus(void);
/*初始化系统,并且重新启动*/
void reset();
/*重新启动系统*/
void restart();
#endif /* __MAIN_H__ */
