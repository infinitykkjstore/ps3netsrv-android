package com.jhonju.infinitysrv.server.commands;

import com.jhonju.infinitysrv.server.Context;
import com.jhonju.infinitysrv.server.exceptions.infinitysrvException;
import com.jhonju.infinitysrv.server.io.IFile;
import com.jhonju.infinitysrv.server.utils.Utils;

import java.io.IOException;

public class GetDirSizeCommand extends FileCommand {

    public GetDirSizeCommand(Context ctx, short filePathLength)
    {
        super(ctx, filePathLength);
        ERROR_CODE_BYTEARRAY = Utils.longToBytesBE(ERROR_CODE);
    }

    @Override
    public void executeTask() throws IOException, infinitysrvException {
        send(Utils.longToBytesBE(calculateFileSize(getFile())));
    }

    private static long calculateFileSize(IFile file) {
        long fileSize = EMPTY_SIZE;
        if (file.isDirectory()) {
            IFile[] files = file.listFiles();
            for (IFile subFile : files) {
                fileSize += calculateFileSize(subFile);
            }
        } else {
            fileSize = file.length();
        }
        return fileSize;
    }
}
