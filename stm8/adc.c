����  ��adc.c��   
 * ʵ��ƽ̨�����STM8������
 *  * ��汾  ��V2.1.0
 *   * ����    �����  QQ��779814207
 *    * ����    ��
 *     * �Ա�    ��http://shop71177993.taobao.com/
 *      * �޸�ʱ�� ��2012-11-18
 *
 *        ���STM8������Ӳ������
 *            |-------------------------------|
 *                |  ����/���� - PF0(Adc2->CH10)  |
 *                    |-------------------------------|
 *
 *                    *******************************************************************************/
�� */

�� */
#include "adc.h"
#include "wwdg.h"
/* �Զ��������� */

/* �Զ���� */

/* ȫ�ֱ������� */

 
 
/*******************************************************************************
 *  * ����: OneChannelGetADValue
 *   * ����: ADC2��ͨ��ѡ���ȡADֵ
 *    * �β�: ADC2_Channel,ADC2_SchmittTriggerChannel
 *     * ����: ��ͨ����ADֵ
 *      * ˵��: ��Ҫ��ȡ��ͨ��ADֵ�ɵ��øú���������ֻ�ǳ�ʼ��ADC_Init���� 
 *       ******************************************************************************/
uint16_t OneChannelGetADValue(ADC2_Channel_TypeDef ADC2_Channel,ADC2_SchmittTrigg_TypeDef ADC2_SchmittTriggerChannel)
{
	  uint16_t ADConversion_Value=0;
	  //  Refresh_WWDG_Window();
	  //      /**< ����ת��ģʽ */
	  //          /**< ʹ��ͨ�� */
	  //              /**< ADCʱ�ӣ�fADC2 = fcpu/18 */
	  //                  /**< ���������˴�TIM TRGO ����ת������ʵ����û���õ���*/
	  //                      /**  ��ʹ�� ADC2_ExtTriggerState**/
	  //                          /**< ת�������Ҷ��� */
	  //                              /**< ��ʹ��ͨ��10��˹���ش����� */
	  //                                  /**  ��ʹ��ͨ��10��˹���ش�����״̬ */
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
