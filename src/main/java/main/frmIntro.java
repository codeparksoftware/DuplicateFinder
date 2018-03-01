package main;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;

import filesystem.FileSearcher;
import filesystem.XFile;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import log.Logger;
import patterns.IObservable;
import patterns.IObserver;
import patterns.IPorgressObserver;
import startui.IButtonListener;
import startui.RingProgressIndicator;

public class frmIntro extends JFrame implements IButtonListener, IPorgressObserver {
	boolean loop = true;
	RingProgressIndicator indicator;
	Button fileCount;
	Map<Long, XFile> list;
	private FileSearcher fs;
	public List<Long> tList;
	Object MUTEX = new Object();
	private static final Logger logger = Logger.getLogger(frmIntro.class.getName());
	JFXPanel fxPanel = new JFXPanel();

	public frmIntro() {
		setResizable(false);
		getContentPane().setBackground(java.awt.Color.white);
		setBackground(java.awt.Color.white);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				stopAllThread();

			}
		});

		setSize(710, 400);
		tList = new ArrayList<Long>();
		fxPanel.setBackground(java.awt.Color.white);
		fxPanel.setSize(this.WIDTH, this.HEIGHT);
		getContentPane().add(fxPanel);

		indicator = new RingProgressIndicator();
		indicator.setButtonListener(this);
		fileCount = new Button("");
		fileCount.setVisible(false);
		fileCount.resize(200, 30);
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				initFX(fxPanel);
			}
		});

		// pack();
	}

	private void initFX(JFXPanel fxPanel) {
		// This method is invoked on the JavaFX thread
		Scene scene = createScene();

		fxPanel.setScene(scene);
	}

	private Scene createScene() {

		StackPane main = new StackPane();
		main.setAlignment(Pos.CENTER_LEFT);
		main.setPrefSize(700, 400);
		main.setMargin(indicator, new Insets(20, 0, 14, 60));
		indicator.setLayoutX(10);
		indicator.setLayoutY(190);

		Image image = new Image("/images/main.png");
		BackgroundSize backgroundSize = new BackgroundSize(700, 400, true, true, true, false);
		// // new BackgroundImage(image, repeatX, repeatY, position, size)
		BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);

		Background background = new Background(backgroundImage);
		main.setBackground(background);
		main.getChildren().add(indicator);
		indicator.setProgress(Double.valueOf(0).intValue());

		VBox vb = new VBox();
		HBox hb1 = new HBox();
//		hb1.setAlignment(Pos.BOTTOM_RIGHT);

		Text txtdf = new Text("Duplicate files:");
		Text txtdfa = new Text("");

		hb1.getChildren().add(txtdf);
		hb1.getChildren().add(txtdfa);

		Text fslbl = new Text("Total Duplicate files space:");
		Text fsalbl = new Text("");

		HBox hb2 = new HBox();
		hb2.getChildren().add(fslbl);
		hb2.getChildren().add(fsalbl);

		HBox hb3 = new HBox();
		Text telbl = new Text("Time elapsed:");
		Text tealbl = new Text("");
		hb3.getChildren().addAll(telbl, tealbl);

		HBox hb4 = new HBox();
		Text sdlbl = new Text("Aranan Dizin:");
		Button btnSearch = new Button("All Computer");
		hb4.getChildren().addAll(sdlbl, btnSearch);
		hb4.setMargin(btnSearch,new Insets(0,15,15,6));

		vb.getChildren().addAll(hb1, hb2, hb3, hb4);
		vb.setMargin(hb1, new Insets(0, 0, 24, 0));
		vb.setMargin(hb2, new Insets(0, 0, 24, 0));
		vb.setMargin(hb3, new Insets(0, 0, 24, 0));
		vb.setMargin(hb4, new Insets(0, 0, 24, 0));
		main.setMargin(vb,new Insets(120, 0, 14, 459) );
		main.getChildren().add(vb);
		Scene scene = new Scene(main, this.WIDTH, this.getHeight(), Color.PALETURQUOISE);

		return (scene);
	}

	public void setIndicator() {
		int a = 0;
		while (loop) {
			a++;
			a = a % 100;
			indicator.setProgress(a);

			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Override
	public void button_listener(String val) {
		if (val == "Start") {

			Thread thr = new Thread() {
				public void run() {

					setIndicator();

				}

			};
			loop = true;
			thr.start();
			Thread thr2 = new Thread() {
				public void run() {

					startSearch();
				}

			};

			thr2.start();

		} else if (val == "Stop") {
			stopAllThread();

		}

	}

	private void stopAllThread() {
		loop = false;
		Set<Thread> setOfThread = Thread.getAllStackTraces().keySet();
		// Iterate over set to find current running
		for (Thread thread : setOfThread) {
			for (int j = 0; j < tList.size(); j++) {
				if (thread.getId() == tList.get(j)) {
					thread.interrupt();
				}
			}
		}
	}

	private void startSearch() {

		fs = new FileSearcher("D:\\levazým\\tutu\\");
		fs.add(this);
		fs.run();

	}

	@Override
	public void update(long val) {

		if (val > 0) {
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					fileCount.setVisible(true);
					fileCount.setText(val + " duplicate file(s)");

				}

			});
		}
	}

	@Override
	public void addThreadId(long val) {
		synchronized (MUTEX) {
			if (!tList.contains(val))
				tList.add(val);
		}

	}

	@Override
	public void removeThreadId(long val) {
		synchronized (MUTEX) {

			tList.remove(val);
		}

	}

	public void finish() {
		this.setVisible(false);

		this.dispose();

	}

	@Override
	public void finished(Map<Long, XFile> list, boolean interrupted) {
		loop = false;

		this.list = list;
		if (!interrupted) {
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					frmMain frm = new frmMain(list);
					frm.show();
					frm.setDefaultCloseOperation(EXIT_ON_CLOSE);
					frm.setLocationRelativeTo(null);
					frm.setVisible(true);
					frm.setExtendedState(frm.getExtendedState() | JFrame.MAXIMIZED_BOTH);
					finish();

				}

			});
		}
	}

}
