package blue.sparse.bshade.versions.v1_9_R1;

import blue.sparse.bshade.versions.api.VersionedPlayer;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_9_R1.ChatComponentText;
import net.minecraft.server.v1_9_R1.IChatBaseComponent;
import net.minecraft.server.v1_9_R1.PacketPlayOutTitle;
import net.minecraft.server.v1_9_R1.PlayerConnection;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_9_R1.util.CraftChatMessage;
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
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
    }
}
