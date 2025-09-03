package com.jhonju.infinitysrv.server.commands;

import static com.jhonju.infinitysrv.server.utils.Utils.INT_CAPACITY;

import com.jhonju.infinitysrv.server.Context;
import com.jhonju.infinitysrv.server.exceptions.infinitysrvException;
import com.jhonju.infinitysrv.server.io.IRandomAccessFile;
import com.jhonju.infinitysrv.server.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ReadFileCommand extends AbstractCommand {
    protected int numBytes;
    protected long offset;

    public ReadFileCommand(Context ctx, int numBytes, long offset) {
        super(ctx);
        this.numBytes = numBytes;
        this.offset = offset;
    }

    private static class ReadFileResult implements IResult {
        private final int bytesReadLength;
        private final byte[] bytesRead;

        public ReadFileResult(int bytesReadLength, byte[] bytesRead) {
            this.bytesReadLength = bytesReadLength;
            this.bytesRead = bytesRead;
        }

        @Override
        public byte[] toByteArray() throws IOException {
            ByteArrayOutputStream out = new ByteArrayOutputStream(INT_CAPACITY + bytesReadLength);
            try {
                out.write(Utils.intToBytesBE(bytesReadLength));
                out.write(bytesRead);
                return out.toByteArray();
            } finally {
                out.close();
            }
        }
    }

    @Override
    public void executeTask() throws IOException, infinitysrvException {
        byte[] readFileResult = new byte[numBytes];
        IRandomAccessFile file = ctx.getReadOnlyFile();
        try {
            file.seek(offset);
            int bytesRead = file.read(readFileResult);
            if (bytesRead < EMPTY_SIZE) {
                throw new infinitysrvException("Error reading file: EOF.");
            }
            send(new ReadFileResult(bytesRead, readFileResult));
        } catch (IOException e) {
            send(ERROR_CODE_BYTEARRAY);
            throw new infinitysrvException("Error reading file.");
        }
    }
}
