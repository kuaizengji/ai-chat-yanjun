/* Name: L4T4v530013020.c Author: Li Yanjun Version: 1.0 Copyright: Copyright (c) 2026 Li Yanjun Description: This program recursively counts how many times m divides n. */

#include <stdio.h>

/* Count one factor each time m divides the current value, then repeat on the quotient. */
int countFactors(int n, int m)
{
    if (n % m != 0)
    {
        return 0;
    }

    return 1 + countFactors(n / m, m);
}

int main(void)
{
    int n;
    int m;
    int count;

    /* Read n first and m second, as required. */
    printf("Please input n and m: ");
    scanf("%d%d", &n, &m);

    /* Reject n before checking m, because both must be greater than 1. */
    if (n <= 1)
    {
        printf("Warning! value of n is invalid!\n");
        return 0;
    }

    if (m <= 1)
    {
        printf("Warning! value of m is invalid!\n");
        return 0;
    }

    /* Pass the two values to the recursive counter and print its result. */
    count = countFactors(n, m);
    printf("There are %d %d(s) in %d.\n", count, m, n);

    return 0;
}
