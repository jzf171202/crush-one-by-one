# crush-one-by-one
这是一个针对Android核心知识模块进行的源码分析实例项目，目的是为了巩固知识并深入学习。
## V 1.0.5
- Android Studio 3.1引入，解决gragdle依赖兼容问题。
- 配置gradle环境变量。
## V 1.0.4
- Android-Universal-Image-Loader源码解析；
- 大文件下下载；
- DiskLruCache缓存与获取；

## V 1.0.3
- ThreadPoolExecutor线程池机制引入；
- 添加线程并发、并行、同步机制处理；

### ThreadPoolExecutor参数
- **int corePoolSize** ——线程池中核心线程的数量。
- **int maximumPoolSize** ——线程池中最大线程数量。
- **long keepAliveTime**——非核心线程的超时时长，当系统中非核心线程闲置时间超过keepAliveTime之后，则会被回收。如果ThreadPoolExecutor的allowCoreThreadTimeOut属性设置为true，则该参数也表示核心线程的超时时长。
- **TimeUnit unit** ——时间单位，有纳秒、微秒、毫秒、秒、分、时、天等。
- **BlockingQueue< Runnable > workQueue** ——线程池中的任务队列，该队列主要用来存储已经被提交但是尚未执行的任务。
- **ThreadFactory threadFactory** —— 线程工厂，为了给线程池提供创建线程的功能。
- **RejectedExecutionHandler handler**——拒绝策略，当线程无法执行新任务时（一般是由于线程池中的线程数量已经达到最大数或者线程池关闭导致的），默认情况下，当线程池无法处理新线程时，会抛出一个RejectedExecutionException。

### ThreadPoolExecutor工作规则
- 如果线程池中的线程数未达到核心线程数，则会立马启用一个核心线程去执行。
- 如果线程池中的线程数已经达到核心线程数，且workQueue未满，则将新线程放入workQueue中等待执行。
- 如果线程池中的线程数已经达到核心线程数且workQueue已满，但未超过非核心线程数，则开启一个非核心线程来执行任务。
- 如果线程池中的线程数已经超过非核心线程数，则拒绝执行该任务。

## V 1.0.2
- RxJava2.0背压的引入；
背压：指在异步场景中，被观察者发送事件速度远快于观察者的处理速度的情况下，一种告诉上游的被观察者降低发送速度的策略。
因为事件产生的速度远远快于事件消费的速度，最终导致数据积累越来越多，从而导致OOM等异常。这就是背压产生的必要性。

上下游是同步的。上游发射了事件但是下游没有接收，就会造成阻塞（即便上游的事件队列长度只有3个 < 128）。为了避免ANR，就要提示MissingBackpressureException异常。

Flowable默认事件队列大小为128。BackpressureStrategy.BUFFER策略下事件队列无限大，和没有采取背压的Observable ( 被观察者 ) / Observer ( 观察者 )类似了。

在上下游异步的情况下，上游会先把事件发送到长度为128的事件队列中，待下游发送请求数据指令后从事件队列中拉取数据。这种“响应式拉取”的思想用于解决上下游流速不均衡的情况。

当上下游在同一个线程中的时候，在下游调用request(n)就会直接改变上游中的requested的值，多次调用便会叠加这个值，而上游每发送一个事件之后便会去减少这个值，当这个值减少至0的时候，上游若继续发送事件便会抛异常了。

当上下游工作在不同的线程里时，每一个线程里都有一个requested，而我们下游调用request（1000）时，实际上改变的是下游线程中的requested，而上游中的requested的值是由RxJava内部调用request(n)去设置的，这个调用会在合适的时候自动触发。

当下游每消费96个事件便会自动触发内部的request()去设置上游的requested的值。

## V 1.0.1
- 添加动态权限管理机制；
市场上一些APP在处理动态权限兼容性问题时，一般是在启动类里做统一的危险性权限的申请，如果有必要的权限被拒绝了，就拒绝跳转到下一页面。这也是一种简单粗暴的处理方式。
一些手机厂商，如小米手机。用户如果拒绝权限申请之后，就只有在设置里手动开启这一种方式。不然执行到用到该权限的代码时就会崩溃。有些码友会在try-catch里弹出对话框通知用户手动开启权限。这也是一种兼容方式。
> 本APP采用第一种简单粗暴的方式。

## V 1.0.0
- 添加基础网络Library，封装主流网络框架Retrofit2.x + Rxjava2.x，并添加缓存和拦截器等功能；
- 搭建APP基础架构，采用“MVC”+“响应式编程”思路，达到解耦的目的；
