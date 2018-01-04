package com.fcup.utilities;

public class ShardDispatcher {

    public static int idToIndex(String shardID, int numberOfPools) {
        return ((int)(preHash(shardID) % numberOfPools));

    }

    private static long preHash(String key) {
        long offsetBasis = 2166136261L;
        int fnvPrime = 16777619;
        long hashCode = offsetBasis + fnvPrime;

        for (char c : key.toCharArray()) {
            hashCode ^= (c - '0');
            hashCode *= fnvPrime;
        }

        return hashCode;
    }
}
