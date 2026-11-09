package model;

import java.io.Serializable;
import java.util.List;

public class Question implements Serializable {
    private final String type;
    private final int marks;
    private int base;
    private int seed;
    private int finalValue;
    private int divisor;
    private String fn;
    private int angle;
    private List<String> options;

    public Question(String type, int marks) {
        this.type = type;
        this.marks = marks;
    }

    public String getType() { return type; }
    public int getMarks() { return marks; }
    public int getBase() { return base; }
    public void setBase(int base) { this.base = base; }
    public int getSeed() { return seed; }
    public void setSeed(int seed) { this.seed = seed; }
    public String getSeedText() { return seed > 0 ? "+" + seed : String.valueOf(seed); }
    public int getFinalValue() { return finalValue; }
    public void setFinalValue(int finalValue) { this.finalValue = finalValue; }
    public int getDivisor() { return divisor; }
    public void setDivisor(int divisor) { this.divisor = divisor; }
    public String getFn() { return fn; }
    public void setFn(String fn) { this.fn = fn; }
    public int getAngle() { return angle; }
    public void setAngle(int angle) { this.angle = angle; }
    public List<String> getOptions() { return options; }
    public void setOptions(List<String> options) { this.options = options; }
}
