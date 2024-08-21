package com.hr.microservices.context;

public final class TenantContextHolder {

    private static final ThreadLocal<String> tenantContext = new ThreadLocal<>();

    public static void set(final String tenant) {
        tenantContext.set(tenant);
    }

    public static String get() {
        return tenantContext.get();
    }

    public static void clear(){
        tenantContext.remove();
    }

}
