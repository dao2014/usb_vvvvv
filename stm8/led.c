
/**件名  ：led.c
 * 描述    ：LED外设库函数   
 * 实验平台：风驰STM8开发板
 * 库版本  ：V2.1.0
 * 作者    ：风驰  QQ：779814207
 * 博客    ：
 * 淘宝    ：http://shop71177993.taobao.com/
 * 修改时间 ：2012-10-20

  风驰STM8开发板硬件连接
    |--------------------|
    |  LED1-PD0          |
    |  LED2-PD1          |
    |  LED3-PD2          |
    |  LED4-PD3          |
    |--------------------|

*******************************************************************************/
件 */
件 */
#include "led.h"

/* 自定义新类型 */

/* 自定义宏 */

/* 全局变量定义 */


/*******************************************************************************
 * 名称: LED_Init
 * 功能: LED外设GPIO引脚初始化操作
 * 形参: 1
 * 返回: 1
 * 说明: 1 
 ******************************************************************************/
void LED_Init(void)
{
	GPIO_Init(LED_PORTC, (LED_1|LED_2), GPIO_MODE_OUT_PP_HIGH_FAST );	//定义LED的管脚为输出模式
        
}



/*******************************************************************************
 * 名称: LED_Operation
 * 功能: LED 灯亮灭设置操作
 * 形参: ledx -> 要操作的led灯
 * 		 state -> 亮还是灭
 * 返回: 1
 * 说明: 1 
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




/******************* (C) COPYRIGHT 风驰iCreate嵌入式开发工作室 *****END OF FILE****/
