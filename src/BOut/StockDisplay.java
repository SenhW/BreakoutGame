package BOut;

import com.golden.gamedev.object.*;

import java.awt.image.BufferedImage;

public enum StockDisplay{
	BALLS, MINIBALLS;
	
	private Sprite[] balls;
	private BufferedImage image;
	private Background background;
	SpriteGroup display;
	private int height, position;
	private String type;
	
  /**
   * Constructor that takes in a BlockDesc and sets a location.
   * @param desc a BlockDesc for displaying the block
   * @param x a double giving the x position of the block
   * @param y a double giving the y position of the block
   */
  public void create(BufferedImage ballImage, String desc, Background bg, SpriteGroup display){
	  image = ballImage;
	  type = desc;
	  background = bg;
	  height = bg.getHeight();
	  this.display = display;
	  if(type.equals("balls")){
		  createBalls();
		  position = GameState.getGameState().getBallsRemaining()-1;//-1 for counting at 0
	  }
	  else if (type.equals("miniballs")){
		  createMiniballs();
		  position = GameState.getGameState().getMiniballsRemaining()-1;//-1 for counting at 0
	  }
	  else{
	  }
	  startDisplay();
  }
  
  public String getType(){
	  return type;
  }
  
  /**
   * creates column structure for balls
   */
  
  private void createBalls(){
	  balls = new Sprite[10];
	  for(int i=0; i<10; i++){//NOTE: index 0 = Ball 1
		  if(i<5){
			  balls[i] = new Sprite(2, height-((i+1)*22)-2);
		  }
		  else{
			  balls[i] = new Sprite(26, height-((i-4)*22)-2);
		  }
		  balls[i].setImmutable(true);
		  balls[i].setActive(false);
		  addBall(balls[i]);
	  }
  }
  
  /**
   * creates column structure for miniballs
   */
  
  private void createMiniballs(){
	  balls = new Sprite[50];
	  for(int i=0; i<50; i++){//NOTE: index 0 = Ball 1
		  if(i<10){
			  balls[i] = new Sprite(50, height-((i+1)*17)-2);
		  }
		  else if(i>=10 && i<20){
			  balls[i] = new Sprite(69, height-((i-9)*17)-2);
		  }
		  else if(i>=20 && i<30){
			  balls[i] = new Sprite(88, height-((i-19)*17)-2);
		  }
		  else if(i>=30 && i<40){
			  balls[i] = new Sprite(107, height-((i-29)*17)-2);
		  }
		  else if(i>=40 && i<50){
			  balls[i] = new Sprite(126, height-((i-39)*17)-2);
		  }
		  balls[i].setImmutable(true);
		  balls[i].setActive(false);
		  balls[i].setVerticalSpeed(0);
		  addBall(balls[i]);
	  }
  }
  
  /**
   * Utility routine for adding a Ball
   * @param b a Ball to add
   */
  private void addBall (Sprite s)
  {
    s.setImage(image);
    s.setBackground(background);
    s.setLayer(-1);
    display.add(s);
  }
  
  /**
   * Initial activation of sprites in display
   */
  
  private void startDisplay(){
	  for(int i=0; i<=position; i++){
		  balls[i].setActive(true);
	  }
  }
  
  /**
   * Updates position and activates balls based upon passed parameter
   * Order matters or else an index is skipped on addition of balls
   * @param amount
   */
  
  public void update(int amount) {
		if(amount > 0){
			position += amount;
			activateBall(amount);
		}
		else{
			activateBall(amount);
			position += amount;
		}
	}
  
  /**
   * activates a number of ball sprites
   * @param amount
   */
  
  private void activateBall(int amount){
	  if(position > balls.length){
		  //This is so no null pointers are called on the arrays
	  }
	  else if(amount > 0){
		  for(int i = 0; i<amount; i++){
			  System.out.println(position);
			  balls[position-i].setActive(!balls[position-i].isActive());
		  }
	  }
	  else{
		  balls[position].setActive(!balls[position].isActive());
	  }
  }

}
