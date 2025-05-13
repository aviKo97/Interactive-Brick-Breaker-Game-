package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

/**
 * The paddle/Exrta paddle class
 */
public class Paddle extends GameObject {

    private int collisionCounter;
    private static final float MOVEMENT_SPEED = 300;
    private final UserInputListener userInputListener;
    private static final float LEFT_BORDER_COOR =15;
    private static final float RIGHT_BORDER_COOR =585;

    /**
     * Construct a new GameObject instance Paddle/Exrta Paddle.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     * @param userInputListener the input from user <>
     */
    public Paddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                  UserInputListener userInputListener) {
        super(topLeftCorner, dimensions, renderable);
        this.userInputListener = userInputListener;
        this.collisionCounter = 0;
    }

    /**
     * update the location of the object according to the user input
     * @param deltaTime The time elapsed, in seconds, since the last frame. Can
     *                  be used to determine a new position/velocity by multiplying
     *                  this delta with the velocity/acceleration respectively
     *                  and adding to the position/velocity:
     *                  velocity += deltaTime*acceleration
     *                  pos += deltaTime*velocity
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        Vector2 paddleSpeed = Vector2.ZERO;
        if(userInputListener.isKeyPressed(KeyEvent.VK_LEFT) && getTopLeftCorner().x() > LEFT_BORDER_COOR){
            paddleSpeed = paddleSpeed.add(Vector2.LEFT);
        }
        if(userInputListener.isKeyPressed(KeyEvent.VK_RIGHT) && getTopLeftCorner().x() <= RIGHT_BORDER_COOR){
            paddleSpeed = paddleSpeed.add(Vector2.RIGHT);
        }
        setVelocity(paddleSpeed.mult(MOVEMENT_SPEED));
    }

    /**
     * What to do whan collision accur
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        this.collisionCounter++;
    }

    /**
     * @return the collision counter
     */
    public int getCollisionCounter(){
        return this.collisionCounter;
    }
}