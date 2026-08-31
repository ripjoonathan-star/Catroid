package org.catrobat.catroid.content.bricks;

import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.actions.ScriptSequenceAction;

/**
 * Brick for making camera follow a sprite with smooth movement
 */
public class CameraFollowSpriteXmlSerializable implements Brick {
    private static final long serialVersionUID = 1L;

    private transient Sprite sprite;

    @Override
    public Brick clone() {
        return new CameraFollowSpriteXmlSerializable();
    }

    @Override
    public int getRequiredResources() {
        return PHYSICS;
    }

    @Override
    public boolean isInitialized() {
        return true;
    }

    @Override
    public void initialize() {
        // No initialization needed
    }

    @Override
    public void addActionToSequence(Sprite sprite, ScriptSequenceAction sequence) {
        sequence.addAction(sprite.getActionFactory()
                .createCameraFollowSpriteAction(sprite));
    }
}
