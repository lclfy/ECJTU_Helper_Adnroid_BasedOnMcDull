﻿在com.mcdull.cert 1.4.5版基础上改进

2017/4/7
√修复了一卡通过渡动画带来的一个死循环bug

2017/4/6
//在ljm4.2新版上修改的话…需要
修复四六级查询
修复电脑维修
修复小班序号状态栏显示
加上刷新失败的图
修复改学号刷新
修复一卡通不会随主题变色的问题
…
//懒得改，在0329版本上进行了如下修改
√修复了第一次安装的时候，主界面calender图标不会变色的问题
√修复日历遮罩问题
√修复天气（暂时不知道还有没有其他的问题）
√添加一卡通进入特效（将build SDK更改为25.0.1）
√修复了几个小bug

剩余任务：p
添加地图导航功能->明天
更新完成后会暂时停止更新
等待ljm修改应用架构，优化代码后
添加一卡通月消费分析

2017/3/29
√一卡通现在在主页如果获取到数据，进入详情时不需要再获取一遍数据了（没有数据继续获取

2017/3/28 Build3
√修复了因修复按钮过小导致的无法点击bug
√暂时把课表放到了日历内部
√暂时清除了顶部滑动栏
√修复了几个小bug

2017/3/28 Build2
√修复了部分按钮过小 实际点击困难的问题
√修复了多次保存个人信息后无法保存的bug

2017/3/28
√课表现在可以通过按钮刷新了
√修复了在个人信息中更改学号后界面信息不会更新的问题（可能有无法同时更新的bug，待测试）
√更改了没有头像时展示在主页与侧边栏的图片
√修改了文本框/提示框，让其变得符合设计规范
√修复了首次安装打开后，按home键回到桌面重进程序会导致主Activity重复加载的问题
√现在在未安装建行App的设备上 可以成功下载并运行建行安装程序
√使原来暂时隐藏的关于应用，CERT介绍等重新显示
√增加了13，14级学生的查询提醒
√主界面针对13，14级学生做了部分优化（其实就是不让点，还隐藏课表，还重设日历标题
√修改了提醒未安装建行的方式
发现了多次保存个人信息后无法保存的bug

2017/3/27 build2
√增加了选修小班查询
√修复了补考成绩不予显示的问题
√修复了数个bug
√主界面增加日历项目
√超过晚上8点显示第二天的日历（判断月份 年底）
√修复了日历里面毛概太长导致教室显示不出来的bug
√日历标题跟随主题颜色（看起来微小的工作）
√课表本地化
△天气链接失效了 准备转用校园通接口里的

2017/3/27
√修复了点击补考安排或考试安排会闪退的问题

2017/3/25 build2
√建行调试通过
√个人中心没有头像的时的图片问题
√修复了注册完信息并不会传递到AVUser里面的错误
√修复了在不同长宽比手机下天气界面拉伸的情况

2017/3/25
√点名器
√现在一卡通的余额信息会从主界面传入activity了
√加入暂无一卡通信息的图
√修复了几个bug（包括加入沉浸式状态栏）
√个人信息保存的时候验证学号密码能否登录教务处（注册的时候也验证）
课表：
√获取json，编辑模型
√填充到界面
√课表没课部分变色

加入新生考试安排查询

2017/3/24
√把主页面的UI切好（颜色不变 图标重做）
包括：√CET，√补考，√成绩，(90%，等嘉豪看代码)电脑维修，√后勤报修，√考试安排，校园导航，√点名工具
√天气温度实时显示
√MD_Select_Item的横线画好了，并将其变为通用item的layout，增加了header
√把其他应用移动到主界面
√为CET查询做了新界面
√查成绩界面显示红色绿色勾叉
√将查询页面有关代码删除
√增加了CET查询输入对话框
90%-做个点名器（差完成点名按钮功能）

2017/3/23
√一卡通密码错误的提示
√一卡通UI简单修改
√修改余额显示方式
√去掉一卡通卡号
70% 添加建行充值按钮（下载部分未做完，未验证能否调用建行）
√修改个人信息页面


2017/3/21
天气over
一卡通近乎over，需要一个建行链接，并检查bug

2017/3/9
1. 删除了进入应用时的图片选择器，小圆点，直接进入注册登录
2. 稍微修改文案
3. 删除无用的多余注册登录页面
4. 删除了版本号
5. 删除了侧边栏的两个项目
6. 精简了第一个页面
7. 删除了设置中的关于我们
8. 自动输入自己的用户名密码
Bug：需要先点一下下面的出行按钮才能点地图，不然闪退（原因不明）

2017/3/15
天气第一阶段over

2017/3/10
返回按钮和加号换成了MD风格
添加了自定义的weatherView，还未应用
weather界面完成

2017/3/9
1. 删除了进入应用时的图片选择器，小圆点，直接进入注册登录
2. 稍微修改文案
3. 删除无用的多余注册登录页面
4. 删除了版本号
5. 删除了侧边栏的两个项目
6. 精简了第一个页面
7. 删除了设置中的关于我们
8. 自动输入自己的用户名密码
Bug：需要先点一下下面的出行按钮才能点地图，不然闪退（原因不明）
