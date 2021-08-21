package com.turong.training.rabbitmq.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;
import java.util.Properties;

@Configuration
@MapperScan("com.turong.training.rabbitmq.mapper")
@Slf4j
public class MybatisConfig {

    private static final String TENANT_COLUMN = "tenant";

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(new TenantLineHandler() {
            @Override
            public Expression getTenantId() {
                String currentTenant = AppContextHolder.getTenant();
                log.info("Current tenant ={}", currentTenant);
                if (StringUtils.isBlank(currentTenant)) {
                    return null;
                    // throw new IllegalArgumentException("The tenant must be present!");
                }
                return new StringValue(currentTenant);
            }

            @Override
            public boolean ignoreTable(String tableName) {
                return false;
            }

            @Override
            public String getTenantIdColumn() {
                return TENANT_COLUMN;
            }

        }));
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    @Bean
    public MybatisConfiguration mybatisConfiguration() {
        MybatisConfiguration configuration = new MybatisConfiguration();

        Properties properties = configuration.getVariables();
        if (Objects.isNull(properties)) {
            properties = new Properties();
        }
        log.info("Current Ibatis configuration={}", properties);
        properties.put("useDeprecatedExecutor", Boolean.FALSE);
        configuration.setVariables(properties);

        return configuration;
    }

}
