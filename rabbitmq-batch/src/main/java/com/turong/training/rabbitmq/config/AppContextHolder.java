package com.turong.training.rabbitmq.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@UtilityClass
@Slf4j
public class AppContextHolder {

    @Getter
    @Setter
    @ToString
    private static class AppContext {

        private String tenant;

        public void clear() {
            setTenant(null);
        }

        public void printAll() {
            log.debug("Context properties={}", toString());
        }
    }

    private static final ThreadLocal<AppContext> CONTEXT = new ThreadLocal<AppContext>();

    public static void setTenant(final String tenant) {
        initializeIfEmpty();
        CONTEXT.get().setTenant(tenant);
    }

    public static String getTenant() {
        if (!hasContext()) {
            return null;
        }
        return CONTEXT.get().getTenant();
    }

    public static void printAllContextProperties() {
        if (hasContext()) {
            CONTEXT.get().printAll();
        }
    }

    private static void initializeIfEmpty() {
        if (!hasContext()) {
            CONTEXT.set(new AppContext());
        }
    }

    private static boolean hasContext() {
        return !Objects.isNull(CONTEXT.get());
    }

}
