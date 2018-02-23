/**
 * 
 */
package Gradient;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

/**
 * @author selami
 *
 */
public class GradientPanel extends JPanel {

	private Color colorStart;
	private Color colorEnd;

	@Override
	protected void paintComponent(Graphics g) {

		super.paintComponent(g);

		Graphics2D grp = (Graphics2D) g;
		GradientPaint paint = new GradientPaint((float) this.location().getX(), (float) this.location().getY(),
				colorStart, this.getWidth(), this.getHeight(), colorEnd);
		grp.setPaint(paint);
		grp.fillRect(0, 0, this.getWidth(), this.getHeight());

	}

	public Color getColorStart() {
		return colorStart;
	}

	public void setColorStart(Color colorStart) {
		this.colorStart = colorStart;
	}

	public Color getColorEnd() {
		return colorEnd;
	}

	public void setColorEnd(Color colorEnd) {
		this.colorEnd = colorEnd;
	}

}
