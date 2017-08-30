package BOut;

import static BOut.BreakOutEngine.SoundsDirectory;

import com.golden.gamedev.engine.BaseAudio;
import com.golden.gamedev.object.Sprite;

public class Miniball extends Sprite implements Cloneable
{
	/**
	 * the id of the current miniball. will be incremented when a new miniball is created.
	 */
	public static int id = 0;

	private int numberOfBlocksHit = 0;

  /**
   * audio channel for the ball's sounds to play
   */
  private transient BaseAudio audio;
  
  /**
   * the sound to make when bouncing off the wall
   */
  private static String BlockBounceSound = SoundsDirectory + "ringout.wav";
  
  /**
   * event source for hearing about changes in status of Miniballs 
   */
  private static BreakoutEventSource<ActiveChangedEvent<Miniball>> activitySource =
    new BreakoutEventSource<ActiveChangedEvent<Miniball>>();
  
  /**
   * getter of event source for changes in activity status of Miniball
   * @return the current BreakoutEventSource<ActiveChangedEvent<Miniballs>>
   */
  public static BreakoutEventSource<ActiveChangedEvent<Miniball>> getActiveChangedSource ()
  {
    return activitySource;
  }
  
  /**
   * should be called as we start a new game, so we can get ready
   */
  public static void newGame () {
    activitySource.reset();
  }

	public Miniball(double x, double y){
		super(x,y);
		this.setVerticalSpeed(-0.5);
		id++;
		notifyActivityChanged();
	}

	public void collisionWithTop(){
		this.setActive(false);
	}
	
	/*
	 * This method is called if there is a collision with the boundary
	 */
	public void collisionWithBounds() {
		this.setActive(false);
	}
	
	public void collisionWithBlock(Block b){
		numberOfBlocksHit++;
		if (audio != null){
      audio.play(BlockBounceSound);
    }
		if(numberOfBlocksHit == 3){ //this is our third block
			this.setActive(false);
		}
		//check that we haven't already hit that block. increment number of blocks hit.
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
   * @param newValue a boolean giving the new value for whether the Miniball is active
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
      activitySource.notify(new ActiveChangedEvent<Miniball>(this));
    }
  }
  
  /**
   * @return a memento (copy) of this Miniball
   */
  public Miniball memento ()
  {
    try
    {
      return (Miniball)this.clone();
    }
    catch (CloneNotSupportedException exc)
    {
      System.err.printf("Problem cloning a Miniball:%n%s", exc);
      return null;
    }
  }
}