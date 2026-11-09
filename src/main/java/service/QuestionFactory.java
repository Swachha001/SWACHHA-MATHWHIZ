package service;

import model.Question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class QuestionFactory {

    private static final Random R = new Random();

    private static int seed() {
        int s = R.nextInt(10) + 1;
        return R.nextBoolean() ? s : -s;
    }

    private static String fmt(double v) {
        String s = String.format(Locale.US, "%.2f", v);
        return s.equals("-0.00") ? "0.00" : s;
    }

    public static Question prime(int n) {
        Question q = new Question("PRIME", 2);
        q.setBase(n);
        q.setSeed(seed());
        q.setFinalValue(n + q.getSeed());
        q.setOptions(List.of("1", "0"));
        return q;
    }

    public static Question mod(int dividend, int divisor) {
        if (divisor == 0) throw new IllegalArgumentException("Divisor must not be zero.");
        Question q = new Question("MOD", 2);
        q.setBase(dividend);
        q.setDivisor(divisor);
        q.setSeed(seed());
        q.setFinalValue(dividend + q.getSeed());
        int correct = MathService.calculateMod(q.getFinalValue(), divisor);
        List<Integer> cand = new ArrayList<>(List.of(correct + 1, correct - 1, correct + 2, correct - 2));
        Collections.shuffle(cand, R);
        List<String> opts = new ArrayList<>();
        opts.add(String.valueOf(correct));
        opts.add(String.valueOf(cand.get(0)));
        opts.add(String.valueOf(cand.get(1)));
        Collections.shuffle(opts, R);
        q.setOptions(opts);
        return q;
    }

    public static Question trig(int coef, String fn, int angle) {
        if (!fn.equals("sin") && !fn.equals("cos") && !fn.equals("tan"))
            throw new IllegalArgumentException("Function must be sin, cos or tan.");
        if (Double.isNaN(MathService.calculateTrig(fn, angle)))
            throw new IllegalArgumentException("tan(" + angle + ") is undefined.");
        Question q = new Question("TRIG", 3);
        q.setBase(coef);
        q.setFn(fn);
        q.setAngle(angle);
        q.setSeed(seed());
        q.setFinalValue(coef + q.getSeed());
        double v = q.getFinalValue() * MathService.calculateTrig(fn, angle);
        String right = fmt(v);
        List<Double> cand = new ArrayList<>(List.of(v + 1, v - 1, -v, v + 2, v - 2, v * 2));
        Collections.shuffle(cand, R);
        List<String> opts = new ArrayList<>();
        opts.add(right);
        for (double c : cand) {
            String s = fmt(c);
            if (!opts.contains(s)) opts.add(s);
            if (opts.size() == 3) break;
        }
        Collections.shuffle(opts, R);
        q.setOptions(opts);
        return q;
    }

    public static String expected(Question q) {
        switch (q.getType()) {
            case "PRIME":
                return String.valueOf(MathService.isPrime(q.getFinalValue()));
            case "MOD":
                return String.valueOf(MathService.calculateMod(q.getFinalValue(), q.getDivisor()));
            default:
                return fmt(q.getFinalValue() * MathService.calculateTrig(q.getFn(), q.getAngle()));
        }
    }

    public static boolean isCorrect(Question q, String answer) {
        if (answer == null) return false;
        try {
            switch (q.getType()) {
                case "PRIME":
                    return Integer.parseInt(answer.trim()) == MathService.isPrime(q.getFinalValue());
                case "MOD":
                    return Integer.parseInt(answer.trim())
                            == MathService.calculateMod(q.getFinalValue(), q.getDivisor());
                default:
                    double exp = q.getFinalValue() * MathService.calculateTrig(q.getFn(), q.getAngle());
                    return MathService.isClose(exp, Double.parseDouble(answer.trim()));
            }
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
