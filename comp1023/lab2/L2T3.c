/*
 * Name:        L2T3.c
 * Author:      Li Yanjun
 * Version:     1.0
 * Copyright:   Copyright (c) 2026 Li Yanjun. All rights reserved.
 * Description: Read an integer, a real number and a character,
 *              then print the character's ASCII code and the two numbers.
 * Student ID:  V########
 */

#include <stdio.h>

int main(void)
{
    int i;
    float j;
    char ch;

    /* %f reads 20.5 into j; the space before %c skips the blank before D. */
    scanf("%d%f %c", &i, &j, &ch);

    printf("%d is the ASCII code of '%c'.\n", ch, ch);
    printf("The value of i is %d.\n", i);
    printf("The value of j is %.1f.\n", j);

    return 0;
}
