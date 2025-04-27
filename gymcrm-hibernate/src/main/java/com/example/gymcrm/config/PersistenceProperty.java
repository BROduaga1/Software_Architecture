package com.example.gymcrm.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Getter
public class PersistenceProperty {

    private final Map<String, String> persistenceProperties = new HashMap<>();
    @Value("${hibernate.hbm2ddl.auto}")
    private String hbm2ddlAuto;
    @Value("${hibernate.hbm2ddl.import_files}")
    private String hbm2ddlAutoImportFiles;
    @Value("${hibernate.dialect}")
    private String hibernateDialect;
    @Value("${hibernate.show_sql}")
    private String showSql;
    @Value("${hibernate.format_sql}")
    private String formatSql;
    @Value("${hibernate.jdbc.lob.non_contextual_creation}")
    private String lobCreation;
    @Value("${hibernate.physical_naming_strategy}")
    private String physicalNamingStrategy;
    @Value("${jakarta.persistence.sql-load-script-source}")
    private String loadScriptSource;

    @PostConstruct
    public void init() {
        persistenceProperties.put("hibernate.hbm2ddl.auto", hbm2ddlAuto);
        persistenceProperties.put("hibernate.hbm2ddl.import_files", hbm2ddlAutoImportFiles);
        persistenceProperties.put("hibernate.dialect", hibernateDialect);
        persistenceProperties.put("hibernate.show_sql", showSql);
        persistenceProperties.put("hibernate.format_sql", formatSql);
        persistenceProperties.put("hibernate.jdbc.lob.non_contextual_creation", lobCreation);
        persistenceProperties.put("jakarta.persistence.sql-load-script-source", loadScriptSource);
        persistenceProperties.put("hibernate.physical_naming_strategy", physicalNamingStrategy);
    }
}
