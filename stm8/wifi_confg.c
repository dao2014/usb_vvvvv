nclude "stm8s.h"
#include "stdio.h"
#include "wifi_confg.h"
#include "led.h"
#include "wwdg.h"
#include <string.h>
#include <stdlib.h>
#include "adc.h"


/* Private defines -----------------------------------------------------------*/
#ifdef _RAISONANCE_
#define PUTCHAR_PROTOTYPE int putchar (char c)
#define GETCHAR_PROTOTYPE int getchar (void)
#elif defined (_COSMIC_)
#define PUTCHAR_PROTOTYPE char putchar (char c)
#define GETCHAR_PROTOTYPE char getchar (void)
#else /* _IAR_ */
#define PUTCHAR_PROTOTYPE int putchar (int c)
#define GETCHAR_PROTOTYPE int getchar (void)
#endif /* _RAISONANCE_ */

#define BLOCK_OPERATION    0

/* Private typedef -----------------------------------------------------------*/
typedef enum {FAILED = 0, PASSED = !FAILED} TestStatus;
__IO TestStatus TransferStatus1 = FAILED;
uint8_t ReadBuffer[FLASH_BLOCK_SIZE];
uint8_t APBuffer[50]={0};
uint8_t STABuffer[50]={0};
__IO uint8_t RxCounter1 = 0x00;
char *AP_SSID;
char *STA_SSID;
char RxBuffer1[300]={0};
char returnerror[]="ERROR";
char retrunok[]="OK";
char retrunno[]="no";
char rest[]="ready";
char cifsr[]="ERROR";
char cifsrR1[]=".";
char cipstatus[]="STATUS";
char cipstart[]="Linked";  
char cipstart1[]="CONNECT";
char sipsendrs[]=">";
char sipsendrs1[]="busy";
char sipsendretn[]="WiriterOK";//确认服务器返回;Unlink   SEND OK
char sipsendretn1[]="SEND";
char sipsendretn2[]="Unlink";  //服务器断开  link is not
char sipsendretn3[]="link is not";
char cipEnd[]="Linked";  //获取客户端发送过来的信息 结束语句  CLOSED
char cipEnd2[]="CLOSED";
char cipEnd3[]="sta";  //校验数据
char apssidstr[]="nap";  //校验数据
char stassidstr[]="sta";  //校验数据

//暂时定数据
//char *datas="set,12345,3.7V,0.2A,7777777777777777";
char *ptr1;
char *ptr2;
char *ptr3;  //主要是判断是否 异常 如果获取到异常就重启 MCU
char *ptr4;  //校验 是否需要重启
int counts=0;  //主要是监控是否 要复位  超过3次读取到 ready 开始 复位
char types=0;//0表示正确  1 表示 等待接收  2表示:错误 3表示:没有改变 4表示其他异常 5,表示AP 重新启动
char status=1;   //跳槽循环作用
uint16_t stmsV;  //电压
 uint16_t stmsI;  //电流
 uint16_t stmsIADC;  //电流ADC
 char staorap=0;  //0表示 热点链接 1表示wifi链接上
 
uint16_t tci;
/* Private functions ---------------------------------------------------------*/
TestStatus Buffercmp(uint8_t* pBuffer1, uint8_t* pBuffer2, uint16_t BufferLength);
void reset_value();
//设置模式
void setCwmode(char *mode);
//设置 AP 模式的密码,以及ssid pass
void setCwsap();
//查看是否有手机连接 每2秒钟检查一次啊
void findCwlif();
//开启 tcp 服务器  必须要设置 路由模式为1 的时候才能够开启 tcp 服务器
void openTcpServier();
//设置路由模式
void setCipMux();
//初始化数据
void reset_value();
//flase配置
void flaseConfig();
//连接AP 路由器
void setCwjap();
//查询是否获取 路由分配的IP
void findCifsr();
// 连接 tcp 服务器
void findCipstart();
//获取连接成功状态 
void findCipstatus();

//等待客户端发送的信息
void waitClientDate();
//获取 ssid
void getSSID(char *byffer);
/**
 ** 字符串拼接方法
 **/
char * str_contact(const char *str1,const char *str2);
//处理获取到的数据格式  格式标准: ssid+个数(一位)+路由器名+pass+个数(暂时个位)+密码
//例如:ssid2TDpass812345678
void saveFlaceDate();
//初始化命令
void wifi_rst();
/**
*
* 复位 MCU
*/
void resetMCU();

/**
*  重构 查找 是否存在字符 
*/
char * putstrstr(char * rt,char * rx);
//查看 SSID 显示
void findATCWLAP();
//退出当前连接
void findATCWQAP();




//初始化 AP模式 开启 tcp 服务器 
void setconfigAp()
{
  status=0;
  reset_value();
 
  printf("AT+RST\r\n");
  do
  {
    Delayms(10);
    Refresh_WWDG_Window();
    ptr1 = strstr(RxBuffer1, rest);  //判断是否存在ready
    if(ptr1 !=NULL )
    {
      //printf("find ERROR !!\r\n");
      status=0;
    }
    //Delayms(500);
    //LED_Operation(LED_2, ON);
  }while(status);
 // wifi_rst();
  setCwmode("2");
  wifi_rst();
  setCwsap();
  wifi_rst();
  findCwlif();
  if(types==5)  //需要重置
  {
    resetMCU();
  }
  do
  {
   if(types==1)
    {
      findCwlif();
    }else
    {
      break;
    }
  }while(types);
  
  openTcpServier();
  waitClientDate();
  saveFlaceDate();
  setCwmode("1");
  wifi_rst();
  
 // printf("=====%s\r\n",ReadBuffer);
}

//连接路由器
void lengWifi()
{
  wifi_rst();
  findATCWLAP();
  //连接AP 路由器
  setCwjap();
  //查询是否获取 路由分配的IP  尝试连接10次 间隔2秒.否则当做密码有问题
  int ciN=0;
  for(ciN;ciN<100;ciN++)
  { 
    Delayms(30);
    Refresh_WWDG_Window();
    //WWDG_SWReset();
    findCifsr();
    if(types==0)
      break;
  }
  //获取连接成功状态 
  findCipstatus();
  //连接tcp
  findCipstart();
}


//退出当前连接
void findATCWQAP()
{
  reset_value();
  printf("AT+CWQAP\r\n");
  do
  {
    ptr1 = putstrstr(RxBuffer1, retrunok);  //判断是否存在热点ptr2 = putstrstr(RxBuffer1, stassidstr);  
     
    if(ptr1 !=NULL )
    {
       
       status=0;
    } 
    
  }while(status);
}

//查看 SSID 显示
void findATCWLAP()
{
  findATCWQAP();
  reset_value();
  printf("AT+CWLAP\r\n");
  do
  {
    ptr1 = putstrstr(RxBuffer1, apssidstr);  //判断是否存在热点
    ptr2 = putstrstr(RxBuffer1, stassidstr);  //  判断是否存在路由
   if(ptr2 !=NULL )
    {
      staorap=1;  //wifi
       status=0;
    }
    if(ptr1 !=NULL )
    {
      staorap=0;  //热点
       status=0;
    } 
    
  }while(status);
}

//初始化命令
void wifi_rst()
{
  
  reset_value();
  printf("AT+RST\r\n");
  //UART1_ReceiverWakeUpCmd(ENABLE);
  do
  {
    Delayms(10);
  Refresh_WWDG_Window();
    ptr1 = strstr(RxBuffer1, rest);  //判断是否存在ready
    if(ptr1!=NULL)
    {
      //printf("find ERROR !!\r\n");
      status=0;
    }
   // Delayms(1000);
  }while(status);
}



//连接AP 路由器
void setCwjap()
{
   reset_value();
  // ReadMultiBlockByte(Block_1,1,ReadBuffer);
   //readyByte(ReadBuffer);
   if(staorap==0)  //说明当前连接的 热点
   {
      printf("%s\r\n",APBuffer);
   }else{ //说明链接的是wifi
      printf("%s\r\n",STABuffer);
   }
    
   do
  {
    ptr1 = putstrstr(RxBuffer1, retrunok);  //判断是否存在ok
    if(ptr1!=NULL)
    {
      //printf("find ERROR !!\r\n");
      status=0;
      Refresh_WWDG_Window();
    }
   // Delayms(1000);
  }while(status);
}



 

//查询是否获取 路由分配的IP
void findCifsr()
{
   reset_value();
   printf("AT+CIFSR\r\n");
   do
  {
    ptr1 = putstrstr(RxBuffer1, cifsrR1);  
     ptr2 = putstrstr(RxBuffer1, returnerror); 
    if(ptr1 !=NULL )
    {
      status=0;
      types=0;
    }
    if(ptr2 !=NULL )
    {
      status=0;
      types=1;
    }
  }while(status);
}


// 连接 tcp 服务器
void findCipstart()
{
  reset_value();
  if(staorap==0)  //说明当前连接的 热点
   {
      printf("AT+CIPSTART=\"TCP\",\"192.168.43.1\",1001\r\n");
   }else{ //说明链接的是wifi
      printf("AT+CIPSTART=\"TCP\",\"192.168.6.100\",1001\r\n");
   }
   
   do
  {
    ptr1 = putstrstr(RxBuffer1, cipstart);  //判断是否存在Linked 表示 还没连接成功
    ptr2 = putstrstr(RxBuffer1,cipstart1);
    if(ptr1 !=NULL || ptr2 !=NULL )
    {
      status=0;
    }
    
  }while(status);
}



//获取连接成功状态 
void findCipstatus()
{
  reset_value();
   printf("AT+CIPSTATUS\r\n");
   do
  {
    ptr1 = putstrstr(RxBuffer1, cipstatus);  //判断是否存在ERROR 表示 还没连接成功
    //ptr2 = putstrstr(RxBuffer1, cifsrR1);  // 没有改变
    if(ptr1 !=NULL )
    {
      //printf("find ERROR !!\r\n");
      status=0;
     
    }
   
    
  }while(status);
}



void getADC()
{
  stmsV=0;
   stmsV = OneChannelGetADValue(ADC2_CHANNEL_2,ADC2_SCHMITTTRIG_CHANNEL2);
  stmsV = 3300 * (uint32_t) stmsV * 2 / 1024;
 // Delayms(10);
 // Refresh_WWDG_Window();
  stmsI=0;
  stmsIADC=0;
  stmsI = OneChannelGetADValue(ADC2_CHANNEL_4,ADC2_SCHMITTTRIG_CHANNEL4);
  stmsIADC = OneChannelGetADValue(ADC2_CHANNEL_4,ADC2_SCHMITTTRIG_CHANNEL4);
  stmsI = 3300 * (uint32_t) stmsI *100/2/5/ 1024;  //求出电阻 R0.05的电压值 然后 I=U/R
 // Delayms(10);
 // Refresh_WWDG_Window();
  tci=0;
  tci = OneChannelGetADValue(ADC2_CHANNEL_3,ADC2_SCHMITTTRIG_CHANNEL3);

}


//发送数据, 最后可能出现问题点的地方
void putDate()
{
  //status=1;
  reset_value();
  printf("AT+CIPSEND=60\r\n");//发送10个字符
  
  do
  {

    ptr1 = putstrstr(RxBuffer1, sipsendrs);  //判断是否存在Linked 表示 还没连接成功  +i 
    ptr2 = putstrstr(RxBuffer1, sipsendretn3);  //
    //ptr3 = putstrstr(RxBuffer1, sipsendretn2);
    if(ptr2 !=NULL )
    {
      status=0;
      //types=2;
    }
    if(ptr1 !=NULL )
    {
      Refresh_WWDG_Window();
      status=0;
    } 
     
    
  }while(status);
  
  //Refresh_WWDG_Window();
  /**/
  
//  Delayms(10);
  Refresh_WWDG_Window();
  ////暂时定数据
 // char *datas="set,12345,3.7V,0.2A,7777777777777777";
 // unsigned char *pIDStart=(unsigned char *)(ID_BaseAddress); 
   
    reset_value();
    getADC();
     //printf("set,123123,123123mV,123123mA,123123Tc,777777777111111111111177" );
    printf("set,123123,%d,%d,%d,7777777771111111111111777777777777777777777777777777777",stmsV,stmsI,stmsIADC,tci);//发送数据
    do
    {
      ptr1 = putstrstr(RxBuffer1, sipsendretn);  //判断是否存在服务器返回错误 sipsendretn sipsendretn
      ptr2 = putstrstr(RxBuffer1, retrunok);  //判断是否存在OK  
      ptr3 = putstrstr(RxBuffer1, sipsendretn2);
      if(ptr1 !=NULL || ptr3 != NULL)
      {
        status=0;
      }
      
    }while(status);
  
}


//设置模式
void setCwmode(char *mode)
{
  reset_value();
  printf("AT+CWMODE=%s\r\n",mode);
  do
  {
    ptr1 = putstrstr(RxBuffer1, retrunok);  //判断是否存在ok
    ptr2 = putstrstr(RxBuffer1, retrunno);  // 没有改变
    if(ptr1 !=NULL )
    {
      //printf("find ERROR !!\r\n");
      status=0;
    } else  if(ptr2 !=NULL )
    {
      status=0;
      
    }
    //Delayms(500);
  }while(status);
}


//设置 AP 模式的密码,以及ssid pass
void setCwsap(){
  reset_value();
  printf("AT+CWSAP=\"ESP\",\"12345678\",5,3\r\n");
  do
  {
    ptr1 = putstrstr(RxBuffer1, retrunok);  //判断是否存在ok
    ptr2 = putstrstr(RxBuffer1, returnerror);  // 没有改变
    if(ptr1 !=NULL )
    {
      //printf("find ERROR !!\r\n");
      status=0;
    } else if(ptr2 !=NULL )
    {
      types=3;
      status=0;
    }
    //Delayms(500);
  }while(status);
}



//查看是否有手机连接 每2秒钟检查一次啊

void findCwlif()
{
  reset_value();
  printf("AT+CWLIF\r\n");
  do
  {
    ptr3 = strstr (RxBuffer1,rest);
    ptr1 = putstrstr(RxBuffer1, retrunok);  //判断是否存在ok
    ptr2 = putstrstr(RxBuffer1, "192.168");  //  
    
    if(ptr3 !=NULL)
    {
      types=5;
      status=0;
    }
    if(ptr1 !=NULL )
    {
      if(ptr2 !=NULL )  //说明有人连接 
      {
         status=0;
         types=0;
      }else
      {
        Delayms(30);
        Refresh_WWDG_Window();
        status=0;
        types=1;
      }
      
    } 
  }while(status);
}

//等待 已经连接上的推送信息
void wailUser()
{
  reset_value();
  do
  {
    ptr1 = putstrstr(RxBuffer1, retrunok);  //判断是否存在ok
    ptr2 = putstrstr(RxBuffer1, "192.168");  //  
    if(ptr1 !=NULL )
    {
      if(ptr2 !=NULL )  //说明有人连接 
      {
         status=0;
      }else
      {
        Delayms(30);
        Refresh_WWDG_Window();
        reset_value();
        printf("AT+CWLIF\r\n");
      }
      
    } 
  }while(status);
}


//开启 tcp 服务器  必须要设置 路由模式为1 的时候才能够开启 tcp 服务器
void openTcpServier()
{
  setCipMux(); //设置模式
  reset_value();
  printf("AT+CIPSERVER=1,1001\r\n");  // 开启tcp 服务器 端口1001
  do
  {
    ptr1 = putstrstr(RxBuffer1, retrunok);  //判断是否存在ok
    if(ptr1 !=NULL )
    {
       status=0;  
    } 
   // Delayms(1000);
  }while(status);
  
}


//等待客户端发送的信息
void waitClientDate()
{
  reset_value();
  types=1;
  do
  {
    ptr2 = putstrstr(RxBuffer1, cipEnd3);
    ptr1 = putstrstr(RxBuffer1, retrunok);  //判断是否存在 客户是否来信息
    //ptr2 = putstrstr(RxBuffer1, cipEnd2);  //判断是否存在客户是否来信息cipEnd3
    
    
    if(ptr2 !=NULL)
    {
       status=0;  
       types=0;
    }else
    {
      if(ptr1!=NULL)
      {
        reset_value();
      }
    }
    
  }while(status);
}


//读取fash的数据 看看是否有数据
uint8_t readFlashDate()
{
  uint8_t i=0;
  
   
  readyByte(ReadBuffer);
  if(ReadBuffer[0] != '\0')
  {
    getSSID(ReadBuffer);
    i = 1; 
  }
  return i;
}




//获取SSID
void getSSID(char *byffer)
{
  int stn=0;
  int i=0;
  char *substr = "nap";
    char *subpass= "sta";
    char bapssid[20]={0};
    char stapssid[20]={0};
    char *ssidstr = strstr(byffer, substr);//获取 ssid
    stn=(*(ssidstr+3))-'0';  //获取ssid个数
    for(i=0;i <stn ;i++)   //获取ssid值
    {
      bapssid[i] = *(ssidstr+15+i);
      if((stn-1)==i){
        bapssid[i+1]='\0';
      }
    }
    AP_SSID=bapssid;
    stn=0;
    char *stassidstr = strstr(ssidstr, subpass);//获取 ssid
    stn=(*(stassidstr+3))-'0';  //获取ssid个数
    for(i=0;i <stn ;i++)   //获取ssid值
    {
      stapssid[i] = *(stassidstr+15+i);
      if((stn-1)==i){
        stapssid[i+1]='\0';
      }
    }
    STA_SSID = stapssid;
    ///获取连接路由或者热点的指令
    int apstartcount=stassidstr-ssidstr-5;
    
    i=0;
    for(i=0; i < apstartcount; i++)
    {
      APBuffer[i]= *(ssidstr+5+i);
      if((apstartcount-1)==i){
        APBuffer[i+1]='\0';  //热点指令
      }
    }
    for(i=0; i < 50; i++)
    {
      STABuffer[i]= *(stassidstr+5+i);
      if(*stassidstr == '\0'){
        APBuffer[i]='\0';  //热点指令
      }
    }
    
}

/*初始化系统,并且重新启动*/
void reset()
{
  erasByte();
  WWDG_SWReset();
}
//重启系统
void restart()
{
  WWDG_SWReset();
}
 

//处理获取到的数据格式  格式标准: ssid+个数(一位)+路由器名+pass+个数(暂时个位)+密码
//例如:ssid2TDpass812345678
void saveFlaceDate()
{
  /**
  int stn=0;
  int i=0;
  char *substr = "ssid";
  char *subpass= "pass";
  char nowssid[50]={0};  //ssid值
  char nowpass[50]={0};  //pass值
  char *ssidstr = strstr(RxBuffer1, substr);//获取 ssid
  char *passtr=strstr(ssidstr, subpass); //获取 pass后面的字符串
  stn=(*(ssidstr+4))-'0';  //获取ssid个数
  for(i=0;i <stn ;i++)   //获取ssid值
  {
    nowssid[i] = *(ssidstr+5+i);
    if((stn-1)==i){
      nowssid[i+1]='\0';
    }
  }
  stn=(*(passtr+4))-'0';  //获取pass个数
  for(i=0;i <stn ;i++)    //获取pass值
  {
    nowpass[i] = *(passtr+5+i);
    if((stn-1)==i){
      nowpass[i+1]='\0';
    }
  }
  char *ch1 = "AT+CWJAP=\"";
  char *ch3 = "\",\"";
  char *ch5 = "\"";
  char *writeBuffer = NULL;
  writeBuffer = str_contact(ch1,nowssid);
  writeBuffer = str_contact(writeBuffer,ch3);
  writeBuffer = str_contact(writeBuffer,nowpass);
  writeBuffer = str_contact(writeBuffer,ch5);
  wraitByte(writeBuffer);
  */
  getSSID(RxBuffer1);
  wraitByte(RxBuffer1);
  Delayms(10);
}

/**
 ** 字符串拼接方法
 **/
char * str_contact(const char *str1,const char *str2)
{
     char * result;
     result = (char*)malloc(strlen(str1) + strlen(str2) + 1); //str1的长度 + str2的长度 + \0;
     if(!result){ //如果内存动态分配失败
        printf("Error: malloc failed in concat! \n");
        exit(EXIT_FAILURE);
     }
     strcpy(result,str1);
     strcat(result,str2); //字符串拼接
    return result;
}
 
//设置路由模式
void setCipMux()
{
  reset_value();
  printf("AT+CIPMUX=1\r\n");  //多人连接
  do
  {
    ptr1 = putstrstr(RxBuffer1, retrunok);  //判断是否存在ok
    if(ptr1 !=NULL )
    {
       status=0;  
    } 
   // Delayms(500);
  }while(status);
  //Delayms(2000);
}

//初始化数据
void reset_value(){
  memset(RxBuffer1,0,300);
  RxCounter1 = 0x00;
  status=1;
  ptr1=NULL;
  ptr2=NULL;
//  ptr3=NULL;
  
}

/**
* 获取数据下标
*/
uint8_t IncrementVar_RxCounter1(void)
{
        if(RxCounter1>=280)  //避免溢出
        {
          memset(RxBuffer1,0,300);
          RxCounter1 = 0x00;
        }
	return RxCounter1++;
}

/**
*  重构 查找 是否存在字符 
*/
char * putstrstr(char * rt,char * rx)
{
  Delayms(10);
  Refresh_WWDG_Window();
  
    char * rts = strstr(rt, rx);
      ptr3 = strstr (RxBuffer1,rest);
      if(ptr3 !=NULL)
     {
      resetMCU();
     }
     if(rts!=NULL)
     {
        LED_Operation(LED_2, ON);
     }else
     {
        LED_Operation(LED_2, OFF);
     }
     
    return rts;
}

/**
*
* 复位 MCU
*/
void resetMCU()
{
    LED_Operation(LED_1, OFF);
   // Delayms(2000);
    WWDG_SWReset();
  //RST_GetFlagStatus(RST_FLAG_EMCF);//复位 MCU
}

/**
* 获取 读取的数据
*/
uint8_t GetVar_RxCounter1(void)
{
return RxCounter1;
}
 


 //延时函数   
//---  微秒级延时--------------------------   
void Delayus(void)   
{    
    asm("nop"); //一个asm("nop")函数经过示波器测试代表100ns   
    asm("nop");   
    asm("nop");   
    asm("nop");    
}   
  
//---- 毫秒级延时程序-----------------------   
void Delayms(unsigned int time)   
{   
   
    
    unsigned int i;   
    while(time--)     
    for(i=900;i>0;i--)   
    Delayus();    
    
}


/**
  * @brief Retargets the C library printf function to the UART.
  * @param c Character to send
  * @retval char Character sent
  */
PUTCHAR_PROTOTYPE
{
  /* Write a character to the UART1 */
  UART1_SendData8(c);
  /* Loop until the end of transmission */
  while (UART1_GetFlagStatus(UART1_FLAG_TXE) == RESET);

  return (c);
}
/**
  * @brief Compares two buffers.
  * @param pBuffer1 First buffer to be compared.
  * @param pBuffer2 Second buffer to be compared.
  * @param BufferLength Buffer's length
  * @retval TestStatus Status of buffer comparison
  * - PASSED: pBuffer1 identical to pBuffer2
  * - FAILED: pBuffer1 differs from pBuffer2
  * @par Required preconditions:
  * None
  */
TestStatus Buffercmp(uint8_t* pBuffer1, uint8_t* pBuffer2, uint16_t BufferLength)
{
    while (BufferLength--)
    {
        if (*pBuffer1 != *pBuffer2)
        {
            return FAILED;
        }

        pBuffer1++;
        pBuffer2++;
    }

    return PASSED;
}


/**
  * @brief Retargets the C library scanf function to the USART.
  * @param None
  * @retval char Character to Read
  */
GETCHAR_PROTOTYPE
{
#ifdef _COSMIC_
  char c = 0;
#else
  int c = 0;
#endif
  /* Loop until the Read data register flag is SET */
  while (UART1_GetFlagStatus(UART1_FLAG_RXNE) == RESET);
    c = UART1_ReceiveData8();
  return (c);
}

