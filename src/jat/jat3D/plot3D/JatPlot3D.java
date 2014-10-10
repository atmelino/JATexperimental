/* JAT: Java Astrodynamics Toolkit
 * 
  Copyright 2012 Tobias Berthold

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

// Original Code under LGPL
// http://java.freehep.org/freehep-java3d/license.html

package jat.jat3D.plot3D;

import jat.jat3D.BodyGroup3D;
import jat.jat3D.jatScene3D;
import jat.jat3D.util;
import jat.jat3D.behavior.jat_KeyBehavior;
import jat.jat3D.behavior.jat_MouseRotate;
import jat.jat3D.behavior.jat_MouseZoom;
import jat.jat3D.behavior.jat_Rotate;
import jat.jat3D.behavior.jat_Zoom;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Bounds;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Node;
import javax.media.j3d.Switch;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

public abstract class JatPlot3D extends Canvas3D {
	private static final long serialVersionUID = 160335685072272523L;
	protected boolean init = false;
	protected boolean parallelProjection = false;
	public SimpleUniverse universe;
	private Bounds bounds;
	protected BodyGroup3D bboxgroup;
	public BoundingBox3D bbox;
	public jatScene3D jatScene;
	public int exponent = 0;
	public ViewingPlatform myvp;
	TransformGroup myvpt;
	public jat_MouseZoom mouseZoom;
	public Switch keyBehaviorSwitch;
	public Switch flightSelectorSwitch;
	public jat_MouseRotate mouseRotate;
	public jat_Rotate jat_rotate;
	public Point3f viewingCenter = new Point3f(0, 0, 0);
	public Point3d initialViewingPosition = new Point3d(2, 2, 1);

	protected JatPlot3D() {
		super(SimpleUniverse.getPreferredConfiguration());
	}

	protected void init() {

		universe = new SimpleUniverse(this);

		ViewingPlatform myvp = universe.getViewingPlatform();
		myvpt = myvp.getViewPlatformTransform();

		Node myScene = createScene();
		BranchGroup mouseGroup = defineMouseBehaviour(myScene);
		setupLights(mouseGroup);
		mouseGroup.compile();

		universe.addBranchGraph(mouseGroup);

		// Set initial viewing position and center
		Transform3D lookAt = new Transform3D();
		lookAt.lookAt(initialViewingPosition, new Point3d(viewingCenter), new Vector3d(0, 0, 1.0));
		lookAt.invert();
		myvp.getViewPlatformTransform().setTransform(lookAt);

		if (parallelProjection) {
			setProjectionPolicy(universe, parallelProjection);
		}
		zoomScene(1.f / (float) Math.pow(10, exponent));
		init = true;
	}

	// addNotify is called when the Canvas3D is added to a container
	public void addNotify() {
		if (!init)
			init();
		super.addNotify(); // must call for Java3D to operate properly when
		// overriding
	}

	public boolean getParallelProjection() {
		return parallelProjection;
	}

	public void setParallelProjection(boolean b) {
		if (parallelProjection != b) {
			parallelProjection = b;
			setProjectionPolicy(universe, parallelProjection);
		}
	}

	/**
	 * Override to provide plot content
	 */
	protected abstract Node createScene();

	/**
	 * Override to provide different mouse behaviour
	 */
	protected BranchGroup defineMouseBehaviour(Node scene) {
		BranchGroup bg = new BranchGroup();
		bg.setCapability(BranchGroup.ALLOW_DETACH);
		bg.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		bg.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		bg.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		Bounds bounds = getDefaultBounds();

		TransformGroup objTransform = new TransformGroup();
		objTransform.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		objTransform.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		objTransform.addChild(scene);
		bg.addChild(objTransform);

		jat_rotate = new jat_Rotate(this);
		mouseRotate = new jat_MouseRotate(jat_rotate);
		float x, y, z;
		x = y = z = (bbox.hi + bbox.lo) / 2;
		mouseRotate.setViewingCenter(new Point3f(x, y, z));
		mouseRotate.setSchedulingBounds(bounds);
		bg.addChild(mouseRotate);

		MouseTranslate mouseTranslate = new MouseTranslate();
		mouseTranslate.setTransformGroup(objTransform);
		mouseTranslate.setSchedulingBounds(bounds);
		bg.addChild(mouseTranslate);

		jat_Zoom jat_zoom = new jat_Zoom(this);
		mouseZoom = new jat_MouseZoom(jat_zoom);
		mouseZoom.setSchedulingBounds(bounds);
		bg.addChild(mouseZoom);

		// Switch for default keyboard behavior, add more children for your own
		// keyboard behavior
		keyBehaviorSwitch = new Switch();
		keyBehaviorSwitch.setCapability(Switch.ALLOW_SWITCH_WRITE);

		// Default key behavior
		jat_KeyBehavior keyBehavior = new jat_KeyBehavior(jat_zoom, jat_rotate);
		keyBehavior.setViewingPlatform(myvp);
		x = y = z = (bbox.hi + bbox.lo) / 2;
		keyBehavior.setViewingCenter(new Point3f(x, y, z));
		keyBehavior.setSchedulingBounds(bounds);
		keyBehaviorSwitch.addChild(keyBehavior);
		keyBehaviorSwitch.setWhichChild(0);
		bg.addChild(keyBehaviorSwitch);

		addBehavior();

		return bg;
	}

	/**
	 * Override to add your own mouse and keyboard behavior-or not
	 */

	public void addBehavior() {
	}

	protected void setupLights(BranchGroup root) {
		DirectionalLight lightD = new DirectionalLight();
		lightD.setDirection(new Vector3f(0.0f, -0.7f, -0.7f));
		lightD.setInfluencingBounds(getDefaultBounds());
		root.addChild(lightD);

		// This second light is added for the Surface Plot, so you
		// can see the "under" surface
		DirectionalLight lightD1 = new DirectionalLight();
		lightD1.setDirection(new Vector3f(0.0f, 0.7f, 0.7f));
		lightD1.setInfluencingBounds(getDefaultBounds());
		root.addChild(lightD1);

		AmbientLight lightA = new AmbientLight();
		lightA.setInfluencingBounds(getDefaultBounds());
		root.addChild(lightA);
	}

	/**
	 * Override to set a different initial transformation
	 */
	protected Transform3D createDefaultOrientation() {
		Transform3D trans = new Transform3D();
		trans.setIdentity();
		// trans.rotY(-Math.PI / 4.);
		trans.setTranslation(new Vector3f(0f, 0f, 0.f));
		return trans;
	}

	/**
	 * Set the projection policy for the plot - either perspective or projection
	 */
	protected void setProjectionPolicy(SimpleUniverse universe, boolean parallelProjection) {
		View view = universe.getViewer().getView();
		if (parallelProjection)
			view.setProjectionPolicy(View.PARALLEL_PROJECTION);
		else
			view.setProjectionPolicy(View.PERSPECTIVE_PROJECTION);
	}

	/**
	 * Returns a bounds object that can be used for most behaviours, lighting
	 * models, etc.
	 */
	protected Bounds getDefaultBounds() {
		if (bounds == null) {
			Point3d center = new Point3d(0, 0, 0);
			bounds = new BoundingSphere(center, 10);
		}
		return bounds;
	}

	public void print_vp_t() {
		Transform3D tf = new Transform3D();
		universe.getViewingPlatform().getViewPlatformTransform().getTransform(tf);
		Vector3f vf = new Vector3f();
		tf.get(vf);
		util.print("viewing platform", vf);
	}

	public Vector3f get_vp_t() {
		Transform3D tf = new Transform3D();
		universe.getViewingPlatform().getViewPlatformTransform().getTransform(tf);
		Vector3f vf = new Vector3f();
		tf.get(vf);
		return vf;
	}

	public void zoomScene(float scale) {
		// scale scene to fit inside box
		Transform3D tscale = new Transform3D();
		tscale.set(scale);
		tscale.setTranslation(jatScene.InitialTranslation);
		tscale.mul(jatScene.InitialRotation);
		jatScene.setTransform(tscale);
	}

	public void jat_translate(float x, float y, float z) {
		Transform3D Trans = new Transform3D();
		myvpt.getTransform(Trans);

		Vector3f v = new Vector3f();
		Trans.get(v);
		Point3d p = new Point3d();
		p.x = v.x + x;
		p.y = v.y + y;
		p.z = v.z + z;
		// util.print("p", p);
		Transform3D lookAt = new Transform3D();
		lookAt.lookAt(p, new Point3d(0.0, 0.0, 0.0), new Vector3d(0, 0, 1.0));
		lookAt.invert();
		update_user();
		myvpt.setTransform(lookAt);

	}

	/**
	 * This function is to be overridden by the class that is derived from
	 * JatPlot3D. For example, to generate output in the user interface.
	 * JatPlot3D is the base class, and it does not know about the GUI elements
	 * that a user of it might implement.
	 */
	public void update_user() {
	}

}
