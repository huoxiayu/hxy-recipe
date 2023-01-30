package com.hxy.recipe.pid;

public class PidControl {

    private final double kp; // 比例控制
    private final double ki; // 积分控制
    private final double kd; // 微分控制

    private double lastErr = 0.0D;
    private double sumErr = 0.0D;

    public PidControl(double kp, double ki, double kd) {
        this.kp = kp;
        this.ki = ki;
        this.kd = kd;
    }

    public double getAdjustByHistoricalData(double target, double predict) {
        double thisErr = target - predict;
        double pAdjust = this.kp * thisErr;
        double iAdjust = this.ki * this.sumErr;
        double dAdjust = this.kd * (thisErr - lastErr);
        double adjust = pAdjust + iAdjust + dAdjust;
        this.lastErr = thisErr;
        this.sumErr += thisErr;
        return adjust;
    }

    public static void main(String[] args) {
        pidControlProcess();
    }

    private static void pidControlProcess() {
        double ki = 0.2D;
        double kp = 0.01D;
        double kd = 0.1D;
        System.out.printf("ki -> %s, kp -> %s, kd -> %s%n", ki, kp, kd);
        PidControl pidManager = new PidControl(ki, kp, kd);
        double target = 100D;
        double actual = 0D;
        double sumErr = 0D;
        for (int time = 1; time <= 100; time++) {
            double adjust = pidManager.getAdjustByHistoricalData(target, actual);
            double err = target - actual;
            String statistic = String.format(
                    "%s times -> target: %.2f, actual: %.2f, error: %.2f, adjust: %.2f",
                    time,
                    target,
                    actual,
                    err,
                    adjust
            );
            System.out.println(statistic);
            actual += adjust;
            sumErr += Math.abs(err);
        }

        System.out.printf("sumErr -> %s", sumErr);
    }

}
