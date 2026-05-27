package com.example.capsule.context;

import java.time.Instant;

public record ServerInfo(String name, String version, Instant startedAt) {}
