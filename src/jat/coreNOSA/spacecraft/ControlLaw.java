/* JAT: Java Astrodynamics Toolkit
 *
 * Copyright (c) 2005 National Aeronautics and Space Administration. All rights reserved.
 *
 * This file is part of JAT. JAT is free software; you can 
 * redistribute it and/or modify it under the terms of the 
 * NASA Open Source Agreement 
 * 
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * NASA Open Source Agreement for more details.
 *
 * You should have received a copy of the NASA Open Source Agreement
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 */
package jat.coreNOSA.spacecraft;


/**
 * A template class intended to be extended to implement various control
 * laws.  This class represents a unity feedback control law.
 * 
 * spacecraft[]-->(sum)-->[Dynamics]->[Control Law]-->acceleration[]
 *                  A -                             |
 *                  |_______________________________|
 * 
 * @author Richard C. Page III
 */
public class ControlLaw {
    
    public ControlLaw(){    }

    public double[] compute_control(double t, double[] x){
        return new double[x.length];
    }
    
    public double[] compute_control(double t, double[] x, double[] xrel){
        return new double[3];
    }
    
}
