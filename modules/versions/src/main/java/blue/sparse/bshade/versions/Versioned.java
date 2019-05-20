package blue.sparse.bshade.versions;

import blue.sparse.bshade.versions.api.VersionedBlock;
import blue.sparse.bshade.versions.api.VersionedEntity;
import blue.sparse.bshade.versions.api.VersionedHologram;
import blue.sparse.bshade.versions.api.VersionedPlayer;
import blue.sparse.bshade.versions.holograms.Hologram;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public final class Versioned {

    private static Map<Class<?>, Constructor<?>> cachedConstructors = new HashMap<>();

    private Versioned() {
    }

    public static VersionedHologram getHologram(Hologram hologram) {
        return getVersioned(VersionedHologram.class, Hologram.class, hologram);
    }

    public static VersionedPlayer getPlayer(Player player) {
        return getVersioned(VersionedPlayer.class, Player.class, player);
    }

    public static VersionedBlock getBlock(Block block) {
        return getVersioned(VersionedBlock.class, Block.class, block);
    }

    public static VersionedEntity getEntity(Entity entity) {
        return getVersioned(VersionedEntity.class, Entity.class, entity);
    }

    private static <T> T getVersioned(Class<T> apiClass, Class<?> type, Object parameter) {
        return getVersioned(apiClass, new Class<?>[]{type}, new Object[]{parameter});
    }

    private static <T> T getVersioned(Class<T> apiClass, Class<?>[] types, Object[] parameters) {
        Constructor<?> cachedConstructor = cachedConstructors.get(apiClass);
        if(cachedConstructor != null) {
            try {
                return apiClass.cast(cachedConstructor.newInstance(parameters));
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        String name = apiClass.getSimpleName() + "Impl";

        Class<T> clazz = getVersionedClass(name);

        try {
            Constructor<T> constructor = clazz.getConstructor(types);
            cachedConstructors.put(apiClass, constructor);
            return constructor.newInstance(parameters);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }

        throw new IllegalStateException(
                String.format("Unable to create an instance of %s for version %s", name, NMSVersion.current().name())
        );
    }

    @SuppressWarnings("unchecked")
    private static <T> Class<T> getVersionedClass(String name) {
        String className = "blue.sparse.bshade.versions." + NMSVersion.current().name() + "." + name;
        try {
            return (Class<T>) Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        throw new IllegalStateException("Unable to locate VersionedPlayerImpl class for version " + NMSVersion.current().name());
    }
}
