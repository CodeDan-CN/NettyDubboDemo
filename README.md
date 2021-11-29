<h1>代码在分支master中</h1>
<!-- wp:heading -->
<h2>RPC框架思路</h2>
<!-- /wp:heading -->

<!-- wp:paragraph {"textColor":"black"} -->
<p class="has-black-color has-text-color">RPC全称"Remote Prcedure Call"，即远程过程调用，<strong>是一个计算机通信协议。该协议允许运行于一台计算机的程序调用另一台计算机的子程序，而程序员无需额外的为这个交互过程进行编程。只需要关系当前计算机中自己负责的业务逻辑即可。</strong></p>
<!-- /wp:paragraph -->

<!-- wp:paragraph {"textColor":"black"} -->
<p class="has-black-color has-text-color">PRC框架的实现落地技术有阿里的Dubbo，Spring的Spring cloud(现阿里的alibaba Spring cloud)，Go语言的rpcx。</p>
<!-- /wp:paragraph -->

<!-- wp:paragraph {"textColor":"black"} -->
<p class="has-black-color has-text-color">简易的PRC协议调用流程图：（client为服务消费者，server为服务提供者）</p>
<!-- /wp:paragraph -->

<!-- wp:image {"id":1694,"width":552,"height":317,"sizeSlug":"large","linkDestination":"none"} -->
<figure class="wp-block-image size-large is-resized"><img src="http://119.29.157.232:8000/wp-content/uploads/2021/11/截屏2021-11-25-11.13.20-1024x590.png" alt="" class="wp-image-1694" width="552" height="317"/></figure>
<!-- /wp:image -->

<!-- wp:paragraph -->
<p>解析流程：</p>
<!-- /wp:paragraph -->

<!-- wp:paragraph {"textColor":"black"} -->
<p class="has-black-color has-text-color">1）clinet(服务消费方)以本地调用方法调用服务</p>
<!-- /wp:paragraph -->

<!-- wp:paragraph {"textColor":"black"} -->
<p class="has-black-color has-text-color">2）client stub接受到本地调用坠毁负责将方法，参数等封装成能够进行网络传输的消息体。</p>
<!-- /wp:paragraph -->

<!-- wp:paragraph {"textColor":"black"} -->
<p class="has-black-color has-text-color">3）client stub将信息进行编码并发送到服务器端</p>
<!-- /wp:paragraph -->

<!-- wp:paragraph {"textColor":"black"} -->
<p class="has-black-color has-text-color">4）server stub收到信息后进行解码</p>
<!-- /wp:paragraph -->

<!-- wp:paragraph {"textColor":"black"} -->
<p class="has-black-color has-text-color">5）server stub根据解码结果调用相应的本地服务</p>
<!-- /wp:paragraph -->

<!-- wp:paragraph {"textColor":"black"} -->
<p class="has-black-color has-text-color">6）本地服务执行并返回结果给server stub</p>
<!-- /wp:paragraph -->

<!-- wp:paragraph {"textColor":"black"} -->
<p class="has-black-color has-text-color">7）server stub将返回导入结果进行编码并发送到服务消费者</p>
<!-- /wp:paragraph -->

<!-- wp:paragraph {"textColor":"black"} -->
<p class="has-black-color has-text-color">8）client stub接受到信息并进行解码</p>
<!-- /wp:paragraph -->

<!-- wp:paragraph {"textColor":"black"} -->
<p class="has-black-color has-text-color">9）clinet得到本地调用方法的结果</p>
<!-- /wp:paragraph -->

<!-- wp:paragraph {"style":{"typography":{"fontSize":"25px"}}} -->
<p style="font-size:25px"><strong><span style="color:#ed0404" class="has-inline-color">PRC框架就是想把2到8的步骤进行封装，让开发者调用远程方法提供的服务时，就和在本地调用一摸一样。</span></strong></p>
<!-- /wp:paragraph -->

<!-- wp:separator {"color":"background_color","className":"is-style-wide"} -->
<hr class="wp-block-separator has-text-color has-background has-background-color-background-color has-background-color-color is-style-wide"/>
<!-- /wp:separator -->

<!-- wp:heading -->
<h2>为什么挑选Dubbo作为PRC框架实例</h2>
<!-- /wp:heading -->

<!-- wp:paragraph -->
<p>因为Dubbo底层采用了Netty作为网络通讯框架，所以要求用Netty实现一个简单的RPC框架就可以参考和借鉴Dubbo的实现。</p>
<!-- /wp:paragraph -->

<!-- wp:paragraph -->
<p>任务：<strong><span style="color:#fa6600" class="has-inline-color">我们得模仿Dubbo指定服务消费者和服务提供者预订的"接口"和"通讯协议"，当服务器消费者远程调用服务提供者的接口时，提供者返回一个字符串，消费者打印提供者返回的数据。</span></strong></p>
<!-- /wp:paragraph -->

<!-- wp:paragraph {"textColor":"black"} -->
<p class="has-black-color has-text-color">设计：</p>
<!-- /wp:paragraph -->

<!-- wp:paragraph {"textColor":"black"} -->
<p class="has-black-color has-text-color">1）创建一个共有接口，定义抽象方法，用于服务消费者和服务提供者之间的约定。</p>
<!-- /wp:paragraph -->

<!-- wp:paragraph {"textColor":"black"} -->
<p class="has-black-color has-text-color">2）创建一个服务提供者，该类需要监听消费者的请求，并按照约定返回数据。</p>
<!-- /wp:paragraph -->

<!-- wp:paragraph {"textColor":"black"} -->
<p class="has-black-color has-text-color">3）创建一个服务消费者，该累需要透明的调用自己不存在的方法，内部需要使用Netty请求提供者返回数据。</p>
<!-- /wp:paragraph -->

<!-- wp:paragraph {"textColor":"black"} -->
<p class="has-black-color has-text-color">宏观程序图：</p>
<!-- /wp:paragraph -->

<!-- wp:image {"id":1697,"sizeSlug":"large","linkDestination":"none"} -->
<figure class="wp-block-image size-large"><img src="http://119.29.157.232:8000/wp-content/uploads/2021/11/截屏2021-11-25-11.41.11-1024x510.png" alt="" class="wp-image-1697"/></figure>
<!-- /wp:image -->

<!-- wp:paragraph {"textColor":"black"} -->
<p class="has-black-color has-text-color">实现基本思路：</p>
<!-- /wp:paragraph -->

<!-- wp:paragraph {"textColor":"black"} -->
<p class="has-black-color has-text-color">客户端通过动态代理，获取到被调用的业务逻辑方法的各种信息，封装为方法信息对象后，转化为对应的proto格式对象后进行编码的传输，服务端这边通过解码得到方法的信息后利用反射执行对应的业务逻辑方法，并把返回值封装成返回对象，把此返回对象转化为对应的proto格式对象进行编码的传输，当客户端解码接受到结果之后，进行提取返回。</p>
<!-- /wp:paragraph -->

<!-- wp:paragraph {"textColor":"black"} -->
<p class="has-black-color has-text-color">为了保证信息发送和接受的顺序性，采用syncronized锁同一个对象时wait和notify来完成，为了提高并发性，采用线程池作为业务执行线程的管理。</p>
<!-- /wp:paragraph -->

<!-- wp:separator {"color":"background_color","className":"is-style-wide"} -->
<hr class="wp-block-separator has-text-color has-background has-background-color-background-color has-background-color-color is-style-wide"/>
<!-- /wp:separator -->

<!-- wp:paragraph -->
<p>项目结构分析：</p>
<!-- /wp:paragraph -->

<!-- wp:image {"id":1711,"width":425,"height":128,"sizeSlug":"full","linkDestination":"none"} -->
<figure class="wp-block-image size-full is-resized"><img src="http://119.29.157.232:8000/wp-content/uploads/2021/11/截屏2021-11-29-08.42.48.png" alt="" class="wp-image-1711" width="425" height="128"/></figure>
<!-- /wp:image -->

<!-- wp:paragraph -->
<p>这是项目总体结构图，使用Maven进行项目的管理，NettyDubboFrame为父工程，负责子工程中统一共有的jar包管理，ServerClient为服务的消费者项目，ServerServices为服务的提供者项目。</p>
<!-- /wp:paragraph -->

<!-- wp:paragraph -->
<p>首先来解析一下ServerServices项目结构：</p>
<!-- /wp:paragraph -->

<!-- wp:image {"id":1713,"width":408,"height":530,"sizeSlug":"large","linkDestination":"none"} -->
<figure class="wp-block-image size-large is-resized"><img src="http://119.29.157.232:8000/wp-content/uploads/2021/11/截屏2021-11-29-08.46.40-789x1024.png" alt="" class="wp-image-1713" width="408" height="530"/></figure>
<!-- /wp:image -->

<!-- wp:paragraph {"textColor":"black"} -->
<p class="has-black-color has-text-color">1）entity包下为实体类包，分别是存储方法信息的Message实体类，存储返回结果的ResultBody实体类和模拟业务逻辑结果的User类。</p>
<!-- /wp:paragraph -->

<!-- wp:paragraph -->
<p>2）mapper包下为数据持久层类，用于模拟从数据库中获取对应的User信息。（如果有时间可以clone下来自己修改添加ORM框架配合jdbc连接数据库进行访问）</p>
<!-- /wp:paragraph -->

<!-- wp:paragraph {"textColor":"black"} -->
<p class="has-black-color has-text-color">3）netty包下为netty的启动，配置和Handler类的编写类，其中ServerApplicaiton为服务提供者的启动类，ServerBootStrap类为服务提供者对象的配置类，ServerChannelHandlerInitializer类为ChannelHandler的初始化，用以添加此channel对应的pipeline中的顺序入站或者出站的编码解码Handler以及自定义Handler。最好MyCHannelHandler为自定义Handler。</p>
<!-- /wp:paragraph -->

<!-- wp:paragraph {"textColor":"black"} -->
<p class="has-black-color has-text-color">4）protoPOJO包下为proto结构文件和对象</p>
<!-- /wp:paragraph -->

<!-- wp:paragraph {"textColor":"black"} -->
<p class="has-black-color has-text-color">5）services包：为正在的业务逻辑代码</p>
<!-- /wp:paragraph -->

<!-- wp:separator {"className":"is-style-default"} -->
<hr class="wp-block-separator is-style-default"/>
<!-- /wp:separator -->

<!-- wp:paragraph {"textColor":"black"} -->
<p class="has-black-color has-text-color">然后来看看服务消费者的项目结构：</p>
<!-- /wp:paragraph -->

<!-- wp:image {"id":1714,"width":444,"height":297,"sizeSlug":"full","linkDestination":"none"} -->
<figure class="wp-block-image size-full is-resized"><img src="http://119.29.157.232:8000/wp-content/uploads/2021/11/截屏2021-11-29-08.58.48.png" alt="" class="wp-image-1714" width="444" height="297"/></figure>
<!-- /wp:image -->

<!-- wp:paragraph -->
<p>1）entity和protoPOJO，services包作用均相同，services包下只有业务逻辑接口，没有正在的业务逻辑代码。</p>
<!-- /wp:paragraph -->

<!-- wp:paragraph -->
<p>2）proxy包：为代理类配置包。</p>
<!-- /wp:paragraph -->
