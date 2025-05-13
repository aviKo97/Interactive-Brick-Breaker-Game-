package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.util.Vector2;
/**
 * The add Paddle stratergy, anouther paddle in the middle of the board
 */
public class AddPaddleStrategy implements CollisionStrategy{
    BrickerGameManager bgm;

    /**
     * the constructor of the strategy
     * @param bgm the main game
     */
    public AddPaddleStrategy(BrickerGameManager bgm) {
        this.bgm = bgm;
    }

    /**
     * Tell the main game to add an extra paddle
     * @param thisObj the brick
     * @param otherObj the ball
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        this.bgm.addObject("Extra Paddle",Vector2.ZERO);
        this.bgm.removeObject(thisObj);
    }
}
