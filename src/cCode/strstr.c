#include <stdio.h> // Used for printing in main example

char *myStrstr(char *str, char *target);

/**
 * Returns pointer into str where target exists entirely, or NULL if target does not exist
 * entirely in str.
 */
char *myStrstr(char *str, char *target) {
    if (!*target) {
        return str;
    }

    /* Note that we only need to iterate over the longer word while the target fits in the
     * remaining space of str. This = n - m + 1 iterations, where n is length of str,
     * and m is the target word length. We will use spaceCounter to keep track of this. */

    char *currCharInStr = str;
    char *targetPtr = target;
    char *spaceCounter = str;
    while (*spaceCounter && *++targetPtr) {
        /* Now spaceCounter has been advanced m times. Once we advance it n + 1 times, it will
         * point to \0 at the end of str. */
        spaceCounter++;
    }

    while (*spaceCounter) {
        char *p1 = currCharInStr;
        targetPtr = target;

        // Keep advancing until characters are no longer equal or we reach end of either string
        while (*p1 && *targetPtr && *targetPtr == *p1) {
            p1++;
            targetPtr++;
        }

        // If we reached the end of the target, then it exists entirely in str
        if (!*targetPtr) {
            return currCharInStr;
        }

        spaceCounter++;
        currCharInStr++;
    }
    return NULL;
}

/* Some examples */
int main(int argc, char **argv) {
    printf("%s\n", myStrstr("bathroom", "bath"));
    printf("%s\n", myStrstr("athroom", "bath"));
    printf("%s\n", myStrstr("a", "bath"));
    printf("%s\n", myStrstr("bathroom", ""));
    printf("%s\n", myStrstr("", ""));
}
