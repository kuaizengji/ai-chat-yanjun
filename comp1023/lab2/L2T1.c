/*
 * File:        L2T1.c
 * Author:      Li Yanjun
 * Student ID:  V########
 * Version:     1.0
 * Date:        10 October 2026
 * Description: Report how many bytes this machine uses to store
 *              char, short int, int, float, long int and double.
 *              The sizes come from sizeof, so the numbers follow
 *              the platform on which the program is compiled.
 */

#include <stdio.h>

int main(void)
{
    /* sizeof yields size_t; the required format uses %d. */
    printf("Number of bytes used to store a char type variable is %d\n",
           (int)sizeof(char));
    printf("Number of bytes used to store a short int type variable is %d\n",
           (int)sizeof(short int));
    printf("Number of bytes used to store an int type variable is %d\n",
           (int)sizeof(int));
    printf("Number of bytes used to store a float type variable is %d\n",
           (int)sizeof(float));
    printf("Number of bytes used to store a long int type variable is %d\n",
           (int)sizeof(long int));
    printf("Number of bytes used to store a double type variable is %d\n",
           (int)sizeof(double));

    return 0;
}
