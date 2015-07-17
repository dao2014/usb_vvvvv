/* Includes ------------------------------------------------------------------*/

#include "wifi_confg.h"
#include "led.h"
#include "wwdg.h"

/* Private typedef -----------------------------------------------------------*/


/* Private function prototypes -----------------------------------------------*/
/* Private functions ---------------------------------------------------------*/
 
static void CLK_Config(void);
static void UART_Config(void);
void Key_Exti_Init();
//flase 驱动
static void configFlas();
extern uint8_t RxBuffer1[];
extern uint8_t ReadBuffer[];
void main(void)
{
  /* Infinite loop */
  /* CLK configuration -----------------------------------------*/
  CLK_Config();
  UART_Config();
  LED_Init();
  Key_Exti_Init();
  asm("rim"); 
  WWDG_Conf();
  readFlashDate();
  if(ReadBuffer[0] == '\0'){
     setconfigAp();
  }
  lengWifi();
  /*  配置GPIO模式， PC4为浮空输入，1中断。用于 ADC模拟输入 AIN2 */
  GPIO_Init(GPIOC, GPIO_PIN_4 , GPIO_MODE_IN_FL_NO_IT);
  GPIO_Init(GPIOD, (GPIO_PIN_3 | GPIO_PIN_2), GPIO_MODE_IN_FL_NO_IT);
  GPIO_Init(GPIOC, GPIO_PIN_5, GPIO_MODE_OUT_PP_HIGH_FAST );
  GPIO_Init(GPIOC, GPIO_PIN_3, GPIO_MODE_OUT_PP_HIGH_FAST );
  GPIO_Init(GPIOB, GPIO_PIN_4, GPIO_MODE_OUT_PP_HIGH_FAST );
  GPIO_Init(GPIOB, GPIO_PIN_5, GPIO_MODE_OUT_PP_HIGH_FAST );
  while (1)
    
  {
    //getADC();
    //Delayms(30);
   // OneChannelGetADValue(ADC2_CHANNEL_2,ADC2_SCHMITTTRIG_CHANNEL2);
   // Refresh_WWDG_Window();
    /**/
    int i = 0;
    for(i;i<15;i++){
      Refresh_WWDG_Window();
      LED_Operation(LED_2, OFF);
      Delayms(30);
      Refresh_WWDG_Window();
      Delayms(30);
      Refresh_WWDG_Window();
      LED_Operation(LED_2, ON);
      Delayms(30);
      Refresh_WWDG_Window();
      
    }
    putDate();
    
    
  }
  
} 
 
/*******************************************************************************
 * 名称: Key_Exti_Init
 * 功能: 按键外设GPIO引脚初始化操作
 * 形参: 1
 * 返回: 1
 * 说明: 1 
 ******************************************************************************/
void Key_Exti_Init(void)
{
  	/* 与按键相连的引脚设置为输入模式 */
    GPIO_Init(GPIOA, GPIO_PIN_1, GPIO_MODE_IN_PU_IT);
	
	/* 将GPIOD端口设置为下降沿触发中断--因为按键按下时产生一个低电平 */
    EXTI_SetExtIntSensitivity(EXTI_PORT_GPIOA, EXTI_SENSITIVITY_FALL_ONLY);
}
 
/**
  * @brief  Configure system clock to run at 16Mhz
  * @param  None
  * @retval None
  */
static void CLK_Config(void)
{
    /* Initialization of the clock */
    /* Clock divider to HSI/1 */
    CLK_HSIPrescalerConfig(CLK_PRESCALER_HSIDIV1);
    
}

//flase 驱动
static void configFlas()
{
  /* Define flash programming Time*/
  FLASH_SetProgrammingTime(FLASH_PROGRAMTIME_STANDARD);

  FLASH_Unlock(FLASH_MEMTYPE_PROG);
  /* Wait until Flash Program area unlocked flag is set*/
  while (FLASH_GetFlagStatus(FLASH_FLAG_PUL) == RESET)
  {}

  /* Unlock flash data eeprom memory */
  FLASH_Unlock(FLASH_MEMTYPE_DATA);
  /* Wait until Data EEPROM area unlocked flag is set*/
  while (FLASH_GetFlagStatus(FLASH_FLAG_DUL) == RESET)
  {}
}

/**
  * @brief  UART1 and UART3 Configuration for half duplex communication
  * @param  None
  * @retval None
  */
static void UART_Config(void)
{
  /* Deinitializes the UART1 and UART3 peripheral */
    UART1_DeInit();
    /* UART1 and UART3 configuration -------------------------------------------------*/
    /* UART1 and UART3 configured as follow:
          - BaudRate = 9600 baud  
          - Word Length = 8 Bits
          - One Stop Bit
          - No parity
          - Receive and transmit enabled
          - UART1 Clock disabled
     */
    /* Configure the UART1 */
    UART1_Init((uint32_t)115200, UART1_WORDLENGTH_8D, UART1_STOPBITS_1, UART1_PARITY_NO,
                UART1_SYNCMODE_CLOCK_DISABLE, UART1_MODE_TXRX_ENABLE);
    
    /* Enable UART1 Transmit interrupt*/
 
    UART1_ITConfig(UART1_IT_RXNE_OR, ENABLE);
    /* Enable general interrupts */
    enableInterrupts();    
}

#ifdef USE_FULL_ASSERT

/**
  * @brief  Reports the name of the source file and the source line number
  *   where the assert_param error has occurred.
  * @param file: pointer to the source file name
  * @param line: assert_param error line source number
  * @retval : None
  */
void assert_failed(u8* file, u32 line)
{ 
  /* User can add his own implementation to report the file name and line number,
     ex: printf("Wrong parameters value: file %s on line %d\r\n", file, line) */

  /* Infinite loop */
  while (1)
  {
  }
}
#endif

/******************* (C) COPYRIGHT 2011 STMicroelectronics *****END OF FILE****/

