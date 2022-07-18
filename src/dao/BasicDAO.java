package dao;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import utils.JDBCUtilsByDruid;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * DAO:DataAccessObjects,就是将数据库操作都封装起来
 * BasicDAO,其他DAO的父类
 */
public class BasicDAO<T> {

    private QueryRunner qr = new QueryRunner();
    //开发通用的DML操作
    public int update(String sql,Object...parameters){
        Connection connection = null;
        try {
            connection= JDBCUtilsByDruid.getConnect();
            int rows = qr.update(connection,sql,parameters);
            return rows;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            JDBCUtilsByDruid.closeConnect(null,null,connection);
        }
    }
    //通用的多行查询操作
    public List<T> queryMany(String sql,Class<T> clazz,Object...parameters){
        Connection connection = null;
        try {
            connection= JDBCUtilsByDruid.getConnect();
            return qr.query(connection,sql,new BeanListHandler<>(clazz),parameters);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            JDBCUtilsByDruid.closeConnect(null,null,connection);
        }
    }

    public T querySingle(String sql,Class clazz,Object...parameters) {
        Connection connection = null;
        try {
            connection=JDBCUtilsByDruid.getConnect();
            return qr.query(connection,sql,new BeanHandler<T>(clazz),parameters);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            JDBCUtilsByDruid.closeConnect(null,null,connection);
        }
    }

    public Object queryScalar(String sql, Object... parameters) {

        Connection connection = null;
        try {
            connection = JDBCUtilsByDruid.getConnect();
            return qr.query(connection, sql, new ScalarHandler(), parameters);

        } catch (SQLException e) {
            throw new RuntimeException(e); //将编译异常->运行异常 ,抛出
        } finally {
            JDBCUtilsByDruid.closeConnect(null, null, connection);
        }
    }
}
