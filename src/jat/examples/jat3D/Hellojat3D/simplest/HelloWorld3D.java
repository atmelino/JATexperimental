package jat.examples.jat3D.Hellojat3D.simplest;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Transform3D;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.universe.SimpleUniverse;

public class HelloWorld3D extends Applet {

	private static final long serialVersionUID = 4610808184465386022L;

	public HelloWorld3D() {
		setLayout(new BorderLayout());
		GraphicsConfiguration config = SimpleUniverse
				.getPreferredConfiguration();

		Canvas3D canvas3D = new Canvas3D(config);
		add("Center", canvas3D);

		BranchGroup scene = new BranchGroup();
		scene.addChild(new ColorCube(0.4));

		SimpleUniverse simpleU = new SimpleUniverse(canvas3D);

		Transform3D lookAt = new Transform3D();

		lookAt.lookAt(new Point3d(2, 2, 1), new Point3d(0, 0, 0), new Vector3d(
				0, 0, 1.0));
		lookAt.invert();
		simpleU.getViewingPlatform().getViewPlatformTransform()
				.setTransform(lookAt);
		simpleU.addBranchGraph(scene);

//		MessageFrame mf = new MessageFrame();
//		mf.setVisible(true);
//
//		MessageConsole mc = new MessageConsole(mf.textArea);
//		mc.redirectOut();
//		mc.redirectErr(Color.RED, null);
//		mc.setMessageLines(100);
	}

//	public static void main(String[] args) {
//		// this code is executed when this class is run as an application
//		System.out.println("HelloWorld3D started in main");
//
//		// Frame frame =
//		new MainFrame(new HelloWorld3D(), 256, 256);
//
//		System.out.println("redirected output");
//
//	}
}
