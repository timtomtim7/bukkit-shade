package blue.sparse.bshade.versions.v1_9_R1;

import blue.sparse.bshade.versions.api.VersionedHologram;
import blue.sparse.bshade.versions.holograms.Hologram;
import net.minecraft.server.v1_9_R1.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_9_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class VersionedHologramImpl extends VersionedHologram {

    private List<EntityArmorStand> stands = new ArrayList<>();
    private List<Player> nearbyPlayers = new ArrayList<>();

    protected VersionedHologramImpl(Hologram hologram) {
        super(hologram);
        Location location = hologram.getLocation();
        location.getWorld().getNearbyEntities(location, 256.0, 256.0, 256.0).forEach(entity -> {
            if (entity instanceof Player) {
                nearbyPlayers.add((Player) entity);
            }
        });
    }

    @Override
    public void delete() {
        sendPacketNearby(new PacketPlayOutEntityDestroy(stands.stream().mapToInt(Entity::getId).toArray()));
    }

    @Override
    public void update(Player player) {
        for (EntityArmorStand stand : stands) {
            sendPacket(player, new PacketPlayOutSpawnEntity(stand, 0));
        }
    }

    @Override
    public void update() {
        float yOffset = 0f;

        List<String> lines = hologram.getLines();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            EntityArmorStand stand;
            Location location = hologram.getLocation();

            if (i < stands.size()) {
                stand = stands.get(i);
            } else {
                World world = ((CraftWorld) location.getWorld()).getHandle();
                stand = new EntityArmorStand(world, location.getX(), location.getY(), location.getZ());
                stands.add(stand);
                sendPacketNearby(new PacketPlayOutSpawnEntity(stand, 0));
            }

            stand.setCustomNameVisible(true);
            stand.setInvisible(true);
            stand.setCustomName(line);
            stand.setLocation(
                    location.getX(),
                    location.getY() - yOffset,
                    location.getZ(),
                    location.getYaw(),
                    location.getPitch()
            );

            sendPacketNearby(new PacketPlayOutEntityTeleport(stand));

            yOffset -= .23;
        }

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
