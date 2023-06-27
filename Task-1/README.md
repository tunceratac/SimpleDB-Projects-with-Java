<h3>Task 1.1:</h3>

Make the following two modifications to the SimpleDB class Page.

a) The methods setInt, setBytes, and setString do not check that the specified value will fit into the byte buffer at the specified offset. If it doesn’t, the ByteBuffer class will throw an IndexOutOfBounds exception. Modify these methods so that they write the specified value only if it fits. If the value does not fit, the methods should print a descriptive message and ignore the request. For example, if the capacity of the page’s byte buffer is 400 bytes, then the call p.setInt(398,12) should print the following message:

ERROR: The integer 12 does not fit at location 398 of the page Note: To get the capacity of a ByteBuffer object, call its capacity() method.
b) Currently, the class writes a string as a “blob” of bytes, prepended by an integer denoting the length of the blob. Modify the method setString so that it instead writes each individual character of the string, followed by the delimiter character '\0'. Modify the method getString analogously.
You should use the ByteBuffer methods getChar and putChar to read and write each character. Note that these methods encode each character using two bytes, regardless of the specified charset. Thus, you will also need to modify the page’s maxLength method.

NOTE 1: You need to re-create the demo university database, because the database you created for simpleDB stores string values differently.

NOTE 2: You can use the FileTest class in Figure 3.12 in your book (Springer edition) to test your codes.

<h3>Task 1.2:</h3>

The SimpleDB buffer manager is grossly inefficient in two ways:

● When looking for a buffer to replace, it uses the first unpinned buffer it finds, instead of doing something intelligent like LRU.

● When checking to see if a block is already in a buffer, it does a sequential scan of the buffers, instead of keeping a data structure (such as a map) to more quickly locate the buffer.

I would like you to fix these problems by modifying the class BufferMgr. Please use the following strategy:

● Keep a list of unpinned buffers. When a replacement buffer needs to be chosen, remove the buffer at the head of the list and use it. When a buffer's pin count becomes 0, add it to the end of the list. This implements LRU replacement.

● Keep a map of allocated buffers, keyed on the block they contain. (A buffer is allocated when its contents are not null and may be pinned or unpinned. A buffer starts out unallocated; it becomes allocated when it is first assigned to a block and stays allocated forever after.) Use this map to determine if a block is currently in a buffer. When a buffer is first allocated, you must add it to the map. When a buffer is replaced, you must change the map—the mapping for the old block must be removed, and the mapping for the new block must be added.

● Get rid of the bufferpool array. You no longer need it.

In addition, you should modify the class Buffer so that each Buffer object knows which buffer it is. Its constructor should have a third argument denoting the buffer's id, and a method getId() that returns that id.

You should also modify BufferMgr to have a method printStatus that displays its current status. The status consists of the id, block, and pinned status of each buffer in the allocated map, plus the ids of each buffer in the unpinned list. For example, here is what the output of the method should look like for a database having 4 buffers, in which blocks 0 to 3 of file “test” were pinned, and then blocks 2 and 0 were unpinned.

Allocated Buffers:
Buffer 1: [file test, block 1] pinned
Buffer 0: [file test, block 0] unpinned
Buffer 3: [file test, block 3] pinned
Buffer 2: [file test, block 2] unpinned
Unpinned Buffers in LRU order: 2 0

Note that the buffers in the above output are in seemingly random order because they were retrieved from a hash map. The information within brackets comes from calling the toString method of BlockId.

There is a test program called Task1Test, which pins and unpins buffers, occasionally calling the buffer manager’s printStatus method. You should download it. It will help you debug your code.

Deliverables
❖ Page.java
❖ Buffer.java
❖ BufferMgr.java