package repository;

import model.Voluntar;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import java.util.ArrayList;

public class RepositoryHibernateVoluntarDB implements IRepositoryVoluntar{

    private static SessionFactory sessionFactory;

    static void initialize() {
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            System.err.println("Exceptie "+e);
            StandardServiceRegistryBuilder.destroy( registry );
        }
    }

    static void close() {
        if ( sessionFactory != null ) {
            sessionFactory.close();
        }
    }



    @Override
    public Voluntar checkBypass(String username, String password) {
        initialize();
        Voluntar voluntar=null;
        //Iterable<Voluntar> a = findAll();
        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            System.out.println("salut");
            Query query = session.createQuery("from Voluntar where username=:username");
            query.setParameter("username",username);
           // query.setParameter("password",password.hashCode());
            session.getTransaction().commit();
            voluntar = (Voluntar) query.uniqueResult();
            close();
        }
        catch (Exception e){
            close();
        }
        return voluntar;
    }

    @Override
    public void add(Voluntar elem) {
        initialize();

        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(elem);
            session.getTransaction().commit();
            close();
        }
        catch (Exception e){
            close();
        }

    }

    @Override
    public void delete(Long aLong) {
        initialize();

        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(aLong);
            session.getTransaction().commit();
            close();
        }
        catch (Exception e){
            close();
        }
    }

    @Override
    public void update(Voluntar elem, Long aLong) {

    }

    @Override
    public Voluntar findById(Long aLong) {
        return null;
    }

    @Override
    public Iterable<Voluntar> findAll() {
        initialize();
        ArrayList<Voluntar> voluntari=null;
        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            System.out.println("salut");
            Query query = session.createQuery("from Voluntar");
            session.getTransaction().commit();
            voluntari = (ArrayList<Voluntar>) query.getResultList();
            close();
        }
        catch (Exception e){
            close();
        }
        return voluntari;
    }
}
