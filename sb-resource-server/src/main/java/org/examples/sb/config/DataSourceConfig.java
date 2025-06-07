package org.examples.sb.config;

import org.examples.sb.helpers.MultiRegionDataSourceRouter;
import org.examples.sb.helpers.RegionResolver;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DataSourceConfig {


    @Bean(name = "dataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "region1DataSource")
    @ConfigurationProperties(prefix = "spring.datasource.region1")
    public DataSource region1DataSource() {
        return DataSourceBuilder.create().build();
    }


    @Bean(name = "region2DataSource")
    @ConfigurationProperties(prefix = "spring.datasource.region2")
    public DataSource region2DataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean
    public DataSource dataSourceRouter(/*@Qualifier("region1DataSource") DataSource region1DataSource, @Qualifier("region2DataSource") DataSource region2DataSource,*/ RegionResolver tenantResolver) {

        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put("DC-R0", dataSource());
        targetDataSources.put("DC-R1", region1DataSource());
        targetDataSources.put("DC-R2", region2DataSource());

        MultiRegionDataSourceRouter router = new MultiRegionDataSourceRouter();
        router.setTargetDataSources(targetDataSources);
        // Default if tenant/region is not found
        router.setDefaultTargetDataSource(dataSource());
        router.setTenantResolver(tenantResolver);
        return router;
    }
    
    /* 
    
    @Primary
    @Bean
    public JdbcTemplate jdbcTemplate(@Qualifier("dataSourceRouter") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
    
    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
        return new NamedParameterJdbcTemplate(dataSource());
    }

    */

}
