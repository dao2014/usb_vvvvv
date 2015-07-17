件名  ：adc.c件   
 * 实验平台：风驰STM8开发板
 *  * 库版本  ：V2.1.0
 *   * 作者    ：风驰  QQ：779814207
 *    * 博客    ：
 *     * 淘宝    ：http://shop71177993.taobao.com/
 *      * 修改时间 ：2012-11-18
 *
 *        风驰STM8开发板硬件连接
 *            |-------------------------------|
 *                |  光敏/热敏 - PF0(Adc2->CH10)  |
 *                    |-------------------------------|
 *
 *                    *******************************************************************************/
件 */

件 */
#include "adc.h"
#include "wwdg.h"
/* 自定义新类型 */

/* 自定义宏 */

/* 全局变量定义 */

 
 
/*******************************************************************************
 *  * 名称: OneChannelGetADValue
 *   * 功能: ADC2单通道选择读取AD值
 *    * 形参: ADC2_Channel,ADC2_SchmittTriggerChannel
 *     * 返回: 该通道的AD值
 *      * 说明: 当要读取多通道AD值采调用该函数，否则只是初始化ADC_Init即可 
 *       ******************************************************************************/
uint16_t OneChannelGetADValue(ADC2_Channel_TypeDef ADC2_Channel,ADC2_SchmittTrigg_TypeDef ADC2_SchmittTriggerChannel)
{
	  uint16_t ADConversion_Value=0;
	  //  Refresh_WWDG_Window();
	  //      /**< 连续转换模式 */
	  //          /**< 使能通道 */
	  //              /**< ADC时钟：fADC2 = fcpu/18 */
	  //                  /**< 这里设置了从TIM TRGO 启动转换，但实际是没有用到的*/
	  //                      /**  不使能 ADC2_ExtTriggerState**/
	  //                          /**< 转换数据右对齐 */
	  //                              /**< 不使能通道10的斯密特触发器 */
	  //                                  /**  不使能通道10的斯密特触发器状态 */
	  //                                      ADC2_Init(ADC2_CONVERSIONMODE_CONTINUOUS , ADC2_Channel, ADC2_PRESSEL_FCPU_D2, ADC2_EXTTRIG_TIM, DISABLE, ADC2_ALIGN_RIGHT, ADC2_SchmittTriggerChannel,DISABLE);
	  //                                         // Delayms(10);
	  //                                          // Refresh_WWDG_Window();
	  //                                              ADC2_Cmd(ENABLE);
	  //                                                  ADC2_StartConversion();
	  //                                                     // Delayms(10);
	  //                                                      // Refresh_WWDG_Window();
	  //                                                          ADConversion_Value = ADC2_GetConversionValue();
	  //                                                           //   Refresh_WWDG_Window();
	  //                                                              // ADConversion_Value = 3300 * (uint32_t) ADConversion_Value * 2 / 1024 ;
	  //                                                                  return ADConversion_Value;
	  //                                                                  }
	  //                                                                   
