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

package jat.jat3D;

import jat.core.util.jatMessages;
import jat.coreNOSA.math.MatrixVector.data.VectorN;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

/**
 * Base class for 3D objects. Planet, moons, and spacecraft extend this class.
 * To be extended by specific object
 * 
 * @author Tobias Berthold
 */

// abstract public class body3D extends TransformGroup
public class Body3D extends TransformGroup {
	Vector3f Vf = new Vector3f();
	Vector3d Vd = new Vector3d();
	Vector3d VRot = new Vector3d();
	Transform3D Trans = new Transform3D();
	protected double scale = 1.0; // scale factor for 3D objects
	protected static String Lightwave_path;
	protected static String Wavefront_path;
	protected static String ThreeDStudio_path;
	jatMessages messages;

	public Body3D() {
//		PathUtil p = new PathUtil();
		// String fs = f.fs;
		// images_path = p.root_path + "data/jat3D/images_hires/";
		// images_path = f.root_path + "data" + fs + "jat3D" + fs +
		// "images_hires" + fs;
		// Wavefront_path = f.root_path + "data" + fs + "jat3D" + fs +
		// "Wavefront" + fs;
		// Lightwave_path = f.root_path + "data" + fs + "jat3D" + fs +
		// "Lightwave" + fs;
		// ThreeDStudio_path = f.root_path + "data" + fs + "jat3D" + fs +
		// "3DStudio" + fs;
		// System.out.println(images_path);
		setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
	}

	// Methods to be implemented in subclasses
	// abstract public void add();

	public void set_scale(double scale) {
		getTransform(Trans);
		Trans.setScale(scale);
		setTransform(Trans);
	}

	/**
	 * Set the position of the body in km
	 * 
	 * @param x
	 *            x position
	 * @param y
	 *            y position
	 * @param z
	 *            z position
	 */
	public void set_position(double x, double y, double z) {
		getTransform(Trans);
		Vd.x = x;
		Vd.y = y;
		Vd.z = z;
		Trans.setTranslation(Vd);
		Trans.setScale(scale);
		setTransform(Trans);
	}

	/**
	 * Set new body position.
	 * 
	 * @param rv
	 *            new position
	 */
	public void set_position(double[] rv) {
		getTransform(Trans);
		Vd.x = rv[0];
		Vd.y = rv[1];
		Vd.z = rv[2];
		Trans.setTranslation(Vd);
		Trans.setScale(scale);
		setTransform(Trans);
	}

	/**
	 * Set new body position.
	 * 
	 * @param r
	 *            new position
	 */
	public void set_position(VectorN r) {
		getTransform(Trans);
		Vd.x = r.x[0];
		Vd.y = r.x[1];
		Vd.z = r.x[2];
		Trans.setTranslation(Vd);
		Trans.setScale(scale);
		setTransform(Trans);
	}

	public void set_position(Point3d r) {
		getTransform(Trans);
		Vd.x = r.x;
		Vd.y = r.y;
		Vd.z = r.z;
		Trans.setTranslation(Vd);
		Trans.setScale(scale);
		setTransform(Trans);
	}

	public void set_position(Vector3f r) {
		getTransform(Trans);
		Vd.x = r.x;
		Vd.y = r.y;
		Vd.z = r.z;
		Trans.setTranslation(Vd);
		Trans.setScale(scale);
		setTransform(Trans);
	}

	public void set_position(Vector3d r) {
		getTransform(Trans);
		Vd.x = r.x;
		Vd.y = r.y;
		Vd.z = r.z;
		Trans.setTranslation(Vd);
		Trans.setScale(scale);
		setTransform(Trans);
	}

	/**
	 * Set_body attitude.using quaternion
	 * 
	 * @param quatObject
	 *            quaternion
	 */
	public void set_attitude(Transform3D quatObject) {
		setTransform(quatObject);
	}

	/**
	 * Set body attitude without changing position or scale using Euler angles
	 * 
	 * @param alpha
	 *            x angle
	 * @param beta
	 *            y angle
	 * @param gamma
	 *            z angle
	 */
	public void set_attitude(double alpha, double beta, double gamma) {
		getTransform(Trans);
		Trans.get(Vf);
		VRot.x = alpha;
		VRot.y = beta;
		VRot.z = gamma;
		Trans.setEuler(VRot);
		Trans.setTranslation(Vf);
		Trans.setScale(scale);
		setTransform(Trans);
	}

	/**
	 * Set body position and attitude
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param alpha
	 * @param beta
	 * @param gamma
	 */
	public void set_pos_attitude(double x, double y, double z, double alpha, double beta, double gamma) {
		getTransform(Trans);
		Trans.get(Vf);
		VRot.x = alpha;
		VRot.y = beta;
		VRot.z = gamma;
		Trans.setEuler(VRot);
		Vd.x = x;
		Vd.y = y;
		Vd.z = z;
		Trans.setTranslation(Vd);
		Trans.setScale(scale);
		setTransform(Trans);
	}

}
