package jat.jat3D.FreeHEP;

import javax.media.j3d.Appearance;
import javax.media.j3d.Geometry;
import javax.media.j3d.Material;
import javax.media.j3d.Node;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.Shape3D;
import javax.vecmath.Color3b;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;
import com.sun.j3d.utils.geometry.Stripifier;

/**
 * @author Joy Kyriakopulos (joyk@fnal.gov)
 * @version $Id: SurfaceBuilder.java 8584 2006-08-10 23:06:37Z duns $
 */
public class SurfaceBuilder extends AbstractPlotBuilder {
	private TimeStamp timeStamp = TimeStamp.sharedInstance();

	private Shape3D shape;

	/**
	 * @param data
	 *            Bin Contents
	 */
	public Node buildContent(NormalizedBinned2DData data) {
		shape = createShape();
		shape.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
		shape.setGeometry(buildGeometry(data));

		return shape;
	}

	public void updatePlot(NormalizedBinned2DData data) {
		shape.setGeometry(buildGeometry(data));
	}

	private Geometry buildGeometry(NormalizedBinned2DData data) {
		timeStamp.print("Starting SurfaceBuilder.buildContent()");

		int nXbins = data.xBins();
		int nYbins = data.yBins();
		float xBinWidth = 1.f / nXbins;
		float yBinWidth = 1.f / nYbins;
		float x, y;
		int i, k, l;

		Point3d bcoord[] = new Point3d[((int) nXbins - 1) * ((int) nYbins - 1) * 4];
		Color3b bcolor[] = new Color3b[((int) nXbins - 1) * ((int) nYbins - 1) * 4];

		for (i = 0; i < ((int) nXbins - 1) * ((int) nYbins - 1) * 4; i++) {
			bcoord[i] = new Point3d();
			bcolor[i] = new Color3b();
		}

		// Fill bcoord array with points that compose the surface

		double z1, z2, z3, z4;
		int bcur = 0;
		for (k = 0, x = -.5f; k < (int) nXbins - 1; k++, x += xBinWidth) {
			for (l = 0, y = -.5f; l < (int) nYbins - 1; l++, y += yBinWidth) {

				// Point x,y
				bcoord[bcur].x = x + xBinWidth / 2.f;
				bcoord[bcur].y = y + yBinWidth / 2.f;
				z1 = bcoord[bcur].z = data.zAt(k, l);
				bcolor[bcur] = data.colorAt(k, l);
				bcur++;

				// Next point in y direction
				bcoord[bcur].x = x + xBinWidth / 2.f;
				bcoord[bcur].y = y + 1.5f * xBinWidth;
				z2 = bcoord[bcur].z = data.zAt(k, l + 1);
				bcolor[bcur] = data.colorAt(k, l + 1);
				bcur++;

				// Next point diagonally
				bcoord[bcur].x = x + 1.5f * xBinWidth;
				bcoord[bcur].y = y + 1.5f * yBinWidth;
				z3 = bcoord[bcur].z = data.zAt(k + 1, l + 1);
				bcolor[bcur] = data.colorAt(k + 1, l + 1);
				bcur++;

				// Next point in x direction
				bcoord[bcur].x = x + 1.5f * xBinWidth;
				bcoord[bcur].y = y + yBinWidth / 2.f;
				z4 = bcoord[bcur].z = data.zAt(k + 1, l);
				bcolor[bcur] = data.colorAt(k + 1, l);
				bcur++;

				if (z1 < 0.1 || z2 < 0 || z3 < 0 || z4 < 0) {
					double z12 = Math.max(z1, z2);
					double z34 = Math.max(z3, z4);
					double zmax = Math.max(z12, z34);
					if (z1 < 0)
						bcoord[bcur - 4].z = zmax;
					if (z2 < 0)
						bcoord[bcur - 3].z = zmax;
					if (z3 < 0)
						bcoord[bcur - 2].z = zmax;
					if (z4 < 0)
						bcoord[bcur - 1].z = zmax;
				}
			}
		}
		//System.out.println("bcur " + bcur);

		// System.out.print("debug3:");

		// We make a GeometryInfo object so that normals can be generated
		// for us and geometry stripified for us. J3D documentation
		// says it's best to do these two steps in this order.

		GeometryInfo geom = new GeometryInfo(GeometryInfo.QUAD_ARRAY);

		// geom.setNormals(normals);
		geom.setCoordinates(bcoord);
		geom.setColors(bcolor);
		NormalGenerator ng = new NormalGenerator();
		ng.generateNormals(geom);

		// Make normals conform to our "handedness" of z direction
		// i.e. change their sign
		Vector3f normals[] = geom.getNormals();
		for (i = 0; i < normals.length; ++i) {
			normals[i].x = -normals[i].x;
			normals[i].y = -normals[i].y;
			normals[i].z = -normals[i].z;
		}
		geom.setNormals(normals);

		Stripifier st = new Stripifier();
		st.stripify(geom);
		geom.recomputeIndices();

		System.out.print("Surface geometry done.\n");
		timeStamp.print("finished, now finalizing, point count = " + bcur);
		return geom.getGeometryArray();
	}

	Shape3D createShape() {
		Shape3D surface = new Shape3D();
		timeStamp.print("geometry set");
		surface.setAppearance(createMaterialAppearance());

		return surface;
	}

	private Appearance createMaterialAppearance() {
		Appearance materialAppear = new Appearance();
		PolygonAttributes polyAttrib = new PolygonAttributes();
		polyAttrib.setCullFace(PolygonAttributes.CULL_NONE);
		materialAppear.setPolygonAttributes(polyAttrib);

		Material material = new Material();
		// set diffuse color to red (this color will only be used
		// if lighting disabled - per-vertex color overrides)
		material.setDiffuseColor(new Color3f(1.0f, 0.0f, 0.0f));
		materialAppear.setMaterial(material);

		return materialAppear;
	}
}
