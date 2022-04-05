package repository;

import Utils.JdbcUtils;
import model.Donatie;
import model.Voluntar;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class RepositoryVoluntarDB implements IRepositoryVoluntar{
    private JdbcUtils dbUtils;



    private static final Logger logger= LogManager.getLogger();

    public RepositoryVoluntarDB(Properties props){
        dbUtils=new JdbcUtils(props);
    }

    public Voluntar extractEntity(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id_voluntar");
        String username = resultSet.getString("username");
        Integer password =resultSet.getInt("password");

        Voluntar voluntar = new Voluntar(username,password);
        voluntar.setId(id);
        return voluntar;
    }



    @Override
    public void add(Voluntar elem) {
        logger.traceEntry("saving task {} ",elem);
        Connection con= dbUtils.getConnection();
        try(PreparedStatement preSmt=con.prepareStatement("insert into Voluntari (username, password) values (?,?)")){
            preSmt.setString(1,elem.getUsername());
            preSmt.setInt(2,elem.getPassword());
            int result=preSmt.executeUpdate();
            logger.trace("Saved {} instances",result);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        logger.traceExit();
    }

    @Override
    public void delete(Long aLong) {
        logger.traceEntry("deleting task {} ",aLong);
        Connection con= dbUtils.getConnection();
        try(PreparedStatement preSmt=con.prepareStatement("DELETE FROM Voluntari WHERE id_voluntar = ?;")){
            preSmt.setLong(1,aLong);
            int result=preSmt.executeUpdate();
            logger.trace("Deleted {} instances",result);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        logger.traceExit();
    }

    @Override
    public void update(Voluntar elem, Long aLong) {
        logger.traceEntry("updating task {} ",elem);

        try(Connection con= dbUtils.getConnection();
            PreparedStatement preSmt=con.prepareStatement("UPDATE Voluntari SET username = ?,password = ? WHERE id_voluntar = ?;")){
            preSmt.setString(1,elem.getUsername());
            preSmt.setInt(2,elem.getPassword());
            preSmt.setLong(3,aLong);
            int result=preSmt.executeUpdate();
            logger.trace("Saved {} instances",result);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        logger.traceExit();
    }

    @Override
    public Voluntar findById(Long aLong) {
        logger.traceEntry();

        Voluntar voluntar=null;
        try(Connection con=dbUtils.getConnection();
            PreparedStatement preSmt=con.prepareStatement("select * from Voluntari where id_voluntar=?")) {
            preSmt.setLong(1,aLong);
            ResultSet result=preSmt.executeQuery();
            if (result.next()) {
                voluntar=extractEntity(result);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        logger.traceExit();
        return voluntar;
    }

    @Override
    public Iterable<Voluntar> findAll() {
        logger.traceEntry();

        List<Voluntar> voluntari=new ArrayList<>();
        try(Connection con=dbUtils.getConnection();
            PreparedStatement preSmt=con.prepareStatement("select * from Voluntari");
            ResultSet result=preSmt.executeQuery();) {

            while (result.next()) {
                Voluntar voluntar=extractEntity(result);
                voluntari.add(voluntar);

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        logger.traceExit();
        return voluntari;
    }


    @Override
    public Voluntar checkBypass(String username, String password) {
        logger.traceEntry();

        Voluntar voluntar = null;
        try(Connection con=dbUtils.getConnection();
            PreparedStatement preSmt=con.prepareStatement("select * from Voluntari where username=? and password=?")) {
            preSmt.setString(1,username);
            preSmt.setInt(2,password.hashCode());
            ResultSet result=preSmt.executeQuery();


            if (result.next()) {
                voluntar = extractEntity(result);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        logger.traceExit();
        return voluntar;
    }

}
