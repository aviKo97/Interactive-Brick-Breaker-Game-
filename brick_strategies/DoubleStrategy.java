package bricker.brick_strategies;

import danogl.GameObject;

/**
 * The Double strategy, have multiple strategies
 */
public class DoubleStrategy implements CollisionStrategy {
    private CollisionStrategy[] strategy;
    /**
     * the constructor of the strategy
     * @param strategy the arry of the strategies
     */
    public DoubleStrategy(CollisionStrategy[] strategy) {
        this.strategy = strategy;
    }

    /**
     * To do all strategies
     * @param thisObj the brick
     * @param otherObj the ball
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        for (CollisionStrategy strategy1:strategy){
            strategy1.onCollision(thisObj, otherObj);
        }
    }
}