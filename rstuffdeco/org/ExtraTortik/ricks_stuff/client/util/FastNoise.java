/*
 * Decompiled with CFR 0.152.
 */
package org.ExtraTortik.ricks_stuff.client.util;

public final class FastNoise {
    private static final float F3 = 0.33333334f;
    private static final float G3 = 0.16666667f;
    private static final float[] GRAD_X = new float[]{1.0f, -1.0f, 1.0f, -1.0f, 1.0f, -1.0f, 1.0f, -1.0f, 0.0f, 0.0f, 0.0f, 0.0f};
    private static final float[] GRAD_Y = new float[]{1.0f, 1.0f, -1.0f, -1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, -1.0f, 1.0f, -1.0f};
    private static final float[] GRAD_Z = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, -1.0f, -1.0f};
    private static final short[] P = new short[]{151, 160, 137, 91, 90, 15, 131, 13, 201, 95, 96, 53, 194, 233, 7, 225, 140, 36, 103, 30, 69, 142, 8, 99, 37, 240, 21, 10, 23, 190, 6, 148, 247, 120, 234, 75, 0, 26, 197, 62, 94, 252, 219, 203, 117, 35, 11, 32, 57, 177, 33, 88, 237, 149, 56, 87, 174, 20, 125, 136, 171, 168, 68, 175, 74, 165, 71, 134, 139, 48, 27, 166, 77, 146, 158, 231, 83, 111, 229, 122, 60, 211, 133, 230, 220, 105, 92, 41, 55, 46, 245, 40, 244, 102, 143, 54, 65, 25, 63, 161, 1, 216, 80, 73, 209, 76, 132, 187, 208, 89, 18, 169, 200, 196, 135, 130, 116, 188, 159, 86, 164, 100, 109, 198, 173, 186, 3, 64, 52, 217, 226, 250, 124, 123, 5, 202, 38, 147, 118, 126, 255, 82, 85, 212, 207, 206, 59, 227, 47, 16, 58, 17, 182, 189, 28, 42, 223, 183, 170, 213, 119, 248, 152, 2, 44, 154, 163, 70, 221, 153, 101, 155, 167, 43, 172, 9, 129, 22, 39, 253, 19, 98, 108, 110, 79, 113, 224, 232, 178, 185, 112, 104, 218, 246, 97, 228, 251, 34, 242, 193, 238, 210, 144, 12, 191, 179, 162, 241, 81, 51, 145, 235, 249, 14, 239, 107, 49, 192, 214, 31, 181, 199, 106, 157, 184, 84, 204, 176, 115, 121, 50, 45, 127, 4, 150, 254, 138, 236, 205, 93, 222, 114, 67, 29, 24, 72, 243, 141, 128, 195, 78, 66, 215, 61, 156, 180};
    private static final short[] PERM = new short[512];
    private static final short[] PERM_MOD12 = new short[512];

    private FastNoise() {
    }

    public static int fastFloor(float x) {
        int xi = (int)x;
        return x < (float)xi ? xi - 1 : xi;
    }

    private static float grad(int gi, float x, float y, float z) {
        return GRAD_X[gi] * x + GRAD_Y[gi] * y + GRAD_Z[gi] * z;
    }

    public static float simplex3D(float x, float y, float z) {
        return FastNoise.simplex3D(x, y, z, 0);
    }

    public static float simplex3D(float x, float y, float z, int seed) {
        float n3;
        float n2;
        float n1;
        float n0;
        int k2;
        int j2;
        int i2;
        int k1;
        int j1;
        int i1;
        float s = (x + y + z) * 0.33333334f;
        int i = FastNoise.fastFloor(x + s);
        int j = FastNoise.fastFloor(y + s);
        int k = FastNoise.fastFloor(z + s);
        float t = (float)(i + j + k) * 0.16666667f;
        float X0 = (float)i - t;
        float Y0 = (float)j - t;
        float Z0 = (float)k - t;
        float x0 = x - X0;
        float y0 = y - Y0;
        float z0 = z - Z0;
        if (x0 >= y0) {
            if (y0 >= z0) {
                i1 = 1;
                j1 = 0;
                k1 = 0;
                i2 = 1;
                j2 = 1;
                k2 = 0;
            } else if (x0 >= z0) {
                i1 = 1;
                j1 = 0;
                k1 = 0;
                i2 = 1;
                j2 = 0;
                k2 = 1;
            } else {
                i1 = 0;
                j1 = 0;
                k1 = 1;
                i2 = 1;
                j2 = 0;
                k2 = 1;
            }
        } else if (y0 < z0) {
            i1 = 0;
            j1 = 0;
            k1 = 1;
            i2 = 0;
            j2 = 1;
            k2 = 1;
        } else if (x0 < z0) {
            i1 = 0;
            j1 = 1;
            k1 = 0;
            i2 = 0;
            j2 = 1;
            k2 = 1;
        } else {
            i1 = 0;
            j1 = 1;
            k1 = 0;
            i2 = 1;
            j2 = 1;
            k2 = 0;
        }
        float x1 = x0 - (float)i1 + 0.16666667f;
        float y1 = y0 - (float)j1 + 0.16666667f;
        float z1 = z0 - (float)k1 + 0.16666667f;
        float x2 = x0 - (float)i2 + 0.33333334f;
        float y2 = y0 - (float)j2 + 0.33333334f;
        float z2 = z0 - (float)k2 + 0.33333334f;
        float x3 = x0 - 1.0f + 0.5f;
        float y3 = y0 - 1.0f + 0.5f;
        float z3 = z0 - 1.0f + 0.5f;
        int ii = i + seed * 1619 & 0xFF;
        int jj = j + seed * 31337 & 0xFF;
        int kk = k + seed * 6971 & 0xFF;
        short gi0 = PERM_MOD12[ii + PERM[jj + PERM[kk]]];
        short gi1 = PERM_MOD12[ii + i1 + PERM[jj + j1 + PERM[kk + k1]]];
        short gi2 = PERM_MOD12[ii + i2 + PERM[jj + j2 + PERM[kk + k2]]];
        short gi3 = PERM_MOD12[ii + 1 + PERM[jj + 1 + PERM[kk + 1]]];
        float t0 = 0.6f - x0 * x0 - y0 * y0 - z0 * z0;
        if (t0 < 0.0f) {
            n0 = 0.0f;
        } else {
            t0 *= t0;
            n0 = t0 * t0 * FastNoise.grad(gi0, x0, y0, z0);
        }
        float t1 = 0.6f - x1 * x1 - y1 * y1 - z1 * z1;
        if (t1 < 0.0f) {
            n1 = 0.0f;
        } else {
            t1 *= t1;
            n1 = t1 * t1 * FastNoise.grad(gi1, x1, y1, z1);
        }
        float t2 = 0.6f - x2 * x2 - y2 * y2 - z2 * z2;
        if (t2 < 0.0f) {
            n2 = 0.0f;
        } else {
            t2 *= t2;
            n2 = t2 * t2 * FastNoise.grad(gi2, x2, y2, z2);
        }
        float t3 = 0.6f - x3 * x3 - y3 * y3 - z3 * z3;
        if (t3 < 0.0f) {
            n3 = 0.0f;
        } else {
            t3 *= t3;
            n3 = t3 * t3 * FastNoise.grad(gi3, x3, y3, z3);
        }
        return 32.0f * (n0 + n1 + n2 + n3);
    }

    public static float fbm3D(float x, float y, float z, int octaves, float lacunarity, float gain) {
        return FastNoise.fbm3D(x, y, z, 0, octaves, lacunarity, gain);
    }

    public static float fbm3D(float x, float y, float z, int seed, int octaves, float lacunarity, float gain) {
        if (octaves <= 0) {
            return 0.0f;
        }
        float sum = 0.0f;
        float amp = 1.0f;
        float maxAmp = 0.0f;
        float fx = x;
        float fy = y;
        float fz = z;
        for (int i = 0; i < octaves; sum += FastNoise.simplex3D(fx, fy, fz, seed + i * 31337) * amp, maxAmp += amp, fx *= lacunarity, fy *= lacunarity, fz *= lacunarity, amp *= gain, ++i) {
        }
        return maxAmp > 0.0f ? sum / maxAmp : sum;
    }

    public static float ridged3D(float x, float y, float z, int octaves, float lacunarity, float gain) {
        return FastNoise.ridged3D(x, y, z, 0, octaves, lacunarity, gain);
    }

    public static float ridged3D(float x, float y, float z, int seed, int octaves, float lacunarity, float gain) {
        float signal;
        if (octaves <= 0) {
            return 0.0f;
        }
        float sum = 0.0f;
        float amp = 1.0f;
        float maxAmp = 0.0f;
        float fx = x;
        float fy = y;
        float fz = z;
        for (int i = 0; i < octaves; signal *= signal, sum += signal * amp, maxAmp += amp, fx *= lacunarity, fy *= lacunarity, fz *= lacunarity, amp *= gain, ++i) {
            float n = FastNoise.simplex3D(fx, fy, fz, seed + i * 31337);
            signal = 1.0f - Math.abs(n);
        }
        return maxAmp > 0.0f ? sum / maxAmp : sum;
    }

    public static void cellular3D(float x, float y, float z, float jitter, float[] outF1F2F2MinusF1) {
        FastNoise.cellular3D(x, y, z, 0, jitter, outF1F2F2MinusF1);
    }

    public static void cellular3D(float x, float y, float z, int seed, float jitter, float[] outF1F2F2MinusF1) {
        int xi = FastNoise.fastFloor(x);
        int yi = FastNoise.fastFloor(y);
        int zi = FastNoise.fastFloor(z);
        float f1 = Float.MAX_VALUE;
        float f2 = Float.MAX_VALUE;
        block0: for (int cx = -1; cx <= 1; ++cx) {
            int cellX = xi + cx;
            int cy = -1;
            while (true) {
                if (cy > 1) continue block0;
                int cellY = yi + cy;
                for (int cz = -1; cz <= 1; ++cz) {
                    float py;
                    float dy;
                    int hx;
                    float px;
                    float dx;
                    float dist;
                    int hy;
                    int cellZ = zi + cz;
                    int h = cellX * 501125321 + cellY * 1136930381 + cellZ * 1720413743 + seed * 6971;
                    h ^= h >>> 15;
                    h *= -2048144789;
                    h ^= h >>> 13;
                    h *= -1028477387;
                    int hz = hy * 31337 + 1013904223;
                    float pz = (float)cellZ + (float)(hz & Integer.MAX_VALUE) * 4.656613E-10f * jitter;
                    float dz = pz - z;
                    if ((dist = (float)Math.sqrt((dx = (px = (float)cellX + (float)((hx = (h ^= h >>> 16)) & Integer.MAX_VALUE) * 4.656613E-10f * jitter) - x) * dx + (dy = (py = (float)cellY + (float)((hy = h * 31337 + 1013904223) & Integer.MAX_VALUE) * 4.656613E-10f * jitter) - y) * dy + dz * dz)) < f1) {
                        f2 = f1;
                        f1 = dist;
                        continue;
                    }
                    if (!(dist < f2)) continue;
                    f2 = dist;
                }
                ++cy;
            }
        }
        if (outF1F2F2MinusF1 == null) return;
        if (outF1F2F2MinusF1.length > 0) {
            outF1F2F2MinusF1[0] = f1;
        }
        if (outF1F2F2MinusF1.length > 1) {
            outF1F2F2MinusF1[1] = f2;
        }
        if (outF1F2F2MinusF1.length <= 2) return;
        outF1F2F2MinusF1[2] = f2 - f1;
    }

    public static float voronoiRidge3D(float x, float y, float z, float jitter) {
        return FastNoise.voronoiRidge3D(x, y, z, 0, jitter);
    }

    public static float voronoiRidge3D(float x, float y, float z, int seed, float jitter) {
        int xi = FastNoise.fastFloor(x);
        int yi = FastNoise.fastFloor(y);
        int zi = FastNoise.fastFloor(z);
        float f1 = Float.MAX_VALUE;
        float f2 = Float.MAX_VALUE;
        int cx = -1;
        block0: while (cx <= 1) {
            int cellX = xi + cx;
            int cy = -1;
            while (true) {
                int cellY;
                if (cy <= 1) {
                    cellY = yi + cy;
                } else {
                    ++cx;
                    continue block0;
                }
                for (int cz = -1; cz <= 1; ++cz) {
                    float py;
                    float dy;
                    int hx;
                    float px;
                    float dx;
                    float dist;
                    int hy;
                    int cellZ = zi + cz;
                    int h = cellX * 501125321 + cellY * 1136930381 + cellZ * 1720413743 + seed * 6971;
                    h ^= h >>> 15;
                    h *= -2048144789;
                    h ^= h >>> 13;
                    h *= -1028477387;
                    int hz = hy * 31337 + 1013904223;
                    float pz = (float)cellZ + (float)(hz & Integer.MAX_VALUE) * 4.656613E-10f * jitter;
                    float dz = pz - z;
                    if ((dist = (float)Math.sqrt((dx = (px = (float)cellX + (float)((hx = (h ^= h >>> 16)) & Integer.MAX_VALUE) * 4.656613E-10f * jitter) - x) * dx + (dy = (py = (float)cellY + (float)((hy = h * 31337 + 1013904223) & Integer.MAX_VALUE) * 4.656613E-10f * jitter) - y) * dy + dz * dz)) < f1) {
                        f2 = f1;
                        f1 = dist;
                        continue;
                    }
                    if (!(dist < f2)) continue;
                    f2 = dist;
                }
                ++cy;
            }
            break;
        }
        return f2 - f1;
    }

    public static void domainWarp3D(float x, float y, float z, float warpStrength, float[] outXYZ) {
        FastNoise.domainWarp3D(x, y, z, 0, warpStrength, outXYZ);
    }

    public static void domainWarp3D(float x, float y, float z, int seed, float warpStrength, float[] outXYZ) {
        if (outXYZ == null) {
            return;
        }
        float qx = FastNoise.simplex3D(x, y, z, seed + 1013904223);
        float qy = FastNoise.simplex3D(x + 13.5f, y + 47.2f, z + 91.8f, seed + 1619);
        float qz = FastNoise.simplex3D(x + 89.1f, y + 23.4f, z + 65.7f, seed + 31337);
        if (outXYZ.length > 0) {
            outXYZ[0] = x + qx * warpStrength;
        }
        if (outXYZ.length > 1) {
            outXYZ[1] = y + qy * warpStrength;
        }
        if (outXYZ.length <= 2) return;
        outXYZ[2] = z + qz * warpStrength;
    }

    static {
        int i = 0;
        while (i < 512) {
            FastNoise.PERM[i] = P[i & 0xFF];
            FastNoise.PERM_MOD12[i] = (short)(PERM[i] % 12);
            ++i;
        }
    }
}
