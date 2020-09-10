package com.ibingbo.binlog;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.DeleteRowsEventData;
import com.github.shyiko.mysql.binlog.event.Event;
import com.github.shyiko.mysql.binlog.event.EventData;
import com.github.shyiko.mysql.binlog.event.EventType;
import com.github.shyiko.mysql.binlog.event.RotateEventData;
import com.github.shyiko.mysql.binlog.event.TableMapEventData;
import com.github.shyiko.mysql.binlog.event.UpdateRowsEventData;
import com.github.shyiko.mysql.binlog.event.WriteRowsEventData;
import com.github.shyiko.mysql.binlog.event.deserialization.EventDeserializer;

/**
 * @author zhangbingbing
 * @date 2020/9/10
 */
public class BinLogApp {

    private static final String host = "localhost";
    private static final String userName = "root";
    private static final String password = "123456";
    private static final int port = 8306;

    private static Map<String, BinaryLogClient.EventListener> table2ListenerMap = new HashMap<>();
    private static Map<Long, String> tableId2NameMap = new HashMap<>();

    private static Map<String, Map<String, Column>> table2ColumnMap = new HashMap<>();
    private static String binlogFile = "mysql-bin.000017";
    private static Long binlogPosition = 6L;

    public static void main(String[] args) throws Exception {
        Pair<String, Long> binlogInfo = parseBinlogFile();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    System.out.println("-------system shutdown-------");
                    logPosition();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        registerListener();
        BinaryLogClient logClient = new BinaryLogClient(host, port, userName, password);
        logClient.setBinlogFilename(binlogInfo.getLeft());
        logClient.setBinlogPosition(binlogInfo.getRight());
        EventDeserializer eventDeserializer = new EventDeserializer();
        logClient.setEventDeserializer(eventDeserializer);
        logClient.registerEventListener(new BinaryLogClient.EventListener() {
            @Override
            public void onEvent(Event event) {
                System.out.println(event.toString());
                EventType eventType = event.getHeader().getEventType();
                if (EventType.ROTATE.equals(eventType)) {
                    RotateEventData data = event.getData();
                    binlogFile = data.getBinlogFilename();
                    binlogPosition = data.getBinlogPosition();
                }
                if (EventType.TABLE_MAP.equals(eventType)) {
                    TableMapEventData data = event.getData();
                    if (!tableId2NameMap.containsKey(data.getTableId())) {
                        tableId2NameMap
                                .put(data.getTableId(), String.format("%s.%s", data.getDatabase(), data.getTable()));
                    }
                }
                if (EventType.UPDATE_ROWS.equals(eventType) || EventType.EXT_UPDATE_ROWS.equals(eventType)) {
                    UpdateRowsEventData data = event.getData();
                    String tableName = tableId2NameMap.get(data.getTableId());
                    if ("test.user".equals(tableName)) {
                        for (Map.Entry<Serializable[], Serializable[]> row : data.getRows()) {
                            Map<String, Column> columnMap = table2ColumnMap.get(tableName);
                            System.out.println("---------before---------");
                            columnMap.values().forEach(column -> {
                                System.out.println(column.name);
                                System.out.println(column.index);
                                System.out.println(column.type);
                                System.out.println(row.getKey()[column.index - 1]);
                            });
                            System.out.println("---------after---------");
                            columnMap.values().forEach(column -> {
                                System.out.println(column.name);
                                System.out.println(column.index);
                                System.out.println(column.type);
                                System.out.println(row.getValue()[column.index - 1]);
                            });
                        }
                    }
                }
                if (EventType.WRITE_ROWS.equals(eventType) || EventType.EXT_WRITE_ROWS.equals(eventType)) {
                    WriteRowsEventData data = event.getData();
                    String tableName = tableId2NameMap.get(data.getTableId());
                    if ("test.user".equals(tableName)) {
                        Map<String, Column> columnMap = table2ColumnMap.get(tableName);

                        data.getRows().forEach(serializables -> {
                            columnMap.forEach((k, v) -> {
                                System.out.println(v.name);
                                System.out.println(v.index);
                                System.out.println(v.type);
                                System.out.println(serializables[v.index - 1]);
                            });
                        });
                    }
                }
                if (EventType.DELETE_ROWS.equals(eventType) || EventType.EXT_DELETE_ROWS.equals(eventType)) {
                    DeleteRowsEventData data = event.getData();
                    String tableName = tableId2NameMap.get(data.getTableId());
                    if ("test.user".equals(tableName)) {
                        Map<String, Column> columnMap = table2ColumnMap.get(tableName);
                        System.out.println("----------delete--------");
                        data.getRows().forEach(serializables -> {
                            columnMap.forEach((k, v) -> {
                                System.out.println(v.name);
                                System.out.println(v.index);
                                System.out.println(v.type);
                                System.out.println(serializables[v.index - 1]);
                            });
                        });
                    }
                }
            }
        });
        logClient.connect();
    }

    public static class Column {

        public String name;
        public int index;
        public String type;

        @Override
        public String toString() {
            return "Column{" +
                    "name='" + name + '\'' +
                    ", index=" + index +
                    ", type='" + type + '\'' +
                    '}';
        }

    }

    public static void registerListener() throws Exception {
        String table = "test.user";
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port, userName, password);
        PreparedStatement statement = connection.prepareStatement("SELECT TABLE_SCHEMA, TABLE_NAME, COLUMN_NAME, "
                + "DATA_TYPE, ORDINAL_POSITION " +
                "FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = ? and TABLE_NAME = ?");
        statement.setString(1, "test");
        statement.setString(2, "user");
        ResultSet resultSet = statement.executeQuery();
        Map<String, Column> map = new HashMap<>(resultSet.getRow());
        while (resultSet.next()) {
            String schema = resultSet.getString("TABLE_SCHEMA");
            String tableName = resultSet.getString("TABLE_NAME");
            String column = resultSet.getString("COLUMN_NAME");
            int index = resultSet.getInt("ORDINAL_POSITION");
            String type = resultSet.getString("DATA_TYPE");
            if (column != null && index >= 1) {
                Column col = new Column();
                col.index = index;
                col.name = column;
                col.type = type;
                map.put(column, col);
            }
        }
        table2ColumnMap.put(table, map);
        statement.close();
        resultSet.close();
        connection.close();
        table2ListenerMap.put(table, new BinaryLogClient.EventListener() {
            @Override
            public void onEvent(Event event) {
                System.out.println(event.getHeader().toString());
                EventData data = event.getData();
                if (data instanceof UpdateRowsEventData) {
                    System.out.println("-------------update-------------");
                    System.out.println(data.toString());
                } else if (data instanceof WriteRowsEventData) {
                    System.out.println("-------------insert-------------");
                    System.out.println(data.toString());
                } else if (data instanceof DeleteRowsEventData) {
                    System.out.println("-------------delete-------------");
                    System.out.println(data.toString());

                }
            }
        });
    }

    public static Pair<String, Long> parseBinlogFile() throws Exception {
        InputStream in = new Object().getClass().getResourceAsStream("/binlog/log");
        Properties properties = new Properties();
        properties.load(in);
        Pair<String, Long> pair =
                new ImmutablePair<>(properties.getProperty("binlog"), Long.valueOf(properties.getProperty(
                        "position")));
        return pair;
    }

    public static void logPosition() throws Exception {
        Properties properties = new Properties();
        properties.setProperty("binlog", binlogFile);
        properties.setProperty("position", binlogPosition.toString());
        properties.store(new FileOutputStream(new Object().getClass().getResource("/binlog/log").getPath()),
                "synchronous recording");

    }

}
