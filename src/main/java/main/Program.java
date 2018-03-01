package main;

import java.awt.event.WindowEvent;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import filesystem.XFile;
import log.Level;
import log.Logger;
import patterns.IObserver;

public class Program {
	private static final Logger logger = Logger.getLogger(Program.class.getName());
	static frmIntro frm = null;

	public static void main(String[] args) {

		try {
			UIManager.setLookAndFeel(new UIFactory().getDefaultUI());
		} catch (ClassNotFoundException e) {
			logger.log(Level.Error, e.getMessage());
		} catch (InstantiationException e) {
			logger.log(Level.Error, e.getMessage());
		} catch (IllegalAccessException e) {
			logger.log(Level.Error, e.getMessage());
		} catch (UnsupportedLookAndFeelException e) {
			logger.log(Level.Error, e.getMessage());
		}

		java.awt.EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {

				frm = new frmIntro();
				frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frm.setLocationRelativeTo(null);
				frm.setVisible(true);
				
			}

		});

	}

}
