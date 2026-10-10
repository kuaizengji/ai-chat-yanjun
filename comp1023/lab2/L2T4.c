/*
 * File:        L2T4.c
 * Author:      Li Yanjun
 * Student ID:  V########
 * Version:     1.0
 * Date:        10 October 2026
 * Description: Read two integers m and n (n is never 0) and print
 *              their sum, difference, product, quotient, remainder
 *              and real division.
 */

#include <stdio.h>

int main(void)
{
    int m;
    int n;

    scanf("%d %d", &m, &n);

    /* Integer / and % use truncating division. Real division casts m. */
    printf("%d + %d = %d\n", m, n, m + n);
    printf("%d - %d = %d\n", m, n, m - n);
    printf("%d * %d = %d\n", m, n, m * n);
    printf("%d / %d = %d\n", m, n, m / n);
    printf("%d %% %d = %d\n", m, n, m % n);
    printf("float(%d) / %d = %f\n", m, n, (float)m / n);

    return 0;
}
