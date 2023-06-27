<h3>Task 2.1</h3>
- Create a new class NQCheckpoint to implement non-quiescent checkpoint records. 
- Modify the interface LogRecord to have a constant NQCKPT with the value 6. You will need to update the constant definitions and the method createLogRecord. 
- Modify the class RecoveryMgr in two ways:

o Add a method checkpoint that writes a non-quiescent checkpoint record to the log. This method will be called from the Transaction class; its argument will be a list of ids of the active transactions.

o Modify the method doRecover to handle non-quiescent checkpoint records. This is the most difficult part of the assignment; think carefully about what needs to happen. As an aid to debugging, your code should print the log records as they are encountered.

- As a further aid to debugging, add a println statement in the undo methods of SetIntRecord and SetStringRecord so you can see when the recover and rollback methods undo a value.


In addition, the Transaction constructor must be modified to keep track of the currently active transactions and to periodically call the recovery manager’s checkpoint method.

To test your code, you can use our Task2TestA and Task2TestB programs.

Program Task2TestA does an NQ checkpoint in the middle of running some transactions and stops before all transactions have finished. Program Task2TestB preforms recovery on the log file. 

<h3>Task 2.2</h3>
SimpleDB currently uses timeout to detect deadlock. Change it so that it uses the wait die deadlock detection strategy. Code should modify the class LockTable as follows: 

- The methods sLock, xLock, and unLock will need to take the transaction’s id as an argument.
- The variable locks must be changed so that a block maps to a list of the transaction ids that hold a lock on the block (instead of just an integer). Use a negative transaction id to denote an exclusive lock.
- Each time through the while loop in sLock and xLock, check to see if the thread needs to be aborted (that is, if there is a transaction on the list that is older than the current transaction). If so, then the code should throw a LockAbortException.
- You will also need to make trivial modifications to the classes Transaction and ConcurrencyMgr so that the transaction id gets passed to the lock manager methods. You should figure out what those changes must be.
There is a test program Task2Test. It creates three transactions, each in their own thread. Transactions A and C both need a lock held by transaction B. Under the wait-die algorithm, transaction A should wait, whereas transaction C should throw an exception.


Deliverables

● For Task 2.1

❖ LogRecord.java

❖ NQCheckpoint.java 

❖ RecoveryMgr.java

● For Task 2.2

❖ Transaction.java

❖ ConcurrencyMgr.java 

❖ LockTable.java

