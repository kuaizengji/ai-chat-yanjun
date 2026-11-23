/* Name: L4T2v530013020.c Author: Li Yanjun Version: 1.0 Copyright: Copyright (c) 2026 Li Yanjun Description: This program reads two characters and prints the difference of their ASCII codes. */

#include <stdio.h>

/* Return the signed ASCII difference of the first character minus the second. */
int differenceASCIICode(char first, char second)
{
    return first - second;
}

int main(void)
{
    char first;
    char second;
    int difference;

    /* Read two characters and skip spaces between them. */
    printf("Please enter two characters: ");
    scanf(" %c %c", &first, &second);

    /* Compute the signed difference with the required function. */
    difference = differenceASCIICode(first, second);

    /* Print both characters and the returned difference. */
    printf("The difference between character '%c' and '%c' is %d.\n",
           first, second, difference);

    return 0;
}
