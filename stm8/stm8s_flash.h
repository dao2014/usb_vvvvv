/**件名  ：flash_eeprom.c
 * 描述    ：Flash内部Data ROM读写实验   
 * 实验平台：风驰STM8开发板
 * 库版本  ：V2.1.0
 * 作者    ：风驰  QQ：779814207
 * 博客    ：
 * 淘宝    ：http://shop71177993.taobao.com/
 * 修改时间 ：2012-12-10
*******************************************************************************/
#ifndef __FLASH_EEPROM_H
#define __FLASH_EEPROM_H
件 */
件 */
#include "stm8s.h"
#include "stm8s_flash.h"

/* 自定义宏 */

/* 全局变量定义 */

void readyByte(uint8_t *Buffer);
void wraitByte(uint8_t *Buffer);
void erasByte(); //擦除 flash

#endif

/******************* (C) COPYRIGHT 风驰iCreate嵌入式开发工作室 *****END OF FILE****/
