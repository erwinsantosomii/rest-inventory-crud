package com.mii.poc.inventory.util;

/**
 *
 * @author ErwinSn
 */
public class DuplicateException extends RuntimeException {
    
    public DuplicateException(String goodsNumber) {
        super("Goods Number '" + goodsNumber + "' already exist");
    }
    
}
