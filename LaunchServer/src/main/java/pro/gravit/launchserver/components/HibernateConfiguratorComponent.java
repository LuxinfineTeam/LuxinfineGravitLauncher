package pro.gravit.launchserver.components;

import org.hibernate.cfg.Configuration;
import pro.gravit.launchserver.LaunchServer;
import pro.gravit.launchserver.dao.LaunchServerDaoFactory;
import pro.gravit.launchserver.hibernate.HibernateManager;
import pro.gravit.launchserver.hibernate.User;
import pro.gravit.launchserver.hibernate.UserDAOImpl;
import pro.gravit.utils.helper.CommonHelper;

import java.nio.file.Paths;

public class HibernateConfiguratorComponent extends Component {
    public String driver;
    public String url;
    public String username;
    public String password;
    public String pool_size;
    public String hibernateConfig;
    public boolean parallelHibernateInit;
    @Override
    public void preInit(LaunchServer launchServer) {
        LaunchServerDaoFactory.setUserDaoProvider(UserDAOImpl::new);
        Runnable init = () -> {
            Configuration cfg = new Configuration()
                    .addAnnotatedClass(User.class)
                    .setProperty("hibernate.connection.driver_class", driver)
                    .setProperty("hibernate.connection.url", url)
                    .setProperty("hibernate.connection.username", username)
                    .setProperty("hibernate.connection.password", password)
                    .setProperty("hibernate.connection.pool_size", pool_size);
            if(hibernateConfig != null)
                cfg.configure(Paths.get(hibernateConfig).toFile());
            HibernateManager.sessionFactory = cfg.buildSessionFactory();
        };
        if(parallelHibernateInit)
            CommonHelper.newThread("Hibernate Thread", true, init);
        else
            init.run();
    }

    @Override
    public void init(LaunchServer launchServer) {

    }

    @Override
    public void postInit(LaunchServer launchServer) {
        //UserService service = new UserService();
        //List<User> users = service.findAllUsers();
        //User newUser = new User();
        //newUser.username = "VeryTestUser";
        //newUser.setPassword("12345");
        //service.saveUser(newUser);
        //for(User u : users)
        //{
        //    LogHelper.info("Found User %s", u.username);
        //}
    }
}
