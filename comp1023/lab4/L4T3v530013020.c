/* Name: L4T3v530013020.c Author: Li Yanjun Version: 1.0 Copyright: Copyright (c) 2026 Li Yanjun Description: This program uses recursion to calculate the sum of squares from 1 through n. */

#include <stdio.h>

/* Return 1*1 + 2*2 + ... + n*n by recurring down to 1. */
int f(int n)
{
    if (n == 1)
    {
        return 1;
    }

    return n * n + f(n - 1);
}

int main(void)
{
    int n;
    int sum;

    /* Read the upper bound of the sum. */
    printf("Input a positive integer (>=1): ");
    scanf("%d", &n);

    /* A legal bound is an integer that is at least 1. */
    if (n < 1)
    {
        printf("Warning! A positive integer no smaller than 1 should be input.\n");
        return 0;
    }

    /* Call the recursive function and print the value it returns. */
    sum = f(n);
    printf("Sum is %d when n is %d.\n", sum, n);

    return 0;
}
