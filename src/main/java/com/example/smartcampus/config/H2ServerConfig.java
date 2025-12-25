package com.example.smartcampus.config;

import org.h2.tools.Server;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
public class H2ServerConfig {

    @EventListener(ApplicationReadyEvent.class)
    public void startServer() throws SQLException {
        String sharedFolderPath = "\\\\192.168.1.100\\h2-shared";
        
        Server.createTcpServer(
            "-tcp",
            "-tcpAllowOthers",
            "-tcpPort", "9092",
            "-ifNotExists",
            "-baseDir", sharedFolderPath
        ).start();

        System.out.println("========================================");
        System.out.println("H2 TCP Server started successfully!");
        System.out.println("URL: jdbc:h2:tcp://192.168.1.100:9092/smartcampus");
        System.out.println("Database file: " + sharedFolderPath + "\\smartcampus.mv.db");
        System.out.println("========================================");
    }
}
