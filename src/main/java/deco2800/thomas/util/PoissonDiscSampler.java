package deco2800.thomas.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PoissonDiscSampler {
    public static List<Vector2> GeneratePoints(float radius, Vector2 sampleRegionSize, int numSamplesBeforeRejection) {
        float cellSize = (float)(radius/Math.sqrt(2));
        Random random = new Random();

        float a = (float)Math.ceil((double)2.0f);

        int grid[][] = new int[(int)Math.ceil((double)(sampleRegionSize.getX()/cellSize))]
                [(int)Math.ceil((double)(sampleRegionSize.getY()/cellSize))];
        List<Vector2> points = new ArrayList<>();
        List<Vector2> spawnPoints = new ArrayList<>();

        spawnPoints.add(new Vector2(sampleRegionSize.getX() / 2, sampleRegionSize.getY() / 2));
        while (spawnPoints.size() > 0) {
            int spawnIndex = random.nextInt(spawnPoints.size());
            Vector2 spawnCentre = spawnPoints.get(spawnIndex);
            boolean candidateAccepted = false;

            for (int i = 0; i < numSamplesBeforeRejection; i++)
            {
                float angle = random.nextFloat() * (float)Math.PI * 2;
                Vector2 dir = new Vector2((float)Math.sin(angle), (float)Math.cos(angle));
                float randRadius = random.nextFloat() * radius + radius;
                Vector2 candidate = new Vector2(spawnCentre.getX() + dir.getX() * randRadius, spawnCentre.getY() + dir.getY() * randRadius);
                if (isValid(candidate, sampleRegionSize, cellSize, radius, points, grid)) {
                    points.add(candidate);
                    spawnPoints.add(candidate);
                    grid[(int)(candidate.getX()/cellSize)][(int)(candidate.getY()/cellSize)] = points.size();
                    candidateAccepted = true;
                    break;
                }
            }
            if (!candidateAccepted) {
                spawnPoints.remove(spawnIndex);
            }

        }

        return points;
    }

    static boolean isValid(Vector2 candidate, Vector2 sampleRegionSize, float cellSize, float radius, List<Vector2> points, int[][] grid) {
        if (candidate.getX() >=0 && candidate.getX() < sampleRegionSize.getX() && candidate.getY() >= 0 && candidate.getY() < sampleRegionSize.getY()) {
            int cellX = (int)(candidate.getX()/cellSize);
            int cellY = (int)(candidate.getY()/cellSize);
            int searchStartX = Math.max(0,cellX -2);
            int searchEndX = Math.min(cellX+2,grid.length-1);
            int searchStartY = Math.max(0,cellY -2);
            int searchEndY = Math.min(cellY+2,grid[0].length-1);

            for (int x = searchStartX; x <= searchEndX; x++) {
                for (int y = searchStartY; y <= searchEndY; y++) {
                    int pointIndex = grid[x][y] - 1;
                    if (pointIndex != -1) {
                        Vector2 pointIndexVector = points.get(pointIndex);
                        Vector2 candidateDiff = new Vector2(candidate.getX() - pointIndexVector.getX(), candidate.getY() - pointIndexVector.getY());
                        float sqrDst = candidateDiff.getX() * candidateDiff.getX() + candidateDiff.getY() * candidateDiff.getY();
                        if (sqrDst < radius*radius) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }
}
