package repository;

import Utils.JdbcUtils;
import model.Donatie;
import model.Donator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class RepositoryDonatorDB implements IRepositoryDonator{
    private JdbcUtils dbUtils;



    private static final Logger logger= LogManager.getLogger();

    public RepositoryDonatorDB(Properties props){
        dbUtils=new JdbcUtils(props);
    }

    public Donator extractEntity(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id_donator");
        String firstname = resultSet.getString("firstname");
        String lastname = resultSet.getString("lastname");
        String adress = resultSet.getString("adress");
        String phone = resultSet.getString("phone");

        Donator donator=new Donator(firstname,lastname,adress,phone);
        donator.setId(id);
        return donator;
    }





    @Override
    public void add(Donator elem) {
        logger.traceEntry("saving task {} ",elem);
        Connection con= dbUtils.getConnection();
        try(PreparedStatement preSmt=con.prepareStatement("insert into Donatori (firstname,lastname,adress,phone) values (?,?,?,?)")){
            preSmt.setString(1,elem.getFirstname());
            preSmt.setString(2,elem.getLastname());
            preSmt.setString(3,elem.getAdress());
            preSmt.setString(4,elem.getPhone());
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
        try(PreparedStatement preSmt=con.prepareStatement("DELETE FROM Donatori WHERE id_donator = ?;")){
            preSmt.setLong(1,aLong);
            int result=preSmt.executeUpdate();
            logger.trace("Deleted {} instances",result);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        logger.traceExit();

    }

    @Override
    public void update(Donator elem, Long aLong) {
        logger.traceEntry("updating task {} ",elem);

        try(Connection con= dbUtils.getConnection();
            PreparedStatement preSmt=con.prepareStatement("UPDATE Donatori SET firstname = ?,lastname = ?, adress = ?, phone = ? WHERE id_donator = ?;")){
            preSmt.setString(1,elem.getFirstname());
            preSmt.setString(2,elem.getLastname());
            preSmt.setString(3,elem.getAdress());
            preSmt.setString(4,elem.getPhone());
            preSmt.setLong(5,aLong);
            int result=preSmt.executeUpdate();
            logger.trace("Saved {} instances",result);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        logger.traceExit();
    }

    @Override
    public Donator findById(Long aLong) {
        logger.traceEntry();

        Donator donator=null;
        try(Connection con=dbUtils.getConnection();
            PreparedStatement preSmt=con.prepareStatement("select * from Donatori where id_donator=?")) {
            preSmt.setLong(1,aLong);
            ResultSet result=preSmt.executeQuery();

            if (result.next()) {
                donator=extractEntity(result);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        logger.traceExit();
        return donator;
    }

    @Override
    public Iterable<Donator> findAll() {
        logger.traceEntry();

        List<Donator> donators=new ArrayList<>();
        try(Connection con=dbUtils.getConnection();
            PreparedStatement preSmt=con.prepareStatement("select * from Donatori");
            ResultSet result=preSmt.executeQuery();) {

            while (result.next()) {
                Donator donator=extractEntity(result);
                donators.add(donator);

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        logger.traceExit();
        return donators;
    }

    @Override
    public Iterable<Donator> getAllByName(String name1, String name2) {
            logger.traceEntry();

        List<Donator> donators=new ArrayList<>();

        try(Connection con=dbUtils.getConnection();
            PreparedStatement preSmt=con.prepareStatement("select * from Donatori where (firstname like '"+name1+"%' and lastname like '"+name2+"%') or (firstname like '"+name2+"%' and lastname like '"+name1+"%')")) {
            //preSmt.setString(1,name1);
            //preSmt.setString(2,name2);
            //preSmt.setString(3,name2);
            //preSmt.setString(4,name1);
            ResultSet result=preSmt.executeQuery();

                while (result.next()) {
                    donators.add(extractEntity(result));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            logger.traceExit();
            return donators;
        }



    @Override
    public Donator findByPhone(String phone) {
        logger.traceEntry();

        Donator donator=null;
        try(Connection con=dbUtils.getConnection();
            PreparedStatement preSmt=con.prepareStatement("select * from Donatori where phone=?")) {
            preSmt.setString(1,phone);
            ResultSet result=preSmt.executeQuery();

            if (result.next()) {
                donator=extractEntity(result);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        logger.traceExit();
        return donator;
    }
}
