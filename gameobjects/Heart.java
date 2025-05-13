package bricker.gameobjects;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * this class is The wxtra heart (life) that fall from the brick
 */
public class Heart extends GameObject {
    private final BrickerGameManager bgm;

    /**
     * Construct a new GameObject instance Heart.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     * @param bgm the main game
     */
    public Heart(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, BrickerGameManager bgm) {
        super(topLeftCorner, dimensions, renderable);
        this.bgm = bgm;
    }

    /**
     * we define that it can only collide with the regular Paddle
     * @param other The other GameObject.
     * @return true if it is a paddle
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        super.shouldCollideWith(other);
        return other.getTag().equals("Paddle");
    }

    /**
     * dfine what to do whan hit the paddle
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        bgm.removeObject(this);
    }

}
