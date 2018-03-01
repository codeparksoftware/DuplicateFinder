package jTable;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

public class EvenOddRenderer extends JPanel implements TableCellRenderer {

	public EvenOddRenderer() {
		super();
		setOpaque(true);
		setLayout(new BorderLayout());
		add(label, BorderLayout.CENTER);
		label.setHorizontalAlignment(SwingConstants.LEFT);

	}

	public static Color getContrastColor(Color color) {
		double y = (299 * color.getRed() + 587 * color.getGreen() + 114 * color.getBlue()) / 1000;
		return y >= 128 ? Color.black : Color.white;
	}

	public static final DefaultTableCellRenderer render = new DefaultTableCellRenderer();
	private static final Color COLOR_1 = Color.ORANGE;
	private static final Color COLOR_2 = Color.white;
	private static final float SIDE = 50;
	private GradientPaint gradientPaint = new GradientPaint(0, 0, COLOR_1, SIDE, SIDE, COLOR_2, false);
	private JLabel label = new JLabel();

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		Component renderer = render.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		((JLabel) renderer).setOpaque(true);
		Color foreground = Color.BLACK, background = Color.white;
		float[] hsbValues = new float[3];
		Color.RGBtoHSB(234, 241, 241, hsbValues);
		long tmp = 0;
		if (isSelected) {
			foreground = Color.WHITE;
			background = Color.BLUE;
		} else {

			foreground = getContrastColor((Color) table.getModel().getValueAt(row, 7));
			background = (Color) table.getModel().getValueAt(row, 7);
		}

		renderer.setForeground(foreground);
		renderer.setBackground(background);
		return renderer;

	}
}
