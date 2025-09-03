package com.jhonju.infinitysrv.server.commands;

import com.jhonju.infinitysrv.server.exceptions.infinitysrvException;

import java.io.IOException;

public interface ICommand {

    void executeTask() throws IOException, infinitysrvException;

}
