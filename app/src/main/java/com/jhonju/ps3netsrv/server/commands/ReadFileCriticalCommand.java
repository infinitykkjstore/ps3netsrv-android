package com.jhonju.infinitysrv.server.commands;

import java.io.IOException;

import com.jhonju.infinitysrv.server.Context;
import com.jhonju.infinitysrv.server.exceptions.infinitysrvException;
import com.jhonju.infinitysrv.server.io.IRandomAccessFile;

public class ReadFileCriticalCommand extends ReadFileCommand {

    public ReadFileCriticalCommand(Context ctx, int numBytes, long offset) {
        super(ctx, numBytes, offset);
    }

    @Override
    public void executeTask() throws IOException, infinitysrvException {
        byte[] result = new byte[numBytes];
        IRandomAccessFile file = ctx.getReadOnlyFile();
        try {
            file.seek(offset);
            if (file.read(result) < EMPTY_SIZE) {
                throw new infinitysrvException("Error reading file. EOF");
            }
        } catch (IOException e) {
            throw new infinitysrvException("Error reading file.");
        }
        send(result);
    }
}
