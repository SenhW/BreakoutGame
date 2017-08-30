package BOut;

/**
 * Implements the PowerUpStrategyDecorator that yields new Miniballs when hit
 * 
 * @author Eliot Moss
 */
public class PowerUpStrategyDecoratorBall extends PowerUpStrategyDecorator {

  /**
   * wraps the given strategy with our behavior
   * @param decorated
   */
  public PowerUpStrategyDecoratorBall (PowerUpStrategy decorated)
  {
    super(decorated);
  }
  
  /**
   * add 5 Miniballs to the GameState
   */
  @Override
  public void activate ()
  {
    super.activate();
    GameState.getGameState().addBalls(1);
  }

}
