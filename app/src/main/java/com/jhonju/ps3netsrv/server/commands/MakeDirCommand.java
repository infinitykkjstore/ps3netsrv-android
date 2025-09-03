package com.jhonju.infinitysrv.server.commands;

import android.os.Build;

import com.jhonju.infinitysrv.server.Context;
import com.jhonju.infinitysrv.server.exceptions.infinitysrvException;
import com.jhonju.infinitysrv.server.io.File;

import java.io.IOException;

public class MakeDirCommand extends FileCommand {

    public MakeDirCommand(Context ctx, short filePathLength) {
        super(ctx, filePathLength);
    }

    @Override
    public void executeTask() throws infinitysrvException, IOException {
        if (ctx.isReadOnly()) {
            send(ERROR_CODE_BYTEARRAY);
            throw new infinitysrvException("Failed to make dir: server is executing as read only");
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            send(((File)getFile()).mkdir() ? SUCCESS_CODE_BYTEARRAY : ERROR_CODE_BYTEARRAY);
        } else {
            send(currentDirectory.createDirectory(fileName) != null ? SUCCESS_CODE_BYTEARRAY : ERROR_CODE_BYTEARRAY);
        }
    }
}
