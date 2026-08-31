package org.catrobat.catroid.content.bricks;

import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.actions.ScriptSequenceAction;
import org.catrobat.catroid.formulaeditor.Formula;

/**
 * Brick for zooming the camera
 * Usage: Set zoom level (1.0 = normal, 2.0 = 2x zoom, 0.5 = 2x out)
 */
public class CameraZoomXmlSerializable implements Brick {
    private static final long serialVersionUID = 1L;

    private Formula zoomValue;

    public CameraZoomXmlSerializable() {
        this.zoomValue = new Formula(1.0);
    }

    public CameraZoomXmlSerializable(Formula zoomValue) {
        this.zoomValue = zoomValue;
    }

    public void setZoomValue(Formula zoomValue) {
        this.zoomValue = zoomValue;
    }

    public Formula getZoomValue() {
        return zoomValue;
    }

    @Override
    public Brick clone() {
        return new CameraZoomXmlSerializable(zoomValue != null ? zoomValue.clone() : new Formula(1.0));
    }

    @Override
    public int getRequiredResources() {
        return PHYSICS;
    }

    @Override
    public boolean isInitialized() {
        return zoomValue != null;
    }

    @Override
    public void initialize() {
        if (zoomValue == null) {
            zoomValue = new Formula(1.0);
        }
    }

    @Override
    public void addActionToSequence(Sprite sprite, ScriptSequenceAction sequence) {
        sequence.addAction(sprite.getActionFactory()
                .createCameraZoomAction(sprite, zoomValue));
    }
}
