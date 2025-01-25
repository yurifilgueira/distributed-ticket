package com.imd.ufrn.servers;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class Server {

    protected ExecutorService executor;
    protected Integer port;

    public abstract void start();

    public Server() {
    }

    public Server(Integer port) {
        this.port = port;
        this.executor = Executors.newVirtualThreadPerTaskExecutor();
    }
}
