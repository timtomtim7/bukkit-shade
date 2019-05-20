package blue.sparse.bshade.versions.v1_13_R2;

import blue.sparse.bshade.versions.api.VersionedHologram;
import blue.sparse.bshade.versions.holograms.Hologram;
import net.minecraft.server.v1_13_R2.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_13_R2.util.CraftChatMessage;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class VersionedHologramImpl extends VersionedHologram {

    private List<EntityArmorStand> stands = new ArrayList<>();
    private List<Player> awarePlayers = new ArrayList<>();

    public VersionedHologramImpl(Hologram hologram) {
        super(hologram);
        update();
    }

    @Override
    public void delete() {
        sendPacketNearby(new PacketPlayOutEntityDestroy(stands.stream().mapToInt(Entity::getId).toArray()));
    }

    @Override
    public void tick() {
        handleNearbyPlayers();
    }

    public void spawnForPlayer(Player player) {
        player.sendMessage("Spawning a hologram for you.");
        for (EntityArmorStand stand : stands) {
            sendPacket(player, new PacketPlayOutSpawnEntityLiving(stand));
        }
    }

    public void destroyForPlayer(Player player) {
        player.sendMessage("Destroying a hologram for you.");
        for (EntityArmorStand stand : stands) {
            sendPacket(player, new PacketPlayOutEntityDestroy(stand.getId()));
        }
    }

    public void handleNearbyPlayers() {
        if (!hologram.isVisible())
            return;

        Location location = hologram.getLocation();
        List<Player> nearbyPlayers = location.getWorld().getNearbyEntities(location, 256.0, 256.0, 256.0).stream()
                .filter(entity -> entity instanceof Player)
                .map(player -> (Player) player)
                .collect(Collectors.toList());

        for (Player player : nearbyPlayers) {
            if (!awarePlayers.contains(player)) {
                spawnForPlayer(player);
                awarePlayers.add(player);
            }
        }

        ArrayList<Player> removedPlayers = new ArrayList<>();
        for (Player player : awarePlayers) {
            if (!nearbyPlayers.contains(player)) {
                if (player.isOnline()) {
                    destroyForPlayer(player);
                }
                removedPlayers.add(player);
            }
        }

        awarePlayers.removeAll(removedPlayers);
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

        handleNearbyPlayers();
    }

    private void sendPacket(List<Player> players, Packet<?> packet) {
        players.forEach(player -> sendPacket(player, packet));
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