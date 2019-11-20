import java.util.ArrayList;
import java.util.List;

class Goods{
    private String goodsName;
    private int count;
    private int maxCount;

    public Goods(int maxCount) {
        this.maxCount = maxCount;
    }

    public synchronized void set(String goodsName){
        while (count==maxCount){
            System.out.println("此时还有商品，需要等待消费者消费了再继续生产");
            try{
                this.wait();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        this.goodsName=goodsName;
        count++;
        System.out.println(Thread.currentThread().getName()+"生产"+this);
        //唤醒消费者线程
        this.notifyAll();
    }
    public synchronized void get(){
        while (count==0){
            System.out.println("商品已经卖完了，需要等待卖家上架");
            //需要让消费者阻塞
            try{
                this.wait();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        count--;
        System.out.println(Thread.currentThread().getName()+"消费"+this);
        //唤醒生产者线程
        this.notifyAll();
    }
    @Override
    public String toString() {
        return "Goods{" +
                "goodsName='" + goodsName + '\'' +
                ", count=" + count +
                '}';
    }
}
class Producer implements Runnable{
    private Goods goods;

    public Producer(Goods goods) {
        this.goods = goods;
    }

    @Override
    public void run() {
        while (true) {
            this.goods.set("一个小黑瓶");
        }
    }
}
class Consumer implements Runnable{
    private Goods goods;

    public Consumer(Goods goods) {
        this.goods = goods;
    }

    @Override
    public void run() {
        while(true){
            this.goods.get();
        }
    }
}
public class CPTest {
    public static void main(String[] args) {
        Goods goods=new Goods(10);
        Producer producer=new Producer(goods);
        Consumer consumer=new Consumer(goods);
        List<Thread> list=new ArrayList<>();
        //产生20个生产者
        for(int i=0;i<20;i++){
            list.add(new Thread(producer,"生产者"+(i+1)));
        }
        //产生30个消费者
        for(int i=0;i<30;i++){
            list.add(new Thread(consumer,"消费者"+(i+1)));
        }
        //启动所有生产者，消费者
        for(Thread thread:list){
            thread.start();
        }
    }
}



运行结果：
'一个小黑瓶', count=0}
商品已经卖完了，需要等待卖家上架
商品已经卖完了，需要等待卖家上架
商品已经卖完了，需要等待卖家上架
商品已经卖完了，需要等待卖家上架
商品已经卖完了，需要等待卖家上架
商品已经卖完了，需要等待卖家上架
商品已经卖完了，需要等待卖家上架
商品已经卖完了，需要等待卖家上架
商品已经卖完了，需要等待卖家上架
商品已经卖完了，需要等待卖家上架
商品已经卖完了，需要等待卖家上架
商品已经卖完了，需要等待卖家上架
商品已经卖完了，需要等待卖家上架
商品已经卖完了，需要等待卖家上架
生产者20生产Goods{goodsName='一个小黑瓶', count=1}
生产者20生产Goods{goodsName='一个小黑瓶', count=2}
生产者20生产Goods{goodsName='一个小黑瓶', count=3}
生产者20生产Goods{goodsName='一个小黑瓶', count=4}
生产者20生产Goods{goodsName='一个小黑瓶', count=5}
生产者20生产Goods{goodsName='一个小黑瓶', count=6}
生产者20生产Goods{goodsName='一个小黑瓶', count=7}
生产者20生产Goods{goodsName='一个小黑瓶', count=8}
生产者20生产Goods{goodsName='一个小黑瓶', count=9}
生产者20生产Goods{goodsName='一个小黑瓶', count=10}
此时还有商品，需要等待消费者消费了再继续生产
此时还有商品，需要等待消费者消费了再继续生产
此时还有商品，需要等待消费者消费了再继续生产
此时还有商品，需要等待消费者消费了再继续生产
此时还有商品，需要等待消费者消费了再继续生产
此时还有商品，需要等待消费者消费了再继续生产
此时还有商品，需要等待消费者消费了再继续生产
此时还有商品，需要等待消费者消费了再继续生产
此时还有商品，需要等待消费者消费了再继续生产
消费者20消费Goods{goodsName='一个小黑瓶', count=9}
消费者20消费Goods{goodsName='一个小黑瓶', count=8}
消费者20消费Goods{goodsName='一个小黑瓶', count=7}
------
Process finished with exit code -1
