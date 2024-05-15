package me.elfrodo.majnruj.client.util;

public class CalcUtil {
    private int valuesSum = 0;
    private int valueCount = 0;
    private int lowestValue = 0;
    private int highestValue = 0;

    public void addValue(int value) {
        valuesSum += value;
        valueCount++;
        if (value < lowestValue) {
            lowestValue = value;
        }
        if (value > highestValue) {
            highestValue = value;
        }
    }

    public float getAverage() {
        // Don't divide by zero :)
        if (valueCount == 0) {
            return 0;
        }
        return (float) valuesSum / valueCount;
    }

    public int getLowestValue() {
        return lowestValue;
    }

    public int getHighestValue() {
        return highestValue;
    }
}
