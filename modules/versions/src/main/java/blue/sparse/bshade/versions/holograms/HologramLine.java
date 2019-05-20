package blue.sparse.bshade.versions.holograms;

public abstract class HologramLine {

    private int index;
    private int ticksPassed;

    public abstract String getContent();

    public void reset() {
        this.ticksPassed = 0;
    }

    public void tick() {
        this.ticksPassed++;
    }

    public abstract int getTickRate();

    public abstract HologramLine clone();

    public int getIndex() {
        return this.index;
    }

    void setIndex(int index) {
        this.index = index;
    }

    public int getTicksPassed() {
        return this.ticksPassed;
    }
}