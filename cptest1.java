class Goods{
    private String goodsName;
    private int count;
    public synchronized void set(String goodsName){
        if (count==1){
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
        this.notify();
    }
public synchronized void get(){
        if (count==0){
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
    this.notify();
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
        this.goods.set("一个小黑瓶");
    }
}
class Consumer implements Runnable{
    private Goods goods;

    public Consumer(Goods goods) {
        this.goods = goods;
    }

    @Override
    public void run() {
        this.goods.get();
    }
}
public class CPTest {
    public static void main(String[] args) {
        Goods goods=new Goods();
        Producer producer=new Producer(goods);
        Consumer consumer=new Consumer(goods);
        Thread produceThread=new Thread(producer,"生产者");
        Thread consumeThread=new Thread(consumer,"消费者");
        produceThread.start();
        try{
            Thread.sleep(1000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        consumeThread.start();
    }
}



运行结果：
生产者生产Goods{goodsName='一个小黑瓶', count=1}
消费者消费Goods{goodsName='一个小黑瓶', count=0}
