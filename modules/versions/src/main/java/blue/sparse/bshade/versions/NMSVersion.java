package blue.sparse.bshade.versions;

import org.bukkit.Bukkit;

public enum NMSVersion {
    UNKNOWN,
    v1_7_R4,
    v1_8_R1,
    v1_8_R2,
    v1_8_R3,
    v1_9_R1,
    v1_9_R2,
    v1_10_R1,
    v1_11_R1,
    v1_12_R1,
    v1_13_R1,
    v1_13_R2,
    v1_14_R1;


    public static String getVersionString() {
        return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    }

    public static NMSVersion current() {
        return NMSVersion.valueOf(getVersionString());
    }
}
