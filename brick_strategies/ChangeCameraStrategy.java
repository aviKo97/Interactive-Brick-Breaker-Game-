package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.util.Vector2;

/**
 * The Change Camera stratergy, the camera changes
 */
public class ChangeCameraStrategy implements CollisionStrategy{

    private final BrickerGameManager bgm;

    /**
     * the constructor of the strategy
     * @param bgm the main game
     */
    public ChangeCameraStrategy(BrickerGameManager bgm) {

        this.bgm = bgm;
    }
    /**
     * Tell the main game to add a camera effect
     * @param thisObj the brick
     * @param otherObj the ball
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        this.bgm.removeObject(thisObj);
        if (otherObj.getTag().equals("Ball")){
            this.bgm.addObject("Camera", Vector2.ZERO);
        }
    }
}
