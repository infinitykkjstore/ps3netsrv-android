package com.jhonju.infinitysrv.server.commands;

import static com.jhonju.infinitysrv.server.utils.Utils.LONG_CAPACITY;

import com.jhonju.infinitysrv.server.Context;
import com.jhonju.infinitysrv.server.exceptions.infinitysrvException;
import com.jhonju.infinitysrv.server.io.IFile;
import com.jhonju.infinitysrv.server.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadDirCommand extends AbstractCommand {
    private static final long MAX_ENTRIES = 4096;
    private static final short MAX_FILE_NAME_LENGTH = 512;
    private static final int READ_DIR_ENTRY_LENGTH = 529;

    public ReadDirCommand(Context ctx) {
        super(ctx);
    }

    private static class ReadDirResult implements IResult {
        private final List<ReadDirEntry> entries;

        public ReadDirResult(List<ReadDirEntry> entries) {
            this.entries = entries;
        }

        public byte[] toByteArray() throws IOException {
            if (entries != null) {
                ByteArrayOutputStream out = new ByteArrayOutputStream(entries.size() * READ_DIR_ENTRY_LENGTH + LONG_CAPACITY);
                try {
                    out.write(Utils.longToBytesBE(entries.size()));
                    for (ReadDirEntry entry : entries) {
                        out.write(entry.toByteArray());
                    }
                    return out.toByteArray();
                } finally {
                    out.close();
                }
            }
            return null;
        }

    }

    private static class ReadDirEntry {
        private final long aFileSize;
        private final long bModifiedTime;
        private final boolean cIsDirectory;
        private final char[] dName;

        public ReadDirEntry(long fileSize, long modifiedTime, boolean isDirectory, String name) {
            this.aFileSize = fileSize;
            this.bModifiedTime = modifiedTime;
            this.cIsDirectory = isDirectory;
            int length = Math.min(name.length(), MAX_FILE_NAME_LENGTH);
            this.dName = new char[MAX_FILE_NAME_LENGTH];
            for (int i = 0; i < length; i++) {
                this.dName[i] = name.charAt(i);
            }
        }

        public byte[] toByteArray() throws IOException {
            ByteArrayOutputStream out = new ByteArrayOutputStream(READ_DIR_ENTRY_LENGTH);
            try {
                out.write(Utils.longToBytesBE(this.aFileSize));
                out.write(Utils.longToBytesBE(this.bModifiedTime));
                out.write(cIsDirectory ? 1 : 0);
                out.write(Utils.charArrayToByteArray(dName));
                return out.toByteArray();
            } finally {
                out.close();
            }
        }
    }

    @Override
    public void executeTask() throws IOException, infinitysrvException {
        IFile file = ctx.getFile();
        if (file == null || !file.isDirectory()) {
            send(Utils.longToBytesBE(EMPTY_SIZE));
        } else {
            List<ReadDirEntry> entries = new ArrayList<>();
            IFile[] files = file.listFiles();
            for (IFile f : files) {
                if (entries.size() == MAX_ENTRIES) break;
                entries.add(new ReadDirEntry(f.isDirectory() ? EMPTY_SIZE : f.length(), f.lastModified() / MILLISECONDS_IN_SECOND, f.isDirectory(), f.getName() != null ? f.getName() : ""));
            }
            send(new ReadDirResult(entries));
        }
        ctx.setFile(null);
    }
}
