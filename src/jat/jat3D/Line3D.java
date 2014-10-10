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

import javax.media.j3d.BranchGroup;
import javax.media.j3d.LineArray;
import javax.media.j3d.Shape3D;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;

public class Line3D extends BranchGroup {

	public Point3f p1, p2;
	static Color3f col1 = new Color3f(Colors.gold);
	static Color3f col2 = new Color3f(Colors.gray);
	LineArray a = new LineArray(2, LineArray.COORDINATES | LineArray.COLOR_3);

	public Line3D() {
		this(new Point3f(0, 0, 0), new Point3f(1, 1, 1));
	}

	public Line3D(Point3f p1, Point3f p2) {
		this(p1,p2,col1,col2);
	}

	public Line3D(float x1, float x2, float x3) {
		this(new Point3f(0, 0, 0), new Point3f(x1, x2, x3),col1,col2);
	}

	public Line3D(Point3f p1, Point3f p2, Color3f col1) {
		this(p1,p2,col1,col1);
	}

	public Line3D(Point3f x1, Point3f x2, Color3f col1, Color3f col2) {
		super();
		a.setCoordinate(0, x1);
		a.setCoordinate(1, x2);
		a.setColor(0, col1);
		a.setColor(1, col2);
		setCapability(BranchGroup.ALLOW_DETACH);
		setUserData("Line");
		addChild(new Shape3D(a));
	}

	// void common_to_all_constructors() {
	// setCapability(BranchGroup.ALLOW_DETACH);
	// setUserData("Line");
	// addChild(new Shape3D(a));
	// }

	public void change(int i) {
		p1 = new Point3f(0, 0, i);
		p2 = new Point3f(1, 1, 1);
		a = new LineArray(2, LineArray.COORDINATES | LineArray.COLOR_3);
		a.setCoordinate(0, p1);
		a.setCoordinate(1, p2);
		a.setColor(0, Colors.gold);
		a.setColor(1, Colors.gray);
		setChild(new Shape3D(a), 0);
	}

}
