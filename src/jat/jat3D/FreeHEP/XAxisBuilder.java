package jat.jat3D.FreeHEP;
import javax.media.j3d.Node;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;

/**
 * @author Joy Kyriakopulos (joyk@fnal.gov)
 * @version $Id: XAxisBuilder.java 8584 2006-08-10 23:06:37Z duns $
 */
public class XAxisBuilder extends AxisBuilder
{
	public XAxisBuilder()
	{
	}
	public XAxisBuilder(String label, String[] tickLabels, double[] tickLocations)
	{
		setLabel(label);
		setTickLabels(tickLabels);
		setTickLocations(tickLocations);
	}
	public Node getNode()
	{
		
//		TransformGroup tg = new TransformGroup();
//
//		Transform3D transform1 = new Transform3D();
//		Vector3f vector = new Vector3f(1.5f, .0f, .0f);
//		transform1.setTranslation(vector);
//
//		Transform3D transform2 = new Transform3D();
//		transform2.rotZ(-Math.PI/2);
//		transform1.mul(transform2);
//
//		tg.setTransform(transform1);
//		tg.addChild(super.getNode());
		
		Transform3D t3d = new Transform3D();
		t3d.set(1/scale,new Vector3f(lo,-hi,lo));
		//Transform3D trans = new Transform3D();
		//trans.setTranslation(new Vector3f(0,-1,2));
		//t3d.mul(trans);
		TransformGroup tg = new TransformGroup(t3d);
		tg.addChild(super.getNode());
		return tg;		
	}
}
