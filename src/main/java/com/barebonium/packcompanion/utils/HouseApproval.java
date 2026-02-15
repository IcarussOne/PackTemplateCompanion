package com.barebonium.packcompanion.utils;

import java.util.Random;

public class HouseApproval {
    public static boolean houseApproves;
    static {
        shouldHouseApprove();
    }
    public static void shouldHouseApprove(){
        Random rand = new Random();
        houseApproves = rand.nextInt(5) == 0;
    }
}
