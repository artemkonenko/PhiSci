import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;


public class Way extends LinkedList<Spot> implements IDrawble{
	private static final long serialVersionUID = 1L;

	@Override
	public void draw(Graphics g, int scale) {
		int x = -1;
		int y = -1;
		g.setColor(Color.magenta);
		
		for (Spot spot: this) {
			if (this.indexOf(spot) != this.lastIndexOf(spot)) {
				//this.removeRange( this.indexOf(spot) , this.lastIndexOf(spot));
				break;
			} else {
				if (x != -1 && y != -1) {
					g.drawLine(x, y, spot.x * scale + scale / 2, spot.y * scale + scale / 2);
				}
				
				x = spot.x * scale + scale / 2;
				y = spot.y * scale + scale / 2;
				g.drawOval(x, y, 2, 2);
			}
		}
	}
	
	public void clean() {

	}
	
}
