package simpledb.query;

import simpledb.server.SimpleDB;
import simpledb.tx.Transaction;
import simpledb.record.*;

public class RenameScan implements Scan {
   private Scan s;
   private String oldName, newName;

   public RenameScan(Scan s, String oldName, String newName) {
      this.s = s;
      this.oldName = oldName;
      this.newName = newName;
   }

   public void beforeFirst() {
      s.beforeFirst();
   }

   public boolean next() {
      return s.next();
   }

   public void close() {
      s.close();
   }

   public Constant getVal(String fldname) {
      if (fldname.equals(newName))
         fldname = oldName;
      return s.getVal(fldname);
   }

   public int getInt(String fldname) {
      if (fldname.equals(newName))
         fldname = oldName;
      return s.getInt(fldname);
   }

   public String getString(String fldname) {
      if (fldname.equals(newName))
         fldname = oldName;
      return s.getString(fldname);
   }

   public boolean hasField(String fldname) {
      if (fldname.equals(newName))
         return s.hasField(oldName);
      return s.hasField(fldname);
   }
}
