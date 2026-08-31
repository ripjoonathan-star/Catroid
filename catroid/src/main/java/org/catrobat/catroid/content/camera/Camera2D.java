package org.catrobat.catroid.content.camera;

import android.graphics.Matrix;
import android.graphics.PointF;

import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.RenderLayer;

/**
 * 2D Camera system for Catroid with follow, zoom, and shake capabilities.
 * Supports layer-based rendering for proper depth management.
 */
public class Camera2D {
    private float x;
    private float y;
    private float zoom = 1.0f;
    private Sprite followTarget;
    private boolean isFollowing = false;

    // Shake effect
    private float shakeIntensity = 0f;
    private float shakeDuration = 0f;
    private float shakeTimer = 0f;
    private boolean isShaking = false;

    // Boundaries
    private float minX = Float.NEGATIVE_INFINITY;
    private float maxX = Float.POSITIVE_INFINITY;
    private float minY = Float.NEGATIVE_INFINITY;
    private float maxY = Float.POSITIVE_INFINITY;

    // Screen dimensions
    private int screenWidth = 1080;
    private int screenHeight = 1920;

    // Smoothing
    private float followSpeed = 0.1f;

    public Camera2D(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.x = screenWidth / 2f;
        this.y = screenHeight / 2f;
    }

    /**
     * Set the sprite to follow
     */
    public void setFollowTarget(Sprite sprite) {
        this.followTarget = sprite;
        this.isFollowing = sprite != null;
        if (isFollowing) {
            // Immediately focus on target
            this.x = followTarget.look.getXInPixels();
            this.y = followTarget.look.getYInPixels();
        }
    }

    /**
     * Update camera position (called every frame)
     * @param deltaTime time since last frame in seconds
     */
    public void update(float deltaTime) {
        // Update following
        if (isFollowing && followTarget != null) {
            float targetX = followTarget.look.getXInPixels();
            float targetY = followTarget.look.getYInPixels();

            // Smooth follow
            this.x += (targetX - this.x) * followSpeed;
            this.y += (targetY - this.y) * followSpeed;
        }

        // Update shake effect
        if (isShaking) {
            shakeTimer += deltaTime;
            if (shakeTimer >= shakeDuration) {
                isShaking = false;
                shakeTimer = 0f;
                shakeIntensity = 0f;
            }
        }

        // Apply boundaries
        clampPosition();
    }

    /**
     * Start camera shake effect
     * @param intensity shake strength in pixels
     * @param duration shake duration in seconds
     */
    public void shake(float intensity, float duration) {
        this.shakeIntensity = intensity;
        this.shakeDuration = duration;
        this.shakeTimer = 0f;
        this.isShaking = true;
    }

    /**
     * Get shake offset (random offset when shaking)
     */
    public PointF getShakeOffset() {
        if (!isShaking || shakeIntensity == 0) {
            return new PointF(0, 0);
        }
        float progress = shakeTimer / shakeDuration;
        float intensity = shakeIntensity * (1 - progress); // Fade out shake
        float offsetX = (float) (Math.random() - 0.5f) * intensity * 2;
        float offsetY = (float) (Math.random() - 0.5f) * intensity * 2;
        return new PointF(offsetX, offsetY);
    }

    /**
     * Set zoom level
     * @param zoom zoom factor (1.0 = normal, 2.0 = 2x zoom, 0.5 = 2x out)
     */
    public void setZoom(float zoom) {
        this.zoom = Math.max(0.1f, zoom);
    }

    /**
     * Smoothly zoom to target
     */
    public void zoomTo(float targetZoom, float speed) {
        float diff = targetZoom - zoom;
        zoom += diff * speed;
        zoom = Math.max(0.1f, zoom);
    }

    /**
     * Set follow speed (0.0 - 1.0)
     */
    public void setFollowSpeed(float speed) {
        this.followSpeed = Math.max(0.01f, Math.min(1.0f, speed));
    }

    /**
     * Set camera boundaries
     */
    public void setBoundaries(float minX, float maxX, float minY, float maxY) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }

    /**
     * Clamp camera position to boundaries
     */
    private void clampPosition() {
        float halfWidth = screenWidth / (2 * zoom);
        float halfHeight = screenHeight / (2 * zoom);

        if (x - halfWidth < minX) x = minX + halfWidth;
        if (x + halfWidth > maxX) x = maxX - halfWidth;
        if (y - halfHeight < minY) y = minY + halfHeight;
        if (y + halfHeight > maxY) y = maxY - halfHeight;
    }

    /**
     * Get transformation matrix for rendering
     */
    public Matrix getTransformMatrix() {
        Matrix matrix = new Matrix();

        // Apply shake
        PointF shakeOffset = getShakeOffset();

        // Translate to camera position
        float translateX = screenWidth / 2f - (x + shakeOffset.x) * zoom;
        float translateY = screenHeight / 2f - (y + shakeOffset.y) * zoom;

        matrix.postScale(zoom, zoom, screenWidth / 2f, screenHeight / 2f);
        matrix.postTranslate(shakeOffset.x * zoom, shakeOffset.y * zoom);
        matrix.postTranslate(translateX, translateY);

        return matrix;
    }

    /**
     * Convert world coordinates to screen coordinates
     */
    public PointF worldToScreen(float worldX, float worldY) {
        float screenX = (worldX - x) * zoom + screenWidth / 2f;
        float screenY = (worldY - y) * zoom + screenHeight / 2f;
        return new PointF(screenX, screenY);
    }

    /**
     * Convert screen coordinates to world coordinates
     */
    public PointF screenToWorld(float screenX, float screenY) {
        float worldX = (screenX - screenWidth / 2f) / zoom + x;
        float worldY = (screenY - screenHeight / 2f) / zoom + y;
        return new PointF(worldX, worldY);
    }

    // Getters
    public float getX() { return x; }
    public float getY() { return y; }
    public float getZoom() { return zoom; }
    public boolean isFollowing() { return isFollowing; }
    public boolean isShaking() { return isShaking; }
    public float getFollowSpeed() { return followSpeed; }
    public Sprite getFollowTarget() { return followTarget; }
}
