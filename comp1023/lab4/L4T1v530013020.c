/*
 * Name:        L4T1v530013020.c
 * Author:      Li Yanjun
 * Version:     1.0
 * Copyright:   Copyright (c) 2026 Li Yanjun
 * Description: This program reads a positive integer and prints its square root, base-10 log, and natural log.
 */

#include <stdio.h>
#include <math.h>

int main(void)
{
    int n;

    /* Ask for one integer and store it in n. */
    printf("Input a positive integer: ");
    scanf("%d", &n);

    /* Reject zero and negative values before calling the math library. */
    if (n <= 0)
    {
        printf("Warning! You should input a positive integer!\n");
        return 0;
    }

    /* Print the three required results with six digits after the decimal point. */
    printf("The square root of %d = %.6f\n", n, sqrt(n));
    printf("The Log10 of %d = %.6f\n", n, log10(n));
    printf("The natural log of %d = %.6f\n", n, log(n));

    return 0;
}
