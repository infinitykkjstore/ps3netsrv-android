package com.jhonju.infinitysrv.server.commands;

import android.net.Uri;
import android.os.Build;

import com.jhonju.infinitysrv.app.infinitysrvApp;
import com.jhonju.infinitysrv.server.Context;
import com.jhonju.infinitysrv.server.exceptions.infinitysrvException;
import com.jhonju.infinitysrv.server.io.DocumentFile;
import com.jhonju.infinitysrv.server.io.File;
import com.jhonju.infinitysrv.server.io.IFile;
import com.jhonju.infinitysrv.server.utils.Utils;

import java.io.IOException;
import java.nio.ByteBuffer;
import com.jhonju.infinitysrv.server.charset.StandardCharsets;

public abstract class FileCommand extends AbstractCommand {
    protected short filePathLength;
    protected String fileName;
    protected androidx.documentfile.provider.DocumentFile currentDirectory;

    public FileCommand(Context ctx, short filePathLength) {
        super(ctx);
        this.filePathLength = filePathLength;
    }

    private String getFormattedPath(String path) {
        path = path.replaceAll("\\x00+$", "");
        if (path.equals("/.") || path.equals("/")) path = "";
        if (path.startsWith("/")) path = path.replaceFirst("/", "");
        return path;
    }

    protected IFile getFile() throws IOException, infinitysrvException {
        ByteBuffer buffer = Utils.readCommandData(ctx.getInputStream(), this.filePathLength);
        if (buffer == null) {
            send(ERROR_CODE_BYTEARRAY);
            throw new infinitysrvException("ERROR: command failed receiving filename.");
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return new File(new java.io.File(ctx.getRootDirectory(), new String(buffer.array(), StandardCharsets.UTF_8).replaceAll("\\x00+$", "")));
        }

        androidx.documentfile.provider.DocumentFile documentFile = androidx.documentfile.provider.DocumentFile.fromTreeUri(infinitysrvApp.getAppContext(), Uri.parse(ctx.getRootDirectory()));
        if (documentFile == null || !documentFile.exists()) {
            send(ERROR_CODE_BYTEARRAY);
            throw new infinitysrvException("ERROR: wrong path configuration.");
        }

        String path = getFormattedPath(new String(buffer.array(), StandardCharsets.UTF_8));
        if (!path.isEmpty()) {
            String[] paths = path.split("/");
            if (paths.length > 0) {
                for (String s : paths) {
                    currentDirectory = documentFile;
                    documentFile = documentFile.findFile(s);
                    if (documentFile == null) {
                        fileName = s;
                        break;
                    }
                }
            }
        }
        return new DocumentFile(documentFile);
    }
}
