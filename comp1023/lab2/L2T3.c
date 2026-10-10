/*
 * File:        L2T3.c
 * Author:      Li Yanjun
 * Student ID:  V########
 * Version:     1.0
 * Date:        10 October 2026
 * Description: Read an integer, a real number and a character,
 *              then print the character's ASCII code and the two numbers.
 *
 * Fixes applied to the given program:
 *   - include stdio.h so printf and scanf are declared
 *   - give main the return type int
 *   - store the real number in j (float); the unused float f is removed
 *   - pass addresses to scanf, and skip the space before the character
 *   - print the ASCII code with %d, and print the values rather than their addresses
 *   - finish each output line and terminate the last printf
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
