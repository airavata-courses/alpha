package team.alpha;

import com.sun.jersey.spi.container.servlet.ServletContainer;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;

public class JettyServer {

    private Application application;
    private Server server;

    public JettyServer(Application application) {
        this.application = application;
    }

    private void run() throws Exception {
        ServletContainer container = new ServletContainer(application);
        server = new Server(Configuration.DEFAULT_JETTY_PORT);
        Context root = new Context(server, "/", Context.SESSIONS);
        root.addServlet(new ServletHolder(container), "/*");
        server.start();
    }

    private void shutdown() throws Exception {
        server.stop();
        server.join();
    }

    public static void main(String[] args) throws Exception {
        Configuration configuration = new Configuration();
        JettyServer jetty = new JettyServer(new Application(configuration));
        jetty.run();
    }
}
