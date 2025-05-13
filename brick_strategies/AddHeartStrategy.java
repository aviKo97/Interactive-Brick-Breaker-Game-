package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.util.Vector2;

/**
 * The add life stratergy, the falling heart
 */
public class AddHeartStrategy implements CollisionStrategy{

    private final BrickerGameManager bgm;

    /**
     * the constructor of the strategy
     * @param bgm the main game
     */
    public AddHeartStrategy(BrickerGameManager bgm) {
        this.bgm = bgm;
    }

    /**
     * Tell the main game to add a falling heart
     * @param thisObj the brick
     * @param otherObj the ball
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        bgm.addObject("Heart",new Vector2(thisObj.getCenter()));
        bgm.removeObject(thisObj);
    }
}
