package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.table.TableModel;

import Gradient.GradientPanel;
import common.DialogResult;
import common.MapUtils;
import common.MessageBox;
import filesystem.XFile;
import jTable.XJTable;
import log.Level;
import log.Logger;
import patterns.ImageFactory;

public class frmMain extends JFrame {
	private XJTable table;

	public static final String[] columnNames = { "Icon", "File", "Select All", "Size", "Type", "Path", "Hash",
			"Color" };

	private static final Logger logger = Logger.getLogger(frmMain.class.getName());

	public frmMain(Map<Long, XFile> lst) {

		setUI();

		if (lst != null) {
			fillGrid(MapUtils.sortByComparator(lst, false));
		}

		this.pack();

	}

	private void setUI() {
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		URL iconUrl = this.getClass().getResource("/images/duplicate_main_50.png");
		Toolkit tk = this.getToolkit();
		ImageIcon bannericon = new ImageIcon(tk.getImage(iconUrl));
		URL icoForm = this.getClass().getResource("/images/clean16.png");
		// Form
		setIconImage(tk.getImage(icoForm));
		this.setTitle(".::Duplicate Finder&Cleaner::. ");
		setPreferredSize(new Dimension(1024, 768));
		// Form

		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));

		GradientPanel panel_1 = new GradientPanel();
		panel_1.setColorStart(Color.WHITE);
		panel_1.setColorEnd(Color.ORANGE);

		panel.add(panel_1, BorderLayout.NORTH);
		panel_1.setLayout(new BorderLayout(0, 0));
		panel_1.setSize(panel.getWidth(), 50);
		JLabel label = new JLabel();
		panel_1.add(label, BorderLayout.WEST);
		label.setIcon(bannericon);

		JLabel lblNewLabel = new JLabel("       Find & Clean Your Duplicate Files!");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 17));
		panel_1.add(lblNewLabel, BorderLayout.CENTER);

		JButton btnCleanAll = new RoundButton(new ImageIcon(getClass().getResource("/images/trash.png")),
				"/images/trashd.png", "/images/trashg.png");
		btnCleanAll.setBorder(BorderFactory.createEmptyBorder());
		btnCleanAll.setContentAreaFilled(false);
		btnCleanAll.setSize(50, 50);
		btnCleanAll.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				TableModel tm = table.getModel();
				int count = 0;
				for (int i = 0; i < tm.getRowCount(); i++) {

					count++;
				}
				DialogResult result = MessageBox
						.showMessage("The " + count + " selected files will be deleted from your computer.\r\n\r\n"
								+ "Do you confirm the deletion?", "Deletion files");
				if (result == DialogResult.Yes) {
					table.setEnabled(false);
					Worker worker = new Worker();
					worker.execute();
					table.setEnabled(true);
				}

			}
		});
		panel_1.add(btnCleanAll, BorderLayout.EAST);

		JPanel panel_2 = new JPanel();
		panel.add(panel_2, BorderLayout.CENTER);
		panel_2.setLayout(new BorderLayout(0, 0));

		setJTable(panel_2);
	}

	private void setJTable(JPanel panel_2) {
		table = new XJTable();

		table.setModel(new AbstractFileModel(columnNames));
		table.setCheckBOXCol(2);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setRowSelectionAllowed(true);
		table.setFillsViewportHeight(true);
		table.setPreferredScrollableViewportSize(table.getPreferredSize());
		table.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
		table.setFont(new Font(table.getFont().getName(), Font.PLAIN, 11));
		table.getColumnModel().getColumn(0).setMaxWidth(50);
		table.getColumnModel().getColumn(1).setPreferredWidth(150);
		table.getColumnModel().getColumn(1).setMaxWidth(200);

		table.getColumnModel().getColumn(2).setMaxWidth(100);
		table.getColumnModel().getColumn(3).setMaxWidth(100);
		table.getColumnModel().getColumn(4).setMaxWidth(100);
		table.getColumnModel().getColumn(5).setPreferredWidth((int) this.getWidth() / 2);
		// table.getColumnModel().getColumn(4).sizeWidthToFit();
		table.getColumnModel().getColumn(6).setMaxWidth(0);
		table.getColumnModel().getColumn(6).setMinWidth(0);
		table.getColumnModel().getColumn(6).setPreferredWidth(0);
		// table.removeColumn(table.getColumnModel().getColumn(5));
		table.getColumnModel().getColumn(7).setMaxWidth(0);
		table.getColumnModel().getColumn(7).setMinWidth(0);
		table.getColumnModel().getColumn(7).setPreferredWidth(0);
		table.setRowHeight(40);
		// table.setRowHeight(1, 30);
		JScrollPane js = new JScrollPane(table);
		panel_2.add(js);

	}

	private void fillGrid(Map<Long, XFile> lst) {

		Iterator<XFile> iterator = lst.values().iterator();
		int tmp = 0;
		while (iterator.hasNext()) {
			XFile entry = (XFile) iterator.next();
			if (entry.count > 1) {
				entry.img = ImageFactory.Factory(entry.ext);
				if (tmp % 2 == 0) {
					entry.color = new Color(255, 164, 0);
				} else {
					entry.color = new Color(253, 204, 115);
				}
				initializeFile(entry);
				tmp++;
			}
		}
	}

	private void initializeFile(XFile x) {

		AbstractFileModel abs = (AbstractFileModel) table.getModel();
		for (int i = 0; i < x.paths.size(); i++) {

			FileModel fm = new FileModel(x, i);
			abs.addRow(fm);
		}

	}

	public void work() {

		TableModel tm = table.getModel();
		int count = 0;

		for (int i = 0; i < tm.getRowCount(); i++) {

			try {
				if (((boolean) tm.getValueAt(i, 2)) == true) {
			
					Files.deleteIfExists(new File((String) tm.getValueAt(i, 5)).toPath());
					AbstractFileModel abs = (AbstractFileModel) tm;
					abs.DeleteRow(i);
					table.scrollRectToVisible(table.getCellRect(i, 0, true));
					i--;
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.log(Level.Error, "Deletion worker (941) " + e.getMessage());
			}

			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				logger.log(Level.Error, "Deletion worker (942) " + e.getMessage());
			}

		}
	}

	class Worker extends SwingWorker<Void, Void> {

		@Override
		protected Void doInBackground() throws Exception {
			work();
			return null;

		}

		@Override
		protected void done() {
			MessageBox.showMessage("Operation completed.", "Deletion selected files.", 1000);
		}

	}

 

	class RoundButton extends JButton {
		protected Shape shape;
		protected Shape base;

		protected RoundButton(Icon icon, String i2, String i3) {
			super(icon);
			setPressedIcon(new ImageIcon(getClass().getResource(i2)));
			setRolloverIcon(new ImageIcon(getClass().getResource(i3)));
		}

		@Override
		public void updateUI() {
			super.updateUI();
			setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
			setBackground(Color.BLACK);
			setContentAreaFilled(false);
			setFocusPainted(false);
			// setVerticalAlignment(SwingConstants.TOP);
			setAlignmentY(Component.TOP_ALIGNMENT);
			initShape();
		}

		@Override
		public Dimension getPreferredSize() {
			Icon icon = getIcon();
			Insets i = getInsets();
			int iw = Math.max(icon.getIconWidth(), icon.getIconHeight());
			return new Dimension(iw + i.right + i.left, iw + i.top + i.bottom);
		}

		protected void initShape() {
			if (!getBounds().equals(base)) {
				Dimension s = getPreferredSize();
				base = getBounds();
				shape = new Ellipse2D.Double(0, 0, s.width - 1, s.height - 1);
			}
		}

		@Override
		protected void paintBorder(Graphics g) {
			initShape();
			Graphics2D g2 = (Graphics2D) g.create();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setPaint(getBackground());
			// g2.setStroke(new BasicStroke(1f));
			// g2.draw(shape);
			g2.dispose();
		}

		@Override
		public boolean contains(int x, int y) {
			initShape();
			return Objects.nonNull(shape) && shape.contains(x, y);
		}
	}

}
