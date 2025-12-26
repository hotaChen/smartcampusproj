package com.example.smartcampus.config;

import org.h2.tools.Server;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class H2ServerConfig {

    @EventListener(ApplicationReadyEvent.class)
    public void startServer() throws SQLException {
        String sharedFolderPath = "\\\\DESKTOP-2QVAB5J\\h2-shared";
        String dbFileName = "smartcampus";
        String fullPath = sharedFolderPath + "\\" + dbFileName;

        System.out.println("========================================");
        System.out.println("正在启动 H2 TCP 服务器...");
        System.out.println("共享文件夹路径: " + sharedFolderPath);

        File folder = new File(sharedFolderPath);
        System.out.println("共享文件夹是否存在: " + folder.exists());
        System.out.println("共享文件夹可访问: " + folder.canRead());

        try {
            Server server = Server.createTcpServer(
                "-tcp",
                "-tcpAllowOthers",
                "-tcpPort", "9092",
                "-ifNotExists",
                "-baseDir", sharedFolderPath
            ).start();

            System.out.println("H2 TCP 服务器启动成功!");
            System.out.println("TCP连接: jdbc:h2:tcp://172.28.96.1:9092/" + dbFileName);
            System.out.println("直接连接: jdbc:h2:" + fullPath);
            System.out.println("数据库文件路径: " + fullPath + ".mv.db");

            Thread.sleep(1000);

            File dbFile = new File(fullPath + ".mv.db");
            System.out.println("数据库文件已创建: " + dbFile.exists());

            if (!dbFile.exists()) {
                System.out.println("正在创建数据库...");
                createDatabase(sharedFolderPath, dbFileName);
            }

            System.out.println("========================================");

        } catch (SQLException e) {
            System.err.println("========================================");
            System.err.println("H2 服务器启动失败!");
            System.err.println("错误信息: " + e.getMessage());
            System.err.println("========================================");
            e.printStackTrace();
            throw e;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void createDatabase(String folderPath, String dbName) {
        String url = "jdbc:h2:" + folderPath + "/" + dbName + ";DB_CLOSE_DELAY=-1";

        System.out.println("创建数据库连接: " + url);

        try (Connection conn = DriverManager.getConnection(url, "sa", "");
             Statement stmt = conn.createStatement()) {

            System.out.println("数据库连接成功!");

            File dbFile = new File(folderPath + "/" + dbName + ".mv.db");
            System.out.println("验证数据库文件: " + dbFile.exists());
            System.out.println("数据库文件大小: " + (dbFile.exists() ? dbFile.length() + " bytes" : "N/A"));

        } catch (SQLException e) {
            System.err.println("数据库创建失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
