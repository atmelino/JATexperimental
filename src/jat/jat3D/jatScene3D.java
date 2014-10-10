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

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Node;
import javax.media.j3d.SceneGraphObject;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;

/**
 * The class that holds all of the JAT scene elements. Allows dynamic addition
 * and removal of elements, as well as scaling.
 * 
 * @author Tobias Berthold
 * 
 * 
 */
public class jatScene3D extends TransformGroup {
	private BranchGroup jatBranchGroup;
	public Transform3D InitialRotation;
	public Vector3f InitialTranslation;
	jatMessages messages;

	public jatScene3D() {
		// super();
		InitialRotation = new Transform3D();
		InitialTranslation = new Vector3f();
		setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
		setCapability(TransformGroup.ALLOW_CHILDREN_READ);
		setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
		jatBranchGroup = new BranchGroup();
		jatBranchGroup.setCapability(BranchGroup.ALLOW_DETACH);
		jatBranchGroup.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		jatBranchGroup.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		jatBranchGroup.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		jatBranchGroup.setUserData("jatBranchGroup");
		addChild(jatBranchGroup);
	}

	public jatScene3D(jatMessages messages) {
		this();
		this.messages = messages;

	}

	/**
	 * Add an element to the scene
	 * 
	 * @param b
	 *            body to add
	 * @param name
	 *            name of added body; is used to identify the body if and when
	 *            removed
	 */
	public void add(Body3D b, String name) {
		if(messages!=null)
		{
			messages.addln("[jatScene3D add]");
		}
		BranchGroup bg = new BranchGroup();
		bg.setUserData(name);
		bg.setCapability(BranchGroup.ALLOW_DETACH);
		bg.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		bg.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		bg.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		bg.addChild(b);
		jatBranchGroup.addChild(bg);
	}

	public void add(Body3D b, Vector3f position, String name) {

		b.set_position(position);
		BranchGroup bg = new BranchGroup();
		bg.setUserData(name);
		bg.setCapability(BranchGroup.ALLOW_DETACH);
		bg.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		bg.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		bg.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		bg.addChild(b);
		jatBranchGroup.addChild(bg);
	}

	public void add(Node n, String name) {
		BranchGroup bg = new BranchGroup();
		bg.setUserData(name);
		bg.setCapability(BranchGroup.ALLOW_DETACH);
		bg.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		bg.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		bg.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		bg.addChild(n);
		jatBranchGroup.addChild(bg);
	}

	/**
	 * Remove an element from the scene
	 * 
	 * @param name
	 *            Name of element to be removed
	 */
	public void remove(String name) {
		try {
			java.util.Enumeration enum1 = jatBranchGroup.getAllChildren();
			int index = 0;

			while (enum1.hasMoreElements() != false) {
				SceneGraphObject sgObject = (SceneGraphObject) enum1.nextElement();
				Object userData = sgObject.getUserData();

				if (userData instanceof String && ((String) userData).compareTo(name) == 0) {
					System.out.println("Removing: " + sgObject.getUserData());
					// jatBranchGroup.detach();
					jatBranchGroup.removeChild(index);
				}
				index++;
			}
		} catch (Exception e) {
			// the scenegraph may not have yet been synchronized...
		}
	}

}
