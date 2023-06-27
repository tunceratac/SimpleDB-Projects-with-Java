package simpledb.buffer;

import java.util.*;
import simpledb.file.*;
import simpledb.log.LogMgr;

/**
 * Manages the pinning and unpinning of buffers to blocks.
 * @author Edward Sciore
 *
 */
public class BufferMgr {
   private int numAvailable;
   private static final long MAX_TIME = 10000; // 10 seconds
   private List<Buffer> unpinnedBuffers;
   private Map<BlockId, Buffer> allocatedBuffers;
   
   /**
    * Creates a buffer manager having the specified number 
    * of buffer slots.
    * This constructor depends on a {@link FileMgr} and
    * {@link simpledb.log.LogMgr LogMgr} object.
    * @param numbuffs the number of buffer slots to allocate
    */
   public BufferMgr(FileMgr fm, LogMgr lm, int numbuffs) {
	   unpinnedBuffers = new LinkedList<>();
	   allocatedBuffers = new HashMap<>();
	   numAvailable = numbuffs;
	   for (int i = 0; i < numbuffs; i++)
	      unpinnedBuffers.add(new Buffer(fm, lm, i));
   }
   
   /**
    * Returns the number of available (i.e. unpinned) buffers.
    * @return the number of available buffers
    */
   public synchronized int available() {
      return numAvailable;
   }
   
   /**
    * Flushes the dirty buffers modified by the specified transaction.
    * @param txnum the transaction's id number
    */
   public synchronized void flushAll(int txnum) {
      for (Buffer buff : unpinnedBuffers)
         if (buff.modifyingTx() == txnum)
         buff.flush();
   }
   
   
   /**
    * Unpins the specified data buffer. If its pin count
    * goes to zero, then notify any waiting threads.
    * @param buff the buffer to be unpinned
    */
   
   public synchronized void unpin(Buffer buff) {
      buff.unpin();
      if (!buff.isPinned()) {
         numAvailable++;
         unpinnedBuffers.add(buff);
         notifyAll();
      }
   }
   
   /**
    * Pins a buffer to the specified block, potentially
    * waiting until a buffer becomes available.
    * If no buffer becomes available within a fixed 
    * time period, then a {@link BufferAbortException} is thrown.
    * @param blk a reference to a disk block
    * @return the buffer pinned to that block
    */
   public synchronized Buffer pin(BlockId blk) {
      try {
         long timestamp = System.currentTimeMillis();
         Buffer buff = tryToPin(blk);
         while (buff == null && !waitingTooLong(timestamp)) {
            wait(MAX_TIME);
            buff = tryToPin(blk);
         }
         if (buff == null)
            throw new BufferAbortException();
         return buff;
      }
      catch(InterruptedException e) {
         throw new BufferAbortException();
      }
   }  
   
   private boolean waitingTooLong(long starttime) {
      return System.currentTimeMillis() - starttime > MAX_TIME;
   }
   
   /**
    * Tries to pin a buffer to the specified block. 
    * If there is already a buffer assigned to that block
    * then that buffer is used;  
    * otherwise, an unpinned buffer from the pool is chosen.
    * Returns a null value if there are no available buffers.
    * @param blk a reference to a disk block
    * @return the pinned buffer
    */
   private Buffer tryToPin(BlockId blk) {
	      Buffer buff = allocatedBuffers.get(blk);
	      if (buff == null) {
	         buff = chooseUnpinnedBuffer();
	         if (buff == null)
	            return null;
	         buff.assignToBlock(blk);
	         allocatedBuffers.put(blk, buff);
	      }
	      if (!buff.isPinned())
	         numAvailable--;
	      buff.pin();
	      return buff;
	   }
   
   private Buffer findExistingBuffer(BlockId blk) {
      for (Buffer buff : unpinnedBuffers) {
         BlockId b = buff.block();
         if (b != null && b.equals(blk))
            return buff;
      }
      return null;
   }
   
   private Buffer chooseUnpinnedBuffer() {
	      if (unpinnedBuffers.isEmpty()) {
	         return null;
	      } else {
	         return unpinnedBuffers.remove(0);
	      }
	   }
   
   public void printStatus() {
	      System.out.println("Allocated Buffers:");
	      for (Buffer buff : allocatedBuffers.values()) {
	         System.out.println("Buffer " + buff.getId() + ": " + buff.block() + (buff.isPinned() ? " pinned" : " unpinned"));
	      }
	      System.out.print("Unpinned Buffers in LRU order: ");
	      for (Buffer buff : unpinnedBuffers) {
	         System.out.print(buff.getId() + " ");
	      }
	      System.out.println();
	   }
   
}
