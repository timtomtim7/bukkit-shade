package blue.sparse.bshade.versions.v1_13_R2;

import blue.sparse.bshade.versions.api.VersionedHologram;
import blue.sparse.bshade.versions.holograms.Hologram;
import net.minecraft.server.v1_13_R2.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class VersionedHologramImpl extends VersionedHologram {

    private List<EntityArmorStand> stands = new ArrayList<>();
    private List<Player> awarePlayers = new ArrayList<>();

    public VersionedHologramImpl(Hologram hologram) {
        super(hologram);
    }

    @Override
    public void delete() {
        sendPacketNearby(new PacketPlayOutEntityDestroy(stands.stream().mapToInt(Entity::getId).toArray()));
    }

    @Override
    public void spawnForPlayer(Player player) {
        for (EntityArmorStand stand : stands) {
            sendPacket(player, new PacketPlayOutSpawnEntityLiving(stand));
        }
    }

    @Override
    public void destroyForPlayer(Player player) {
        for (EntityArmorStand stand : stands) {
            sendPacket(player, new PacketPlayOutEntityDestroy(stand.getId()));
        }
    }

    @Override
    public void update() {
        double yOffset = hologram.getHieght() - hologram.getLineSpacing();

        List<String> lines = hologram.getLineContent();

        if (!hologram.isVisible()) {
            awarePlayers.forEach(this::destroyForPlayer);
            awarePlayers.clear();
            return;
        }

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            ChatComponentText lineComponent = new ChatComponentText(line);
            Location location = hologram.getLocation();

            EntityArmorStand stand;

            if (i < stands.size()) {
                stand = stands.get(i);
                stand.setCustomName(lineComponent);
            } else {
                World world = ((CraftWorld) location.getWorld()).getHandle();
                stand = new EntityArmorStand(world, location.getX(), location.getY(), location.getZ());
                stands.add(stand);
                stand.setCustomName(lineComponent);
                stand.setInvisible(true);
                stand.setCustomNameVisible(true);
                stand.setNoGravity(true);
                sendPacketNearby(new PacketPlayOutSpawnEntityLiving(stand));
            }

            stand.setLocation(
                    location.getX(),
                    location.getY() + yOffset,
                    location.getZ(),
                    location.getYaw(),
                    location.getPitch()
            );


            sendPacketNearby(new PacketPlayOutEntityTeleport(stand));
            sendPacketNearby(new PacketPlayOutEntityMetadata(stand.getId(), stand.getDataWatcher(), true));
            yOffset -= hologram.getLineSpacing();
        }

        hologram.handleNearbyPlayers();
    }

    private void sendPacket(Player player, Packet<?> packet) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    private void sendPacketNearby(Packet<?> packet) {
        Location location = hologram.getLocation();
        location.getWorld().getNearbyEntities(location, 256.0, 256.0, 256.0).forEach(entity -> {
            if (entity instanceof Player) {
                sendPacket((Player) entity, packet);
            }
        });
    }
}