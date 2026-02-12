package com.barebonium.packcompanion.enumstates;

import java.util.Locale;

public enum Status {
    DEPRECATED,
    PROBLEMATIC;

    @Override
    public String toString() {
        return super.toString().toUpperCase(Locale.ROOT);
    }
}
