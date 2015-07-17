
/**����  ��led.c
 * ����    ��LED����⺯��   
 * ʵ��ƽ̨�����STM8������
 * ��汾  ��V2.1.0
 * ����    �����  QQ��779814207
 * ����    ��
 * �Ա�    ��http://shop71177993.taobao.com/
 * �޸�ʱ�� ��2012-10-20

  ���STM8������Ӳ������
    |--------------------|
    |  LED1-PD0          |
    |  LED2-PD1          |
    |  LED3-PD2          |
    |  LED4-PD3          |
    |--------------------|

*******************************************************************************/
�� */
�� */
#include "led.h"

/* �Զ��������� */

/* �Զ���� */

/* ȫ�ֱ������� */


/*******************************************************************************
 * ����: LED_Init
 * ����: LED����GPIO���ų�ʼ������
 * �β�: 1
 * ����: 1
 * ˵��: 1 
 ******************************************************************************/
void LED_Init(void)
{
	GPIO_Init(LED_PORTC, (LED_1|LED_2), GPIO_MODE_OUT_PP_HIGH_FAST );	//����LED�Ĺܽ�Ϊ���ģʽ
        
}



/*******************************************************************************
 * ����: LED_Operation
 * ����: LED ���������ò���
 * �β�: ledx -> Ҫ������led��
 * 		 state -> ��������
 * ����: 1
 * ˵��: 1 
 ******************************************************************************/
void LED_Operation(GPIO_Pin_TypeDef ledx, u8 state)
{
  if(ON != state) {
    switch (ledx) {
    case LED_1: 
      GPIO_WriteLow(LED_PORTC, ledx);
      break;
      case LED_2: 
      GPIO_WriteLow(LED_PORTC, ledx);
      break;
    }
  } else {
    switch (ledx) {
      case LED_1: 
        GPIO_WriteHigh(LED_PORTC, ledx);
        break;
        case LED_2: 
        GPIO_WriteHigh(LED_PORTC, ledx);
        break;
    }
  }
}




/******************* (C) COPYRIGHT ���iCreateǶ��ʽ���������� *****END OF FILE****/
