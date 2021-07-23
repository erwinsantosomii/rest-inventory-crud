package com.mii.poc.inventory.util;

/**
 *
 * @author ErwinSn
 */
public class DataNotFoundException extends RuntimeException {
    
    public DataNotFoundException(Long id) {
        super("Data with id '" + id + "' not found");
    }
    
    public DataNotFoundException(String goodsNumber) {
        super("Data with goods number '" + goodsNumber + "' not found");
    }
    
}
