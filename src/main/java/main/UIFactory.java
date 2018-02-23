package main;

import javax.swing.UIManager;

import system.OSDetector;

public class UIFactory {

	public String getDefaultUI() {
		if (OSDetector.isUnix())
			return "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
		else if (OSDetector.isWindows())
			return "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";

		return UIManager.getCrossPlatformLookAndFeelClassName();
	}

}
