
/**件名  ：adc.h件   
 * 实验平台：风驰STM8开发板
 * 库版本  ：V2.1.0
 * 作者    ：风驰  QQ：779814207
 * 博客    ：
 * 淘宝    ：http://shop71177993.taobao.com/
 *修改时间 ：2012-11-18

  风驰STM8开发板硬件连接
    |-------------------------------|
    |  光敏/热敏 - PF0(Adc2->CH10)  |
    |-------------------------------|

*******************************************************************************/

#ifndef __ADC_H
#define __ADC_H
件 */

件 */
#include "stm8s.h"

/* 自定义新类型 */

/* 自定义宏 */

/* 全局变量定义 */

/**************************************************************************
 * 函数名：Send_ADC_Value
 * 描述  ：ADC转换结果显示函数
 * 输入  ：AD_Value--ADC转换结果值
 *
 * 输出  ：1
 * 返回  ：1 
 * 调用  ：内部调用 
 *************************************************************************/

uint16_t OneChannelGetADValue(ADC2_Channel_TypeDef ADC2_Channel,ADC2_SchmittTrigg_TypeDef ADC2_SchmittTriggerChannel);

#endif
