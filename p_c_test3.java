package producer;

import java.sql.Connection;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.UUID;

class Goods{
    private String id;
    private String name;
    private double price;

    public Goods(String id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Goods{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
class Producer implements Runnable{
    private final Queue<Goods> queue;

    public Producer(Queue<Goods> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while(true){
            try{
                Thread.sleep(1000);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            synchronized (this.queue){
                if(this.queue.size()>=10){
                    System.out.println(Thread.currentThread().getName()+"容易已满，停止生产，等待消费");
                    try{
                        this.queue.wait();
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                }else{
                    //当前时间戳
                    //String.valueOf(System.currentTimeMIllis());
                    String id=UUID.randomUUID().toString();
                    String name="包子";
                    double price=new Random().nextDouble();
                    Goods goods=new Goods(id,name,price);
                    System.out.println(Thread.currentThread().getName()+"生产商品"+goods);
                    this.queue.add(goods);
                }
            }
        }
    }
}
class Consumer implements  Runnable{
    private final Queue<Goods> queue;

    public Consumer(Queue<Goods> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while(true){
            try{
                Thread.sleep(1000);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            synchronized (this.queue){
                if(this.queue.isEmpty()) {
                    System.out.println(Thread.currentThread().getName() + "容器已空，停止消费，等待生产");
                    this.queue.notifyAll();
                }else{
                    Goods goods=this.queue.poll();
                    if(goods!=null){
                        System.out.println(Thread.currentThread().getName()+"消费商品"+goods);
                    }
                }
            }
        }
    }
}
final class ProducerConsumerLauncher{
    private  ProducerConsumerLauncher(){

    }

    public static void run(int producers,int consumers){
        System.out.println("生产者："+producers+"消费者："+consumers);
        Queue<Goods> queue=new LinkedList<>();
        Runnable producer=new Producer(queue);
        Runnable consumer=new Consumer(queue);
        //生产者线程
        for(int i=0;i<producers;i++){
            new Thread(producer,"生产者"+i).start();
        }
        //消费者线程
        for(int i=0;i<consumers;i++){
            new Thread(consumer,"消费者"+i).start();
        }
    }
}
public class Test {
    public static void main(String[] args) {
        int defaultProducers=5;
        int defaultConsumers=5;
        int producers=defaultProducers;
        int consumers=defaultConsumers;
        if(args.length==1){
            try{
                producers=Integer.parseInt(args[0]);
            }catch (NumberFormatException e){
                producers=defaultProducers;
            }
        }
        if(args.length==2){
            try{
                producers=Integer.parseInt(args[0]);
            }catch (NumberFormatException e){
                producers=defaultProducers;
            }
            try{
                consumers=Integer.parseInt(args[0]);
            }catch (NumberFormatException e){
                consumers=defaultConsumers;
            }
        }
        if(producers<=0){
            producers=defaultProducers;//开发者，决定需求
        }
        if(consumers<=0){
            consumers=defaultConsumers;
        }
        ProducerConsumerLauncher.run(producers,consumers);
    }
    /*
    public static void main(String[] args) {
        Queue<Goods> queue=new LinkedList<>();
        Runnable producer=new Producer(queue);
        Runnable consumer=new Consumer(queue);
        //生产者线程
        for(int i=0;i<5;i++){
            new Thread(producer,"生产者"+i).start();
        }
        //消费者线程
        for(int i=0;i<5;i++){
            new Thread(consumer,"消费者"+i).start();
        }
        //线程池

        //可配置--外部参数传给程序
        //1.scanner键盘输入
        //2.file
        //3.properties（key=value）
        //4.命令行参数
        //5.数据库 mysql（JDBC）
        //6.System.getProperty
    }
*/
}



运行结果：
生产者：5消费者：5
消费者4容器已空，停止消费，等待生产
消费者3容器已空，停止消费，等待生产
消费者2容器已空，停止消费，等待生产
消费者1容器已空，停止消费，等待生产
消费者0容器已空，停止消费，等待生产
生产者4生产商品Goods{id='1e20de21-8020-4598-ae21-dab0fc4414ed', name='包子', price=0.5883402080798885}
生产者3生产商品Goods{id='b133d2d8-377a-4e12-b39d-429ab6c3bf2b', name='包子', price=0.9863598035105284}
生产者2生产商品Goods{id='bd68a8cb-15e1-4763-ac65-cfd7c2347c80', name='包子', price=0.9678416464584148}
生产者0生产商品Goods{id='3108d92e-7f09-4358-954f-25061e58c605', name='包子', price=0.21578169495497113}
生产者1生产商品Goods{id='2fa283b7-5283-4672-addc-853f6f69b539', name='包子', price=0.1642551464682276}
消费者0消费商品Goods{id='1e20de21-8020-4598-ae21-dab0fc4414ed', name='包子', price=0.5883402080798885}
消费者4消费商品Goods{id='b133d2d8-377a-4e12-b39d-429ab6c3bf2b', name='包子', price=0.9863598035105284}
消费者3消费商品Goods{id='bd68a8cb-15e1-4763-ac65-cfd7c2347c80', name='包子', price=0.9678416464584148}
消费者2消费商品Goods{id='3108d92e-7f09-4358-954f-25061e58c605', name='包子', price=0.21578169495497113}
消费者1消费商品Goods{id='2fa283b7-5283-4672-addc-853f6f69b539', name='包子', price=0.1642551464682276}
生产者4生产商品Goods{id='187b0d99-2104-4336-9acb-63a2faf02c00', name='包子', price=0.16316315949395455}
生产者1生产商品Goods{id='09542b9e-023d-4217-b27f-9a6abf26b3e9', name='包子', price=0.7607839046800118}
生产者0生产商品Goods{id='c922a44a-a61e-4903-b4e4-1c5451f9c53b', name='包子', price=0.4347018188093368}
生产者3生产商品Goods{id='063275b8-2018-4fef-ab12-a702f0ca6246', name='包子', price=0.4916966591985713}
生产者2生产商品Goods{id='0d0e9514-8b5a-41d4-8204-8d117c78bd4e', name='包子', price=0.46764686172313186}
消费者0消费商品Goods{id='187b0d99-2104-4336-9acb-63a2faf02c00', name='包子', price=0.16316315949395455}
消费者1消费商品Goods{id='09542b9e-023d-4217-b27f-9a6abf26b3e9', name='包子', price=0.7607839046800118}
消费者2消费商品Goods{id='c922a44a-a61e-4903-b4e4-1c5451f9c53b', name='包子', price=0.4347018188093368}
消费者4消费商品Goods{id='063275b8-2018-4fef-ab12-a702f0ca6246', name='包子', price=0.4916966591985713}
消费者3消费商品Goods{id='0d0e9514-8b5a-41d4-8204-8d117c78bd4e', name='包子', price=0.46764686172313186}

Process finished with exit code -1
