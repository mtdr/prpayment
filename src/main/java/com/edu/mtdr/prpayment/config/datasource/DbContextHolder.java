package com.edu.mtdr.prpayment.config.datasource;

/**
 * Holder of selected DB
 */
public class DbContextHolder {
    private static final ThreadLocal<DbTypeEnum> contextHolder = new ThreadLocal<>();

    /**
     * @param dbType changing on db
     */
    public static void setCurrentDb(DbTypeEnum dbType) {
        contextHolder.set(dbType);
    }

    /**
     * @return selected db
     */
    public static DbTypeEnum getCurrentDb() {
        return contextHolder.get();
    }

    /**
     * remove threadLocal
     */
    public static void clear() {
        contextHolder.remove();
    }
}
