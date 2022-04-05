package repository;

import Utils.JdbcUtils;
import model.Donatie;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class RepositoryDonatieDB implements IRepositoryDonatie{
    private JdbcUtils dbUtils;



    private static final Logger logger= LogManager.getLogger();

    public RepositoryDonatieDB(Properties props){
        dbUtils=new JdbcUtils(props);
    }

    public Donatie extractEntity(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id_donatie");
        float sum = resultSet.getFloat("sum");
        Long idCaz = resultSet.getLong("id_caz");
        Long idDonator = resultSet.getLong("id_donator");

        Donatie donation = new Donatie(idDonator,sum,idCaz);
        donation.setId(id);
        return donation;
    }


    @Override
    public void add(Donatie elem) {
        logger.traceEntry("saving task {} ",elem);
        Connection con= dbUtils.getConnection();
        try(PreparedStatement preSmt=con.prepareStatement("insert into Donatii (sum,id_caz,id_donator) values (?,?,?)")){
            preSmt.setFloat(1,elem.getSum());
            preSmt.setLong(2,elem.getCazID());
            preSmt.setLong(3,elem.getDonatorID());
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
        try(PreparedStatement preSmt=con.prepareStatement("DELETE FROM Donatii WHERE id_donatie = ?;")){
            preSmt.setLong(1,aLong);
            int result=preSmt.executeUpdate();
            logger.trace("Deleted {} instances",result);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        logger.traceExit();
    }

    @Override
    public void update(Donatie elem, Long aLong) {
        logger.traceEntry("updating task {} ",elem);

        try(Connection con= dbUtils.getConnection();
            PreparedStatement preSmt=con.prepareStatement("UPDATE Donatii SET sum = ?,id_caz = ?, id_donator = ? WHERE id_donatie = ?;")){
            preSmt.setFloat(1,elem.getSum());
            preSmt.setLong(2,elem.getCazID());
            preSmt.setLong(3,elem.getDonatorID());
            preSmt.setLong(4,aLong);
            int result=preSmt.executeUpdate();
            logger.trace("Saved {} instances",result);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        logger.traceExit();
    }

    @Override
    public Donatie findById(Long aLong) {
        logger.traceEntry();

        Donatie donation=null;
        try(Connection con=dbUtils.getConnection();
            PreparedStatement preSmt=con.prepareStatement("select * from Donatii where id_donatie=?")) {
            preSmt.setLong(1,aLong);
            ResultSet result=preSmt.executeQuery();

            if (result.next()) {
                donation=extractEntity(result);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        logger.traceExit();
        return donation;
    }

    @Override
    public Iterable<Donatie> findAll() {
        logger.traceEntry();

        List<Donatie> donations=new ArrayList<>();
        try(Connection con=dbUtils.getConnection();
            PreparedStatement preSmt=con.prepareStatement("select * from Donatii");
            ResultSet result=preSmt.executeQuery();) {

            while (result.next()) {
                Donatie donation=extractEntity(result);
                donations.add(donation);

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        logger.traceExit();
        return donations;

    }
}
