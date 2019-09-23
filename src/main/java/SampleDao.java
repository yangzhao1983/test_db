import groovy.sql.GroovyRowResult;
import groovy.sql.Sql;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by kaiser_zhao on 2019/9/20.
 */
public class SampleDao {

    private static OracleDBConnectionManager oracleDBConnectionManager;

    public static void main(String...strings){
        select("select * from STUDENT");
    }

    public static void select(String query) {

        Sql querySql = null;

        try {
            oracleDBConnectionManager = new OracleDBConnectionManager();
            querySql = new Sql(oracleDBConnectionManager.getDataSource());
            GroovyRowResult result = querySql.firstRow(query);

            System.out.println("Get the first row of the results");
            System.out.println((String)result.get("SNO"));

            List<GroovyRowResult> groovyRowResults = querySql.rows(query);


            for (GroovyRowResult groovyRowResult1: groovyRowResults) {
                List<String> inner = new ArrayList<String>();
                Set<String> keys = groovyRowResult1.keySet();


                for (String k: keys) {
                    if(groovyRowResult1.get(k) instanceof  String) {
                        System.out.println("Value for String " + k + " is " + (String) result.get(k));
                    }
                }

            }
        } catch (SQLException e) {
        } finally {
            if(querySql!=null){
                querySql.close();
            }
        }
    }
}
