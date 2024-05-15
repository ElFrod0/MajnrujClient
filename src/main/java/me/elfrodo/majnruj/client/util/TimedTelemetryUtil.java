package me.elfrodo.majnruj.client.util;

import java.util.Timer;
import java.util.TimerTask;

import me.elfrodo.majnruj.client.MajnrujClient;
import me.elfrodo.majnruj.client.network.MajnrujTelemetryPacket;

public class TimedTelemetryUtil {
    MajnrujClient client = MajnrujClient.instance();
    private Timer timer;
    private static final CalcUtil fpsCalcUtil = new CalcUtil();
    private static final CalcUtil pingCalcUtil = new CalcUtil();

    public void initialize() {
        client.getLogger().info("Initializing sending periodic telemetry packets...");
        timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (MajnrujClient.isConnectedToMajnrujServer) {
                    MajnrujTelemetryPacket.sendPeriodic(fpsCalcUtil, pingCalcUtil);
                }
            }
        }, Constants.PERIODIC_TELEMETRY_STARTUP_DELAY * 2, Constants.PERIODIC_TELEMETRY_PERIOD);

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (MajnrujClient.isConnectedToMajnrujServer) {
                    fpsCalcUtil.addValue(client.getClientInfoGetters().getFps());
                    try {
                        pingCalcUtil.addValue(client.getClientInfoGetters().getLatency());
                    } catch (NullPointerException e) {
                        // Do nothing
                    }
                }
            }
        }, Constants.PERIODIC_TELEMETRY_STARTUP_DELAY, Constants.PERIODIC_TELEMETRY_SHORT_PERIOD);
    }

    public void cancel() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
    }
}
