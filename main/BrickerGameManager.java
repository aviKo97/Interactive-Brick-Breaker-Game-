package bricker.main;

import bricker.brick_strategies.*;
import bricker.gameobjects.Ball;
import bricker.gameobjects.Brick;
import bricker.gameobjects.Heart;
import bricker.gameobjects.Paddle;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.Camera;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Objects;
import java.util.Random;


/**
 * This class is responsible for implementing the game
 */
public class BrickerGameManager extends GameManager {
    private static final float BALL_X_SPEED = 150;
    private static final float BALL_Y_SPEED = 150;
    private static final float GAME_WIDTH = 700;
    private static final float GAME_HIGH = 500;
    private static final float BALL_SIZE = 20;
    private static final int MAX_HEARTS = 4;
    private static final int NUMBER_OF_STRATEGIES = 5;
    private static final float BRICK_HIGHT = 15;
    private static final float GAP = 4;
    private static int brickRows;
    private static int brickCols;
    private final float BRICK_WIDTH = (GAME_WIDTH-40-brickCols*2)/brickCols;
    private Ball ball;
    private Paddle paddle;
    private Paddle extraPaddle;
    private boolean isExtraPaddle = false;
    private ImageReader imageReader;
    private SoundReader soundReader;
    private UserInputListener inputListener;
    private WindowController windowController;
    private static int life = 3;
    private GameObject[] hearts;
    private GameObject numberOfLife;
    private Counter numOfBricks = new Counter(brickRows*brickCols);
    private boolean cameraOn = false;
    private int startCameraTime;

    /**
     * this is a constructor for the game window
     * @param windowTitle the title abouve
     * @param windowDimensions the dimansion we want
     */
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions) {
        super(windowTitle, windowDimensions);
    }

    /**
     * this function builts the starting game
     * @param imageReader Contains a single method: readImage, which reads an image from disk.
     *                 See its documentation for help.
     * @param soundReader Contains a single method: readSound, which reads a wav file from
     *                    disk. See its documentation for help.
     * @param inputListener Contains a single method: isKeyPressed, which returns whether
     *                      a given key is currently pressed by the user or not. See its
     *                      documentation.
     * @param windowController Contains an array of helpful, self explanatory methods
     *                         concerning the window.
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.inputListener = inputListener;
        this.windowController = windowController;
        createBackground();
        createBorders();
        createBall();
        createPaddle();
        createBricks();
        createHearts();
        createNumberOfLife();
    }

    /**
     * this function updatets all the elements we run in the game
     * @param deltaTime The time, in seconds, that passed since the last invocation
     *                  of this method (i.e., since the last frame). This is useful
     *                  for either accumulating the total time that passed since some
     *                  event, or for physics integration (i.e., multiply this by
     *                  the acceleration to get an estimate of the added velocity or
     *                  by the velocity to get an estimate of the difference in position).
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        for (GameObject obj:gameObjects()){
            if (Objects.equals(obj.getTag(), "Puck") &&
                    obj.getCenter().y() > windowController.getWindowDimensions().y()){
                gameObjects().removeGameObject(obj);
                System.out.println("REMOVING PUCK");
            }
            if (Objects.equals(obj.getTag(), "Heart") &&
                    obj.getCenter().y() > windowController.getWindowDimensions().y()){
                gameObjects().removeGameObject(obj);
                System.out.println("REMOVING HEART");
            }
        }
        if (isExtraPaddle && extraPaddle.getCollisionCounter() >= 4){
            gameObjects().removeGameObject(extraPaddle);
            isExtraPaddle = false;
        }
        //if the ball went out of the board
        if (ball.getCenter().y() > GAME_HIGH) {
            //if he has more life then 1
            if (life > 1) {
                gameObjects().removeGameObject(this.hearts[life - 1], Layer.UI);
                life--;
                ball.setCenter(new Vector2(GAME_WIDTH/2,GAME_HIGH/2));
                this.gameObjects().removeGameObject(numberOfLife,Layer.UI);
                createNumberOfLife();
            } else {
                if (windowController.openYesNoDialog("You lose! Play again?")) {
                    windowController.resetGame();
                } else {
                    windowController.closeWindow();
                }
            }
        }
        //if he wins
        if (this.numOfBricks.value() == 0 || inputListener.isKeyPressed(KeyEvent.VK_W)){
            if (windowController.openYesNoDialog( "You win! Play again?")) {
                windowController.resetGame();
            } else {
                windowController.closeWindow();
            }
        }
        //if we need to turn off the camera
        if (cameraOn && (ball.getCollisionCounter()- startCameraTime) >= 4){
            turnOffCamera();
        }
    }

    /**
     * this function adds objects to the game
     * @param str the object we want to add
     * @param vector2 the place we want to add it to
     */
    public void addObject(String str,Vector2 vector2){
        switch (str){
            case "Extra paddle":
                if (!isExtraPaddle){
                    createExtraPaddle();
                }
                break;
            case "Puck":
                createPuck(vector2);
                break;
            case "Heart":
                createHeart(vector2);
                break;
            case "Camera":
                if(!cameraOn){
                    turnOnCamera();
                    startCameraTime = ball.getCollisionCounter();
                }
                else {
                    turnOffCamera();
                }
        }
    }

    /**
     * this function removes the objects we want from the game
     * @param gameObject the object we are removing
     */
    public void removeObject(GameObject gameObject) {
        //remove bricks
        if (gameObject.getTag().equals("Brick")) {
            gameObjects().removeGameObject(gameObject, Layer.STATIC_OBJECTS);
            numOfBricks.decrement();
            gameObject.setTag("Brick removed");
        }
        //remove heart
        if (gameObject.getTag().equals("Heart")){
            gameObjects().removeGameObject(gameObject);
            if (life<=3){
                deleteHeartsAndLife();
                life++;
                createHearts();
                createNumberOfLife();}
        }
    }

    //**********PRIVATE METHODS**********
    private void createHeart(Vector2 vector2) {
        Renderable heartImage = imageReader.readImage("assets/heart.png",true);
        Heart heart = new Heart(Vector2.ZERO,new Vector2(10, 10),heartImage,this);
        heart.setTag("Heart");
        heart.setVelocity(new Vector2(0,100));
        heart.setCenter(vector2);
        this.gameObjects().addGameObject(heart);
    }

    private void createExtraPaddle() {
        Renderable paddleImage = imageReader.readImage("assets/paddle.png",true);
        extraPaddle = new Paddle(Vector2.ZERO,new Vector2(100,15),paddleImage,this.inputListener);
        extraPaddle.setCenter(new Vector2(GAME_WIDTH/2,GAME_HIGH/2));
        this.gameObjects().addGameObject(extraPaddle);
        extraPaddle.setTag("Paddle");
        isExtraPaddle = true;
    }

    private void createPuck(Vector2 vector2) {
        Renderable ballImage = imageReader.readImage("assets/mockBall.png",true);
        Sound collisionSound = soundReader.readSound("assets/blop_cut_silenced.wav");
        Random random = new Random();
        Ball puck;
        for (int i = 0; i < 2; i++) {
            puck = new Ball(Vector2.ZERO,new Vector2(15,15),ballImage,collisionSound);
            puck.setTag("Puck");
            double angle = random.nextDouble()*Math.PI;
            float velX = (float)Math.cos(angle)* BALL_X_SPEED;
            float velY = (float)Math.sin(angle)* BALL_Y_SPEED;
            puck.setVelocity(new Vector2(velX,velY));
            puck.setCenter(vector2);
            this.gameObjects().addGameObject(puck);
        }
    }

    private void createBackground() {
        Renderable backgroundImage =
                imageReader.readImage("assets/DARK_BG2_small.jpeg",false);
        GameObject background =
                new GameObject(Vector2.ZERO,new Vector2(GAME_WIDTH, GAME_HIGH),backgroundImage);
        this.gameObjects().addGameObject(background, Layer.BACKGROUND);
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
    }
    private void createNumberOfLife() {
        TextRenderable textRenderable = new TextRenderable(Integer.toString(life));
        if (life >= 3){
            textRenderable.setColor(Color.GREEN);
        }
        if (life == 2){
            textRenderable.setColor(Color.YELLOW);
        }
        if (life == 1){
            textRenderable.setColor(Color.RED);
        }
        numberOfLife = new GameObject(Vector2.ZERO,new Vector2(8, 8),textRenderable);
        this.gameObjects().addGameObject(numberOfLife,Layer.UI);
        numberOfLife.setTag("number of life");
        numberOfLife.setTopLeftCorner(new Vector2(20,GAME_HIGH-15));
    }
    private void createHearts() {
        Renderable heartImage = imageReader.readImage("assets/heart.png",true);
        this.hearts = new GameObject[MAX_HEARTS];
        for (int i = 0; i < life; i++) {
            GameObject heart = new GameObject(Vector2.ZERO,new Vector2(10, 10),heartImage);
            this.hearts[i] = heart;
            heart.setTopLeftCorner(new Vector2(i*15+40, GAME_HIGH-15));
            heart.setTag("heart");
            this.gameObjects().addGameObject(heart,Layer.UI);
            }
    }

    private void deleteHeartsAndLife() {
        for (int i = 0; i < life; i++) {
            gameObjects().removeGameObject(this.hearts[i],Layer.UI);
        }
        gameObjects().removeGameObject(numberOfLife,Layer.UI);
    }
    private void createBorders() {
        //create left border
        GameObject leftBorder = new GameObject(Vector2.ZERO,new Vector2(10, GAME_HIGH),null);
        leftBorder.setCenter(new Vector2(5, GAME_HIGH*0.5f));
        this.gameObjects().addGameObject(leftBorder, Layer.STATIC_OBJECTS);
        //create right border
        GameObject rightBorder = new GameObject(Vector2.ZERO,new Vector2(10, GAME_HIGH),null);
        rightBorder.setCenter(new Vector2(GAME_WIDTH-5, GAME_HIGH*0.5f));
        this.gameObjects().addGameObject(rightBorder,Layer.STATIC_OBJECTS);
        //create up border
        GameObject upBorder = new GameObject(Vector2.ZERO,new Vector2(GAME_WIDTH, 10),null);
        upBorder.setCenter(new Vector2(GAME_WIDTH*0.5f, 5));
        this.gameObjects().addGameObject(upBorder,Layer.STATIC_OBJECTS);
    }

    private void createPaddle() {
        Renderable paddleImage = imageReader.readImage("assets/paddle.png",true);
        paddle = new Paddle(Vector2.ZERO,new Vector2(100,15),paddleImage,this.inputListener);
        paddle.setCenter(new Vector2(GAME_WIDTH/2,GAME_HIGH-20));
        this.gameObjects().addGameObject(paddle);
        paddle.setTag("Paddle");
    }

    private void createBall() {
        Random random = new Random();
        float ballX = BALL_X_SPEED;
        if(random.nextBoolean())
        {
            ballX *= -1;
        }
        Renderable ballImage = imageReader.readImage("assets/ball.png",true);
        Sound collisionSound = soundReader.readSound("assets/blop_cut_silenced.wav");
        ball = new Ball(Vector2.ZERO,new Vector2(BALL_SIZE,BALL_SIZE),ballImage,collisionSound);
        ball.setVelocity(new Vector2(ballX,BALL_Y_SPEED));
        ball.setCenter(new Vector2(GAME_WIDTH/2,GAME_HIGH/2));
        this.gameObjects().addGameObject(ball);
        ball.setTag("Ball");
    }

    private void createBricks() {
        StrategyFactory strategyFactory = new StrategyFactory(this,NUMBER_OF_STRATEGIES);
        Renderable brickImage = imageReader.readImage("assets/brick.png",false);
        for (int i = 0; i < brickRows; i++) {
            for (int j = 0; j < brickCols; j++) {
                GameObject brick =
                        new Brick(Vector2.ZERO,new Vector2(BRICK_WIDTH,BRICK_HIGHT),
                                brickImage,strategyFactory.add());
                brick.setTopLeftCorner(new Vector2(BRICK_HIGHT + j * BRICK_WIDTH + GAP * j,
                                                    BRICK_HIGHT + i * BRICK_HIGHT + GAP * i));
                brick.setTag("Brick");
                this.gameObjects().addGameObject(brick,Layer.STATIC_OBJECTS);
            }
        }
    }

    private void turnOnCamera(){
        cameraOn = true;
        setCamera(
                new Camera(
                        ball, //object to follow
                        Vector2.ZERO, //follow the center of the object
                        windowController.getWindowDimensions().mult(1.2f), //widen the frame a bit
                        windowController.getWindowDimensions() //share the window dimensions
                )
        );

    }
    private void turnOffCamera(){
        cameraOn = false;
        setCamera(null);
    }

    /**
     * the main game run
     * @param args arg1 - the num of row and arg2- the num of cols of the brick
     */
    public static void main(String[] args) {
        if (args.length != 0) {
            brickCols = Integer.parseInt(args[0]);
            brickRows = Integer.parseInt(args[1]);
        }
        else {
            brickRows = 7;
            brickCols = 8;
        }
        BrickerGameManager bgm = new BrickerGameManager("BrickerGame",new Vector2(GAME_WIDTH, GAME_HIGH));
        bgm.run();
    }
}