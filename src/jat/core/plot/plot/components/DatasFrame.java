package jat.core.plot.plot.components;

import jat.core.plot.plot.MatrixTablePanel;
import jat.core.plot.plot.canvas.PlotCanvas;
import jat.core.plot.plot.plots.Plot;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 * BSD License
 * 
 * @author Yann RICHET
 */

public class DatasFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private PlotCanvas plotCanvas;

	private LegendPanel legend;

	public JTabbedPane panels;

	public DatasFrame(PlotCanvas p, LegendPanel l) {
		super("Data");
		plotCanvas = p;
		legend = l;
		JPanel panel = new JPanel();
		panels = new JTabbedPane();
		
		for (Plot plot: plotCanvas.getPlots()) 
			panels.add(new DataPanel(/*this, */plot), plot.getName());
		

		panel.add(panels);
		setContentPane(panel);
		pack();
		setVisible(true);
	}

	public class DataPanel extends JPanel {

		private static final long serialVersionUID = 1L;

		MatrixTablePanel XY;

		JCheckBox visible;

		JButton color;

		JPanel plottoolspanel;

		Plot plot;

		DatasFrame dframe;

		public DataPanel(/*DatasFrame _dframe,*/Plot _plot) {
			plot = _plot;
			//dframe = _dframe;
			visible = new JCheckBox("visible");
			visible.setSelected(plot.getVisible());
			color = new JButton();
			color.setBackground(plot.getColor());
			XY = new MatrixTablePanel( plotCanvas.reverseMapedData( plot.getData()));

			visible.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					if (visible.isSelected())
						plot.setVisible(true);
					else
						plot.setVisible(false);
					/*dframe.*/plotCanvas.repaint();
				}
			});
			color.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Color c = JColorChooser.showDialog(plotCanvas, "Choose plot color", plot.getColor());
					color.setBackground(c);
					plot.setColor(c);
					legend.updateLegends();
					/*dframe.*/plotCanvas.linkedLegendPanel.repaint();
					/*dframe.*/plotCanvas.repaint();
				}
			});

			this.setLayout(new BorderLayout());
			plottoolspanel = new JPanel();
			plottoolspanel.add(visible);
			plottoolspanel.add(color);
			this.add(plottoolspanel, BorderLayout.NORTH);
			this.add(XY, BorderLayout.CENTER);
		}
	}
}