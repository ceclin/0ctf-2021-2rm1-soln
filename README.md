# 0ctf-2021-2rm1-soln

solution to 2rm1 of 0CTF/TCTF 2021 Quals

This solution uses `rebind` in the second phase.
Detailed writeup is on https://github.com/waderwu/My-CTF-Challenges/tree/master/0ctf-2021/2rm1

------

因为看到有人的文章里放了这个仓库的链接，所以重构了一下代码，比之前更加简洁明了而且只需要依赖 [jrmp](https://github.com/ceclin/jrmp) 这个库，旧版代码可以查看v0.1.0这个tag。

虽然上面给出了出题人writeup链接，但后续没有再补充细节，不过已经有一些公开的其他选手的writeup和相关的分析文章，因此这里只简单介绍一下这个解法。

rmiserver的jdk版本是8u232，`UserInter.sayHello`有一个String参数，在此版本下可以让其反序列化任意对象，结合Gadget类就可以RCE。

因为rmiclient会向registry查询0ops对象，然后向server发起RMI调用，所以可以把0ops重新绑定到我们自己实现的恶意的server，让这个server返回一个含有Gadget实例的异常，从而实现RCE。

这个仓库的结构如下：common模块中是题目自带的一些类，evil模块是用于打包成jar后下载到rmiserver上执行的，soln是这个解法的具体实现。

如何复现？把evil模块中用于外带flag的url改成你的；把evil模块打包成jar，放在你的服务器上；把`downloadJar`方法中的url改成你的jar的url；运行soln模块的main方法即可。
