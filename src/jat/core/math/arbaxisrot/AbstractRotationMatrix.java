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

import java.text.DecimalFormat;

/**
 * A class to do 3D rotations of a point about a line.
 *
 * @author Glenn Murray
 */
public abstract class AbstractRotationMatrix {
	
    // Static initialization---------------------------------------------

    /** How close a double must be to a double to be "equal". */
    public static final double TOLERANCE = 1E-9;


    // Instance variables------------------------------------------------

    /** The rotation matrix.  This is a 4x4 matrix. */
    protected double[][] matrix;

    // The parameters for the rotation.
    /** x-coordinate of a point on the line of rotation. */
    protected double a;

    /** y-coordinate of a point on the line of rotation. */
    protected double b;

    /** z-coordinate of a point on the line of rotation. */
    protected double c;

    /** x-coordinate of the line's direction vector. */
    protected double u;

    /** y-coordinate of the line's direction vector. */
    protected double v;

    /** z-coordinate of the line's direction vector. */
    protected double w;

    // Some intermediate values...
    /** An intermediate value used in computations (u^2). */
    protected double u2;     
    protected double v2;     
    protected double w2;     
    protected double cosT;   
    protected double oneMinusCosT;   
    protected double sinT;   
    
    /** The 1,1 entry in the matrix. */
    protected double m11;
    protected double m12; 
    protected double m13; 
    protected double m14; 
    protected double m21; 
    protected double m22; 
    protected double m23; 
    protected double m24; 
    protected double m31;
    protected double m32;
    protected double m33;
    protected double m34;


    // Constructors------------------------------------------------------

    // Methods-----------------------------------------------------------

    /**
     * Multiply this matrix times the given coordinates (x, y, z, 1),
     * representing a point P(x, y, z)..
     *
     * @param x The point's x-coordinate.
     * @param y The point's y-coordinate.
     * @param z The point's z-coordinate.
     * @return The product, in a vector <#, #, #, 1>, representing the
     * rotated point.
     */
    public double[] timesXYZ(double x, double y, double z) {
        double[] p = new double[4];
        p[0] = m11*x + m12*y + m13*z + m14;
        p[1] = m21*x + m22*y + m23*z + m24;
        p[2] = m31*x + m32*y + m33*z + m34;
        p[3] = 1;

        return p;
    }


    /**
     * Multiply this matrix times the given coordinates (x, y, z, 1),
     * representing a point P(x, y, z)..
     *
     * @param point The point as double[] {x, y, z}.
     * @return The product, in a vector <#, #, #, 1>, representing the
     * rotated point.
     */
    public double[] timesXYZ(double[] point) {
        return timesXYZ(point[0], point[1], point[2]);
    }


    /**
     * Compute the rotated point from the formula given in the paper,
     * as opposed to multiplying this matrix by the given point.
     * Theoretically this should give the same answer as {@link
     * #timesXYZ(double[])}.  For repeated calculations this will be
     * slower than using {@link #timesXYZ(double[])} because in effect
     * it repeats the calculations done in the constructor.
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
    public abstract double[] rotPointFromFormula(double a, double b, double c,
                                                 double u, double v, double w,
                                                 double x, double y, double z,
                                                 double theta);


    /**
     * <p>Compute the rotated point from the formula given in the paper,
     * as opposed to multiplying this matrix by the given point.</p>
     * 
     * <p>This delegates to {@link #rotPointFromFormula(double, double, double,
     * double, double, double, double, double, double, double)}. </p>.
     *
     * @param rInfo The information for the rotation as an array
     * [a,b,c,u,v,w,x,y,z,theta].
     * @return The product, in a vector <#, #, #>, representing the
     * rotated point.
     */
    public double[] rotPointFromFormula(double[] rInfo) {
        return rotPointFromFormula(rInfo[0], rInfo[1], rInfo[2], 
                                   rInfo[3], rInfo[4], rInfo[5], 
                                   rInfo[6], rInfo[7], rInfo[8], rInfo[9]);
    }


    /**
     * Check whether a vector's length is less than {@link #TOLERANCE}.
     *
     * @param u The vector's x-coordinate.
     * @param v The vector's y-coordinate.
     * @param w The vector's z-coordinate.
     * @return length = Math.sqrt(u^2 + v^2 + w^2) if it is greater than
     * {@link #TOLERANCE}, or -1 if not.
     */
    public double longEnough(double u, double v, double w) {
        double l = Math.sqrt(u*u + v*v + w*w);
        if (l > TOLERANCE) {
            return l;
        } else {
            return -1;
        }
    }


    /**
     * Find the distance from the line representing the axis of
     * rotation of this matrix and a given point (x, y, z).  This is
     * useful for testing.  We use the ff. formula for the distance
     * (cf. "Point-Line distance in MathWorld).
     *
     * <pre>
     *                |(u, v, w) x (a-x, b-y, c-z)|
     *                -----------------------------
     *                        |(u, v, w)|
     * </pre>
     *
     * This method assumes that constructors in derived classes check that the
     * length of the direction vector <u,v,w> is nonzero.
     *
     * @param x The point's x-coordinate.
     * @param y The point's y-coordinate.
     * @param z The point's z-coordinate.
     * @return The distance from the point (x, y, z) to the axis of
     * rotation ( a line through (a, b, c) with direction <u, v, w>).
     */
    public double distanceFromAxis(double x, double y, double z) {
        // Some intermediate values.
        double n1 = v*(c - z) - w*(b - y);
        double n2 = -(u*(c - z) - w*(a - x));
        double n3 = u*(b - y) - v*(a - x);
        double l = Math.sqrt(u*u + v*v + w*w);

        return Math.sqrt(n1*n1 + n2*n2 + n3*n3)/l;
    }
    

    /**
     * Get the matrix.
     * @return The matrix as a 4x4 double[][].
     */
    public double[][] getMatrix() {
        if (matrix==null) {
            matrix = new double[][] {{m11, m12, m13, m14}, 
                                     {m21, m22, m23, m24},
                                     {m31, m32, m33, m34},
                                     {0,   0,   0,   1}}; 
        }
        return matrix;
    }

    /**
     * Print out the matrix.
     */
    public void logMatrix() {
        DecimalFormat df = new DecimalFormat("#.###");
        System.out.println();
        System.out.println("\t"+df.format(m11)+"\t"+df.format(m12)
                           +"\t"+df.format(m13)+"\t"+df.format(m14));
        System.out.println("\t"+df.format(m21)+"\t"+df.format(m22)
                           +"\t"+df.format(m23)+"\t"+df.format(m24));
        System.out.println("\t"+df.format(m31)+"\t"+df.format(m32)
                           +"\t"+df.format(m33)+"\t"+df.format(m34));
        System.out.println("\t0\t0\t0\t1");
        System.out.println();
    }


    // Inner classes-----------------------------------------------------
}//end class
 
