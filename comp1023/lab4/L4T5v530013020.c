/*
 * Name:        L4T5v530013020.c
 * Author:      Li Yanjun
 * Version:     1.0
 * Copyright:   Copyright (c) 2026 Li Yanjun
 * Description: This program calculates m to the power n with one recursive function and one loop.
 */

#include <stdio.h>

/* Return m raised to n by multiplying m once for each remaining exponent. */
int PowerRec(int m, int n)
{
    if (n == 0)
    {
        return 1;
    }

    return m * PowerRec(m, n - 1);
}

/* Return m raised to n by multiplying m inside a loop n times. */
int PowerLoop(int m, int n)
{
    int product;
    int i;

    product = 1;
    for (i = 0; i < n; i++)
    {
        product = product * m;
    }

    return product;
}

int main(void)
{
    int m;
    int n;
    int recursiveProduct;
    int loopProduct;

    /* Read the base first and the exponent second. */
    printf("Input m and n: ");
    scanf("%d%d", &m, &n);

    /* A positive base is an integer greater than zero. */
    if (m <= 0)
    {
        printf("Warning! Input of m is invalid!\n");
        return 0;
    }

    /* A natural-number exponent is zero or a positive integer. */
    if (n < 0)
    {
        printf("Warning! Input of n is invalid!\n");
        return 0;
    }

    /* Compute the same power with both required functions and print both results. */
    recursiveProduct = PowerRec(m, n);
    loopProduct = PowerLoop(m, n);
    printf("Product is %d using recursion.\n", recursiveProduct);
    printf("Product is %d using loop.\n", loopProduct);

    return 0;
}
