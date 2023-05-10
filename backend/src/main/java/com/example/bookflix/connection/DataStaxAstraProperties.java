package com.example.bookflix.connection;
import org.springframework.boot.context.properties.ConfigurationProperties;
import java.io.File;

@ConfigurationProperties(prefix = "datastax.astra")     //expose the property
public class DataStaxAstraProperties {
    
    private File secureConnectBundle;

    public File getSecureConnectBundle() {
        return secureConnectBundle;
    }

    public void setSecureConnectBundle(File secureConnectBundle) {
        this.secureConnectBundle = secureConnectBundle;
    }



}


        // <dependency>
    	// 	<groupId>org.apache.cassandra</groupId>
    	// 	<artifactId>cassandra-all</artifactId>
    	// 	<version>4.1.1</version>
		// </dependency>
		// <dependency>
		// 	<groupId>org.cassandraunit</groupId>
		// 	<artifactId>cassandra-unit</artifactId>
		// 	<version>4.3.1.0</version>
		// 	<scope>test</scope>
		// 	<exclusions>
		// 		<exclusion>
		// 			<groupId>junit</groupId>
		// 			<artifactId>junit</artifactId>
		// 		</exclusion>
		// 	</exclusions>
		// </dependency>