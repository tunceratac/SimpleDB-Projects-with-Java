package simpledb.query;


public class UnionScan implements Scan {
   private Scan s1, s2;
   private boolean fromS1;

   public UnionScan(Scan s1, Scan s2) {
      this.s1 = s1;
      this.s2 = s2;
      fromS1 = true;
   }

   public void beforeFirst() {
      s1.beforeFirst();
      s2.beforeFirst();
   }

   public boolean next() {
      if (fromS1) {
         if (s1.next()) {
            return true;
         } else {
            fromS1 = false;
            return s2.next();
         }
      } else {
         return s2.next();
      }
   }

   public void close() {
      s1.close();
      s2.close();
   }

   public Constant getVal(String fldname) {
      if (fromS1) {
         return s1.getVal(fldname);
      } else {
         return s2.getVal(fldname);
      }
   }

   public int getInt(String fldname) {
      if (fromS1) {
         return s1.getInt(fldname);
      } else {
         return s2.getInt(fldname);
      }
   }

   public String getString(String fldname) {
      if (fromS1) {
         return s1.getString(fldname);
      } else {
         return s2.getString(fldname);
      }
   }

   public boolean hasField(String fldname) {
      return s1.hasField(fldname) && s2.hasField(fldname);
   }
}
