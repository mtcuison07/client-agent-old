/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rmj.client.agent;

import org.rmj.appdriver.GRider;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author kalyptus
 */
public class XMClientTest {
    private static GRider grider;
    private static String lsNewId;

    public XMClientTest() {
    }

   @BeforeClass
   public static void setUpClass() throws Exception {
      grider = new GRider("IntegSys");
      grider.logUser("IntegSys", "01050044");
   }

   @AfterClass
   public static void tearDownClass() throws Exception {
   }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

//   @Test
//   public void testNewRecord() {
//      System.out.println("newRecord");
//      XMClient instance = new XMC;
//      boolean expResult = false;
//      boolean result = instance.newRecord();
//      assertEquals(expResult, result);
//      // TODO review the generated test code and remove the default call to fail.
//   }

   /**
    * Test of openRecord method, of class XMClient.
    */
   @Test
   public void testOpenRecord() {
      System.out.println("openRecord"); //Danilo Sabayton
      String fstransNox = "2708000417";
      XMClient instance = new XMClient(grider, "01", false);
      boolean result = instance.openRecord(fstransNox);
      
      System.out.println("First Name  : " + instance.getMaster(3));
      System.out.println("Middle Name : " + instance.getMaster(4));
      System.out.println("Town ID     : " + instance.getMaster(12));
      System.out.println("Town/City   : " + instance.getTown().getMaster(2));

      instance.setMaster(12, "0201");
      System.out.println("Town/City   : " + instance.getTown().getMaster(2));

      instance.setMaster(12, "0510");
      assertEquals("0510", instance.getTown().getMaster(1));
   }

   /**
    * Test of updateRecord method, of class XMClient.
    */
   @Test
   public void testUpdateRecord() {
      System.out.println("updateRecord");
      String fstransNox = "2708000417";
      XMClient instance = new XMClient(grider, "01", false);
      if(!instance.openRecord(fstransNox)){
         assertTrue("Can't open record", false);
         return;
      }
      
      if(!instance.updateRecord()){
         assertTrue("Can't update record", false);
         return;
      }

      instance.setMaster(12, "0510");
      if(!instance.saveRecord()){
         assertTrue("Can't save record", false);
         return;
      }

      if(!instance.openRecord(fstransNox)){
         assertTrue("Can't reopen record", false);
      }
      boolean result = true;

      assertEquals(true, result);
   }

}