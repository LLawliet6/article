package com.LS.article.dao;


import com.LS.article.util.JDBCUtil;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BaseDao {
    public List<Integer> baseBatchInsert(String sql, List<Object[]> paramsList) {
        List<Integer> generatedIds = new ArrayList<>();

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            conn.setAutoCommit(false); // 开启事务
            for (Object[] params : paramsList) {
                for (int i = 0; i < params.length; i++) {
                    pstmt.setObject(i + 1, params[i]);
                }
                pstmt.addBatch();
            }

            pstmt.executeBatch();
            conn.commit();

            // 获取自增 ID
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                while (rs.next()) {
                    generatedIds.add(rs.getInt(1));
                }
            }

            System.out.println("批量插入成功，生成的 ID：" + generatedIds);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("批量插入失败：" + e.getMessage());
        }

        return generatedIds;
    }
    public <T> T baseQueryObject(Class<T> clazz, String sql, Object... args) {
        T t = null;
        Connection connection = JDBCUtil.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Object value = resultSet.getObject(1); // 先获取结果

                // 根据目标类型转换结果
                if (clazz == Integer.class) {
                    value = ((Number) value).intValue();
                } else if (clazz == Long.class) {
                    value = ((Number) value).longValue();
                } else if (clazz == BigInteger.class) {
                    value = new BigInteger(value.toString());
                }
                t = clazz.cast(value);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            JDBCUtil.releaseConnection();
        }
        return t;
    }


    // 公共的查询方法，返回对象的集合
    public <T> List<T> baseQuery(Class<T> clazz, String sql, Object... args) {
        List<T> list = new ArrayList<>();
        Connection connection = JDBCUtil.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // 准备语句对象
            preparedStatement = connection.prepareStatement(sql);
            // 设置语句上的参数
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }

            // 执行查询
            resultSet = preparedStatement.executeQuery();

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            // 将结果集通过反射封装成实体类对象
            while (resultSet.next()) {
                // 使用反射实例化对象
                T obj = clazz.getDeclaredConstructor().newInstance();

                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnLabel(i);
                    Object value = resultSet.getObject(columnName);

                    // 处理 null 值
                    if (value != null) {
                        // 处理 LocalDateTime 类型字段
                        if (value.getClass().equals(LocalDateTime.class)) {
                            value = Timestamp.valueOf((LocalDateTime) value);
                        }

                        try {
                            Field field = clazz.getDeclaredField(columnName);
                            field.setAccessible(true);
                            field.set(obj, value);
                        } catch (NoSuchFieldException e) {
                            // 处理数据库字段和实体类不匹配的情况
                            System.err.println("No such field: " + columnName + " in class " + clazz.getSimpleName());
                        }
                    }
                }
                list.add(obj);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            // 关闭资源
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            JDBCUtil.releaseConnection();
        }
        return list;
    }
    
    // 通用的增删改方法
    public int baseUpdate(String sql,Object ... args) {
        // 获取连接
        Connection connection = JDBCUtil.getConnection();
        PreparedStatement preparedStatement=null;
        int rows = 0;
        try {
            // 准备语句对象
            preparedStatement = connection.prepareStatement(sql);
            // 设置语句上的参数
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i+1,args[i]);
            }

            // 执行 增删改 executeUpdate
            rows = preparedStatement.executeUpdate();
            // 释放资源(可选)


        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (null != preparedStatement) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

            }
            JDBCUtil.releaseConnection();
        }
        // 返回的是影响数据库记录数
        return rows;
    }


    // 批量执行更新操作
    // 批量更新方法
    public void baseBatchUpdate(String sql, List<Object[]> paramsList) {
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false); // 开启事务
            for (Object[] params : paramsList) {
                for (int i = 0; i < params.length; i++) {
                    pstmt.setObject(i + 1, params[i]);
                }
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            conn.commit();
            System.out.println("批量插入完成，SQL：" + sql);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("批量插入失败：" + e.getMessage());
        }
    }

}
