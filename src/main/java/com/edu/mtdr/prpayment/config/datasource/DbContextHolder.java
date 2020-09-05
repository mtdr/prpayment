package com.edu.mtdr.prpayment.config.datasource;

public class DbContextHolder {
    private static final ThreadLocal<DbTypeEnum> contextHolder = new ThreadLocal<>();
    public static void setCurrentDb(DbTypeEnum dbType) {
        contextHolder.set(dbType);
    }
    public static DbTypeEnum getCurrentDb() {
        return contextHolder.get();
    }
    public static void clear() {
        contextHolder.remove();
    }
}
