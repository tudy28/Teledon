package repository;

import model.Entity;
import model.validators.Validator;

import java.sql.*;
import java.util.ArrayList;

public abstract class AbstractDbRepository<ID, E extends Entity<ID>> implements Repository<ID,E> {
    private String dbName;
    private String url;
    private String username;
    private String password;
    private Validator<E> validator;

    public AbstractDbRepository(String dbName, String url, String username, String password, Validator<E> validator) {
        this.dbName = dbName;
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    protected abstract E extractEntity(ResultSet resultSet) throws SQLException;

    protected abstract String createEntityAsQueryInsert(E entity);
    protected abstract String createEntityAsQueryDelete(ID id);
    protected abstract String createEntityAsQueryFindOne(ID id);

    @Override
    public Iterable<E> findAll(){
        return getAllFromDatabase();
    }

    @Override
    public E findById(ID id){
        if (id == null) {
            throw new IllegalArgumentException("ID must not be null...");
        }
        return getOneFromDatabase(id);

    }

    @Override
    public void add(E entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity must not be null...");
        }
        validator.validate(entity);
        insertToDatabase(entity);
    }

    @Override
    public void delete(ID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID must not be null...");
        }
        E e=findById(id);
        if(e != null) {
            deleteFromDatabase(id);
        }

    }

    @Override
    public void update(E newEntity, ID id) {
        E oldEntity = findById(id);
        if(oldEntity!=null) {
            deleteFromDatabase(oldEntity.getId());
            insertToDatabase(newEntity);
        }
    }



    protected void insertToDatabase(E entity) {

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(createEntityAsQueryInsert(entity)))
        {
            statement.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    protected void deleteFromDatabase(ID id) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statementDrop = connection.prepareStatement(createEntityAsQueryDelete(id)))
        {
            statementDrop.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    protected E getOneFromDatabase(ID id) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(createEntityAsQueryFindOne(id));
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                return extractEntity(resultSet);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    protected Iterable<E> getAllFromDatabase() {
        ArrayList<E> list = new ArrayList<E>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + dbName);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                E entity = extractEntity(resultSet);
                list.add(entity);
            }
            return list;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    protected int size() {
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) AS size FROM "+dbName);
            ResultSet resultSet = statement.executeQuery();)
        {
            int count=0;
            while(resultSet.next())
                count=resultSet.getInt("size");
            return count;
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }
}
