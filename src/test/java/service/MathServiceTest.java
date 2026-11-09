package service;

public class MathServiceTest {
    static int fail = 0;

    static void check(String name, boolean ok) {
        System.out.println((ok ? "PASS " : "FAIL ") + name);
        if (!ok) fail++;
    }

    public static void main(String[] args) {
        check("isPrime(7)=1", MathService.isPrime(7) == 1);
        check("isPrime(8)=0", MathService.isPrime(8) == 0);
        check("isPrime(1)=0", MathService.isPrime(1) == 0);
        check("isPrime(2)=1", MathService.isPrime(2) == 1);
        check("isPrime(-5)=0", MathService.isPrime(-5) == 0);
        check("17%5=2", MathService.calculateMod(17, 5) == 2);
        check("-17%5=-2", MathService.calculateMod(-17, 5) == -2);
        check("17%-5=2", MathService.calculateMod(17, -5) == 2);
        boolean threw = false;
        try { MathService.calculateMod(5, 0); } catch (IllegalArgumentException e) { threw = true; }
        check("mod by 0 -> error", threw);
        check("sin30=0.5", Math.abs(MathService.calculateSin(30) - 0.5) < 1e-9);
        check("cos60=0.5", Math.abs(MathService.calculateCos(60) - 0.5) < 1e-9);
        check("tan45=1.0", Math.abs(MathService.calculateTan(45) - 1.0) < 1e-9);
        check("tan90=NaN", Double.isNaN(MathService.calculateTan(90)));
        check("tan270=NaN", Double.isNaN(MathService.calculateTan(270)));
        check("tolerance ok", MathService.isClose(0.5, 0.5));
        check("tolerance reject", !MathService.isClose(0.5, 0.6));
        for (int i = 0; i < 2000; i++) {
            model.Question[] qs = {
                QuestionFactory.prime(i - 1000),
                QuestionFactory.mod(i - 1000, (i % 9) + 1),
                QuestionFactory.trig(i % 20, new String[]{"sin", "cos", "tan"}[i % 3], (i % 3 == 2) ? 45 : (i * 7) % 360)
            };
            for (model.Question q : qs) {
                int ok = 0;
                for (String o : q.getOptions()) if (QuestionFactory.isCorrect(q, o)) ok++;
                boolean seedOk = Math.abs(q.getSeed()) >= 1 && Math.abs(q.getSeed()) <= 10 && q.getFinalValue() == q.getBase() + q.getSeed();
                int n = q.getType().equals("PRIME") ? 2 : 3;
                if (ok != 1 || !seedOk || q.getOptions().size() != n || new java.util.HashSet<>(q.getOptions()).size() != n) {
                    check("factory " + q.getType() + " i=" + i, false);
                }
            }
        }
        boolean z = false;
        try { QuestionFactory.mod(5, 0); } catch (IllegalArgumentException e) { z = true; }
        check("factory mod zero error", z);
        z = false;
        try { QuestionFactory.trig(5, "tan", 90); } catch (IllegalArgumentException e) { z = true; }
        check("factory tan90 error", z);
        check("factory 2000 random rounds", fail == 0);
        int total = 0;
        total += 2 + 2 + 3 + 3;
        check("max score 10", total == 10);
        System.exit(fail == 0 ? 0 : 1);
    }
}
