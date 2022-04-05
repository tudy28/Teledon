package repository;

import Utils.JdbcUtils;
import model.Caz;
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

public class RepositoryCazDB implements IRepositoryCaz{
    private JdbcUtils dbUtils;



    private static final Logger logger= LogManager.getLogger();

    public RepositoryCazDB(Properties props){
        dbUtils=new JdbcUtils(props);
    }

    public Caz extractEntity(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id_caz");
        String nume = resultSet.getString("nume");
        Float sumaTotala = resultSet.getFloat("suma_totala");

        Caz caz = new Caz(nume,sumaTotala);
        caz.setId(id);
        return caz;
    }



    @Override
    public void add(Caz elem) {
        logger.traceEntry("saving task {} ",elem);
        Connection con= dbUtils.getConnection();
        try(PreparedStatement preSmt=con.prepareStatement("insert into Cazuri (nume, suma_totala) values (?,?)")){
            preSmt.setString(1,elem.getName());
            preSmt.setFloat(2,elem.getTotalSum());
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
        try(PreparedStatement preSmt=con.prepareStatement("DELETE FROM Cazuri WHERE id_caz = ?;")){
            preSmt.setLong(1,aLong);
            int result=preSmt.executeUpdate();
            logger.trace("Deleted {} instances",result);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        logger.traceExit();
    }

    @Override
    public void update(Caz elem, Long aLong) {
        logger.traceEntry("updating task {} ",elem);

        try(Connection con= dbUtils.getConnection();
            PreparedStatement preSmt=con.prepareStatement("UPDATE Cazuri SET nume = ?,suma_totala = ? WHERE id_caz = ?;")){
            preSmt.setString(1,elem.getName());
            preSmt.setFloat(2,elem.getTotalSum());
            preSmt.setLong(3,aLong);
            int result=preSmt.executeUpdate();
            logger.trace("Saved {} instances",result);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        logger.traceExit();
    }

    @Override
    public Caz findById(Long aLong) {
        logger.traceEntry();

        Caz caz=null;
        try(Connection con=dbUtils.getConnection();
            PreparedStatement preSmt=con.prepareStatement("select * from Cazuri where id_caz=?")) {
            preSmt.setLong(1,aLong);
            ResultSet result=preSmt.executeQuery();

            if (result.next()) {
                caz=extractEntity(result);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        logger.traceExit();
        return caz;
    }

    @Override
    public Iterable<Caz> findAll() {
        logger.traceEntry();

        List<Caz> cazuri=new ArrayList<>();
        try(Connection con=dbUtils.getConnection();
            PreparedStatement preSmt=con.prepareStatement("select * from Cazuri");
            ResultSet result=preSmt.executeQuery();) {

            while (result.next()) {
                Caz caz=extractEntity(result);
                cazuri.add(caz);

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        logger.traceExit();
        return cazuri;
    }
}
