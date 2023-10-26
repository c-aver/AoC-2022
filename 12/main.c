#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <assert.h>
#include <stdbool.h>
#include <limits.h>

const bool debug = true;

size_t count_lines(FILE* file)
{

    size_t lines = 0;
    while(!feof(file))
    {
        if(fgetc(file) == '\n')
        {
            lines++;
        }
    }
    rewind(file);
    return lines;
}

void wave(size_t mapHeight, size_t mapWidth, char *map, int *dists)
{
    for (size_t i = 0; i < mapHeight; ++i)
    {
        for (size_t j = 0; j < mapWidth; ++j)
        {
            if (i == 4 && j == 1)
            {
                printf("here\n");
            }
            // if (dists[i*mapWidth + j] != -1) continue;
            char curHeight = map[i*mapWidth + j];
            int curDist = dists[i*mapWidth + j];
            if (i > 0)
            {
                char neiHeight = map[(i-1)*mapWidth+j];
                int neiDist = dists[(i-1)*mapWidth+j];
                if (curDist > neiDist + 1 && curHeight <= neiHeight + 1)
                {
                    dists[i*mapWidth + j] = dists[(i-1)*mapWidth+j] + 1;
                    if (debug) printf("Set dist to %d\n", dists[i*mapWidth + j]);
                }
            }
            if (i < mapHeight - 1)
            {
                char neiHeight = map[(i+1)*mapWidth+j];
                int neiDist = dists[(i+1)*mapWidth+j];
                if (curDist > neiDist + 1 && curHeight <= neiHeight + 1)
                {
                    dists[i*mapWidth + j] = dists[(i+1)*mapWidth+j] + 1;
                    if (debug) printf("Set dist to %d\n", dists[i*mapWidth + j]);
                }
            }
            if (j > 0)
            {
                char neiHeight = map[i*mapWidth+(j-1)];
                int neiDist = dists[i*mapWidth+(j-1)];
                if (curDist > neiDist + 1 && curHeight <= neiHeight + 1)
                {
                    dists[i*mapWidth + j] = dists[i*mapWidth+(j-1)] + 1;
                    if (debug) printf("Set dist to %d\n", dists[i*mapWidth + j]);
                }
            }
            if (j < mapWidth - 1)
            {
                char neiHeight = map[i*mapWidth+(j+1)];
                int neiDist = dists[i*mapWidth+(j+1)];
                if (curDist > neiDist + 1 && curHeight <= neiHeight + 1)
                {
                    dists[i*mapWidth + j] = dists[i*mapWidth+(j+1)] + 1;
                    if (debug) printf("Set dist to %d\n", dists[i*mapWidth + j]);
                }
            }
        }
    }
}

void debugPrint(size_t mapHeight, size_t mapWidth, int *dists)
{
    if (!debug) return;
    printf("------------------------\n");
    for (size_t i = 0; i < mapHeight; ++i)
    {
        for (size_t j = 0; j < mapWidth; ++j)
        {
            int dist = dists[i*mapWidth + j];
            if (dist == INT_MAX - 1) printf("%3d", -1);
            else printf("%3d", dist);
        }
        printf("\n");
    }
}

int main(int argc, char *argv[])
{
    size_t n = 1024;
    char buf[n];
    char *program = argv[0];
    if (argc < 2)
    {
        fprintf(stderr, "USAGE: %s INPUT_FILE\n", program);
        exit(1);
    }
    char *filePath = argv[1];
    FILE *f = fopen(filePath, "r");
    size_t mapHeight = count_lines(f) - 1;
    fgets(buf, n, f);
    size_t mapWidth = strlen(buf) - 1;
    rewind(f);
    char *map = malloc(sizeof(char)*mapHeight*mapWidth);

    int *dists = malloc(sizeof(int)*mapHeight*mapWidth);
    for (size_t i = 0; i < mapHeight*mapWidth; ++i)
    {
        dists[i] = INT_MAX - 1;
    }

    size_t endLoc;

    size_t bottomCount = 0;

    for (size_t i = 0; i < mapHeight; ++i)
    {
        for (size_t j = 0; j < mapWidth; ++j)
        {
            map[i*mapWidth + j] = fgetc(f);
            if (map[i*mapWidth + j] == 'S' || map[i*mapWidth + j] == 'a')
            {
                dists[i*mapWidth + j] = 0;
                map[i*mapWidth + j] = 'a';
                bottomCount += 1;
            }
            if (map[i*mapWidth + j] == 'E') {
                endLoc = i*mapWidth + j;
                map[i*mapWidth + j] = 'z';
            }
        }
        fgetc(f);
    }

    debugPrint(mapHeight, mapWidth, dists);

    while (dists[endLoc] == INT_MAX - 1)
    {
        wave(mapHeight, mapWidth, map, dists);
        debugPrint(mapHeight, mapWidth, dists);
    }
    for (size_t i = 0; i < bottomCount; ++i)
    {
        wave(mapHeight, mapWidth, map, dists);
        debugPrint(mapHeight, mapWidth, dists);
    }
    printf("End dist: %d\n", dists[endLoc]);

    free(map);
    free(dists);
    
    return 0;
}