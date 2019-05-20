package blue.sparse.bshade.versions.holograms;

public class HologramLineStatic extends HologramLine {

    private String content;

    public HologramLineStatic(String content) {
        this.content = content;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public int getTickRate() {
        return 0;
    }

    @Override
    public HologramLine clone() {
        return new HologramLineStatic(content);
    }
}
