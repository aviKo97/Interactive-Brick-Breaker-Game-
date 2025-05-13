package bricker.brick_strategies;

import danogl.GameObject;
import danogl.util.Vector2;
import bricker.main.BrickerGameManager;

/**
 * The add Puck stratergy, 2 Pucks will be added.
 */
public class AddPuckStrategy implements CollisionStrategy {
    private final BrickerGameManager bgm;
    /**
     * the constructor of the strategy
     * @param bgm the main game
     */
    public AddPuckStrategy(BrickerGameManager bgm){
        this.bgm = bgm;
    }
    /**
     * Tell the main game to add a Puck ball
     * @param thisObj the brick
     * @param otherObj the ball
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        bgm.addObject("Puck",new Vector2(thisObj.getCenter()));
        bgm.removeObject(thisObj);
    }
}
