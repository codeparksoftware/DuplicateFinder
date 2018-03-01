package patterns;

import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ImageFactory {

	static String[] office = { "ini","inf","txt", "docx", "xlsx", "pptx", "rtf", "mdb", "dot", "pps", "accdb", "xps", "pdf", "sxw",
			"oxt", "odt", "ods", "odg", "odp", "odf", "odb", "odx" };
	static String[] archive = { "rar", "zip", "iso", "jar", "gz", "tar", "7z", "cab", "lz", "pkg", "deb", "rpm",
			"vcd", };
	static String[] exe = { "apk", "exe", "dll", "so", "com", "bin", "ocx", "cmd", "bat", "gadget", "osx", "sys", "vb",
			"vbs" };
	static String[] image = { "ai", "jpg", "jpeg", "bmp", "gif", "png", "ico", "tiff", "psd", "svg" };
	static String[] video = { "vob", "dat", "mpg", "mpeg", "vmw", "avi", "divx", "asf", "mov", "flv", "swf", "3gp",
			"mp4", "mkv", "mpeg", "mpeg4", "mpeg1", "mpeg2" };
	static String[] music = { "mp3", "wav", ".wma", "au", "3gp", "aac", "amr", "mp2", "mpa", "cda", "midi" };
	static String[] web = { "py", "pl", "xml", "htm", "html", "php", "aspx", "json", "cer", "css", "js", "jsp", "php",
			"rss" };
	static String musicImg = "/images/mp3.png";
	static String imageImg = "/images/imagefiles.png";
	static String officeImg = "/images/offices.png";
	static String videoImg = "/images/Videos.png";
	static String webImg = "/images/web.png";
	static String exeImg = "/images/exes.png";
	static String rarImg = "/images/raricon.png";
	static String fileImg = "/images/file.png";

	public static ImageIcon getFromResource(String path) {

		ImageIcon img = null;
		try {
			img = new ImageIcon(ImageIO.read(ImageFactory.class.getResource(path)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return img;

	}

	public static ImageIcon Factory(String type) {
		if (Arrays.asList(office).contains(type)) {
			return getFromResource(officeImg);
		} else if (Arrays.asList(music).contains(type)) {
			return getFromResource(musicImg);
		} else if (Arrays.asList(archive).contains(type)) {
			return getFromResource(rarImg);
		} else if (Arrays.asList(web).contains(type)) {
			return getFromResource(webImg);
		} else if (Arrays.asList(image).contains(type)) {
			return getFromResource(imageImg);
		} else if (Arrays.asList(exe).contains(type)) {
			return getFromResource(exeImg);
		} else if (Arrays.asList(video).contains(type)) {
			return getFromResource(videoImg);
		}
		return getFromResource(fileImg);
	}

}
