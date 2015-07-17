����  ��wwdg.c
 * ����    ��wwdgʵ��   
 * ʵ��ƽ̨�����STM8������
 * ��汾  ��V2.1.0
 * ����    �����  QQ��779814207
 * ����    ��
 * �Ա�    ��http://shop71177993.taobao.com/
 *�޸�ʱ�� ��2012-11-25
*******************************************************************************/
�� */
�� */
#include "wwdg.h"
#include "led.h"

/* �Զ��������� */

/* �Զ���� */
#define COUNTERINIT 0x7f	/* ������ʼֵ */
#define WINDOW      0x77	/* ��������ֵ */

/* ȫ�ֱ������� */

/*******************************************************************************
 * ����: WWDG_Conf
 * ����: WWDG��ʼ������
 * �β�: 1
 * ����: 1
 * ˵��: 1 
 ******************************************************************************/
void WWDG_Conf(void) 
{
    /* WWDG ���� */
    /* WWDG��ʱ���Ǹ�ϵͳʱ�ӵ�
  		Watchdog Window= 0x7F step to 0x3F step
    	= (0x7F-0x3F) * 1 step
    	= 64 * 1 step
    	= 64 * (12288/2Mhz)
    	= 393,216ms
    */
    /* Non Allowed Window = (0x7F-window) * 1 step
    = (0x7F-0x77) * 1 step
    = 7 * 1 step 
    = 7 * (12288/2Mhz) 
    = 43.008ms 
    */
    /* 
     * ���Բ����� 0.0ms to 43.008ms ���ʱ����������
     * ������ʱ����ǣ�43.008ms to 393,216ms 
     */
    WWDG_Init(COUNTERINIT, WINDOW);
}
void Test_WWDGReset(void)
{
	FlagStatus WwdgFlag;
	
	WwdgFlag = RST_GetFlagStatus(RST_FLAG_WWDGF);	/* �õ� WWDG Reset ��־λ״̬ */ 
	/* �����Ƿ���λ */
	if (WwdgFlag)
	{
		/* ��� IWDGF ��־ */
		RST_ClearFlag(RST_FLAG_WWDGF);
		LED_Flick();
	}
}

/*******************************************************************************
 * ����: Refresh_WWDG_Window
 * ����: ����WWDG�����Ĵ���
 * �β�: 1
 * ����: 1
 * ˵��: 1 
 ******************************************************************************/
void Refresh_WWDG_Window(void)
{
	u8 CounterValue;
	CounterValue = (u8)(WWDG_GetCounter() & 0x7F);
	
	/* 
	 * �ж��Ƿ�С�������õĴ�������ֵ 
	 * ֻ��С�ڴ�������ֵ��������
	 */
	if(CounterValue < WINDOW)
	{
		WWDG_SetCounter(COUNTERINIT);
	}
}
/******************* (C) COPYRIGHT ���iCreateǶ��ʽ���������� *****END OF FILE****/