/**����  ��wwdg.c
 * ����    ��wwdgʵ��   
 * ʵ��ƽ̨�����STM8������
 * ��汾  ��V2.1.0
 * ����    �����  QQ��779814207
 * ����    ��
 * �Ա�    ��http://shop71177993.taobao.com/
 *�޸�ʱ�� ��2012-11-25
*******************************************************************************/
#ifndef __WWDG_H
#define __WWDG_H
�� */
�� */
#include "stm8s.h"

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
void WWDG_Conf(void);

void Test_WWDGReset(void);
/*******************************************************************************
 * ����: Refresh_WWDG_Window
 * ����: ����WWDG�����Ĵ���
 * �β�: 1
 * ����: 1
 * ˵��: 1 
 ******************************************************************************/
void Refresh_WWDG_Window(void);

#endif

/*************** (C) COPYRIGHT ���iCreateǶ��ʽ���������� *****END OF FILE****/