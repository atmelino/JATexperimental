package jat.core.plot.plot;

import jat.core.plot.io.ClipBoardPrintable;
import jat.core.plot.io.FilePrintable;
import jat.core.plot.io.StringPrintable;
import jat.core.plot.plot.components.DataToolBar;

import java.awt.BorderLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;

import javax.swing.JPanel;
import javax.swing.JScrollPane;


/**
 * BSD License
 * 
 * @author Yann RICHET
 */

public abstract class DataPanel extends JPanel implements ComponentListener, FilePrintable, ClipBoardPrintable, StringPrintable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1677970683091143235L;

	protected DataToolBar toolBar;

	protected JScrollPane scrollPane;

	public static int[] dimension = new int[] { 400, 400 };

	public DataPanel() {
		setLayout(new BorderLayout());
		initToolBar();
		init();
	}

	protected void initToolBar() {
		toolBar = new DataToolBar(this);
		add(toolBar, BorderLayout.NORTH);
		toolBar.setFloatable(false);
	}

	protected void initSize() {
		scrollPane.setSize(this.getSize());
		// scrollPane.setPreferredSize(this.getSize());
	}

	protected void init() {
		// initSize();
		addComponentListener(this);
	}

	public void update() {
		// this.remove(scrollPane);
		toWindow();
		repaint();
	}

	protected abstract void toWindow();

	public abstract void toClipBoard();

	public abstract void toASCIIFile(File file);

	public void componentHidden(ComponentEvent e) {
	}

	public void componentMoved(ComponentEvent e) {
	}

	public void componentResized(ComponentEvent e) {
		/*
		 * dimension = new int[] { (int) (this.getSize().getWidth()), (int)
		 * (this.getSize().getHeight()) };
		 */
		initSize();
	}

	public void componentShown(ComponentEvent e) {
	}

}