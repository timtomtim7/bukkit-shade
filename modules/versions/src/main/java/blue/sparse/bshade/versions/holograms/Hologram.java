package blue.sparse.bshade.versions.holograms;

import blue.sparse.bshade.versions.Versioned;
import blue.sparse.bshade.versions.api.VersionedHologram;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Hologram {
    public static final double DEFAULT_LINE_SPACING = .23;
    private static List<Hologram> allHolograms = new ArrayList<>();
    private static int ticksPassed = 0;

    private List<HologramLine> lines = new ArrayList<>();
    private Location location;
    private boolean isVisible = true;
    private double lineSpacing;
    private VersionedHologram vHologram;

    static {
        JavaPlugin plugin = JavaPlugin.getProvidingPlugin(Hologram.class);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, Hologram::tick, 0, 1);
    }

    public Hologram(Location location, double lineSpacing, HologramLine... lines) {
        this.lines.addAll(Arrays.asList(lines));
        this.lineSpacing = lineSpacing;
        this.location = location;
        this.vHologram = Versioned.getHologram(this);
        allHolograms.add(this);
    }

    public Hologram(Location location, HologramLine... lines) {
        this(location, DEFAULT_LINE_SPACING, lines);
    }

    public Hologram(Location location, String... lines) {
        this(location, DEFAULT_LINE_SPACING, Arrays.stream(lines).map(HologramLineStatic::new).toArray(HologramLine[]::new));
    }

    private static void tick() {
        for (Hologram hologram : allHolograms) {
            if (ticksPassed % 10 == 0) {
                hologram.vHologram.tick();
            }

            boolean updated = false;

            for (HologramLine line : hologram.lines) {
                if (ticksPassed % line.getTickRate() == 0) {
                    line.tick();
                    updated = true;
                }
            }

            if (updated) {
                hologram.vHologram.update();
            }
        }

        ticksPassed++;
    }

    public static List<Hologram> getAllHolograms() {
        return allHolograms;
    }

    public List<HologramLine> getLines() {
        return Collections.unmodifiableList(lines);
    }

    public List<String> getLineContent() {
        return lines.stream().map(HologramLine::getContent).collect(Collectors.toList());
    }

    public void setLines(List<HologramLine> lines) {
        this.lines = lines.stream().map(HologramLine::clone).collect(Collectors.toList());
        vHologram.update();
    }

    public void setLines(String... lines) {
        setLines(Arrays.stream(lines).map(HologramLineStatic::new).collect(Collectors.toList()));
    }

    public HologramLine getLine(int index) {
        return lines.get(index);
    }

    public void setLine(int index, HologramLine line) {
        HologramLine clone = line.clone();
        clone.setIndex(index);
        clone.reset();
        lines.set(index, clone);
        vHologram.update();
    }

    public void setLine(int index, String string) {
        setLine(index, new HologramLineStatic(string));
    }

    public void addLine(HologramLine line) {
        HologramLine clone = line.clone();
        clone.setIndex(lines.size());
        clone.reset();
        lines.add(clone);
        vHologram.update();
    }

    public void addLine(String string) {
        addLine(new HologramLineStatic(string));
    }

    public void removeLine(int index) {
        lines.remove(index);
        vHologram.update();
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location.clone();
        vHologram.update();
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
        vHologram.update();
    }

    public void delete() {
        allHolograms.remove(this);
        vHologram.delete();
    }

    public double getHieght() {
        int lineCount = lines.size();
        return lineCount * lineSpacing;
    }

    public double getLineSpacing() {
        return lineSpacing;
    }

    public void setLineSpacing(double lineSpacing) {
        this.lineSpacing = lineSpacing;
    }
}
