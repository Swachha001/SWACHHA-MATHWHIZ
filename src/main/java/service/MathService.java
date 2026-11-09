package service;

public class MathService {

    public static final double TOLERANCE = 0.01;

    public static int isPrime(int number) {
        if (number < 2) return 0;
        for (int i = 2; (long) i * i <= number; i++) {
            if (number % i == 0) return 0;
        }
        return 1;
    }

    public static int calculateMod(int a, int b) {
        if (b == 0) throw new IllegalArgumentException("Divisor must not be zero");
        return a % b;
    }

    public static double calculateSin(double degree) {
        return Math.sin(Math.toRadians(degree));
    }

    public static double calculateCos(double degree) {
        return Math.cos(Math.toRadians(degree));
    }

    public static double calculateTan(double degree) {
        double rad = Math.toRadians(degree);
        if (Math.abs(Math.cos(rad)) < 1e-10) return Double.NaN;
        return Math.tan(rad);
    }

    public static double calculateTrig(String fn, double degree) {
        switch (fn) {
            case "sin": return calculateSin(degree);
            case "cos": return calculateCos(degree);
            case "tan": return calculateTan(degree);
            default: throw new IllegalArgumentException("Unknown function: " + fn);
        }
    }

    public static boolean isClose(double expected, double actual) {
        return !Double.isNaN(expected) && !Double.isNaN(actual)
                && Math.abs(expected - actual) <= TOLERANCE;
    }
}
