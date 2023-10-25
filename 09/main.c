#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int tailHist[2][1048576];
size_t histSize = 0;

void move(int *x, int *y, char dir) {
    switch (dir) {
        case 'U':
            *y -= 1;
            break;
        case 'D':
            *y += 1;
            break;
        case 'L':
            *x -= 1;
            break;
        case 'R':
            *x += 1;
            break;
    }
}

int sign(int num) {
    if (num > 0) return 1;
    if (num < 0) return -1;
    return 0;
}

void updateTail(int h_x, int h_y, int *t_x, int *t_y) {
    int d_x = *t_x - h_x, d_y = *t_y - h_y;
    if (abs(d_y) > 1) {
        *t_y -= sign(d_y);
        if (abs(d_x) > 0) {
            *t_x -= sign(d_x);
            return;
        }
    }
    if (abs(d_x) > 1) {
        *t_x -= sign(d_x);
        if (abs(d_y) > 0) {
            *t_y -= sign(d_y);
        }
    }
}

void addToHistory(int x, int y) {
    for (size_t i = 0; i < histSize; ++i) {
        if (x == tailHist[0][i] && y == tailHist[1][i]) return;   // position already in history
    }

    tailHist[0][histSize] = x;
    tailHist[1][histSize] = y;
    histSize += 1;
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

    size_t knots = 10;
    int xs[knots], ys[knots];
    memset(xs, 0, sizeof(*xs)*knots);
    memset(ys, 0, sizeof(*ys)*knots);
    
    addToHistory(xs[knots-1], ys[knots-1]);

    while (fgets(buf, sizeof(buf), f)) {
        int num = (int) strtol(buf + 2, NULL, 10);
        char dir = buf[0];

        for (int i = 0; i < num; ++i) {
            move(&xs[0], &ys[0], dir);
            for (size_t i = 1; i < knots; ++i) {
                updateTail(xs[i-1], ys[i-1], &xs[i], &ys[i]);
            }
            addToHistory(xs[knots-1], ys[knots-1]);
        }
    }

    printf("histSize: %zu\n", histSize);
    
    return 0;
}