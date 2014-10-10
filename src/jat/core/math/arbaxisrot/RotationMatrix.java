/*   Copyright 2011 Glenn Murray
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package jat.core.math.arbaxisrot;


/**
 * <p>A class to do 3D rotations of a point about a line. </p>
 *
 * <p> This uses simplified formulas obtained by normalizing the direction
 * vector for the line.  The matrix obtained is identical to that in {@link
 * RotationMatrixFull}, but this class may offer better performance.
 *
 * @author Glenn Murray
 */
public class RotationMatrix extends AbstractRotationMatrix {

    // Static initialization---------------------------------------------
    /** For debugging. */
    private static final boolean LOG = false;

    // Instance variables------------------------------------------------

    // Constructors------------------------------------------------------

    /**
     * Default constructor.
     */
    public RotationMatrix() {}


    /**
     * Build a rotation matrix for rotations about the line through (a, b, c)
     * parallel to &lt u, v, w &gt by the angle theta.
     *
     * @param a x-coordinate of a point on the line of rotation.
     * @param b y-coordinate of a point on the line of rotation.
     * @param c z-coordinate of a point on the line of rotation.
     * @param uUn x-coordinate of the line's direction vector (unnormalized).
     * @param vUn y-coordinate of the line's direction vector (unnormalized).
     * @param wUn z-coordinate of the line's direction vector (unnormalized).
     * @param theta The angle of rotation, in radians.
     */
    public RotationMatrix(double a,
                          double b,
                          double c,
                          double uUn,
                          double vUn,
                          double wUn,
                          double theta) {
        double l = 0;
        if ( (l = longEnough(uUn, vUn, wUn)) < 0) {
            System.out.println("RotationMatrix: direction vector too short!");
            return;             // Don't bother.
        }

        this.a = a;
        this.b = b;
        this.c = c;

        // In this instance we normalize the direction vector.
        u = uUn/l;
        v = vUn/l;
        w = wUn/l;

        // Set some intermediate values.
        u2 = u*u;
        v2 = v*v;
        w2 = w*w;
        cosT = Math.cos(theta);
        oneMinusCosT = 1-cosT;
        sinT = Math.sin(theta);

         // Build the matrix entries element by element.
        m11 = u2 + (v2 + w2) * cosT;
        m12 = u*v * oneMinusCosT - w*sinT;
        m13 = u*w * oneMinusCosT + v*sinT;
        m14 = (a*(v2 + w2) - u*(b*v + c*w))*oneMinusCosT
            + (b*w - c*v)*sinT;

        m21 = u*v * oneMinusCosT + w*sinT;
        m22 = v2 + (u2 + w2) * cosT;
        m23 = v*w * oneMinusCosT - u*sinT;
        m24 = (b*(u2 + w2) - v*(a*u + c*w))*oneMinusCosT
            + (c*u - a*w)*sinT;

        m31 = u*w * oneMinusCosT - v*sinT;
        m32 = v*w * oneMinusCosT + u*sinT;
        m33 = w2 + (u2 + v2) * cosT;
        m34 = (c*(u2 + v2) - w*(a*u + b*v))*oneMinusCosT
            + (a*v - b*u)*sinT;

        if(LOG) logMatrix();
    }


    // Methods-----------------------------------------------------------

    /**
     * Compute the rotated point from the formula given in the paper, as opposed
     * to multiplying this matrix by the given point.  Theoretically this should
     * give the same answer as {@link #timesXYZ(double[])}.  For repeated
     * calculations this will be slower than using {@link
     * AbstractRotationMatrix#timesXYZ(double[])} because in effect it repeats
     * the calculations done in the constructor.
     *
     *
     * @param a x-coordinate of a point on the line of rotation.
     * @param b y-coordinate of a point on the line of rotation.
     * @param c z-coordinate of a point on the line of rotation.
     * @param u x-coordinate of the line's direction vector.
     * @param v y-coordinate of the line's direction vector.
     * @param w z-coordinate of the line's direction vector.
     * @param x The point's x-coordinate.
     * @param y The point's y-coordinate.
     * @param z The point's z-coordinate.
     * @param theta The angle of rotation, in radians.
     * @return The product, in a vector <#, #, #>, representing the
     * rotated point.
     */
    @Override
    public double[] rotPointFromFormula(double a, double b, double c,
                                        double u, double v, double w,
                                        double x, double y, double z,
                                        double theta) {
        // In this instance we normalize the direction vector.
        double l = 0;
        if ( (l = longEnough(u, v, w)) < 0) {
            System.out.println("RotationMatrixFull direction vector too short");
            return null;             // Don't bother.
        }
        // Normalize the direction vector.
        u = u/l;
        v = v/l;
        w = w/l;
        // Set some intermediate values.
        double u2 = u*u;
        double v2 = v*v;
        double w2 = w*w;
        double cosT = Math.cos(theta);
        double oneMinusCosT = 1 - cosT;
        double sinT = Math.sin(theta);

        // Use the formula in the paper.
        double[] p = new double[3];
        p[0] = (a*(v2 + w2) - u*(b*v + c*w - u*x - v*y - w*z)) * oneMinusCosT
            + x*cosT
            + (-c*v + b*w - w*y + v*z)*sinT;

        p[1] = (b*(u2 + w2) - v*(a*u + c*w - u*x - v*y - w*z)) * oneMinusCosT
            + y*cosT
            + (c*u - a*w + w*x - u*z)*sinT;

        p[2] = (c*(u2 + v2) - w*(a*u + b*v - u*x - v*y - w*z)) * oneMinusCosT
            + z*cosT
            + (-b*u + a*v - v*x + u*y)*sinT;

        return p;
    }


    // Inner classes-----------------------------------------------------
}//end class

