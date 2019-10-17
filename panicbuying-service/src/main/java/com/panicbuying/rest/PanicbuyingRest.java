package com.panicbuying.rest;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.panicbuying.api.GoodsService;
import com.panicbuying.api.OrderService;
import com.panicbuying.api.SeckillService;
import com.panicbuying.dto.GoodsResultDTO;
import com.panicbuying.dto.SeckillRankDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 抢购对外接口
 *
 * @author: zhaozhonghui
 * @create: 2019-04-25 14:29
 */
@Slf4j
@RestController
@RequestMapping("/seckill")
@Api(tags = "抢购接口")
public class PanicbuyingRest {

    @Autowired
    private SeckillService seckillService;

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderService orderService;


    @GetMapping("/init")
    @ApiOperation("初始化库存接口")
    public String initGoods() {
        return goodsService.initGoods();
    }

    @GetMapping("/start")
    @ApiOperation("抢购商品接口")
    public String seckillGoods(@ApiParam("商品编码") @RequestParam("goodsCode") Long goodsCode, @ApiParam("用户ID") @RequestParam("userId") Long userId) {
        return seckillService.doSeckill(goodsCode, userId);
    }

    /**
     *  corePoolSize：核心线程池大小， 当新的任务到线程池后，线程池会创建新的线程（即使有空闲线程），直到核心线程池已满。
     * maximumPoolSize：最大线程池大小，顾名思义，线程池能创建的线程的最大数目
     * keepAliveTime：程池的工作线程空闲后，保持存活的时间
     * TimeUnit： 时间单位
     * BlockingQueue<Runnable>：用来储存等待执行任务的队列
     * threadFactory：线程工厂
     * RejectedExecutionHandler： 当队列和线程池都满了时拒绝任务的策略
     *
     *corePoolSize 和 maximumPoolSize
     * 默认情况下线程中的线程初始时为 0， 当有新的任务到来时才会创建新线程，当线程数目到达 corePoolSize 的数量时，新的任务会被缓存到 workQueue 队列中。如果不断有新的任务到来，队列也满了的话，线程池会再新建线程直到总的线程数目达到 maximumPoolSize。如果还有新的任务到来，则要根据 handler 对新的任务进行相应拒绝处理。
     *
     * BlockingQueue<Runnable>
     * 一个阻塞队列，用来存储等待执行的任务，常用的有如下几种：
     *
     * ArrayBlockingQueue：是一个基于数组结构的有界阻塞队列，此队列按 FIFO（先进先出）原则对元素进行排序。
     * LinkedBlockingQueue：一个基于链表结构的阻塞队列，此队列按FIFO （先进先出） 排序元素，吞吐量通常要高于ArrayBlockingQueue。静态工厂方法Executors.newFixedThreadPool()使用了这个队列。
     * SynchronousQueue：一个不存储元素的阻塞队列。每个插入操作必须等到另一个线程调用移除操作，否则插入操作一直处于阻塞状态，吞吐量通常要高于LinkedBlockingQueue，静态工厂方法Executors.newCachedThreadPool使用了这个队列。
     * PriorityBlockingQueue：一个具有优先级得无限阻塞队列。
     * RejectedExecutionHandler
     * 当队列和线程池都满了，说明线程池处于饱和状态，那么必须采取一种策略处理提交的新任务。有下面四种JDK提供的策略：
     *
     * AbortPolicy，表示无法处理新任务时抛出异常, 默认策略
     * CallerRunsPolicy：用调用者所在线程来运行任务。
     * DiscardOldestPolicy： 该策略将丢弃最老的一个请求，也就是即将被执行的任务，并尝试再次提交当前任务。
     * DiscardPolicy：不处理，丢弃掉
     * 除了这些JDK提供的策略外，还可以自己实现 RejectedExecutionHandler 接口定义策略。
     *
     *
     */
    @GetMapping("/multi")
    @ApiOperation("测试多线程抢购商品接口")
    public void multiSeckillGoods() {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("demo-pool-%d").build();
        ExecutorService singleThreadPool = new ThreadPoolExecutor(20, 40,
                60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

        int i = 0;
        while (i < ((ThreadPoolExecutor) singleThreadPool).getMaximumPoolSize()) {
            Long goodsCode = (long) (Math.random() * 3 + 1);
            Long userId = (long) (Math.random() * 10 + 1);
            singleThreadPool.execute(() -> seckillService.doSeckill(goodsCode, userId));
            i++;
            log.info("i的值:======={}",i);
        }
        singleThreadPool.shutdown();
    }

    @GetMapping("/rank")
    @ApiOperation("用户抢购排行")
    public List<SeckillRankDTO> seckillRank(){
       return orderService.getSeckillRank();
    }

    @GetMapping("/getGoods")
    @ApiOperation("查询商品")
    public List<GoodsResultDTO> getGoodsList(@ApiParam("查询参数")@RequestParam("param")String param){
        return goodsService.getGoodsListFromElasticSearch(param);
    }

}
