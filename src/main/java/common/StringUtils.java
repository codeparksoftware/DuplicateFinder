package common;

import java.io.File;
import java.text.DecimalFormat;

public class StringUtils {
	public static String getFileExtension(File file) {
		String name = file.getName();
		try {
			return name.substring(name.lastIndexOf(".") + 1);
		} catch (Exception e) {
			return "";
		}
	}

	public static String getFileLength(long val) {
		if (val == 0)
			return "0 Bytes";
		if (val < 1024)
			return String.valueOf(val) + "  bytes";
		if (val >= 1024 && val < 1024 * 1024)
			return String.format("%.2f", (val / 1024f)) + "  KB";
		if (val >= Math.pow(1024, 2) && val < Math.pow(1024, 3))
			return new DecimalFormat("#.##").format(val / (float) (1024 * 1024)) + "  MB";
		if (val >= (long) Math.pow(1024, 3) && val < Math.pow(1024, 4))
			return new DecimalFormat("#.##").format(val / (float) Math.pow(1024, 3)) + "  GB";
		if (val >= (long) Math.pow(1024, 4))
			return new DecimalFormat("#.##").format(val / (double) Math.pow(1024, 4)) + "  TB";
		return String.valueOf(val) + "  bytes";

	}
}
