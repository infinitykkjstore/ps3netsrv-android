package com.jhonju.infinitysrv.server.commands;

import java.io.IOException;

public interface IResult {
    byte[] toByteArray() throws IOException;
}
