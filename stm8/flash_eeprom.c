/**����  ��flash_eeprom.c
 * ����    ��Flash�ڲ�Data ROM��дʵ��   
 * ʵ��ƽ̨�����STM8������
 * ��汾  ��V2.1.0
 * ����    �����  QQ��779814207
 * ����    ��
 * �Ա�    ��http://shop71177993.taobao.com/
 * �޸�ʱ�� ��2012-12-10
*******************************************************************************/
�� */
�� */
#include "flash_eeprom.h"
#include "stm8s.h"

/* �Զ��������� */

/* �Զ���� */

/* ȫ�ֱ������� */
uint32_t add = 0x004000;

/*******************************************************************************
 * ����: WriteMultiBlockByte
 * ����: ����д���Block�ֽ�
 * �β�: BlockStartAddress    �ֽڱ�д���Block�׵�ַ
         FLASH_MemType        FLASH Memory��������
         FLASH_ProgMode       FLASH ���ģʽ
         Buffer               Ҫд��flash eeprom ���ֽ�����
         BlockNum             Ҫд��flash eeprom ��Block����
 * ����: 1
 * ˵��: ÿ���ͺŵ�EEPROM�Ĵ�С��һ�������ô˺�����ʱ��Ҫע�⽫Ҫд�����ֽ�����
         �Ĵ�С�Ƿ񳬹����ͺŵ�EEPROM�ĵ�ַ��
         ��������EEPROM���ͺ���STM8S208, STM8S207, STM8S007, STM8AF52Ax, STM8AF62Ax 
         EEPROM�ĵ�ַ�Ǵ�0x004000��0x0047ff����2048Byte,ÿ��Block��128Byte,һ��16��Block.
         ��������EEPROM���ͺ���STM8S105, STM8S005, STM8AF626x
         EEPROM�ĵ�ַ�Ǵ�0x004000��0x0043ff����1024Byte,ÿ��Block��128Byte,һ��8��Block.
         С������EEPROM���ͺ���STM8S103, STM8S003, STM8S903 
         EEPROM�ĵ�ַ�Ǵ�0x004000��0x00427f����1024Byte,ÿ��Block��64Byte,һ��10��Block.

 ******************************************************************************/

void erasByte()
{
add = 0x004000;
 FLASH_SetProgrammingTime(FLASH_PROGRAMTIME_STANDARD);
  
  FLASH_Unlock(FLASH_MEMTYPE_PROG);
  /* Wait until Flash Program area unlocked flag is set*/
  while (FLASH_GetFlagStatus(FLASH_FLAG_PUL) == RESET)
  { Refresh_WWDG_Window();}
  FLASH_Unlock(FLASH_MEMTYPE_DATA);//����
   
  /* Wait until Data EEPROM area unlocked flag is set*/
  while (FLASH_GetFlagStatus(FLASH_FLAG_DUL) == RESET)
  { Refresh_WWDG_Window();}
  //FlagStatus flag_status = FLASH_GetFlagStatus(FLASH_FLAG_DUL);
  int counts = 0;
  for(counts=0;counts<64;counts++)
  {
    FLASH_EraseByte(add);
    add +=1;
  }
  FLASH_Lock(FLASH_MEMTYPE_DATA);  //����
}


void wraitByte(uint8_t *Buffer)
{
  add = 0x004000;
  FLASH_SetProgrammingTime(FLASH_PROGRAMTIME_STANDARD);
  
  FLASH_Unlock(FLASH_MEMTYPE_PROG);
  /* Wait until Flash Program area unlocked flag is set*/
  while (FLASH_GetFlagStatus(FLASH_FLAG_PUL) == RESET)
  { Refresh_WWDG_Window();}
  FLASH_Unlock(FLASH_MEMTYPE_DATA);//����
   
  /* Wait until Data EEPROM area unlocked flag is set*/
  while (FLASH_GetFlagStatus(FLASH_FLAG_DUL) == RESET)
  { Refresh_WWDG_Window();}
  //FlagStatus flag_status = FLASH_GetFlagStatus(FLASH_FLAG_DUL);
  while(1){
    Refresh_WWDG_Window();
    uint8_t dates = *Buffer++;
    FLASH_ProgramByte(add++, dates);
    if(dates == '\0')
    {
      break;
    }
  }
  FLASH_Lock(FLASH_MEMTYPE_DATA);  //����
  
}

void readyByte(uint8_t *Buffer)
{
   add=0x004000;
   int counts=0;
    while(1){
      Refresh_WWDG_Window();
      uint8_t dates = FLASH_ReadByte(add++);
      Buffer[counts++] = dates;
      if(counts >100)
      {
        break;
      }
    }
}

/*************** (C) COPYRIGHT ���iCreateǶ��ʽ���������� *****END OF FILE****/
