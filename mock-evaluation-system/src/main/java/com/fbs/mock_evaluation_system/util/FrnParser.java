package com.fbs.mock_evaluation_system.util;

public class FrnParser {

    private FrnParser() {
        // Prevent instantiation
    }

    public static String extractMiddleBlock(String frn) {
        int startIndex = 4; // After "FRN-"
        int endIndex = frn.indexOf("/");

        if (endIndex == -1) {
            throw new IllegalArgumentException("Invalid FRN format");
        }

        return frn.substring(startIndex, endIndex);
    }

    public static String extractRollNumber(String frn) {
        int slashIndex = frn.lastIndexOf("/");

        if (slashIndex == -1 || slashIndex == frn.length() - 1) {
            throw new IllegalArgumentException("Invalid FRN format");
        }

        return frn.substring(slashIndex + 1);
    }
}