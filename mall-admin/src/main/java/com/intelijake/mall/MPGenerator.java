package com.intelijake.mall;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.IFill;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.fill.Column;

/**
 * MyBatis-Plus Code Generator
 * This class automatically generates Java code (Entities, Mappers, Services, Controllers)
 * based on your database schema.
 */
public class MPGenerator {

    /**
     * --- CONFIGURATION ---
     * Update these settings to match your project needs.
     */

    // 1. Set the names of the database tables you want to generate code for, separated by commas.
    // I have updated this list to match the translated table names from your SQL script.
    private static final String DB_TABLES = "admin_user,customer,product_category,product,customer_order,order_item";

    // 2. Set to true if you want to add Swagger annotations for API documentation.
    private static final Boolean ENABLE_SWAGGER = false;

    // --- MAIN GENERATOR METHOD ---
    public static void main(String[] args) {

        // 1. DATA SOURCE CONFIGURATION
        // Configure the connection to your database.
        // IMPORTANT: Change the username and password to your actual database credentials.
        DataSourceConfig dataSourceConfig = new DataSourceConfig.Builder(
                "jdbc:mysql://localhost:3306/mall", // Database URL
                "root",       // Your DB username
                "1234"        // Your DB password
        ).build();

        // 2. GLOBAL CONFIGURATION
        GlobalConfig.Builder globalConfigBuilder = new GlobalConfig.Builder();

        // Set the output directory for the generated code.
        String projectPath = System.getProperty("user.dir");
        globalConfigBuilder.outputDir(projectPath + "/mall-admin/src/main/java");

        // Set the author name to be added in the comments of generated files.
        globalConfigBuilder.author("Jake");

        // Set to true if you want to automatically open the output directory after generation.
        globalConfigBuilder.openDir(false);

        // Enable Swagger annotations if configured.
        if (ENABLE_SWAGGER) {
            globalConfigBuilder.enableSwagger();
        }

        // Set the date type to be used (e.g., java.util.Date or java.time.LocalDateTime).
        globalConfigBuilder.dateType(DateType.TIME_PACK); // Using java.time classes

        // 3. PACKAGE CONFIGURATION
        PackageConfig.Builder packageConfigBuilder = new PackageConfig.Builder();

        packageConfigBuilder.parent("com.intelijake.mall");

        // You can customize sub-package names here if needed.
        packageConfigBuilder.entity("pojo");
/*        packageConfigBuilder.mapper("mapper");
        packageConfigBuilder.service("service");
        packageConfigBuilder.controller("controller");*/

        // 4. STRATEGY CONFIGURATION
        StrategyConfig.Builder strategyConfigBuilder = new StrategyConfig.Builder();

        // Set the table names to be processed.
        strategyConfigBuilder.addInclude(DB_TABLES.split(","));

        // --- Entity Strategy ---
        strategyConfigBuilder.entityBuilder()
                // Use AUTO type for primary keys that are auto-incrementing.
                .idType(IdType.AUTO)
                // Convert table and column names from snake_case (e.g., user_name) to camelCase (e.g., userName).
                .naming(NamingStrategy.underline_to_camel)
                .columnNaming(NamingStrategy.underline_to_camel)
                // Enable Lombok annotations (@Data, @Getter, @Setter, etc.) for cleaner code.
                .enableLombok()
                // Specify the column used for logical deletion.
                .logicDeleteColumnName("is_deleted")
                .logicDeletePropertyName("isDeleted");

        // --- Automatic Field Filling ---
        // Automatically set values for certain fields on insert or update.
        IFill createTime = new Column("create_time", FieldFill.INSERT);
        IFill updateTime = new Column("update_time", FieldFill.INSERT_UPDATE);
        strategyConfigBuilder.entityBuilder().addTableFills(createTime, updateTime);

        // --- Controller Strategy ---
        strategyConfigBuilder.controllerBuilder()
                // Generate RESTful controllers (@RestController).
                .enableRestStyle()
                // Generate kebab-case URLs (e.g., /user-order/list) from camelCase method names.
                .enableHyphenStyle();

/*        // --- Mapper Strategy ---
        strategyConfigBuilder.mapperBuilder()
                // Enable the @Mapper annotation on mapper interfaces.
                .enableMapperAnnotation();*/

        // 5. CREATE AND RUN GENERATOR
        // Initialize the generator with the configurations.
        AutoGenerator autoGenerator = new AutoGenerator(dataSourceConfig);
        autoGenerator.global(globalConfigBuilder.build());
        autoGenerator.packageInfo(packageConfigBuilder.build());
        autoGenerator.strategy(strategyConfigBuilder.build());

        // Execute the code generation process.
        autoGenerator.execute();

        System.out.println("Code generation completed successfully!");
    }
}
