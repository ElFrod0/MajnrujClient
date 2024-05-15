package me.elfrodo.majnruj.client.util;

import java.util.List;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ChecksumUtil {
    public String getChecksumFromList(List<String> list) {
        try {
            // Convert mod list into SHA-256 checksum
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String combinedInfo = String.join(",", list);
            digest.update(combinedInfo.getBytes());
            byte[] hash = digest.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            String checksum = hexString.toString();
            //System.out.println("[MAJNRUJ Client] ModList SHA256 Checksum: " + checksum);
            return checksum;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
