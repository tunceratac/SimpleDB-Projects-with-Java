package simpledb.tx.recovery;

import simpledb.file.Page;
import simpledb.log.LogMgr;
import simpledb.tx.Transaction;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NQCheckpoint implements LogRecord {
    private Set<Integer> activeTxIds;

    public NQCheckpoint(Set<Integer> activeTxIds) {
        this.activeTxIds = activeTxIds;
    }

    public NQCheckpoint(Page p) {
        int totalRecordSize = p.contents().capacity();
        int size = (totalRecordSize - 4) / 4;
        activeTxIds = new HashSet<>();
        for (int i = 0; i < size; i++) {
            int txId = p.getInt(4 + 4 * i);
            activeTxIds.add(txId);
        }
    }

    @Override
    public int op() {
        return LogRecord.NQCKPT;
    }

    @Override
    public int txNumber() {
        return -1;
    }

    @Override
    public void undo(Transaction tx) {
        // do nothing
    }

    public Set<Integer> getActiveTxIds() {
        return activeTxIds;
    }

    public static int writeToLog(LogMgr lm, List<Integer> txs) {
        int recSize = 4 + 4 * txs.size();
        byte[] rec = new byte[recSize];
        Page p = new Page(rec);
        p.setInt(0, NQCKPT);
        int offset = 4;

        for (int txId : txs) {
            p.setInt(offset, txId);
            offset += 4;
        }

        return lm.append(rec);
    }
}
