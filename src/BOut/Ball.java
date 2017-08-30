package BOut;

import com.golden.gamedev.object.*;
import com.golden.gamedev.engine.*;
import static BOut.BreakOutEngine.*;

public class Ball extends Sprite implements Cloneable
{	
  /**
   * we represent the ball's velocity vector as an angle and a magnitude;
   * the angle is measured in radians, with 0 meaning straight up;
   * this representation avoids round off of magnitude as angle changes
   */
  private double angle;

  /**
   * the magnitude of the ball's velocity vector (i.e., its speed),
   * in pixels per millisecond
   */
  private double magnitude;

  /**
   * audio channel for the ball's sounds to play
   */
  private transient BaseAudio audio;

  /**
   * the sound to make when bouncing off the wall
   */
  private static String BoundaryBounceSound = SoundsDirectory + "wall-bounce.wav";
  
  /**
   * the sound to make when bouncing off the paddle
   */
  private static String PaddleBounceSound = SoundsDirectory + "paddle-bounce.wav";
  
  /**
   * event source for hearing about changes in status of Balls 
   */
  private static BreakoutEventSource<ActiveChangedEvent<Ball>> activitySource =
    new BreakoutEventSource<ActiveChangedEvent<Ball>>();
  
  /**
   * getter of event source for changes in activity status of Ball
   * @return the current BreakoutEventSource<ActiveChangedEvent<Ball>>
   */
  public static BreakoutEventSource<ActiveChangedEvent<Ball>> getActiveChangedSource ()
  {
    return activitySource;
  }
  
  /**
   * should be called as we start a new game, so we can get ready
   */
  public static void newGame () {
    activitySource.reset();
  }
  
  /**
   * set the magnitude of the velocity vector of the ball,
   * retaining the current angle
   * @param magnitude a double, giving speed in pixels per millisecond
   */
  public void setMagnitude (double magnitude)
  {
    this.magnitude = magnitude;
    calcSpeeds();
  }
  
  /**
   * set the angle of the velocity vector of the ball,
   * retaining the current manitude
   * @param angle a double, giving the angle in radians; note that
   * in this coordinate system, angles are measured CLOCKwise!
   */
  public void setAngle (double angle)
  {
    this.angle = angle;
    calcSpeeds();
  }
  
  /**
   * Takes in the position of the Ball, without an image provided.
   * @param x
   * @param y
   */
  public Ball (double x, double y)
  {
    super(x, y);
    notifyActivityChanged();
  }

  /**
   * handles a vertical bounce, which for now just flips the vertical speed
   */
  private void bounceVertical ()
  {
    double y = this.getVerticalSpeed();
    this.setVerticalSpeed(-y);
  }

  /**
   * handles a horizontal bounce, which for now just flips the horizontal speed
   */
  private void bounceHorizontal ()
  {
    double x = this.getHorizontalSpeed();
    this.setHorizontalSpeed(-x);
  }

  /**
   * This method is called if there is a collision with the boundary,
   * and is given 4 Booleans depending on the side that 
   * this ball hits.  If it hits a corner, it would be given 2 trues. 
   * @param onLeft
   * @param onRight
   * @param onTop
   * @param onBottom
   */
  public void collisionWithBounds (boolean onLeft, boolean onRight,
                                   boolean onTop , boolean onBottom)
  {		
    // the speed checks prevent acting twice by mistake
    if ((onLeft && this.getHorizontalSpeed() < 0) || (onRight && this.getHorizontalSpeed() > 0))
    {
      this.bounceHorizontal();
      if (audio != null)
      {
        audio.play(BoundaryBounceSound);
      }
    }
    
    if (onTop && this.getVerticalSpeed() < 0)
    {
      this.bounceVertical();
      if (audio != null)
      {
        audio.play(BoundaryBounceSound);
      }
      
    } else if (onBottom)
    {
      this.setActive(false); // fell off the bottom
    }
  }

  /**
   * For collisions with Blocks and other objects, takes in two parameters to tell if the ball 
   * collides with something on its top or bottom or if it collides on its left or right with 
   * something.
   * @param isTopOrBottom
   * @param isLeftOrRight
   */
  public void collisionWithBlock (boolean isTopOrBottom, boolean isLeftOrRight)
  {
    if (isTopOrBottom)
    {
      this.bounceVertical();
    }
    if (isLeftOrRight)
    {
      this.bounceHorizontal();
    }
  }

  /**
   * Takes in the paddle it collides with, and then addresses its behavior on the bounce.
   * @param paddle
   */
  public void collisionWithPaddle (Paddle paddle)
  {
    if (this.getVerticalSpeed() > 0)
    {
      this.bounceVertical();
      if (audio != null)
      {
        audio.play(PaddleBounceSound);
      }
    }
  }

  /**
   * calculates horiz and vert speeds from magnitude and angle
   */
  private void calcSpeeds ()
  {
    this.setHorizontalSpeed(-Math.sin(angle)*magnitude);
    this.setVerticalSpeed  (-Math.cos(angle)*magnitude);
  }
    
   /**
   * Must be set for any sounds to play.  If null, no sounds will be made when the ball 
   * collides with something.
   * @param ba
   */
  public void setAudio (BaseAudio ba)
  {
    this.audio = ba;
  }

  /**
   * wraps activity changes so that we can notify listeners
   * @param newValue a boolean giving the new value for whether the Ball is active
   */
  public void setActive (boolean newValue)
  {
    boolean changed = (this.isActive() ^ newValue);
    super.setActive(newValue);
    if (changed)
    {
      notifyActivityChanged();
    }
  }
  
  /**
   * use to notify activity change listeners (if any)
   */
  private void notifyActivityChanged ()
  {
    if (activitySource != null && activitySource.anyListeners())
    {
      activitySource.notify(new ActiveChangedEvent<Ball>(this));
    }
  }

  /**
   * @return a memento (copy) of this Ball
   */
  public Ball memento ()
  {
    try
    {
      return (Ball)this.clone();
    }
    catch (CloneNotSupportedException exc)
    {
      System.err.printf("Problem cloning a Ball:%n%s", exc);
      return null;
    }
  }
  
}