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
char sipsendretn[]="WiriterOK";//ȷ�Ϸ���������;Unlink   SEND OK
char sipsendretn1[]="SEND";
char sipsendretn2[]="Unlink";  //�������Ͽ�  link is not
char sipsendretn3[]="link is not";
char cipEnd[]="Linked";  //��ȡ�ͻ��˷��͹�������Ϣ �������  CLOSED
char cipEnd2[]="CLOSED";
char cipEnd3[]="sta";  //У������
char apssidstr[]="nap";  //У������
char stassidstr[]="sta";  //У������

//��ʱ������
//char *datas="set,12345,3.7V,0.2A,7777777777777777";
char *ptr1;
char *ptr2;
char *ptr3;  //��Ҫ���ж��Ƿ� �쳣 �����ȡ���쳣������ MCU
char *ptr4;  //У�� �Ƿ���Ҫ����
int counts=0;  //��Ҫ�Ǽ���Ƿ� Ҫ��λ  ����3�ζ�ȡ�� ready ��ʼ ��λ
char types=0;//0��ʾ��ȷ  1 ��ʾ �ȴ�����  2��ʾ:���� 3��ʾ:û�иı� 4��ʾ�����쳣 5,��ʾAP ��������
char status=1;   //����ѭ������
uint16_t stmsV;  //��ѹ
 uint16_t stmsI;  //����
 uint16_t stmsIADC;  //����ADC
 char staorap=0;  //0��ʾ �ȵ����� 1��ʾwifi������
 
uint16_t tci;
/* Private functions ---------------------------------------------------------*/
TestStatus Buffercmp(uint8_t* pBuffer1, uint8_t* pBuffer2, uint16_t BufferLength);
void reset_value();
//����ģʽ
void setCwmode(char *mode);
//���� AP ģʽ������,�Լ�ssid pass
void setCwsap();
//�鿴�Ƿ����ֻ����� ÿ2���Ӽ��һ�ΰ�
void findCwlif();
//���� tcp ������  ����Ҫ���� ·��ģʽΪ1 ��ʱ����ܹ����� tcp ������
void openTcpServier();
//����·��ģʽ
void setCipMux();
//��ʼ������
void reset_value();
//flase����
void flaseConfig();
//����AP ·����
void setCwjap();
//��ѯ�Ƿ��ȡ ·�ɷ����IP
void findCifsr();
// ���� tcp ������
void findCipstart();
//��ȡ���ӳɹ�״̬ 
void findCipstatus();

//�ȴ��ͻ��˷��͵���Ϣ
void waitClientDate();
//��ȡ ssid
void getSSID(char *byffer);
/**
 ** �ַ���ƴ�ӷ���
 **/
char * str_contact(const char *str1,const char *str2);
//�����ȡ�������ݸ�ʽ  ��ʽ��׼: ssid+����(һλ)+·������+pass+����(��ʱ��λ)+����
//����:ssid2TDpass812345678
void saveFlaceDate();
//��ʼ������
void wifi_rst();
/**
*
* ��λ MCU
*/
void resetMCU();

/**
*  �ع� ���� �Ƿ�����ַ� 
*/
char * putstrstr(char * rt,char * rx);
//�鿴 SSID ��ʾ
void findATCWLAP();
//�˳���ǰ����
void findATCWQAP();




//��ʼ�� APģʽ ���� tcp ������ 
void setconfigAp()
{
  status=0;
  reset_value();
 
  printf("AT+RST\r\n");
  do
  {
    Delayms(10);
    Refresh_WWDG_Window();
    ptr1 = strstr(RxBuffer1, rest);  //�ж��Ƿ����ready
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
  if(types==5)  //��Ҫ����
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

//����·����
void lengWifi()
{
  wifi_rst();
  findATCWLAP();
  //����AP ·����
  setCwjap();
  //��ѯ�Ƿ��ȡ ·�ɷ����IP  ��������10�� ���2��.����������������
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
  //��ȡ���ӳɹ�״̬ 
  findCipstatus();
  //����tcp
  findCipstart();
}


//�˳���ǰ����
void findATCWQAP()
{
  reset_value();
  printf("AT+CWQAP\r\n");
  do
  {
    ptr1 = putstrstr(RxBuffer1, retrunok);  //�ж��Ƿ�����ȵ�ptr2 = putstrstr(RxBuffer1, stassidstr);  
     
    if(ptr1 !=NULL )
    {
       
       status=0;
    } 
    
  }while(status);
}

//�鿴 SSID ��ʾ
void findATCWLAP()
{
  findATCWQAP();
  reset_value();
  printf("AT+CWLAP\r\n");
  do
  {
    ptr1 = putstrstr(RxBuffer1, apssidstr);  //�ж��Ƿ�����ȵ�
    ptr2 = putstrstr(RxBuffer1, stassidstr);  //  �ж��Ƿ����·��
   if(ptr2 !=NULL )
    {
      staorap=1;  //wifi
       status=0;
    }
    if(ptr1 !=NULL )
    {
      staorap=0;  //�ȵ�
       status=0;
    } 
    
  }while(status);
}

//��ʼ������
void wifi_rst()
{
  
  reset_value();
  printf("AT+RST\r\n");
  //UART1_ReceiverWakeUpCmd(ENABLE);
  do
  {
    Delayms(10);
  Refresh_WWDG_Window();
    ptr1 = strstr(RxBuffer1, rest);  //�ж��Ƿ����ready
    if(ptr1!=NULL)
    {
      //printf("find ERROR !!\r\n");
      status=0;
    }
   // Delayms(1000);
  }while(status);
}



//����AP ·����
void setCwjap()
{
   reset_value();
  // ReadMultiBlockByte(Block_1,1,ReadBuffer);
   //readyByte(ReadBuffer);
   if(staorap==0)  //˵����ǰ���ӵ� �ȵ�
   {
      printf("%s\r\n",APBuffer);
   }else{ //˵�����ӵ���wifi
      printf("%s\r\n",STABuffer);
   }
    
   do
  {
    ptr1 = putstrstr(RxBuffer1, retrunok);  //�ж��Ƿ����ok
    if(ptr1!=NULL)
    {
      //printf("find ERROR !!\r\n");
      status=0;
      Refresh_WWDG_Window();
    }
   // Delayms(1000);
  }while(status);
}



 

//��ѯ�Ƿ��ȡ ·�ɷ����IP
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


// ���� tcp ������
void findCipstart()
{
  reset_value();
  if(staorap==0)  //˵����ǰ���ӵ� �ȵ�
   {
      printf("AT+CIPSTART=\"TCP\",\"192.168.43.1\",1001\r\n");
   }else{ //˵�����ӵ���wifi
      printf("AT+CIPSTART=\"TCP\",\"192.168.6.100\",1001\r\n");
   }
   
   do
  {
    ptr1 = putstrstr(RxBuffer1, cipstart);  //�ж��Ƿ����Linked ��ʾ ��û���ӳɹ�
    ptr2 = putstrstr(RxBuffer1,cipstart1);
    if(ptr1 !=NULL || ptr2 !=NULL )
    {
      status=0;
    }
    
  }while(status);
}



//��ȡ���ӳɹ�״̬ 
void findCipstatus()
{
  reset_value();
   printf("AT+CIPSTATUS\r\n");
   do
  {
    ptr1 = putstrstr(RxBuffer1, cipstatus);  //�ж��Ƿ����ERROR ��ʾ ��û���ӳɹ�
    //ptr2 = putstrstr(RxBuffer1, cifsrR1);  // û�иı�
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
  stmsI = 3300 * (uint32_t) stmsI *100/2/5/ 1024;  //������� R0.05�ĵ�ѹֵ Ȼ�� I=U/R
 // Delayms(10);
 // Refresh_WWDG_Window();
  tci=0;
  tci = OneChannelGetADValue(ADC2_CHANNEL_3,ADC2_SCHMITTTRIG_CHANNEL3);

}


//��������, �����ܳ��������ĵط�
void putDate()
{
  //status=1;
  reset_value();
  printf("AT+CIPSEND=60\r\n");//����10���ַ�
  
  do
  {

    ptr1 = putstrstr(RxBuffer1, sipsendrs);  //�ж��Ƿ����Linked ��ʾ ��û���ӳɹ�  +i 
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
  ////��ʱ������
 // char *datas="set,12345,3.7V,0.2A,7777777777777777";
 // unsigned char *pIDStart=(unsigned char *)(ID_BaseAddress); 
   
    reset_value();
    getADC();
     //printf("set,123123,123123mV,123123mA,123123Tc,777777777111111111111177" );
    printf("set,123123,%d,%d,%d,7777777771111111111111777777777777777777777777777777777",stmsV,stmsI,stmsIADC,tci);//��������
    do
    {
      ptr1 = putstrstr(RxBuffer1, sipsendretn);  //�ж��Ƿ���ڷ��������ش��� sipsendretn sipsendretn
      ptr2 = putstrstr(RxBuffer1, retrunok);  //�ж��Ƿ����OK  
      ptr3 = putstrstr(RxBuffer1, sipsendretn2);
      if(ptr1 !=NULL || ptr3 != NULL)
      {
        status=0;
      }
      
    }while(status);
  
}


//����ģʽ
void setCwmode(char *mode)
{
  reset_value();
  printf("AT+CWMODE=%s\r\n",mode);
  do
  {
    ptr1 = putstrstr(RxBuffer1, retrunok);  //�ж��Ƿ����ok
    ptr2 = putstrstr(RxBuffer1, retrunno);  // û�иı�
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


//���� AP ģʽ������,�Լ�ssid pass
void setCwsap(){
  reset_value();
  printf("AT+CWSAP=\"ESP\",\"12345678\",5,3\r\n");
  do
  {
    ptr1 = putstrstr(RxBuffer1, retrunok);  //�ж��Ƿ����ok
    ptr2 = putstrstr(RxBuffer1, returnerror);  // û�иı�
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



//�鿴�Ƿ����ֻ����� ÿ2���Ӽ��һ�ΰ�

void findCwlif()
{
  reset_value();
  printf("AT+CWLIF\r\n");
  do
  {
    ptr3 = strstr (RxBuffer1,rest);
    ptr1 = putstrstr(RxBuffer1, retrunok);  //�ж��Ƿ����ok
    ptr2 = putstrstr(RxBuffer1, "192.168");  //  
    
    if(ptr3 !=NULL)
    {
      types=5;
      status=0;
    }
    if(ptr1 !=NULL )
    {
      if(ptr2 !=NULL )  //˵���������� 
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

//�ȴ� �Ѿ������ϵ�������Ϣ
void wailUser()
{
  reset_value();
  do
  {
    ptr1 = putstrstr(RxBuffer1, retrunok);  //�ж��Ƿ����ok
    ptr2 = putstrstr(RxBuffer1, "192.168");  //  
    if(ptr1 !=NULL )
    {
      if(ptr2 !=NULL )  //˵���������� 
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


//���� tcp ������  ����Ҫ���� ·��ģʽΪ1 ��ʱ����ܹ����� tcp ������
void openTcpServier()
{
  setCipMux(); //����ģʽ
  reset_value();
  printf("AT+CIPSERVER=1,1001\r\n");  // ����tcp ������ �˿�1001
  do
  {
    ptr1 = putstrstr(RxBuffer1, retrunok);  //�ж��Ƿ����ok
    if(ptr1 !=NULL )
    {
       status=0;  
    } 
   // Delayms(1000);
  }while(status);
  
}


//�ȴ��ͻ��˷��͵���Ϣ
void waitClientDate()
{
  reset_value();
  types=1;
  do
  {
    ptr2 = putstrstr(RxBuffer1, cipEnd3);
    ptr1 = putstrstr(RxBuffer1, retrunok);  //�ж��Ƿ���� �ͻ��Ƿ�����Ϣ
    //ptr2 = putstrstr(RxBuffer1, cipEnd2);  //�ж��Ƿ���ڿͻ��Ƿ�����ϢcipEnd3
    
    
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


//��ȡfash������ �����Ƿ�������
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




//��ȡSSID
void getSSID(char *byffer)
{
  int stn=0;
  int i=0;
  char *substr = "nap";
    char *subpass= "sta";
    char bapssid[20]={0};
    char stapssid[20]={0};
    char *ssidstr = strstr(byffer, substr);//��ȡ ssid
    stn=(*(ssidstr+3))-'0';  //��ȡssid����
    for(i=0;i <stn ;i++)   //��ȡssidֵ
    {
      bapssid[i] = *(ssidstr+15+i);
      if((stn-1)==i){
        bapssid[i+1]='\0';
      }
    }
    AP_SSID=bapssid;
    stn=0;
    char *stassidstr = strstr(ssidstr, subpass);//��ȡ ssid
    stn=(*(stassidstr+3))-'0';  //��ȡssid����
    for(i=0;i <stn ;i++)   //��ȡssidֵ
    {
      stapssid[i] = *(stassidstr+15+i);
      if((stn-1)==i){
        stapssid[i+1]='\0';
      }
    }
    STA_SSID = stapssid;
    ///��ȡ����·�ɻ����ȵ��ָ��
    int apstartcount=stassidstr-ssidstr-5;
    
    i=0;
    for(i=0; i < apstartcount; i++)
    {
      APBuffer[i]= *(ssidstr+5+i);
      if((apstartcount-1)==i){
        APBuffer[i+1]='\0';  //�ȵ�ָ��
      }
    }
    for(i=0; i < 50; i++)
    {
      STABuffer[i]= *(stassidstr+5+i);
      if(*stassidstr == '\0'){
        APBuffer[i]='\0';  //�ȵ�ָ��
      }
    }
    
}

/*��ʼ��ϵͳ,������������*/
void reset()
{
  erasByte();
  WWDG_SWReset();
}
//����ϵͳ
void restart()
{
  WWDG_SWReset();
}
 

//�����ȡ�������ݸ�ʽ  ��ʽ��׼: ssid+����(һλ)+·������+pass+����(��ʱ��λ)+����
//����:ssid2TDpass812345678
void saveFlaceDate()
{
  /**
  int stn=0;
  int i=0;
  char *substr = "ssid";
  char *subpass= "pass";
  char nowssid[50]={0};  //ssidֵ
  char nowpass[50]={0};  //passֵ
  char *ssidstr = strstr(RxBuffer1, substr);//��ȡ ssid
  char *passtr=strstr(ssidstr, subpass); //��ȡ pass������ַ���
  stn=(*(ssidstr+4))-'0';  //��ȡssid����
  for(i=0;i <stn ;i++)   //��ȡssidֵ
  {
    nowssid[i] = *(ssidstr+5+i);
    if((stn-1)==i){
      nowssid[i+1]='\0';
    }
  }
  stn=(*(passtr+4))-'0';  //��ȡpass����
  for(i=0;i <stn ;i++)    //��ȡpassֵ
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
 ** �ַ���ƴ�ӷ���
 **/
char * str_contact(const char *str1,const char *str2)
{
     char * result;
     result = (char*)malloc(strlen(str1) + strlen(str2) + 1); //str1�ĳ��� + str2�ĳ��� + \0;
     if(!result){ //����ڴ涯̬����ʧ��
        printf("Error: malloc failed in concat! \n");
        exit(EXIT_FAILURE);
     }
     strcpy(result,str1);
     strcat(result,str2); //�ַ���ƴ��
    return result;
}
 
//����·��ģʽ
void setCipMux()
{
  reset_value();
  printf("AT+CIPMUX=1\r\n");  //��������
  do
  {
    ptr1 = putstrstr(RxBuffer1, retrunok);  //�ж��Ƿ����ok
    if(ptr1 !=NULL )
    {
       status=0;  
    } 
   // Delayms(500);
  }while(status);
  //Delayms(2000);
}

//��ʼ������
void reset_value(){
  memset(RxBuffer1,0,300);
  RxCounter1 = 0x00;
  status=1;
  ptr1=NULL;
  ptr2=NULL;
//  ptr3=NULL;
  
}

/**
* ��ȡ�����±�
*/
uint8_t IncrementVar_RxCounter1(void)
{
        if(RxCounter1>=280)  //�������
        {
          memset(RxBuffer1,0,300);
          RxCounter1 = 0x00;
        }
	return RxCounter1++;
}

/**
*  �ع� ���� �Ƿ�����ַ� 
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
* ��λ MCU
*/
void resetMCU()
{
    LED_Operation(LED_1, OFF);
   // Delayms(2000);
    WWDG_SWReset();
  //RST_GetFlagStatus(RST_FLAG_EMCF);//��λ MCU
}

/**
* ��ȡ ��ȡ������
*/
uint8_t GetVar_RxCounter1(void)
{
return RxCounter1;
}
 


 //��ʱ����   
//---  ΢�뼶��ʱ--------------------------   
void Delayus(void)   
{    
    asm("nop"); //һ��asm("nop")��������ʾ�������Դ���100ns   
    asm("nop");   
    asm("nop");   
    asm("nop");    
}   
  
//---- ���뼶��ʱ����-----------------------   
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

