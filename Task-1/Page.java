package simpledb.file;

import java.nio.ByteBuffer;
import java.nio.charset.*;

public class Page {
   private ByteBuffer bb;
   public static Charset CHARSET = StandardCharsets.US_ASCII;
   public static char DELIMITER = '\0';

   // For creating data buffers
   public Page(int blocksize) {
      bb = ByteBuffer.allocateDirect(blocksize);
   }
   
   // For creating log pages
   public Page(byte[] b) {
      bb = ByteBuffer.wrap(b);
   }

   public int getInt(int offset) {
      return bb.getInt(offset);
   }

   public void setInt(int offset, int n) {
	  if(offset + Integer.BYTES <= bb.capacity()) {
	      bb.putInt(offset, n);
	  }
	  else {
		  System.err.println("ERROR: The integer " + n + " does not fit at location " 
	  + offset + " of the page.");
	  }
   }

   public byte[] getBytes(int offset) {
      bb.position(offset);
      int length = bb.getInt();
      byte[] b = new byte[length];
      bb.get(b);
      return b;
   }

   public void setBytes(int offset, byte[] b) {
       if (offset + Integer.BYTES + b.length <= bb.capacity()) {
           bb.position(offset);
           bb.putInt(b.length);
           bb.put(b);
       } else {
           System.err.println("ERROR: The byte array does not fit at location " + offset + " of the page");
       }
   }
   
   public String getString(int offset) {
       bb.position(offset);
       StringBuilder sb = new StringBuilder();
       char c = bb.getChar();
       while (c != DELIMITER) {
           sb.append(c);
           c = bb.getChar();
       }
       return sb.toString();
   }

   public void setString(int offset, String s) {
       int requiredSpace = s.length() * Character.BYTES + Character.BYTES; // for the delimiter
       if (offset + requiredSpace <= bb.capacity()) {
           for (char c : s.toCharArray()) {
               bb.putChar(offset, c);
               offset += Character.BYTES;
           }
           bb.putChar(offset, DELIMITER);
       } else {
           System.err.println("ERROR: The string \"" + s + "\" does not fit at location " + offset + " of the page");
       }
   }

   public static int maxLength(int strlen) {
       return (strlen * Character.BYTES) + Character.BYTES;
   }

   // a package private method, needed by FileMgr
   ByteBuffer contents() {
      bb.position(0);
      return bb;
   }
}
