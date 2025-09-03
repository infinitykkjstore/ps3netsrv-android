package com.jhonju.infinitysrv.server.commands;

import com.jhonju.infinitysrv.server.Context;
import com.jhonju.infinitysrv.server.exceptions.infinitysrvException;
import com.jhonju.infinitysrv.server.io.IFile;

import java.io.IOException;

public class OpenDirCommand  extends FileCommand {

    public OpenDirCommand(Context ctx, short filePathLength) { super(ctx, filePathLength); }

    @Override
    public void executeTask() throws infinitysrvException, IOException {
        IFile file = getFile();
        if (file.exists()) {
            ctx.setFile(file);
            send(file.isDirectory() ? SUCCESS_CODE_BYTEARRAY : ERROR_CODE_BYTEARRAY);
        } else {
            ctx.setFile(null);
            send(ERROR_CODE_BYTEARRAY);
        }
    }
}
