package bricker.brick_strategies;

import danogl.GameObject;

/**
 * The interface of a strategy, Have to do onCollision method.
 */
public interface CollisionStrategy {
    /**
     * The collision implamentation
     * @param thisObj the brick
     * @param otherObj the ball
     */
    void onCollision(GameObject thisObj, GameObject otherObj);
}
