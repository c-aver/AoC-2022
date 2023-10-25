#include <stdio.h>
#include <stdlib.h>

char crt[40][6];

void updateResultAndCRT(int *result, int cycle, int xReg) {
    int x = (cycle-1)%40, y = (cycle-1)/40;
    if (abs(x - xReg) <= 1) {               // pixel is currently being drawn
        crt[x][y] = '#';
    } else {
        crt[x][y] = '.';
    }

    if ((cycle - 20) % 40 != 0) return;     // no need to update
    int sigStr = cycle*xReg;
    printf("Adding %d*%d = %d to %d, now %d\n", cycle, xReg, sigStr, *result, *result + sigStr);
    *result += sigStr;
}

int main(int argc, char *argv[]) {
    size_t n = 16;
    char buf[n];
    char *program = argv[0];
    if (argc < 2) {
        fprintf(stderr, "USAGE: %s INPUT_FILE\n", program);
        exit(1);
    }
    char *filePath = argv[1];
    FILE *f = fopen(filePath, "r");
    int cycle = 0;
    int x = 1;
    int result = 0;

    while (fgets(buf, sizeof(buf), f)) {
        cycle += 1;
        updateResultAndCRT(&result, cycle, x);                // during cycle
        if (buf[0] == 'n') {
            // noop
        } else if (buf[0] == 'a') {
            int v = (int) strtol(buf + 5, NULL, 10);
            cycle += 1;
            updateResultAndCRT(&result, cycle, x);            // during second cycle of addx
            x += v;
        } else {
            fprintf(stderr, "Unknown command: %s\n", buf);
            exit(1);
        }
    }

    printf("Part 1: %d\n", result);

    printf("CRT image: \n");
    for (size_t i = 0; i < 6; ++i) {
        for (size_t j = 0; j < 40; ++j) {
            printf("%c", crt[j][i]);
        }
        printf("\n");
    }
    
    return 0;
}