/**����  ��flash_eeprom.c
 * ����    ��Flash�ڲ�Data ROM��дʵ��   
 * ʵ��ƽ̨�����STM8������
 * ��汾  ��V2.1.0
 * ����    �����  QQ��779814207
 * ����    ��
 * �Ա�    ��http://shop71177993.taobao.com/
 * �޸�ʱ�� ��2012-12-10
*******************************************************************************/
#ifndef __FLASH_EEPROM_H
#define __FLASH_EEPROM_H
�� */
�� */
#include "stm8s.h"
#include "stm8s_flash.h"

/* �Զ���� */

/* ȫ�ֱ������� */

void readyByte(uint8_t *Buffer);
void wraitByte(uint8_t *Buffer);
void erasByte(); //���� flash

#endif

/******************* (C) COPYRIGHT ���iCreateǶ��ʽ���������� *****END OF FILE****/
