package jat.core.plot.plot;

import jat.core.plot.plot.canvas.Plot2DCanvas;
import jat.core.plot.plot.canvas.Plot3DCanvas;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;


/**
 * BSD License
 * 
 * @author Yann RICHET
 */
public class FrameView extends JFrame {

	private static final long serialVersionUID = 1L;

	public FrameView(Plot2DCanvas... canvas) {
		JPanel panel = new JPanel();
		for (int i = 0; i < canvas.length; i++)
			panel.add(new Plot2DPanel(canvas[i]));
		setContentPane(panel);
		pack();
		setSize(600,600);
		setVisible(true);
	}

	public FrameView(Plot3DCanvas... canvas) {
		JPanel panel = new JPanel();
		for (int i = 0; i < canvas.length; i++)
			panel.add(new Plot3DPanel(canvas[i]));
		setContentPane(panel);
		pack();
		setSize(600,600);
		setVisible(true);
	}

	public FrameView(String title, JComponent panel) {
		super(title);
		setContentPane(panel);
		pack();
		setSize(600,600);
		setVisible(true);
	}

	public FrameView(JComponent... panels) {
		JPanel panel = new JPanel();
		for (int i = 0; i < panels.length; i++)
			panel.add(panels[i]);
		setContentPane(panel);
		pack();
		setSize(600,600);
		setVisible(true);
	}

	public FrameView(JPanel panel) {
		setContentPane(panel);
		pack();
		setSize(600,600);
		setVisible(true);
		// Exit when window is closed! Not default behavior
		this.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}

}