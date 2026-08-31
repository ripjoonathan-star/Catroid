package org.catrobat.catroid.content;

/**
 * Defines render layers for proper depth sorting in camera system.
 * Layers are rendered in order from background to foreground.
 */
public enum RenderLayer {
    BACKGROUND(0, "Background"),
    PARALLAX_FAR(1, "Parallax Far"),
    GAME_WORLD(2, "Game World"),
    PLAYER(3, "Player"),
    ENEMY(4, "Enemy"),
    PARTICLE(5, "Particle"),
    UI(6, "UI"),
    UI_OVERLAY(7, "UI Overlay");

    private final int depth;
    private final String displayName;

    RenderLayer(int depth, String displayName) {
        this.depth = depth;
        this.displayName = displayName;
    }

    public int getDepth() {
        return depth;
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * Returns the layer for a given depth value
     */
    public static RenderLayer fromDepth(int depth) {
        for (RenderLayer layer : values()) {
            if (layer.depth == depth) {
                return layer;
            }
        }
        return GAME_WORLD; // Default fallback
    }
}
