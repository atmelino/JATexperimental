package jat.coreNOSA.measurements;



import jat.coreNOSA.algorithm.integrators.LinePrinter;
import jat.coreNOSA.math.Interpolator;
import jat.coreNOSA.math.MatrixVector.data.Matrix;
import jat.coreNOSA.math.MatrixVector.data.VectorN;

import java.io.IOException;
import java.util.Random;



public class generatePolarization{
	
	private static  Matrix GPSDataMat,recDataMat;
	private static VectorN GPSXaxis,recXaxis;
	private static VectorN GPSYaxis,recYaxis;
	private static double GPSlengthy,GPSlengthx;
	private static double recLengthy,recLengthx;
	public static double[] gpsAng;
	public static LinePrinter polarResults;
	
	public generatePolarization(String GPSmodel, String ReceiverModel)
	{
		//Read in the Polarization files associated with the 
		//appropriate antenna models.  Save these off for later
		
		Random generator = new Random();
		try {
			GPSXaxis = new VectorN(Interpolator.readXAxis(GPSmodel));
			GPSYaxis = new VectorN(Interpolator.readYAxis(GPSmodel));
			recXaxis = new VectorN(Interpolator.readXAxis(ReceiverModel));
			recYaxis = new VectorN(Interpolator.readYAxis(ReceiverModel));
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		GPSDataMat = Interpolator.readData(GPSmodel, GPSXaxis.length, GPSYaxis.length);
		recDataMat = Interpolator.readData(ReceiverModel, recXaxis.length, recYaxis.length);
		
        setGPSlengthy(GPSDataMat.getColumnDimension()-1);
        setGPSlengthx(GPSDataMat.getRowDimension()-1);
        
        setRecLengthy(recDataMat.getColumnDimension()-1);
        setRecLengthx(recDataMat.getRowDimension()-1);

		gpsAng = new double[33];
		for(int i = 0;i<33; i++)
			gpsAng[i]= (double)generator.nextInt(360)*(Math.PI/180);
		
	
		String out_directory = "C:\\GOESR\\output\\";
		String alphaFile = "polarResults.txt";
		polarResults = new LinePrinter(out_directory+alphaFile);


	
		
		
	}

	public static double returnPolarization(int prn, double recAz, double recEl, double GPSAz, double GPSEl, double recAng, double yaw)
	{
		double polarization;
		
		//Extract the polarization values from the respective files
		double r1 = get_value(GPSEl,GPSAz,GPSDataMat,GPSXaxis,GPSYaxis);
		double r2 = get_value(recEl,recAz,recDataMat,recXaxis,recYaxis);
	
		//Convert the values to volts
		r1 = Math.pow(10,(r1/20));
		r2 = Math.pow(10,(r2/20));
		

		//Determine angle between the semi-major axes of the polarazation
		//elipses
		double alpha = (gpsAng[prn]+yaw*(Math.PI/180))-recAng;
		
		//pol loss = 10 log{((r1^2+1)(r2^2+1)/((r1*r2+1)^2*cos^2(alpha) + (r1+r2)^2*sin^2(alpha))}
		polarization = 10.0*Math.log10( (r1*r1+1)*(r2*r2+1)/((r1*r2+1)*(r1*r2+1)*(Math.cos(alpha)*Math.cos(alpha)) + (r1+r2)*(r1+r2)*Math.sin(alpha)*Math.sin(alpha)));
		
		
		VectorN results = new VectorN(9);
		results.set(0,prn);
		results.set(1,polarization);
		results.set(2,r1);
		results.set(3,r2);
		results.set(4,GPSEl);
		results.set(5,GPSAz);
		results.set(6,recEl);
		results.set(7,recAz);
		results.set(8,alpha);

	    polarResults.println(results.toString());
		
		
		return polarization;
	}
	
	public static double get_value(double x, double y, Matrix dataMat, VectorN Xaxis, VectorN Yaxis)
    {
		double out;
		int lowerx = 0;
		int lowery = 0;
		int upperx = 0;
		int uppery = 0;
        double lengthy=dataMat.getColumnDimension()-1;
        double lengthx=dataMat.getRowDimension()-1;
		
		if(x<Xaxis.get(0) || y < Yaxis.get(0))     // too low: really should catch exception.. later
		{
			System.out.println("Interpolation value too low");
			System.exit(0);
		}
        if(x>Xaxis.get(Xaxis.getLength()-1)) // too high: really should catch exception..
        {
			System.out.println("generatePolarization:  Interpolation value too High!");
			System.out.println("X is : " + x + " Y is: " + y);
			System.exit(0);	
        }
        if(y>Yaxis.get(Yaxis.getLength()-1))
        {
        	y = y -2;
        }
        
		//Determine the X bracketing Value
		int i;
		for(i=0;i<lengthx;i++)
		{
			//System.out.println("XAxis at i: " + Xaxis.get(i) + " XAxis at i+1: " + Xaxis.get(i+1));
			if(x>=Xaxis.get(i) && x<=Xaxis.get(i+1))
			{
				//System.out.println("x "+x+" i "+i);
				lowerx =i;
				upperx =i+1;
				break;
			}
		}
		
//		Determine the Y bracketing Value
		for(i=0;i<lengthy;i++)
		{
			if(y>=Yaxis.get(i) && y<=Yaxis.get(i+1))
			{
				//System.out.println("x "+x+" i "+i);
				lowery =i;
				uppery =i+1;
				break;
			}
		}
		
		//Isolate the square on the grid where our point lies
		//Number them successivly counter clockwise from the
		//Bottom left
		double y1 = dataMat.get(lowerx,lowery);
		double y2 = dataMat.get(upperx,lowery);
		double y3 = dataMat.get(upperx,uppery);
		double y4 = dataMat.get(lowerx,uppery);
		
		//Define some utility variables
		double t = (x - lowerx)/(upperx-lowerx);
		double u = (y - lowery)/(uppery-lowery);
		
		//Compute the interpolated value
		out = (1-t)*(1-u)*y1 + t*(1-u)*y2 + t*u*y3 + (1-t)*u*y4;
		
		
		return out;
    }

	public static double getGPSlengthx() {
		return GPSlengthx;
	}

	public static void setGPSlengthx(double gPSlengthx) {
		GPSlengthx = gPSlengthx;
	}

	public static double getGPSlengthy() {
		return GPSlengthy;
	}

	public static void setGPSlengthy(double gPSlengthy) {
		GPSlengthy = gPSlengthy;
	}

	public static double getRecLengthy() {
		return recLengthy;
	}

	public static void setRecLengthy(double recLengthy) {
		generatePolarization.recLengthy = recLengthy;
	}

	public static double getRecLengthx() {
		return recLengthx;
	}

	public static void setRecLengthx(double recLengthx) {
		generatePolarization.recLengthx = recLengthx;
	}

}

