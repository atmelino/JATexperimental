package jat.core.plot.plot.plotObjects;

import jat.core.plot.plot.render.AbstractDrawer;

/**
 * BSD License
 * 
 * @author Yann RICHET
 */
public interface Editable {
	public double[] isSelected(int[] screenCoord, AbstractDrawer draw);

	public void edit(Object editParent);

	public void editnote(AbstractDrawer draw);

}
