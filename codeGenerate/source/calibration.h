#ifndef CALIBRATION
#define CALIBRATION
/**
 * @file calibration.h
 * @version 1.01
 * @author aokutlu
 * @copyright copyright
 * @date 24.11.2020
 * @brief Calibration source
 */
/********************************************************************************************************
* Defines
********************************************************************************************************/
#define TAG
#define PI (3.14) 
/**
 *  This is commnent
 *	This is comment
 */

#define VOID(VAL) (CAL(VAL)) 


/********************************************************************************************************
* Typedefs
********************************************************************************************************/
typedef struct 
{
  float x;
  float y;
  float z;
struct 
{
  uint8 d;
  uint8 e;
  uint8 f;
struct 
{
  uint8 d;
  uint8 e;
  uint8 f;
}position_asd;
/**
 *  This is commnent
 */

}position_g;
/**
 *  This is commnent
 */

union 
{
  uint8 a;
  uint8 b;
  uint8 c;
}position_f;
/**
 *  This is commnent
 */

}position_t;
/**
 *  This is commnent
 */


typedef union 
{
  float x;
  float y;
  float z;
}position_t;
/**
 *  This is commnent
 */


typedef enum 
{
  CENTER,
  LEFT =-1,
  RIGHT =1,
}positions;
/**
 *  This is commnent
 */


typedef union 
{
  float x;
  float y;
  float z;
}position_t;
/**
 *  This is commnent
 */




/********************************************************************************************************
* Global Function Prototypes
********************************************************************************************************/
void calibration_aokutlu8(void);


/********************************************************************************************************
* Extern Global Variables
********************************************************************************************************/
extern float g_pram1;
extern uint32 g_pram2;
extern uint32 g_pram3;
extern float g_pram1[];
extern float g_pram2[];


/********************************************************************************************************
* Extern Global Function Prototypes
********************************************************************************************************/
extern void calibration_aokutlu4(void);


/********************************************************************************************************/
#endif //CALIBRATION