import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;

import javax.swing.JComponent;

public class RenderedPlanet extends JComponent{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Graphics2D painting;
	public static Image image;

	RenderedPlanet (){
		setDoubleBuffered(false);
	}
	
	public void paintComponent(Graphics g) 
	{
		if (image == null) 
		{
			image = createImage(getSize().width, getSize().height);
			painting = (Graphics2D) image.getGraphics();
			painting.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
			clear();
		}
		g.drawImage(image, 0, 0, null);
	}
	
	public void clear(){
		if (painting != null) {
			painting.setPaint(Color.white);
			painting.fillRect(0, 0, this.getSize().width, this.getSize().height);
			painting.setPaint(Color.black);
			repaint();
		}
	}
	
	public void drawPlanet(Planet planet){
		if (painting != null) {
			Ellipse2D ellipse = new Ellipse2D.Double(
				planet.getPosition().x/(4*Math.pow(10, 8)) - planet.getR(),
				planet.getPosition().y/(4*Math.pow(10, 8)) - planet.getR(),
				planet.getR() * 2,
				planet.getR() * 2
			);
			
			painting.setColor(planet.getColor());
			painting.fill(ellipse);
			painting.draw(ellipse);
			
			repaint();
		}
	}

}