package com.jhonju.infinitysrv.server.commands;

import com.jhonju.infinitysrv.server.Context;
import com.jhonju.infinitysrv.server.exceptions.infinitysrvException;
import com.jhonju.infinitysrv.server.utils.Utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class WriteFileCommand extends AbstractCommand {

    private final int numBytes;

    public WriteFileCommand(Context ctx, int numBytes) {
        super(ctx);
        this.numBytes = numBytes;
    }

    @Override
    public void executeTask() throws IOException, infinitysrvException {
        if (ctx.isReadOnly()) {
            send(ERROR_CODE_BYTEARRAY);
            throw new infinitysrvException("Failed to write file: server is executing as read only");
        }

        if (ctx.getReadOnlyFile() == null) {
            send(ERROR_CODE_BYTEARRAY);
            throw new infinitysrvException("ERROR: file is null");
        }

        if (numBytes > BUFFER_SIZE) {
            send(ERROR_CODE_BYTEARRAY);
            throw new infinitysrvException(String.format("ERROR: data to write (%d) is larger than buffer size (%d)", numBytes, BUFFER_SIZE));
        }

        ByteBuffer buffer = Utils.readCommandData(ctx.getInputStream(), numBytes);
        if (buffer == null) {
            send(ERROR_CODE_BYTEARRAY);
            throw new infinitysrvException("ERROR: on write file - content is null");
        }

        send(ERROR_CODE_BYTEARRAY); //TODO: fix writeOnlyFile and remove this line
//        try (FileOutputStream fos = new FileOutputStream(ctx.getWriteOnlyFile())) {
//            byte[] content;
//            try {
//                content = buffer.array();
//                fos.write(content);
//            } catch (IOException ex) {
//                send(ERROR_CODE_BYTEARRAY);
//                throw new infinitysrvException("ERROR: writing file " + ex.getMessage());
//            }
//            send(Utils.intToBytesBE(content.length));
//        }
    }
}
