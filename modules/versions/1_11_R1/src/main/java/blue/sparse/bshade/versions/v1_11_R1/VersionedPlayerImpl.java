package blue.sparse.bshade.versions.v1_11_R1;

import blue.sparse.bshade.versions.api.VersionedPlayer;
import net.minecraft.server.v1_11_R1.ChatComponentText;
import net.minecraft.server.v1_11_R1.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class VersionedPlayerImpl extends VersionedPlayer {

    public VersionedPlayerImpl(Player player) {
        super(player);
    }

    @Override
    public void sendTitle(String title, String subTitle, int fadeIn, int stay, int fadeOut) {
        player.sendTitle(title, subTitle, fadeIn, stay, fadeOut);
    }

    @Override
    public void sendActionBar(String message) {
        PacketPlayOutChat packet = new PacketPlayOutChat(new ChatComponentText(message), (byte)2);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }
}
