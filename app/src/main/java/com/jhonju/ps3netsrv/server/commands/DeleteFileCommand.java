package com.jhonju.infinitysrv.server.commands;

import com.jhonju.infinitysrv.server.Context;
import com.jhonju.infinitysrv.server.exceptions.infinitysrvException;

import java.io.IOException;

public class DeleteFileCommand extends FileCommand {

    public DeleteFileCommand(Context ctx, short filePathLength) {
        super(ctx, filePathLength);
    }

    @Override
    public void executeTask() throws infinitysrvException, IOException {
        if (ctx.isReadOnly()) {
            send(ERROR_CODE_BYTEARRAY);
            throw new infinitysrvException("Failed to delete file: server is executing as read only");
        }
        send(getFile().delete() ? SUCCESS_CODE_BYTEARRAY : ERROR_CODE_BYTEARRAY);
    }
}
