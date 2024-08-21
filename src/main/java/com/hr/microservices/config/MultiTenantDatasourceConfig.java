package com.hr.microservices.config;

import com.hr.microservices.context.TenantContextHolder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class MultiTenantDatasourceConfig {

    /**
     * Tenant-1 (INDIA) data source
     */
    @Bean("indiaDatasourceProperties")
    @ConfigurationProperties(prefix = "datasource.tenants.india")
    public DataSourceProperties indiaDatasourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "indiaDatasource")
    public DataSource indiaDatasource(@Qualifier("indiaDatasourceProperties") DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }

    /**
     * Tenant-2 (CHINA) data source
     */
    @Bean("chinaDatasourceProperties")
    @ConfigurationProperties(prefix = "datasource.tenants.china")
    public DataSourceProperties chinaDatasourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "chinaDatasource")
    public DataSource chinaDatasource(@Qualifier("chinaDatasourceProperties") DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }

    /**
     * Defines the tenant data sources as well as the routing data source which selects the data source
     * based on the tenant value in the context
     *
     * @param indiaDataSource
     * @param chinaDataSource
     * @return
     */
    @Primary
    @Bean
    public DataSource datasource(@Qualifier("indiaDatasource") DataSource indiaDataSource,
                                 @Qualifier("chinaDatasource") DataSource chinaDataSource) {

        final Map<Object, Object> tenantsDatasourceMap = new HashMap<>();
        tenantsDatasourceMap.put("india", indiaDataSource);
        tenantsDatasourceMap.put("china", chinaDataSource);

        final AbstractRoutingDataSource routingDatasource = new AbstractRoutingDataSource() {
            @Override
            protected Object determineCurrentLookupKey() {
                return TenantContextHolder.get();
            }
        };
        routingDatasource.setTargetDataSources(tenantsDatasourceMap);
        routingDatasource.setDefaultTargetDataSource(indiaDataSource);
        return routingDatasource;
    }
}
