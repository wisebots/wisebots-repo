package com.wisebots.utils;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Comparator;

class KeyComparator implements Comparator, Serializable {

    /**
	 *
	 */
	private static final long serialVersionUID = 1L;


	/**
     * Compares two DB keys.
     *
     * @param key1 first key
     * @param key2 second key
     *
     * @return comparison result
     */
    public int compare(Object key1, Object key2) {
    	byte[] k1 = (byte[])key1;
    	byte[] k2 = (byte[])key2;
        return new BigInteger(k1).compareTo(new BigInteger(k2));
    }


}

