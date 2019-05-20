package blue.sparse.bshade.versions.holograms;

import blue.sparse.bshade.versions.util.ColoredScroller;
import blue.sparse.bshade.versions.util.Scroller;

import java.util.function.IntFunction;

public class HologramLineScrolling extends HologramLine {

    private int tickRate;
    private int width;
    private ColoredScroller scroller;

    public HologramLineScrolling(int tickRate, int width, IntFunction<String> function) {
        this.tickRate = tickRate;
        this.width = width;
        scroller = new ColoredScroller(width, function);
    }

    public HologramLineScrolling(int tickRate, int width, String content) {
        this(tickRate, width, i -> content);
    }

    @Override
    public String getContent() {
        return scroller.get(getTicksPassed());
    }

    @Override
    public int getTickRate() {
        return tickRate;
    }

    @Override
    public HologramLine clone() {
        return null;
    }

    public int getWidth() {
        return width;
    }
}
