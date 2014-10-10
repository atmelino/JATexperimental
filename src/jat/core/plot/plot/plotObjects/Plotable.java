package jat.core.plot.plot.plotObjects;

import jat.core.plot.plot.render.AbstractDrawer;

import java.awt.Color;


/**
 * BSD License
 * 
 * @author Yann RICHET
 */
public interface Plotable {
	public void plot(AbstractDrawer draw);

	public void setVisible(boolean v);

	public boolean getVisible();

	public void setColor(Color c);

	public Color getColor();

}