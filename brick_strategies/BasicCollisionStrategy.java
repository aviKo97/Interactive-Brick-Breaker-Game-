package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;

/**
 * The Basic stratergy, The brick will be removed
 */
public class BasicCollisionStrategy implements CollisionStrategy{

    private final BrickerGameManager bgm;
    /**
     * the constructor of the strategy
     * @param bgm the main game
     */
    public BasicCollisionStrategy(BrickerGameManager bgm){

        this.bgm = bgm;
    }
    /**
     * Tell the main game to remove the brick
     * @param thisObj the brick
     * @param otherObj the ball
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj){
        this.bgm.removeObject(thisObj);
    }
}
