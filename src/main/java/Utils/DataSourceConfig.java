/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

import jakarta.annotation.sql.DataSourceDefinition;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;

/**
 *
 * @author OIJ
 */

@Singleton
@Startup
@DataSourceDefinition(
    name = "java:app/jdbc/MySQL",
    className = "com.mysql.cj.jdbc.MysqlDataSource",
    user = "jakarta",
    password = "Jakarta@1!",
    databaseName = "jakarta",
    serverName = "localhost",
    portNumber = 3306,
    properties = { 
        "useSSL=false",
        "allowPublicKeyRetrieval=true"
    }
)
public class DataSourceConfig {
    // No implementation required
}
