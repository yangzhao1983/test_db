import oracle.ucp.jdbc.PoolDataSource;
import oracle.ucp.jdbc.PoolDataSourceFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Created by kaiser_zhao on 2019/9/17.
 */
public class OracleDBConnectionManager {

    private static final String CONNECTION_CLASS_NAME = "oracle.jdbc.pool.OracleDataSource";
    private static final String JDBC_URL = "jdbc:oracle:thin:@//slc04ktl.us.oracle.com:1521/xe";
    private static final int POOL_SIZE_INITIAL = 1;
    private static final int POOL_SIZE_MIN = 1;
    private static final int POOL_SIZE_MAX = 3;
    private static final int POOL_CONNECTION_REUSE_COUNT_MAX = 1000;
    private static final int POOL_CONNECTION_REUSE_TIME_MAX = 150;
    private static final int POOL_CONNECTION_WAIT_TIMEOUT_MAX = 300;
    private static final boolean USE_CONNECTION_POOL = true;

    public static DataSource getDataSource() {
        Connection testConn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            PoolDataSource pds = PoolDataSourceFactory.getPoolDataSource();
            pds.setConnectionFactoryClassName(CONNECTION_CLASS_NAME);

            pds.setURL(JDBC_URL);
            pds.setUser("sys as SYSDBA");
            pds.setPassword("oracle");


            pds.setInitialPoolSize(POOL_SIZE_INITIAL);
            pds.setMinPoolSize(POOL_SIZE_MIN);
            pds.setMaxPoolSize(POOL_SIZE_MAX);
            pds.setMaxConnectionReuseCount(POOL_CONNECTION_REUSE_COUNT_MAX);
            pds.setMaxConnectionReuseTime(POOL_CONNECTION_REUSE_TIME_MAX);
            pds.setValidateConnectionOnBorrow(USE_CONNECTION_POOL);
            pds.setConnectionWaitTimeout(POOL_CONNECTION_WAIT_TIMEOUT_MAX);

            Properties connProps = new Properties();
            //auto-commit should always be false
            connProps.setProperty("autoCommit", "true");
            pds.setConnectionProperties(connProps);

            testConn = pds.getConnection();

            stmt = testConn.createStatement();
            rs = stmt.executeQuery("select * from STUDENT");
            while(rs.next()){
                System.out.println("===========");
                System.out.println(rs.getString("SNO"));
                System.out.println(rs.getString("SNAME"));
                System.out.println(rs.getString("SSEX"));
                System.out.println(rs.getDate("SBIRTHDAY"));
                System.out.println(rs.getString("CLASS"));
            }
            return pds;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            try{
                rs.close();
            }catch (Exception e){
                // do sth
            }finally {
                rs = null;
            }

            try{
               stmt.close();
            }catch (Exception e){
                // do sth
            }finally {
                stmt = null;
            }

            try{
                testConn.close();
            }catch (Exception e){
                // do sth
            }finally {
                testConn = null;
            }
        }
        return null;
    }

    public static void main(String...strings){
        getDataSource();
    }
}
