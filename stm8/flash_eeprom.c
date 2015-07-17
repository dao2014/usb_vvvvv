/**件名  ：flash_eeprom.c
 * 描述    ：Flash内部Data ROM读写实验   
 * 实验平台：风驰STM8开发板
 * 库版本  ：V2.1.0
 * 作者    ：风驰  QQ：779814207
 * 博客    ：
 * 淘宝    ：http://shop71177993.taobao.com/
 * 修改时间 ：2012-12-10
*******************************************************************************/
件 */
件 */
#include "flash_eeprom.h"
#include "stm8s.h"

/* 自定义新类型 */

/* 自定义宏 */

/* 全局变量定义 */
uint32_t add = 0x004000;

/*******************************************************************************
 * 名称: WriteMultiBlockByte
 * 功能: 任意写多个Block字节
 * 形参: BlockStartAddress    字节被写入的Block首地址
         FLASH_MemType        FLASH Memory操作类型
         FLASH_ProgMode       FLASH 编程模式
         Buffer               要写进flash eeprom 的字节数组
         BlockNum             要写进flash eeprom 的Block个数
 * 返回: 1
 * 说明: 每种型号的EEPROM的大小不一样，调用此函数的时候要注意将要写进的字节数组
         的大小是否超过该型号的EEPROM的地址。
         大容量的EEPROM的型号是STM8S208, STM8S207, STM8S007, STM8AF52Ax, STM8AF62Ax 
         EEPROM的地址是从0x004000到0x0047ff，共2048Byte,每个Block是128Byte,一共16个Block.
         中容量的EEPROM的型号是STM8S105, STM8S005, STM8AF626x
         EEPROM的地址是从0x004000到0x0043ff，共1024Byte,每个Block是128Byte,一共8个Block.
         小容量的EEPROM的型号是STM8S103, STM8S003, STM8S903 
         EEPROM的地址是从0x004000到0x00427f，共1024Byte,每个Block是64Byte,一共10个Block.

 ******************************************************************************/

void erasByte()
{
add = 0x004000;
 FLASH_SetProgrammingTime(FLASH_PROGRAMTIME_STANDARD);
  
  FLASH_Unlock(FLASH_MEMTYPE_PROG);
  /* Wait until Flash Program area unlocked flag is set*/
  while (FLASH_GetFlagStatus(FLASH_FLAG_PUL) == RESET)
  { Refresh_WWDG_Window();}
  FLASH_Unlock(FLASH_MEMTYPE_DATA);//解锁
   
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
  FLASH_Lock(FLASH_MEMTYPE_DATA);  //锁上
}


void wraitByte(uint8_t *Buffer)
{
  add = 0x004000;
  FLASH_SetProgrammingTime(FLASH_PROGRAMTIME_STANDARD);
  
  FLASH_Unlock(FLASH_MEMTYPE_PROG);
  /* Wait until Flash Program area unlocked flag is set*/
  while (FLASH_GetFlagStatus(FLASH_FLAG_PUL) == RESET)
  { Refresh_WWDG_Window();}
  FLASH_Unlock(FLASH_MEMTYPE_DATA);//解锁
   
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
  FLASH_Lock(FLASH_MEMTYPE_DATA);  //锁上
  
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

/*************** (C) COPYRIGHT 风驰iCreate嵌入式开发工作室 *****END OF FILE****/
