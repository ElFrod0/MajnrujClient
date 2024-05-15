package me.elfrodo.majnruj.client.network;

import com.google.common.io.ByteArrayDataOutput;

import me.elfrodo.majnruj.client.MajnrujClient;
import me.elfrodo.majnruj.client.util.CalcUtil;
import me.elfrodo.majnruj.client.util.ClientInformationGetters;
import me.elfrodo.majnruj.client.util.ComputerInformationGetters;
import me.elfrodo.majnruj.client.util.Constants;

public class MajnrujTelemetryPacket {
    private static MajnrujClient instance = MajnrujClient.instance();
    public static int fpsLimit = -1;
    public static int lowestFps = -1;
    public static int averageFps = -1;
    public static int highestFps = -1;
    public static int ramUsage = -1;
    public static int highestPing = -1;
    public static int averagePing = -1;
    public static int lowestPing = -1;
    
    private static ClientInformationGetters clientInfo = instance.getClientInfoGetters();
    private static ComputerInformationGetters pcInfo = instance.getComputerInfoGetters();
    
    public static void send() {
        ByteArrayDataOutput out = Packet.out();

        // Computer Information
        out.writeUTF(pcInfo.getCPU());
        out.writeUTF(pcInfo.getGPU());
        out.writeUTF(pcInfo.getRAM());
        out.writeInt(clientInfo.getTotalRAM()); // Not a Computer information, but don't want to mess up the order of data.
        int[] displayResolution = pcInfo.getDisplayResolution();
        out.writeInt(displayResolution[0]);
        out.writeInt(displayResolution[1]);
        out.writeUTF(pcInfo.getOperatingSystem());
        out.writeUTF(pcInfo.getOperatingSystemBuild());

        // Client Information
        out.writeUTF(clientInfo.getJava());
        out.writeInt(clientInfo.getRenderDistance());
        out.writeInt(clientInfo.getGUIScale());
        out.writeUTF(clientInfo.getGraphicSettings());
        out.writeUTF(clientInfo.getLanguage());

        instance.getLogger().info("Sending telemetry packet...");
        Packet.send(Constants.MAJNRUJ_TELEMETRY, out);
    }

    public static void sendPeriodic(CalcUtil fpsCalcUtil, CalcUtil pingCalcUtil) {
        ByteArrayDataOutput out = Packet.out();

        out.writeInt(clientInfo.getFpsLimit());
        out.writeInt(fpsCalcUtil.getLowestValue());
        out.writeFloat(fpsCalcUtil.getAverage());
        out.writeInt(fpsCalcUtil.getHighestValue());
        out.writeInt(clientInfo.getUsedRAM());
        out.writeInt(clientInfo.getTotalRAM());
        out.writeInt(pingCalcUtil.getLowestValue());
        out.writeFloat(pingCalcUtil.getAverage());
        out.writeInt(pingCalcUtil.getHighestValue());

        instance.getLogger().info("Sending periodic telemetry packet...");
        Packet.send(Constants.MAJNRUJ_PERIODIC_TELEMETRY, out);
    }
}
