package com.example.capsule;

import io.github.wallawood.WallawoodConfig;
import io.github.wallawood.WallawoodServer;

public class App {
    public static void main(String[] args) {
        var config = WallawoodConfig.fromProperties();
        WallawoodServer.start(App.class, config);
    }
}
