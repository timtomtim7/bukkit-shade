package blue.sparse.bshade.versions.v1_8_R2;

import blue.sparse.bshade.versions.api.VersionedPlayer;
import net.minecraft.server.v1_8_R2.*;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class VersionedPlayerImpl extends VersionedPlayer {

    public VersionedPlayerImpl(Player player) {
        super(player);
    }

    @Override
    public void sendTitle(String title, String subTitle, int fadeIn, int stay, int fadeOut) {
        IChatBaseComponent chatTitle = new ChatComponentText(title);
        IChatBaseComponent chatSubTitle = new ChatComponentText(subTitle);

        CraftPlayer craftPlayer = (CraftPlayer) player;
        PlayerConnection connection = craftPlayer.getHandle().playerConnection;
        connection.sendPacket(new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, chatTitle));
        connection.sendPacket(new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, chatSubTitle));
        connection.sendPacket(new PacketPlayOutTitle(fadeIn, stay, fadeOut));
    }

    @Override
    public void sendActionBar(String message) {
        PacketPlayOutChat packet = new PacketPlayOutChat(new ChatComponentText(message), (byte)2);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }
}
