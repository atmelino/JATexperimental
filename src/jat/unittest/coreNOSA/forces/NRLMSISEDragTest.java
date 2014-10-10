package jat.unittest.coreNOSA.forces;

import jat.coreNOSA.forces.NRLMSISE_Drag;
import jat.coreNOSA.spacecraft.Spacecraft;

import java.io.IOException;

public class NRLMSISEDragTest extends ForceModelTest {

  public static void main(String[] args) {
    junit.textui.TestRunner.run(NRLMSISEDragTest.class);
  }

  /*
   * Test method for 'jat.forces.HarrisPriester.acceleration(Time, BodyRef, Spacecraft)'
   */
  public void testAccelerationTimeBodyRefSpacecraft() throws IOException {
    Spacecraft sc = new Spacecraft();
    sc.set_area(20);
    sc.set_mass(1000);
    sc.set_cd(1.2);
    NRLMSISE_Drag force = new NRLMSISE_Drag(sc.cd(), sc.area(), sc.mass());
    
    testForceModelAcceleration(sc, force, "nrlmsise_drag.txt", 
        "NRLMSISE atmosphere drag");
  }
}
