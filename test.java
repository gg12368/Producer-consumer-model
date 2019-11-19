生产者消费者模型的铺垫示例：
wait和notify方法的应用

package wait_notify;
class ThrowBall implements Runnable{
    private boolean flag;
    //private boolean flag=true;
    //private Object ball=new Object();
    private Object ball;

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public ThrowBall(boolean flag, Object ball) {
        this.flag = flag;
        this.ball = ball;
    }

    //public ThrowBall(boolean flag) {
      //  this.flag = flag;
   // }

    @Override
    public void run() {
        if(flag){
            QiangQiu();
        }else{
            ThrowQiu();
        }
    }
    public void QiangQiu(){
        synchronized (ball){
            System.out.println(Thread.currentThread().getName()+"等待抢球");
            try{
                ball.wait();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+"抢到绣球");
        }
    }
    public synchronized void ThrowQiu(){
        synchronized (ball){
            System.out.println("随机挑选一个壮汉");
            ball.notifyAll();
            System.out.println("成功领证");
        }
    }
}
public class XiuBoll {
    public static void main(String[] args) {
        //ThrowBall throwBall=new ThrowBall();
        Object ball=new Object();
        Thread boyThread=new Thread(new ThrowBall(true,ball),"痴汉1");
        Thread boyThread2=new Thread(new ThrowBall(true,ball),"痴汉2");
        Thread boyThread3=new Thread(new ThrowBall(true,ball),"痴汉3");
        Thread girlThread=new Thread(new ThrowBall(false,ball),"女神");
        boyThread.start();
        boyThread2.start();
        boyThread3.start();
        try{
            Thread.sleep(1000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        girlThread.start();
    }
}

运行结果：
痴汉1等待抢球
痴汉3等待抢球
痴汉2等待抢球
随机挑选一个壮汉
成功领证
痴汉1抢到绣球
痴汉2抢到绣球
痴汉3抢到绣球
