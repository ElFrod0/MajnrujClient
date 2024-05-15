package me.elfrodo.majnruj.client.util;

import net.minecraft.client.Minecraft;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.Math;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HardwareAbstractionLayer;

public class ComputerInformationGetters {
    private static String renderer;

    public String getCPU() {
        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hardware = systemInfo.getHardware();
        CentralProcessor processor = hardware.getProcessor();
        String cpuName = processor.getProcessorIdentifier().getName();
        int physicalCores = processor.getPhysicalProcessorCount();
        int logicalCores = processor.getLogicalProcessorCount();

        return (cpuName + "(Cores: " + physicalCores + ", Threads:" + logicalCores + ", Arch: " + getCPUArch() + ")");
    }

    public String getCPUArch() {
        OperatingSystemMXBean osMXBean = ManagementFactory.getOperatingSystemMXBean();
        return osMXBean.getArch();
    }

    public String getGPU() {
        return renderer;
    }

    public String getRAM() {
        com.sun.management.OperatingSystemMXBean osMXBean = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        long installedMemoryBytes = osMXBean.getTotalMemorySize();
        int installedMemoryMB = (int)Math.floor(installedMemoryBytes / 1024.0 / 1024.0);
        return (installedMemoryMB + " MB");
    }

    public int[] getDisplayResolution() {
        Minecraft minecraft = Minecraft.getInstance();
        int width = minecraft.getWindow().getWidth();
        int height = minecraft.getWindow().getHeight();
        return new int[] { width, height };
    }

    public String getOperatingSystem() {
        return System.getProperty("os.name");
    }

    public String getOperatingSystemBuild() {
        return System.getProperty("os.version");
    }
    
    // This is used in MixinWindow
    public void setGPU(String gpu) {
        renderer = gpu;
    }
}
