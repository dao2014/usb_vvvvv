#ifndef __WIFI_CONFG_H__
#define __WIFI_CONFG_H__
#include "flash_eeprom.h"

#define TxBufferSize1   (countof(RxBuffer1) - 1)
#define KEY_PORTA        GPIOA	/* ���尴����������GPIO�˿� */

/* LED�����ӵ�GPIO���Ŷ��� */
#define KEY_1     	GPIO_PIN_1
/* Private macro -------------------------------------------------------------*/
#define countof(a)   (sizeof(a) / sizeof(*(a)))
/* Exported functions ------------------------------------------------------- */
uint8_t GetVar_RxCounter1(void);
/***/
uint8_t IncrementVar_RxCounter1(void);

//��ȡfash������ �����Ƿ��б���
uint8_t readFlashDate();
//��ʼ�� APģʽ ���� tcp ������ 
void setconfigAp();
//��������, �����ܳ��������ĵط�
 void putDate();
void getADC();
 //����·����
void lengWifi();
//��ȡfash������ �����Ƿ�������
//int readFlashDate();
void Delayms(unsigned int time) ;  //���뼶��ʱ����--
//---  ΢�뼶��ʱ--------------------------   
void Delayus(void);
/*��ʼ��ϵͳ,������������*/
void reset();
/*��������ϵͳ*/
void restart();
#endif /* __MAIN_H__ */
