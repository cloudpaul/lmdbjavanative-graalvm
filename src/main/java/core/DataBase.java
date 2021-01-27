package core;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.lmdbjava.ByteBufferProxy;
import org.lmdbjava.CursorIterable;
import org.lmdbjava.Dbi;
import org.lmdbjava.Env;
import org.lmdbjava.Txn;

public class DataBase {
    private final String pathLmdb = null;
    private Dbi<ByteBuffer> db;
    private final Env<ByteBuffer> env;
    private final File dbDirectory;

    private File FilterDbPath(final String fullPath) {
        return new File(fullPath);
    }

    public DataBase(final File dbDirectory) {
        this.dbDirectory = dbDirectory;
        this.env = Env.create(ByteBufferProxy.PROXY_SAFE)
                .setMaxDbs(0)
                .open(this.dbDirectory);

        for (final byte[] obj:this.env.getDbiNames()) {
            System.out.println(new String(obj));
        }
    }

	
    public String GetDbNames() {
        if (db == null) {
            return "";
        }
        return "String.valueOf(.getName())";
    }

    public ArrayList<KeyValue> GetData() {
        final Txn<ByteBuffer> rtx = env.txnRead();
        final ArrayList<KeyValue> results = new ArrayList<>();
        db = env.openDbi((byte[]) null);

        final CursorIterable<ByteBuffer> cursor = db.iterate(rtx);
        for (final CursorIterable.KeyVal<ByteBuffer> kv : cursor) {
            // Storing in the data class
            results.add(new KeyValue(kv.key(), kv.val()));
        }
        return results;
    }

    public ArrayList<KeyValue> searchData(final String keyValue, final Boolean valueSearch) {
        final Txn<ByteBuffer> rtx = env.txnRead();
        // To byte
        final ArrayList<KeyValue> results = new ArrayList<>();
        db = env.openDbi((byte[]) null);
        final CursorIterable<ByteBuffer> cursor = db.iterate(rtx);
        for (final CursorIterable.KeyVal<ByteBuffer> kv : cursor) {
            // copy of key
            final int size = kv.key().remaining();
            final byte[] keyByte = new byte[size];
            kv.key().get(keyByte);
            final String dbString = new String(keyByte);
            // copy of value
            final int sizeValue = kv.val().remaining();
            final byte[] valueByte = new byte[sizeValue];
            kv.val().get(valueByte);
            final String dbStringValue = new String(valueByte);
            if (valueSearch && dbStringValue.contains(keyValue)) {
                results.add(new KeyValue(ByteBuffer.wrap(dbString.getBytes()), ByteBuffer.wrap(dbStringValue.getBytes())));
            } else if (!valueSearch && dbString.contains(keyValue)) {
                results.add(new KeyValue(ByteBuffer.wrap(dbString.getBytes()), ByteBuffer.wrap(dbStringValue.getBytes())));
            }
        }
        return results;
    }


}
