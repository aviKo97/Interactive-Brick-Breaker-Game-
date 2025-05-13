package bricker.brick_strategies;

import bricker.main.BrickerGameManager;

import java.util.Random;

/**
 * A strategy factory that gives us a strategies we want
 */
public class StrategyFactory {
    private static final int MAX_STRATEGIES = 3;
    private int curStrategies;
    private final BrickerGameManager brickerGameManager2;
    private final int numOfStrategies;

    /**
     * Constructor for the project
     * @param brickerGameManager2 the main game
     * @param numOfStrategies number of strategies to choose from
     */
    public StrategyFactory(BrickerGameManager brickerGameManager2 , int numOfStrategies) {
        this.brickerGameManager2 = brickerGameManager2;
        this.numOfStrategies = numOfStrategies;
        this.curStrategies = 0;
    }

    /**
     * the function that add a strategy to the brick
     * @return a strategy
     */
    public CollisionStrategy add() {
        curStrategies = 0;
        int num = randomNumber(numOfStrategies, false);
        return getStrategy(num);
    }

    private CollisionStrategy getStrategy(int num){

        switch (num)
        {
            case 0:
                return new BasicCollisionStrategy(brickerGameManager2);
            case 1:
                return new AddPuckStrategy(brickerGameManager2);
            case 2:
                return new AddPaddleStrategy(brickerGameManager2);
            case 3:
                return new ChangeCameraStrategy(brickerGameManager2);
            case 4:
                return new AddHeartStrategy(brickerGameManager2);
            case 5:
                if (curStrategies > MAX_STRATEGIES){
                    return getStrategy(randomNumber(numOfStrategies-1,true));
                }
                else {
                    int num1 = randomNumber(numOfStrategies, true);
                    int num2 = randomNumber(numOfStrategies, true);
                    curStrategies+=2;
                    return new DoubleStrategy(new CollisionStrategy[]{
                        getStrategy(num1),
                        getStrategy(num2)});
                }
            default:
                return null;
        }
    }

    private int randomNumber(int num,boolean isEqualOdds) {
        Random random = new Random();
        if (!isEqualOdds){
            boolean isRegular = random.nextBoolean();
            if (isRegular){
                return 0;
            }
            else {
                return random.nextInt(num)+1;
            }
        }
        else {
            return random.nextInt(num+1);
        }
    }

}
