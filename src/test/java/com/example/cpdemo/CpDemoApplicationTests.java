package com.example.cpdemo;

import com.example.cpdemo.mapper.TestMapper;
import com.example.cpdemo.util.HttpConnect;
import com.example.cpdemo.util.SpringUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import us.codecraft.xsoup.Xsoup;

import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@SpringBootTest
class CpDemoApplicationTests {
    @Autowired
    TestMapper testMapper;

    @Test
    void contextLoads() throws InterruptedException {
        for (int j = 0; j < 20; j++) {
            Thread t1 = new Thread(() -> {
                for (int i = 0; i < 100; i++) {
                    log.info("执行了 {}", testMapper.selectById("104").getName());
                }
            });
            t1.start();
        }
        Thread.sleep(20000);
    }

    @Test
    void test7() throws InterruptedException {
        Thread t1 = new Thread(() -> {

            log.info("执行了 {}", testMapper.selectById("104").getName());

        });
        t1.setDaemon(true);
        t1.start();
        t1.join();
    }

    @Test
    void test1() {
        Runnable runnable = () -> {
            System.out.println("123");
            TestMapper bean = SpringUtils.getBean(TestMapper.class);
            System.out.println(bean.selectById("104"));
            log.info("我是{}", Thread.currentThread());
        };
        Thread t1 = new Thread(runnable);
        Thread t2 = new Thread(runnable);
        Thread t3 = new Thread(runnable);
        Thread t4 = new Thread(runnable);
        t1.start();
        t2.start();
        t3.start();
        t4.start();
    }

    @Test
    void threadTest() throws InterruptedException {
        Thread myThread = new Thread("t1") {
            @Override
            public void run() {
                log.info("{}:我恁爹", Thread.currentThread());
            }
        };
        myThread.start();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                log.info("我恁妈");
            }
        };
        Thread myRunable = new Thread(runnable);
        myRunable.start();
        TimeUnit.SECONDS.sleep(1111111111);
        Runnable lambda = () -> log.info("高端技术！");
        Thread lamThread = new Thread(lambda);
        lamThread.start();
    }

    @Test
    void test2() throws ExecutionException, InterruptedException {
        FutureTask<Integer> futureTask = new FutureTask<Integer>(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                log.warn("sad");
                Thread.sleep(10000);
                return 123;
            }
        });
        Thread thread = new Thread(futureTask);
        thread.start();
        log.error("{}", futureTask.get());
        ;
    }

    @Test
    void TwoPhase() throws InterruptedException {
        Thread twoPhase = new Thread(() -> {
            while (true) {
                Thread thread = Thread.currentThread();
                if (thread.isInterrupted()) {
                    log.warn("料理后事////");
                    break;
                }
                log.info("正在执行监控////");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    thread.interrupt();
                    e.printStackTrace();

                }

            }

        }, "twoPhase");
        twoPhase.start();
        Thread.sleep(5000);
        twoPhase.interrupt();

    }

    static int i = 0;
    static Object room = new Object();

    @Test
    void test5() throws InterruptedException {
        Thread add = new Thread(() -> {
            for (int j = 0; j < 5000; j++) {
                synchronized (room) {
                    i++;
                }
            }
        });
        Thread minus = new Thread(() -> {
            for (int j = 0; j < 5000; j++) {
                synchronized (room) {
                    i--;
                }
            }
        });
        add.start();
        minus.start();
        add.join();
        minus.join();
        log.info("{}", i);

    }

    @Test
    void testX() throws InterruptedException {
        GuardObject guardObject = new GuardObject();
        new Thread(() -> {
            log.info("在等待。。");
            guardObject.get(123);
        }, "t1").start();
        new Thread(() -> {
            log.info("正在加载。。");
            try {
                Thread.sleep(5000);
                guardObject.complete("Fuck you Tony!");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }, "t2").start();
        Thread.sleep(10000);

    }

    class GuardObject {
        String response;
        public void get(long timeout) {
            long beginTime = System.currentTimeMillis();
            long passedTime=0;
            synchronized (this) {
                while (response == null) {
                    if(passedTime>=timeout)
                    try {
                        this.wait(timeout-passedTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                     passedTime=System.currentTimeMillis()-beginTime;

                }
                log.info(response);
            }
        }

        public void complete(String res) throws InterruptedException {
            synchronized (this) {
                this.response = res;
                this.notifyAll();
            }
        }
    }
    @Test
    void  thisTest() throws InterruptedException {
        t1 t1 = new t1();
        Thread thread1 = new Thread(() -> {
            try {
                t1.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"x1");
        Thread thread2 = new Thread(() -> {
           t1.set();
        },"x2");
        thread1.start();
        thread2.start();

        Thread.sleep(5000);

    }

    final  class t1{
     private Object o=new Object();

      void set(){
          synchronized (o){
              o.notifyAll();
          }
      }
      void get() throws InterruptedException {
          synchronized (o){
              log.info("我睡了");
             o.wait();
              log.info("我醒了");
          }
      }
    }
    @Test
    void queTest() throws InterruptedException {
        MessQueue messQueue = new MessQueue();
        new Thread(()->{
            for (int i=0;i<14;i++){
                try {

                    messQueue.push(new Date().toString());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"provider").start();
        new Thread(()->{
            for (int i=0;i<8;i++){
                try {
                    Thread.sleep(1000);
                    messQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"consumer1").start();


        new Thread(()->{
            for (int i=0;i<8;i++){
                try {
                    Thread.sleep(2000);
                    messQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"consumer2").start();
    Thread.sleep(50000);

    }
    class MessQueue{
        LinkedList<String> list=new LinkedList();
        final int MAX_SIZE=3;
        void push(String message) throws InterruptedException {
            synchronized (this){
                if(list.size()==MAX_SIZE){
                    log.warn("队列满了，等待中.....");
                    this.wait();
                }
                    list.push(message);
                    log.info("存人了{}",message);
                    this.notifyAll();
            }

        }
        String take() throws InterruptedException {
            synchronized (this){
                if(list.size()==0){
                    log.warn("队列为空，等待中.....");
                    this.wait();
                }
                String firstOne= list.removeFirst();
                log.info("取出了{}",firstOne);
                this.notifyAll();
                return firstOne;
            }
        }
    }







    @Test
    void dTest() throws FileNotFoundException {
        //zhonghua <video <source src
        //String url="https://vmts.china.com/api/video/onaliyun/query?id=3021940&ttype=mp4";
        //网易 <video> src
       // String url="https://flv.bn.netease.com/e588c33306cd4bfaadeebb96f62d489f9921e4640c1763903fbd83ed1f5c2a3329bdee125a32e019dcc6a287c9a147b03773437e92da2ee8a11e59f5ebbeb87858d1c1e86ee2c1831b86a58feed2c06f33d3796b6fdbcdfbdd32820800215840f5b158f0887613a36dee0b8e80da7d865f99b9df864cab02.mp4";
        //sina  <video> src
      // String url="https://edge.ivideo.sina.com.cn/45390344802.mp4?KID=sina,viask&Expires=1654099200&ssig=RMgByPG5ZV&reqid=";

       String url="https://ips.ifeng.com/video19.ifeng.com/video09/2022/05/21/p6933606090101760000-102-103521.mp4?reqtype=tsl&vid=b9cd8057-575f-4855-8624-606e021a0791&uid=Xu242R&from=v_Free&pver=vHTML5Player_v2.0.0&sver=&se=&cat=&ptype=&platform=pc&sourceType=h5&dt=1654049489077&gid=kYb8MYEdCEC1&sign=ad716188b1b1c0ac1f1b47f7ea83ff26&tm=1654049489077";

       HttpConnect.downloadByStream(url);
    }
    @Test
    void cTest() throws FileNotFoundException {
        //qq
        String url="https://apd-2b7dd5f421546815b9212392592800b2.v.smtcdns.com/omts.tc.qq.com/AaAvDdsgJfMOHfu5-71RMXi7owZ-77vQTZxu56F1TKVs/uwMROfz2r57IIaQXGdGnC2deOm6ep2iiZZnM7ha1GY22DYlR/svp_50001/mx0qcb1jQ_wrtpAXfyy1_XO7zRxoly7ZZNhyJCjITGrRiD5LHXiemIjpH0JDtUsKO6cpqg4eP7DAYnOLa939NB3N57ZuI32XEf82d_RHarXQU7PQjqW4oxsWTEP0GFIkthO_6XL0_5udnsKtoXdyuvxvsLJSF0tRLe6aMBK9cDz-hX2kq15FMg/02_szg_8117_50001_0bc3yeaasaaajqambu4v5rrfdqodbhaqacka.f304110.1.ts";
        HttpConnect.downloadByStream(url);
    }
    @Test
    void tt(){

        ////String url="https://new.qq.com/omn/20220601/20220601A0401R00.html?pgv_ref=aio2015&ptlang=2052";
        String url="https://github.com/";
        System.out.println(HttpConnect.getStringResponse(url));
    }




    @Test
    void rawTest(){
        String url="http://ishare.ifeng.com/c/s/v002fSIpdu5SkoZ65--4CHNLZmDNpet9Ln84JUntY87V0xF4__";
        String stringResponse = HttpConnect.getStringResponse(url);
        String[] split = stringResponse.split("\"");
        List<String> list = Arrays.asList(split);
        int mobileUrl = list.indexOf("mobileUrl");
        System.out.println(list.get(mobileUrl + 2));
    }

    public static void main(String[] args) {
        String origin="Hello 123 ?? World!";
       // String origin="Hello";
        String pattern="123";
        Pattern compile = Pattern.compile(pattern);
        Matcher matcher=compile.matcher(origin);
        System.out.println(matcher.group(1));




    }



}
