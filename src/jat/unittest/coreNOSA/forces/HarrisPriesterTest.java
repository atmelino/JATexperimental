package jat.unittest.coreNOSA.forces;

import jat.coreNOSA.forces.HarrisPriester;
import jat.coreNOSA.spacecraft.Spacecraft;

import java.io.IOException;

public class HarrisPriesterTest extends ForceModelTest {

  public static void main(String[] args) {
    junit.textui.TestRunner.run(HarrisPriesterTest.class);
  }

  /*
   * Test method for 'jat.forces.HarrisPriester.acceleration(Time, BodyRef, Spacecraft)'
   */
  public void testAccelerationTimeBodyRefSpacecraft() throws IOException {
    Spacecraft sc = new Spacecraft();
    sc.set_area(20);
    sc.set_mass(1000);
    sc.set_cd(1.2);
    HarrisPriester force = new HarrisPriester(sc.cd(), sc.area(), sc.mass());
    
    testForceModelAcceleration(sc, force, "harris_priester.txt", 
        "Harris Priester atmosphere drag");
  }
}
