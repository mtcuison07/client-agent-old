/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rmj.client.agent;

import java.util.Date;
import javax.swing.JOptionPane;
import org.rmj.appdriver.constants.EditMode;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.iface.XMRecord;
import org.rmj.client.base.Client;
import org.rmj.integsys.pojo.UnitClient;
import org.rmj.parameters.agent.XMOccupation;
import org.rmj.parameters.agent.XMTownCity;

/**
 *
 * @author kalyptus
 */
public class XMEmployee implements XMRecord {
   public XMEmployee(GRider foGRider, String fsBranchCd, boolean fbWithParent){
      this.poGRider = foGRider;
      if(foGRider != null){
         this.pbWithParnt = fbWithParent;
         this.psBranchCd = fsBranchCd;
         poCtrl = new Client();
         poCtrl.setGRider(foGRider);
         poCtrl.setBranch(psBranchCd);
         poCtrl.setWithParent(fbWithParent);
         pnEditMode = EditMode.UNKNOWN;
      }
   }

   @Override
   public void setMaster(String fsCol, Object foData){
      setMaster(poData.getColumn(fsCol), foData);
   }

   @Override
   public void setMaster(int fnCol, Object foData) {
      if(pnEditMode != EditMode.UNKNOWN){
        // Don't allow for sClientID, cRecdStat, sModified, and dModified
        if(!(fnCol == poData.getColumn("sTransNox") ||
             fnCol == poData.getColumn("cRecdStat") ||
             fnCol == poData.getColumn("sModified") ||
             fnCol == poData.getColumn("dModified"))){

           if(fnCol == poData.getColumn("dBirthDte")){
              if(foData instanceof Date)
                 poData.setValue(fnCol, foData);
              else
                 poData.setValue(fnCol, null);
           }else if(fnCol == poData.getColumn("nGrssIncm")){
              if(foData instanceof Number)
                 poData.setValue(fnCol, foData);
              else
                 poData.setValue(fnCol, 0.00);
           }else{
                 poData.setValue(fnCol, foData);
           }
        }
      }
   }

   @Override
   public Object getMaster(String fsCol){
      return getMaster(poData.getColumn(fsCol));
   }
   
   @Override
   public Object getMaster(int fnCol) {
      if(pnEditMode == EditMode.UNKNOWN || poCtrl == null)
         return null;
      else{
         return poData.getValue(fnCol);
      }
   }

   @Override
   public boolean newRecord() {
      poData = poCtrl.newRecord();

      if(poData == null){
         return false;
      }
      else{

         //set the values of foreign key(object) to null
         poBirth = null;
         poTownx = null;
         poOccpn = null;

         pnEditMode = EditMode.ADDNEW;
         return true;
      }
   }

   @Override
   public boolean openRecord(String fstransNox) {
      poData = poCtrl.openRecord(fstransNox);

      if(poData.getClientID() == null){
         JOptionPane.showMessageDialog(null, "Unable to load client record!", "Warning", JOptionPane.INFORMATION_MESSAGE + JOptionPane.OK_OPTION);
         return false;
      }
      else{
         //set the values of foreign key(object) to null
         poBirth = null;
         poTownx = null;
         poOccpn = null;

         pnEditMode = EditMode.READY;
         return true;
      }
   }

   @Override
   public boolean updateRecord() {
      if(pnEditMode != EditMode.READY) {
         return false;
      }
      else{
         pnEditMode = EditMode.UPDATE;
         return true;
      }
   }

   @Override
   public boolean saveRecord() {
      if(pnEditMode == EditMode.UNKNOWN){
         return false;
      }
      else{
         // Perform testing on values that needs approval here...

         UnitClient loResult;
         if(pnEditMode == EditMode.ADDNEW)
            loResult = poCtrl.saveRecord(poData, "");
         else
            loResult = poCtrl.saveRecord(poData, (String) poData.getValue(1));

         if(loResult == null)
            return false;
         else{
            pnEditMode = EditMode.READY;
            poData = loResult;
            return true;
         }
      }
   }

   @Override
   public boolean deleteRecord(String fsTransNox) {
      if(pnEditMode != EditMode.READY){
         return false;
      }
      else{
         boolean lbResult = poCtrl.deleteRecord(fsTransNox);
         if(lbResult)
            pnEditMode = EditMode.UNKNOWN;

         return lbResult;
      }
   }

   @Override
   public boolean deactivateRecord(String fsTransNox) {
      if(pnEditMode != EditMode.READY){
         return false;
      }
      else{
         boolean lbResult = poCtrl.deactivateRecord(fsTransNox);
         if(lbResult)
            pnEditMode = EditMode.UNKNOWN;

         return lbResult;
      }
   }

   @Override
   public boolean activateRecord(String fsTransNox) {
      if(pnEditMode != EditMode.READY){
         return false;
      }
      else{
         boolean lbResult = poCtrl.activateRecord(fsTransNox);
         if(lbResult)
            pnEditMode = EditMode.UNKNOWN;

         return lbResult;
      }
   }

   @Override
   public void setBranch(String foBranchCD) {
      psBranchCd = foBranchCD;
   }

   @Override
   public int getEditMode() {
      return pnEditMode;
   }

   // Added methods here
   public void setGRider(GRider foGRider) {
      this.poGRider = foGRider;
      this.psUserIDxx = foGRider.getUserID();
      if(psBranchCD.isEmpty())
         psBranchCD = poGRider.getBranchCode();
   }

   public void setUserID(String fsUserID) {
      this.psUserIDxx = fsUserID;
   }

   public XMTownCity getBirthPlace(){
      if(poBirth == null)
         poBirth = new XMTownCity(poGRider, psBranchCd, true);

      poBirth.openRecord(poData.getBirthPlace());
      return poBirth;
   }

   public XMTownCity getTown(){
      if(poTownx == null)
         poTownx = new XMTownCity(poGRider, psBranchCd, true);

      poTownx.openRecord(poData.getTownID());
      return poTownx;
   }

   public XMOccupation getOccupation(){
      if(poOccpn == null)
         poOccpn = new XMOccupation(poGRider, psBranchCd, true);

      poOccpn.openRecord(poData.getOccptnID());
      return poOccpn;
   }

   // Member variables here
   private boolean pbWithParnt = false;
   private String psBranchCD = "";
   private String psUserIDxx = "";
   private UnitClient poData;
   private Client poCtrl;
   private GRider poGRider;
   private int pnEditMode;
   private String psBranchCd;

   private XMTownCity poBirth = null;
   private XMTownCity poTownx = null;
   private XMOccupation poOccpn = null;
}


