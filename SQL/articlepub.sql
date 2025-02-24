/*
 Navicat Premium Dump SQL

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80037 (8.0.37)
 Source Host           : localhost:3306
 Source Schema         : articlepub

 Target Server Type    : MySQL
 Target Server Version : 80037 (8.0.37)
 File Encoding         : 65001

 Date: 20/12/2024 14:07:23
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `PASSWORD` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES (1, 'admin', '63a9f0ea7bb98050796b649e85481845');

-- ----------------------------
-- Table structure for article_attachment
-- ----------------------------
DROP TABLE IF EXISTS `article_attachment`;
CREATE TABLE `article_attachment`  (
  `aid` int NOT NULL AUTO_INCREMENT COMMENT '附件id',
  `hid` int NOT NULL COMMENT '所属文章id',
  `file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '附件原始文件名',
  `file_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '附件路径',
  `upload_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '附件上传时间',
  PRIMARY KEY (`aid`) USING BTREE,
  INDEX `hid`(`hid` ASC) USING BTREE,
  CONSTRAINT `article_attachment_ibfk_1` FOREIGN KEY (`hid`) REFERENCES `article_headline` (`hid`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of article_attachment
-- ----------------------------
INSERT INTO `article_attachment` VALUES (2, 1, 'cead5d6f-bc24-4ac2-9f9d-20cc806bb021_qq.docx', 'uploads/cead5d6f-bc24-4ac2-9f9d-20cc806bb021_qq.docx', '2024-12-05 22:47:40');
INSERT INTO `article_attachment` VALUES (3, 46, 'e8ada870-b246-4b5d-8f7b-64c581efbbac_2024年12月3日-第15周上机要求.docx', 'uploads/e8ada870-b246-4b5d-8f7b-64c581efbbac_2024年12月3日-第15周上机要求.docx', '2024-12-05 23:13:50');
INSERT INTO `article_attachment` VALUES (4, 47, '8f77260f-84eb-4307-bf00-22091c057962_2024年12月3日-第15周上机要求.docx', 'uploads/8f77260f-84eb-4307-bf00-22091c057962_2024年12月3日-第15周上机要求.docx', '2024-12-06 10:35:08');
INSERT INTO `article_attachment` VALUES (5, 48, '89e6165a-a6da-4296-b22b-060ea6de97dd_2024年12月3日-第15周上机要求.docx', 'uploads/89e6165a-a6da-4296-b22b-060ea6de97dd_2024年12月3日-第15周上机要求.docx', '2024-12-06 11:08:17');
INSERT INTO `article_attachment` VALUES (6, 48, 'a3bf496b-5507-4899-ab28-ba811641dd43_qq.docx', 'uploads/a3bf496b-5507-4899-ab28-ba811641dd43_qq.docx', '2024-12-06 11:08:17');
INSERT INTO `article_attachment` VALUES (7, 49, '7c760538-13fe-47c9-bbf6-a5b784d2fb62_qq.docx', 'uploads/7c760538-13fe-47c9-bbf6-a5b784d2fb62_qq.docx', '2024-12-06 16:51:02');
INSERT INTO `article_attachment` VALUES (8, 49, 'f16ca4ef-1697-40c1-b3c5-d90c34abe358_笔记本allarticle.txt', 'uploads/f16ca4ef-1697-40c1-b3c5-d90c34abe358_笔记本allarticle.txt', '2024-12-06 16:51:02');
INSERT INTO `article_attachment` VALUES (9, 50, 'f2c04a30-ffde-4690-9c39-b6e5b47437c1_qq.docx', 'uploads/f2c04a30-ffde-4690-9c39-b6e5b47437c1_qq.docx', '2024-12-12 13:32:29');
INSERT INTO `article_attachment` VALUES (10, 50, 'ee70bc4f-19c0-4f8d-93d0-b01e1304fb63_笔记本allarticle.txt', 'uploads/ee70bc4f-19c0-4f8d-93d0-b01e1304fb63_笔记本allarticle.txt', '2024-12-12 13:32:29');
INSERT INTO `article_attachment` VALUES (11, 51, '284707f4-eb92-46fc-b1d9-b220f40baac1_各项数值.txt', 'uploads/284707f4-eb92-46fc-b1d9-b220f40baac1_各项数值.txt', '2024-12-16 15:32:23');
INSERT INTO `article_attachment` VALUES (12, 51, 'e46b2a93-7c1f-4136-9468-d9b5d0f47649_普奇神父替身.docx', 'uploads/e46b2a93-7c1f-4136-9468-d9b5d0f47649_普奇神父替身.docx', '2024-12-16 15:32:23');

-- ----------------------------
-- Table structure for article_headline
-- ----------------------------
DROP TABLE IF EXISTS `article_headline`;
CREATE TABLE `article_headline`  (
  `hid` int NOT NULL AUTO_INCREMENT COMMENT '文章id',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文章标题',
  `article` varchar(5000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文章内容',
  `type` int NOT NULL COMMENT '文章类型id',
  `publisher` int NOT NULL COMMENT '文章发布用户id',
  `page_views` int NOT NULL COMMENT '文章浏览量',
  `create_time` datetime NULL DEFAULT NULL COMMENT '文章发布时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '文章最后的修改时间',
  `is_deleted` int NULL DEFAULT NULL COMMENT '文章是否被删除 1 删除  0 未删除',
  `image_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`hid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 52 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of article_headline
-- ----------------------------
INSERT INTO `article_headline` VALUES (1, '虚构推理：如何将“真相”捏造为“谎言”', '<p><span style=\"color: rgb(34, 34, 34);\">1、虚拟：虚拟往往存在于人们的意识当中，并不存在的事物通过人们的想象虚构出来的事物称为虚拟。</span><strong style=\"color: rgb(51, 51, 51);\">就像我所观看的动漫作品中的人物存在性则是虚拟，而有些人习惯在观看动漫剧情中自我代入而无法分清虚拟和真实的世界，</strong><span style=\"color: rgb(34, 34, 34);\">这是一种很不可取的做法，理智追漫才能更加准确的了解到一部动漫作品的内涵。</span></p><p>2、真实：真实区别于虚拟，客观事实存在的事物就相当于现实。很多成年人在看到一些动漫角色时走进一个误区，认为动漫则是人们想象出来的产物。<strong style=\"color: rgb(51, 51, 51);\">据其根本这种说法过于偏激，动漫人物由人构思虽然形象是虚拟的，但是优秀的主人公身上散发出来的精神体现是真实的。例如热血动漫中路飞的精神坚持、勇敢、向往自由、友情的燃烧这些都是真实存在的，是我们应该去学习的精神品质。</strong></p><p><strong style=\"color: rgb(51, 51, 51);\">区别</strong></p><p>反过来说《虚构推理》真正富有韵味的地方在哪里呢？在我看来对于樱川九郎和岩永琴子的丰满形象塑造仅仅只是因此，真正耐人寻味和推敲的是对于剧情主线与真真假假的辩论，以及钢人七濑这个人物的是否存在性的判定。我认为钢人七濑这个角色的趣味性就在于它存在于真实和虚假之间。</p>', 2, 1, 161, '2024-11-16 10:00:00', '2024-11-29 12:16:28', 0, NULL);
INSERT INTO `article_headline` VALUES (2, '海贼王', '<p>本指南讲解了机器学习的基本概念，包括监督学习、无监督学习和强化学习等，及其常用算法。</p>', 1, 2, 188, '2024-11-05 14:30:00', '2024-12-04 20:39:57', 0, 'uploads/0ccbea2a-62fc-406d-a5b2-77fe4e45fbed_花.jpg');
INSERT INTO `article_headline` VALUES (3, '计算机网络基础', '本文深入探讨了计算机网络的基础知识，涵盖IP地址、DNS以及各种网络拓扑结构。', 2, 2, 132, '2024-11-10 16:45:00', '2024-01-10 16:45:00', 0, NULL);
INSERT INTO `article_headline` VALUES (4, '命运石之门讲解', '故事发生在一个名为“未来道具研究所”的组织中，冈部伦太郎和他的朋友们在这个实验室中进行各种实验。2010年7月28日，冈部在科学讲义会场遇到了天才少女牧濑红莉栖。在一次实验中，冈部发现自己的手机可以通过发送电子邮件来改变世界线的变动，这一发现让他成为了能够影响世界进程的关键人物。故事的核心在于冈部通过不断发送D-MAIL和时间跳跃来尝试改变一些重要事件的结果，尤其是为了阻止青梅竹马的真由理死亡。\n\n主要角色\n‌冈部伦太郎‌：本作的主人公，自称“凤凰院凶真”。\n‌牧濑红莉栖‌：天才少女，对时间理论有深刻理解。\n‌椎名真由理‌：冈部的青梅竹马，故事中的重要角色。\n‌阿万音铃羽‌：未来人，拥有改变世界线的能力。\n世界线概念\n故事中提到的一个重要概念是“世界线”。与平行宇宙不同，世界线是指在一个唯一的世界中，通过改变过去来影响未来的路径。冈部的行动就像是在一条铁轨上驾驶火车，每一次改变都会让火车驶入不同的分支轨道，但最终这些分支都会导向一个主要的结果。这种设定使得故事充满了悬疑和探索的乐趣。\n\n结局和主题思想\n故事的结局是开放的，冈部通过不断的努力，最终进入了所谓的“命运石之门线”，这是一条美好的未来。这个结局传达了即使世界线如何变动，最终都会有一个美好的结果的主题思想。通过冈部的努力和坚持，故事传递了希望和坚持的力量。\n\n这部作品以其复杂的剧情和深刻的主题思想成为了经典，吸引了大量粉丝并获得了良好的评价。', 3, 1, 215, '2024-11-15 09:20:00', '2024-11-21 22:30:41', 0, NULL);
INSERT INTO `article_headline` VALUES (5, '七大罪', 'Spring、Spring MVC和MyBatis这三个开源框架的整合，它们共同构成了一个轻量级的Java EE企业应用开发框架。以下是对这三个框架的简要介绍：\n\nSpring框架：\n\n核心功能：Spring是一个开源的Java平台，提供了全面的基础设施支持，用于解决企业级应用开发中的复杂性。它的核心功能包括依赖注入（DI）和面向切面编程（AOP）。\n优点：简化了Java开发，提供了声明式事务管理，支持各种ORM框架集成，以及广泛的中间件集成。\n组件：Spring还提供了其他组件，如Spring MVC（Web应用开发）、Spring Security（安全控制）、Spring Data（数据访问）等。\nSpring MVC：\n\n功能：Spring MVC是Spring的一个模块，用于构建Web应用程序。它是一个基于Java的实现MVC设计模式的框架。\n优点：简化了Web应用开发，支持RESTful应用开发，提供了强大的数据绑定和渲染能力。\n特点：它通过前端控制器（DispatcherServlet）来处理所有的HTTP请求，并使用注解或XML配置来映射URL到对应的处理器（Controller）。\nMyBatis：\n\n功能：MyBatis是一个持久层框架，它简化了数据库交互和结果映射的过程。\n优点：提供了XML或注解的方式来配置SQL语句，支持动态SQL，允许开发者编写更加灵活和优化的数据库操作代码。\n特点：MyBatis不强制要求使用ORM（对象关系映射），而是提供了一个映射过程，使得开发者可以自由地编写SQL语句，同时还能享受到对象映射的便利。', 1, 2, 176, '2024-01-20 12:00:00', '2024-11-21 22:33:16', 1, NULL);
INSERT INTO `article_headline` VALUES (6, '刺客五六七', 'Java是一种广泛使用的编程语言，由Sun Microsystems（现在是Oracle Corporation的一部分）在1995年发布。Java是一种面向对象的语言，它具有平台无关性，这意味着“一次编写，到处运行”（Write Once, Run Anywhere，简称WORA）的能力，因为Java程序在任何支持Java虚拟机（JVM）的设备上都能运行。以下是Java技术的几大核心领域：\n\nJava基础：\n\n包括Java语言的基本语法、数据类型、控制结构、面向对象编程（OOP）概念、异常处理等。\nJava集合框架：\n\n提供了一系列接口和类，用于存储和处理数据集合，如List、Set、Map等。\nJava I/O（输入/输出）：\n\n用于处理文件和数据流，包括文件的读写、网络通信等。\n多线程和并发：\n\nJava提供了强大的多线程支持，允许开发者创建和管理并发执行的线程。\nJava网络编程：\n\n允许开发者创建客户端和服务器应用程序，处理网络通信。\nJava数据库连接（JDBC）：\n\n提供了一种执行SQL语句的标准方法，允许Java程序连接和操作数据库。\nJava Web技术：\n\n包括Servlet、JSP（JavaServer Pages）、Java EE（Enterprise Edition）等技术，用于构建Web应用程序。', 1, 10, 75, '2024-11-09 15:25:33', '2024-11-21 22:32:01', 0, NULL);
INSERT INTO `article_headline` VALUES (17, '死亡笔记', '‌红黑树是一种自平衡的二叉查找树，通过特定的操作保持树的平衡，从而确保操作的效率。‌ 红黑树通过以下规则来维持平衡：‌12\n\n‌节点颜色‌：每个节点可以是红色或黑色。根节点是黑色的，所有叶子节点（NIL或NULL）也是黑色的。如果一个节点是红色的，那么它的子节点必须是黑色的。\n\n‌平衡条件‌：红黑树确保从根到叶子的最长可能路径不多于最短可能路径的两倍。这意味着树的高度大致上是平衡的，从而保证了在最坏情况下，插入、删除和查找操作的时间复杂度为O(log n)，其中n是树中元素的数量。\n\n‌操作规则‌：在插入和删除节点时，红黑树通过旋转和重新着色来维持平衡。具体操作包括左旋、右旋、变色等，以确保树的平衡性不被破坏。\n\n红黑树的这些特性使其在实际应用中非常高效，常用于实现关联数组和其他需要高效查找、插入和删除操作的数据结构中。', 2, 11, 132, '2024-11-11 10:43:44', '2024-11-21 22:31:15', 1, NULL);
INSERT INTO `article_headline` VALUES (18, '测试', '测试机', 1, 11, 0, '2024-11-11 16:04:42', '2024-11-11 16:04:42', 1, NULL);
INSERT INTO `article_headline` VALUES (19, '测试', '测试测试', 3, 11, 174, '2024-11-13 16:43:21', '2024-11-13 17:44:02', 1, NULL);
INSERT INTO `article_headline` VALUES (20, '测试2', '测试测试', 5, 11, 24, '2024-11-16 12:02:41', '2024-11-16 12:02:41', 1, NULL);
INSERT INTO `article_headline` VALUES (21, '你好', '<p>吼吼吼吼吼吼吼吼吼吼吼吼吼吼吼</p>', 4, 11, 82, '2024-11-17 11:05:44', '2024-11-29 15:23:25', 1, 'uploads/6ef7a1cc-7697-4453-890e-04f5b806b666_6.png');
INSERT INTO `article_headline` VALUES (22, '测试99', '<p>红红vbnfgnf`131231好的惚惚哈哈哈哈哈哈哈哈哈哈哈语言搜索</p>', 5, 11, 168, '2024-11-17 14:12:06', '2024-11-29 11:53:19', 1, 'uploads/522bfffc-db99-40b6-90fc-929a1e93bbb3_6.png');
INSERT INTO `article_headline` VALUES (23, '未闻花名的友谊', '<p>哈哈哈哈哈</p>', 5, 11, 55, '2024-11-21 18:24:52', '2024-12-05 13:28:49', 0, 'uploads/546cd92d-e86c-42e6-905d-2797b56ac864_花.jpg');
INSERT INTO `article_headline` VALUES (24, '纵有疾风起，人生不言弃——读《强风吹拂》有感', '<p>	  跑步是孤单的运动，但跑步者却可以不孤单。尽管故事主线离不开跑步，但诉说的却是每个队员真实面对的人生。即便是一个田径队的队友，由于自身经历的不同，每个主角都有着自己的人生课题。阿雪虽然是个尖子生但却始终面对不了母亲的再嫁；King哥充满了毕业找工作的焦虑，阿走摆脱不了高中田径队变态教练的阴影，伤病的清濑更是无法忘记挑战箱根的心愿。每个人物都有着自己挥不去的故事和丰富的人格，这也让故事本身丰满又充盈。然而跑步将他们的人生在这一年的训练中联系在了一起，每个人都试图发自内心的去诘问跑步的意义。是什么让一个人拼尽全力却又无悔呢？</p><p>	   作者给出的答案是，跑步的意义是让人变“强”。这个“强”我觉得是一个因人而异的变量，因为跑步这项运动正如清濑所说，是没有意义的，甚至取得冠军也是没有意义的。“没有意义也不是什么坏事。这不是在说什么漂亮话。跑步的目的，当然是要取得胜利，但胜利其实有许多种形式。所谓的胜利，不单是指在所有参赛者中跑出最好的成绩。就像人活在这世上，怎样才算”人生胜利组”，也没有明确的定义。”跑步，在这里是一个模糊化的指代，它可以替换为每个人生活中为之奋斗的任何目标。但在竹青庄的十个人心中，在箱根驿传的赛道上，跑步是所有队员精神的凝聚和集中释放。我非常喜欢书中清濑对“如何评价长跑选手”的描述，非常值得品味。</p><p>	“你知道对长跑选手来说，最棒的赞美是什么吗？”</p><p>	“是‘快’吗？”</p><p>	“不，是‘强’，”清濑说，“光跑得快，是没办法在长跑中脱颖而出的。气候、场地、比赛的发展、体能，还有自己的精神状态——长跑选手必须冷静地分析这许多要素，即使面对再大的困难，也要坚韧不拔地突破难关。长跑选手需要的，是真正的‘强’。所以我们必须把‘强’当作最高的荣誉，每天不断跑下去。”变强需要时间，也可以说它永远没有终点。长跑是值得一生投入的竞赛，有些人即使老了，仍然没有放弃慢跑或马拉松运动。</p>', 5, 11, 42, '2024-11-23 11:13:58', '2024-12-15 15:39:45', 0, 'uploads/f72bd8c4-1824-40df-adec-d18e7e1cad14_吹.jpg');
INSERT INTO `article_headline` VALUES (25, '测试测试测试测试测试测试', '', 1, 11, 4, '2024-11-23 12:43:04', '2024-11-23 12:43:18', 1, NULL);
INSERT INTO `article_headline` VALUES (26, '《排球少年》:在成长的道路上，就是要不断改变', '<p>aerhearyaeryaery的购啊AEEEEEEE</p>', 4, 11, 110, '2024-11-23 15:31:57', '2024-12-05 16:22:58', 0, 'uploads/2be2b3a9-5d28-4ec8-a4ac-9eedcdd57282_球.jpg');
INSERT INTO `article_headline` VALUES (27, '圣诞“人类补完计划”:重温《EVA》的青春与热血', '<p>象一下，冬天的白雪覆盖着城市的每一个角落，街头的彩灯闪烁着温暖的光辉，圣诞节的气息扑面而来。在这样一个特别的时刻，我们不禁要问：什么样的情感才足以裹挟我们的心灵，驱使我们共同寻找那份失落已久的热情？答案或许就在经典动画《新世纪福音战士》（EVA）中。</p><p>作为一部对生命、成长、爱与信仰进行深刻探讨的作品，《EVA》不仅仅超越了动画的界限，更成为了无数人青春岁月的烙印。从1995年首播的那一刻起，它就以其令人震撼的剧情、复杂的人物关系和浓厚的哲学思考俘获了无数观众的心。而“人类补完计划”作为故事的核心理念，正是这份探索和挣扎的结晶。</p><p>在《EVA》里，“人类补完计划”旨在消除人与人之间的隔阂，实现人类精神上的统一和进化。不仅如此，这一计划的实施更是伴随着巨大的危险和未知。它质疑了人性的本质，探讨了人类所追求的真正意义。圣诞节的庆祝与“人类补完计划”的主题碰撞在一起，形成了奇妙的层次与反思，令人深思。</p><p>你或许还记得在夜深人静时，那些伴随《EVA》响起的音乐，每一段旋律都如同一条流淌的记忆，带你重回那个血脉贲张的状态。比如经典的《残酷な天使のテーゼ》，不仅旋律激昂，其歌词更是引人入胜，探讨了命运与选择，这种对生命深刻的反思是否让你在某个时刻为之动容？而如今，我们终于迎来了以“人类补完计划”为主题的圣诞特别活动，诚邀所有EVA的粉丝们共同参与，让我们在回顾经典的同时，探讨更深层的人性与情感。</p><p>想象一下，当你走入这样一场活动时，银色的星光下，耳边回响着熟悉的旋律，仿佛重新激活了内心深处那股被遗忘的热情和期待。而此次的活动并不仅限于EVA迷，还欢迎所有热爱生命、追求和平与爱的朋友们加入，共同庆祝这份特殊的情感。作为过去的青春记忆，如今的我们又能从中领会到怎样的情感启示呢？每一个音符都是彼此心灵的共鸣。</p><p>在EVA的故事中，每个角色都有着复杂而深刻的背景，他们的挣扎和成长承载了观众无数的情感经历。无论是驾驶EVA的少年少女，还是神秘的使徒，每一个角色都以其独特的方式反映出人类内心的矛盾与冲突。这些元素不仅增强了剧情的厚度，更让人思考人性在面对困境时所展现出的坚韧与美丽。</p><p>值得一提的是，随着近几年科技的发展，AI绘画与AI创作工具逐渐走入我们的视野，改变了我们创作与体验的方式。今年，《EVA》也加入了这些元素，通过人工智能技术为经典形象注入新的生命。这不仅为老粉丝带来了新鲜感，也吸引了大量新观众的目光，形成了新的动漫文化生态。而随着这些科技的不断演进，我们的创作及欣赏方式也因此不断改变，让人不禁思考：未来又将如何再塑经典？</p><p>另外，EVA作为一种文化现象，其影响早已超出日本国界，全球范围内的动漫社区活跃着，cosplay、漫展等文化现象盛行。无论是在国内的各大活动中，还是在海外的漫展上，都能看到穿着EVA角色服装的粉丝们聚在一起，相互交流。这不仅是对作品的一种致敬，更加深了粉丝之间的情感纽带。</p><p>观看《EVA》时，被角色们的情感所震撼，往往让人感同身受。那种沉浸感和强烈的共情能力，让每个人都在荧幕前经历了角色的心路历程。正如剧中所探讨的那样，真正的成长是经历过无数的聚散离合，努力找到彼此不会伤害到的距离。或许，这也是我们每个人在现实生活中需要反思与学习的课题。</p><p>在这次活动中，让我们不仅仅是为了回忆过去的青春，更重要的是让每个人在交流中找到共鸣，将那份热血与激情重新点燃。正如EVA所教会我们的那样：“能做什么就该去做，否则日后后悔就不好了。”反思过去，珍惜现在，期待未来，我们在这条探索人性与情感的道路上，继续前行。</p><p>回顾《EVA》的辉煌历程，我们可以发现，这部作品不仅改变了无数人的青春，也在我们心中播下了思考的种子。期待大家在这个圣诞节，共同参与“人类补完计划”，重燃热情，创造美好回忆。无论你是EVA的忠实粉丝，还是初次接触这部经典的朋友，欢迎你和我们一起，感受那份特别的热血与希望。在这个特别的日子里，愿我们都能收获爱的力量，与志同道合的人一起，共同探讨这个宏伟的主题——人性之光。</p>', 1, 1, 37, '2024-11-29 15:33:11', '2024-12-05 16:22:28', 0, 'uploads/16491cf9-4f6e-429f-8718-ffdba648577d_eva.png');
INSERT INTO `article_headline` VALUES (28, '所以我放弃了编剧——双城之战观后感', '<p class=\"ql-align-right\"><em>——把一个人写得糟糕，莫名其妙，当然是为了给他埋伏笔，先抑后扬，初中老师教的。</em></p><p>看完《双城之战》第一季之后，我写了我人生中第一个公开发表的影评。那个时候还没学电影，还不懂得好莱坞过山车结构，什么都不懂。只是觉得维克托帅爆了。</p><p>我很喜欢维克托跑起来的那个时候，那一段他的情绪表达来到了巅峰。那一段他还只是一个追求海克斯科技带来进步的科学家。</p><p>3年前，我认为塑造得最成功的是维克托，第二就是希尔科。那个让人又爱又恨的“大反派”。</p><p>同时，我痛斥了那个“软饭男”。那个《英雄联盟》中的未来守护者。我在游戏里有多喜欢这个角色，我看完第一季就有多讨厌。他对不起我的期待。</p><p>他确实守护了未来。</p><p>如果把《双城之战》第二季当做一个简单的动画，可能他得不到什么好评。它集结了我几乎所有的，公式化的点。包括公式化编剧，包括公式化LGBT，包括公式化起承转合。如果把一个艺术品当做数学难题来做，诚然，只要你套对了公式，走对了过程以及步骤，做对了这道名为“双城之战”的题，那么你会得到卷面满分。这是初高中的事。</p><p>但是，如果把它和《英雄联盟》的游戏结合起来。我很难不承认这是非常成功的一部剧。别的剧可能需要很多，彩蛋，预告片。它不用，它只是把整个故事切成了三幕，不用刻意去看剧透，官方在合适的时间放出英雄皮肤，即，他们在剧中会出现的样子。然后再在合适的时候将他们挂上货架，如此，完美。如果说他在和游戏的营销上，我不得不承认他是完美的。</p><p>特别是，“软饭男”。三年前，我并不欣赏杰斯。我最主观的审美告诉我，郎才女貌才是唯一主题。这才是我想看的东西。当他和凯特琳，在我看来天造地和的两位逐渐分离，而他转头奔向了给他权利，金钱，支持的梅尔时，我觉得这个人糟糕透了。他没有我喜欢的少年意气，他在系列剧中做不到那种只有英雄才值得的事。他，彻彻底底，是个软饭男。</p><p>而现在的我只会轻描淡写地说一句——第一季的杰斯只是没有达成我的主观意愿而已。我的主观意识太强烈，眼里容不得除了我想要的以外的任何剧情。所以，我不喜欢他和梅尔在一起。人物上，我也更喜欢皮城女警。再加上对维克托的单方偏爱，我并不觉得这是一个合格的搭档。</p><p>在第二季的剧情中，他成为了那个钢铁侠。在无数的宇宙时间线中成为了唯一的救赎，完成了他的使命。就杰斯本人而言，角色塑造是没有问题的。</p><p>但，为什么演变到最后就是钢铁侠剧情了呢？和钢铁侠托尼斯塔克比，他只是做到了，在无数时间线中，完成了他唯一的救赎。但，这个故事并没有那么长，并没有那么多救赎，并没有那么多的痛定思痛，有的只是3年的等待，两个月的放映。仅仅只是18集的剧集，我想这个结果是不让人满意的。</p><p>托尼斯塔克的响指为什么能够荡气回肠，是因为漫威铺垫了10年，在十多年的剧情中，从《钢铁侠1》，到《复仇者联盟之终局之战》，有太多部电影。而这么多这么多部电影，几乎把所有出场人物的来龙去脉，故事背景，人物关系，全部讲得淋漓尽致。所以在托尼斯塔克打响指的那一刻，才真正意义上能够为过去那么多年的故事画个句号。杰斯，我不觉得本身塑造得不行，可能是在剧透之后我大概知道会是一个好的结局，那么杰斯一定会做到，而在这种情况之下，失去了代入感，失去了沉浸感，更像是一个旁观者在看一个故事。没有在谁的角度思考，只是想把这个故事读完。知道到底这个编剧写了个什么故事。</p><p>就这样。</p><p>杰斯不再是软饭男，或者他从来都不是，他是一个注定要走完完整Hero\'s Journey的男一号。他是那个，当你作为召唤师选择他的时候，他会坚定地喊出那句他已经喊了十年的台词</p><p>——<strong><em>为了更美好的明天而战</em></strong>。</p>', 1, 11, 18, '2024-12-05 10:16:53', '2024-12-15 15:41:19', 0, 'uploads/7e96df48-a07c-46d3-8d67-3239296b2ec4_1.jpg');
INSERT INTO `article_headline` VALUES (29, '测', '<p>钱钱钱</p>', 1, 11, 1, '2024-12-05 15:07:08', '2024-12-05 15:07:08', 1, 'uploads/0f9c2504-0998-4768-b71d-e574cd0e117c_花.jpg');
INSERT INTO `article_headline` VALUES (30, '确定', '<p>在这种</p>', 1, 11, 2, '2024-12-05 18:07:41', '2024-12-05 18:07:41', 1, NULL);
INSERT INTO `article_headline` VALUES (31, '钱钱钱', '<p>去1</p>', 1, 11, 0, '2024-12-05 18:10:57', '2024-12-05 18:10:57', 1, 'uploads/f713e310-2293-41dc-bd32-d60392b98ada_eva.png');
INSERT INTO `article_headline` VALUES (32, '强强强强钱钱钱', '<p>哈哈哈哈哈哈哈哈哈哈哈</p>', 1, 11, 1, '2024-12-05 21:39:27', '2024-12-05 21:39:27', 1, NULL);
INSERT INTO `article_headline` VALUES (33, '啊啊啊啊', '<p>啊啊啊</p>', 1, 11, 0, '2024-12-05 21:41:36', '2024-12-05 21:41:36', 1, NULL);
INSERT INTO `article_headline` VALUES (34, '威威', '<p>单位</p>', 1, 11, 2, '2024-12-05 21:58:01', '2024-12-05 21:58:01', 1, NULL);
INSERT INTO `article_headline` VALUES (35, '得到', '<p>得到</p>', 1, 11, 0, '2024-12-05 22:07:07', '2024-12-05 22:07:07', 1, NULL);
INSERT INTO `article_headline` VALUES (36, '呜呜呜呜', '<p>得到</p>', 1, 11, 0, '2024-12-05 22:09:11', '2024-12-05 22:09:11', 1, NULL);
INSERT INTO `article_headline` VALUES (37, '坎坎坷坷', '<p>刚刚</p>', 1, 11, 0, '2024-12-05 22:11:00', '2024-12-05 22:11:00', 1, 'uploads/e20da6ae-d3c0-4556-898a-452806b4602d_eva.png');
INSERT INTO `article_headline` VALUES (38, '扭扭捏捏男男女女', '<p><br></p>', 1, 11, 0, '2024-12-05 22:12:12', '2024-12-05 22:12:12', 1, 'uploads/7bfc5ee1-47ee-4d5d-a654-d3a47132683f_双.png');
INSERT INTO `article_headline` VALUES (39, '哒哒哒哒哒哒1', '<p>搜索</p>', 1, 11, 0, '2024-12-05 22:25:46', '2024-12-05 22:25:46', 1, NULL);
INSERT INTO `article_headline` VALUES (40, '1212', '<p>威威</p>', 1, 11, 0, '2024-12-05 22:27:10', '2024-12-05 22:27:10', 1, NULL);
INSERT INTO `article_headline` VALUES (41, '大多数1', '<p>11我弟弟</p>', 1, 11, 7, '2024-12-05 22:35:36', '2024-12-06 10:16:40', 1, NULL);
INSERT INTO `article_headline` VALUES (42, '噩噩', '<p>噩噩</p>', 1, 11, 0, '2024-12-05 22:44:11', '2024-12-05 22:44:11', 1, NULL);
INSERT INTO `article_headline` VALUES (43, '嗡嗡嗡', '<p>搜索</p>', 1, 11, 0, '2024-12-05 22:47:40', '2024-12-05 22:47:40', 1, NULL);
INSERT INTO `article_headline` VALUES (44, '兵长为什么选择阿尔敏放弃团长？艾伦的一番话是关键', '<p class=\"ql-align-justify\">进击的巨人第三季18集惨烈的战斗进入了收尾阶段，调查兵团的众人对这场战斗进行了深刻的反思，也为到底拯救埃尔文还是阿尔敏产生了争执。巨人药剂只有一支，智慧型巨人也只有一个。最终阿尔敏留人间，团长去阴间。作为一直主张要复活埃尔文的兵长，为什么会在最后选择了阿尔敏呢？</p><p class=\"ql-align-justify\">兵长的心路历程，其实可以分为三个阶段：</p><p>   <span style=\"color: rgb(64, 64, 64);\">第一阶段，坚决要救埃尔文。兵长救谁第一反应是私人感情，第二反应是存活价值。这个时候的兵长，从私心上来说，毕竟埃尔文与他是多年的战友，感情极深，他不希望自己的主君死去；从公心上来说，他认为埃尔文将来能发挥的作用大于阿尔敏。于私于公，他都没有理由放弃埃尔文。</span></p><p><span style=\"color: rgb(64, 64, 64);\">  第二阶段，开始动摇，但仍觉得埃尔文更重要。让兵长开始动摇的节点，是弗洛克的一句话:“这个地狱需要他”，“我们需要这个恶魔”。正是这句话提醒着兵长：让团长在这个地狱活下去对团长本人是一种折磨。这个时候，从私心上兵长已经不愿意救团长了，但从公心上他依然认为团长比较重要，所以他依然坚持要救团长。</span></p><p><span style=\"color: rgb(64, 64, 64);\">        第三阶段，动摇进一步加剧，最终选择放过团长，给阿尔敏注射。动摇加剧的开始，是艾伦最后提到的“看海”。紧接着出现的是兵长的一幕幕回忆。第一幕是三人组看海，第二幕是团长关于“实现梦想过后要怎么办”的答复，如果说之前兵长坚决认为团长比阿尔敏重要，那么从这里他已经开始把两者放在天秤上比较。同样是关于梦想的画面，阿尔敏的表现透露出对未来的希望，团长的表现则透露出迷茫。同样是追梦之人，团长却比阿尔敏透露出更多的疲态，因为他已经背负了太多阿尔敏所没有承受过的东西，而且已经快要超过他的承受极限。</span></p><p><span style=\"color: rgb(64, 64, 64);\">到了王政篇，在“将艾伦交给王政府”和“推翻王政府”之间，他出于私心选择了后者，尽管他认为前者对人类更加有利。而到了玛利亚夺还战，他的梦想与人类大义再次产生冲突的时候，他却选择为了人类大义放弃梦想。这一反常态的举动就是他已经“濒临崩溃”的证明。王政篇他的“自我检讨”已经让我们看出他虽然可以为了梦想放弃人性，但他舍弃不了最基本的是非观。他十分清楚自己的动机不是为了人类，尽管客观事实上他确实为人类作出了不小的贡献。这种负罪感和自我厌恶越积越深，到了玛利亚夺还战终于快要将他压垮，所以这次他在离梦想仅一步之遥的时候舍弃梦想选择了大义。</span></p><p><br></p>', 1, 11, 7, '2024-12-05 22:51:38', '2024-12-15 16:03:23', 0, 'uploads/42158637-bc7d-4fc0-aa57-bb75ddf201dc_下载.jpg');
INSERT INTO `article_headline` VALUES (45, 'EE', '<p>QQ</p>', 1, 11, 0, '2024-12-05 23:09:33', '2024-12-05 23:09:33', 1, NULL);
INSERT INTO `article_headline` VALUES (46, '侵权', '<p>我</p>', 1, 11, 0, '2024-12-05 23:13:50', '2024-12-05 23:13:50', 1, NULL);
INSERT INTO `article_headline` VALUES (47, '《英雄联盟双城之战》杰斯为什么要炮轰维克托，毁掉地下乌托邦？', '<p>《英雄联盟：双城之战》第二季中，维克托与海克斯核心融合后，与昔日好友杰斯分道扬镳，来到底城建立了一个地下乌托邦。</p><p><br></p><p>此时的维克托就像耶稣临世，拥有神奇的治愈能力，他治愈了许许多多受微光毒害的底城人。</p><p>越来越多的人慕名前来，被治愈后留在这里，大家合力建设起一个理想的乌托邦世界。</p><p>这里五彩缤纷，美轮美奂，这里禁止武器，人与人之间互帮互助，和谐共处。</p><p>金克丝发现残暴嗜血的狼人居然是范德尔，与蔚一起带着狼人来到维克托处寻求治疗。</p><p>在狼人的身体里，兽性正在吞噬着仅存的人性，即使对维克托来说，这也是一个棘手的难题。</p><p>尽管如此，维克托仍然想尽力一试，狼人留在这里风险很大，维克托救人的条件是晚上将狼人囚禁起来。</p><p>蔚与金克丝来到乌托邦之后，被这里的景象所打动，萌生了留下来帮助建设此地的想法。</p><p>维克托进入狼人的精神世界，对范德尔的治疗成效显著。</p><p>范德尔与蔚、金克丝一家三口团聚，场面温馨感人，一切都在向着好的方向发展</p><p>当安蓓萨带人来犯，蔚说服了凯特琳与她联手做戏，控制住了安蓓萨以及炼金术士辛吉德。</p><p>然而，出乎所有人意料的是，杰斯突然出现在地下乌托邦，将他的海克斯武器对准了维克托。</p><p>遭受炮轰后的维克托，身负重伤，栽倒在地，随之无数名经他救治过的底城人民昏厥倒地。</p><p>原来，维克托并非真正治愈了这些人，而是用自身的能量维持着这些人的健康纯净状态。</p><p>一旦维克托一人倒下了，这些人没有了他提供的能量支撑，也都倒下了。</p><p>狼人的情况更糟，范德尔的意识消退，狼人嗜血的兽性占了上风，对身边人展开残暴攻击。</p><p>伊莎为了救大家，端起金克丝的武器，与狼人同归于尽。</p><p>本来因一家团聚难得正常的金克丝，这下又要疯癫暴走了。</p><p>因此可以猜测，杰斯卷入奥术异象后，经历了一系列复杂且恐怖的事情，很可能只有他一个人出来了。</p><p>为了阻止骇人未来景象的发生，杰斯来到了地下乌托邦，用海克斯科技锤炮轰了维克托，大概率是为了毁掉海克斯核心。</p><p>因为他看到了或者亲历了维克托以及他所融合的海克斯核心，在未来所造成的巨大灾难。</p>', 1, 11, 35, '2024-12-06 10:35:08', '2024-12-15 15:10:06', 0, 'uploads/2cc56d88-29d2-42eb-8cca-b3c5a76f4fcc_双.png');
INSERT INTO `article_headline` VALUES (48, '附件显示下载测试', '<p>111</p>', 1, 11, 6, '2024-12-06 11:08:17', '2024-12-06 11:08:17', 1, 'uploads/e6b45d7a-ef91-40a2-8e5c-1b0c11c74792_双.png');
INSERT INTO `article_headline` VALUES (49, '用这篇万字长文，致敬10年EVA', '<p class=\"ql-align-justify\">《新世纪福音战士（EVA）》诞生以来的二十多年里，光是中文影评就已汗牛充栋。读者可能会问：为什么还需要一个新的诠释——你又凭什么自我标榜为“新”？笔者写作的初衷正是对市面上已有分析的一些基本不满。</p><p class=\"ql-align-justify\"><br></p><p class=\"ql-align-justify\">作为《EVA》的标志性元素，“人类补完计划”、三次冲击、EVA机体、使徒等等贯穿全片，作为一整套背景设定串起了所有角色的选择和命运——然而，或许因为这些元素已经越出科幻边界而不值得严肃对待，很多知识竞赛式影评（比如这篇）把《EVA》降格为一幅由对哲学宗教的引用和诠释组成的拼贴画，却独独没看到这部影视作品内源的哲思：对科技工程试图改变人类根本境况的隐喻。另一些解读则见木不见林，聚焦于几个台前角色（几位驾驶员少年外加葛城美里）和最表层情节，对背后推动剧情发展的力量——碇源堂、SEELE和碇唯以及他们各自的补完计划，却因为细节过于零碎稀少、呈现过于隐晦而说不出个所以然，无法看到众多人物的命运和选择在世界观层次上的意义和联系。</p><p class=\"ql-align-justify\"><br></p><p class=\"ql-align-justify\">本文正是要直面这些挑战。我将从一个鲜为人知的概念，“形而上生物学”切入，整合以三次冲击和“人类补完计划”为核心的《EVA》宇宙设定，厘清三个补完计划的背景、分歧和争夺，借此呈现受末日科技催生的欲望驱使，老少两代人物给自己和彼此制造的悲剧。</p><p class=\"ql-align-justify\"><br></p><p class=\"ql-align-justify\">01</p><p class=\"ql-align-justify\">“形而上生物学”：《EVA》宇宙论</p><p class=\"ql-align-justify\"><br></p><p class=\"ql-align-justify\">&nbsp;“人类补完计划”是《EVA》宇宙设定的一个核心环节，要重探《EVA》不妨从这里开始。每当谈到“人类补完计划”，人们几乎总是回避其设定细节。毕竟这个词几乎总是出现在宽泛深奥的哲理对白，如下两则：</p><p class=\"ql-align-justify\"><br></p><p class=\"ql-align-justify\">葛城美里心想：将人类这种已走上绝路的残缺群体，用人工进化的方式使其变为完全的单体生物，这就是补完计划。还真是个理想的世界（《Air/真心为你》）</p><p class=\"ql-align-justify\"><br></p><p class=\"ql-align-justify\">SEELE委员会内部对话：我等并非为搭上名为eva的方舟，而甘愿放弃人类的形态</p><p class=\"ql-align-justify\">那不过是为了让闭塞的人类再生，所进行的通过仪式</p><p class=\"ql-align-justify\">毁灭的宿命中也包含了新生的喜悦</p><p class=\"ql-align-justify\">为了让神、人类及所有的生命，终能透过死合而为一（出处同上）</p><p class=\"ql-align-justify\"><br></p><p class=\"ql-align-justify\">可见，补完首要推动者SEELE委员会相当苦于人类最根本的生存处境——自我与他人关系，甚至将其看作人类的痛苦和困境的唯一根源。不只在“人类补完计划”的层面，这也是片中人物关系的核心主题，“豪猪困境”（葛城美里：“豪猪的距离吗？越是靠近越会伤害对方”，第4话）。</p><p class=\"ql-align-justify\"><br></p><p class=\"ql-align-justify\">其实在现实中，我们不也常抽象地把人生的艰难归咎于人与人之间的隔膜吗？但这并不就是一切问题的唯一根源，就算是，我们也不寄望从根源上一劳永逸解决一切问题，毕竟人际关系是暂时无望实质性改善、因此需要接受下来的存在前提。这却正是《EVA》宇宙和现实世界的本质区别：在那里，借助科技的神力，人类的根本处境是可完善的（“补完”），人类有可能放弃个体形态、融合为单体生物，神。正是在完善的希望的反衬下，人类的现状才显得如此可憎，才孕育出SEELE骇人听闻的人工进化计划。而这个计划的前提和关键就是“形而上生物学”。</p><p><br></p>', 1, 11, 5, '2024-12-06 16:51:02', '2024-12-16 11:16:41', 0, 'uploads/02afc17e-72d9-4dca-95de-9b649997193b_eva.png');
INSERT INTO `article_headline` VALUES (50, '测试999', '<p>啊啊</p>', 1, 11, 1, '2024-12-12 13:31:10', '2024-12-12 13:31:10', 1, 'uploads/19c2f3ae-14d5-45c4-9141-11a106810a73_eva.png');
INSERT INTO `article_headline` VALUES (51, 'jojo的奇妙冒险石之海解说', '<p>奇妙冒险</p>', 2, 11, 20, '2024-12-16 15:32:14', '2024-12-16 17:52:50', 0, 'uploads/e3a97a8b-7e36-41af-af57-5fb0a98372b1_test.png');

-- ----------------------------
-- Table structure for article_type
-- ----------------------------
DROP TABLE IF EXISTS `article_type`;
CREATE TABLE `article_type`  (
  `tid` int NOT NULL AUTO_INCREMENT COMMENT '文章类型id',
  `tname` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文章类型描述',
  PRIMARY KEY (`tid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of article_type
-- ----------------------------
INSERT INTO `article_type` VALUES (1, '热血');
INSERT INTO `article_type` VALUES (2, '推理');
INSERT INTO `article_type` VALUES (3, '科幻');
INSERT INTO `article_type` VALUES (4, '奇幻');
INSERT INTO `article_type` VALUES (5, '励志');

-- ----------------------------
-- Table structure for article_user
-- ----------------------------
DROP TABLE IF EXISTS `article_user`;
CREATE TABLE `article_user`  (
  `uid` int NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户登录名',
  `user_pwd` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户登录密码密文',
  PRIMARY KEY (`uid`) USING BTREE,
  UNIQUE INDEX `username_unique`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of article_user
-- ----------------------------
INSERT INTO `article_user` VALUES (1, 'zhangsan', 'e10adc3949ba59abbe56e057f20f883e');
INSERT INTO `article_user` VALUES (2, 'lisi', 'e10adc3949ba59abbe56e057f20f883e');
INSERT INTO `article_user` VALUES (5, 'zhangxiaoming', 'e10adc3949ba59abbe56e057f20f883e');
INSERT INTO `article_user` VALUES (6, 'xiaohei', 'e10adc3949ba59abbe56e057f20f883e');
INSERT INTO `article_user` VALUES (9, 'liudhusai', '289dff07669d7a23de0ef88d2f7129e7');
INSERT INTO `article_user` VALUES (10, 'zhang', 'e10adc3949ba59abbe56e057f20f883e');
INSERT INTO `article_user` VALUES (11, 'liushuai', 'e10adc3949ba59abbe56e057f20f883e');
INSERT INTO `article_user` VALUES (12, 'admin', '63a9f0ea7bb98050796b649e85481845');

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`  (
  `cid` int NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `article_id` int NOT NULL COMMENT '对应的文章ID',
  `user_id` int NOT NULL COMMENT '评论者的用户ID',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '评论内容',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评论时间',
  `pid` int NULL DEFAULT NULL,
  PRIMARY KEY (`cid`) USING BTREE,
  INDEX `article_id`(`article_id` ASC) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  CONSTRAINT `comment_ibfk_1` FOREIGN KEY (`article_id`) REFERENCES `article_headline` (`hid`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `comment_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `article_user` (`uid`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '评论表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of comment
-- ----------------------------
INSERT INTO `comment` VALUES (1, 19, 11, '不错', '2024-11-13 21:37:46', NULL);
INSERT INTO `comment` VALUES (3, 17, 11, '不错', '2024-11-13 21:52:44', NULL);
INSERT INTO `comment` VALUES (4, 19, 1, '可以哈', '2024-11-14 16:54:04', NULL);
INSERT INTO `comment` VALUES (9, 19, 11, '牛的', '2024-11-15 16:30:33', 4);
INSERT INTO `comment` VALUES (11, 22, 1, '不错哈', '2024-11-17 14:50:14', NULL);
INSERT INTO `comment` VALUES (12, 17, 1, '我也觉得', '2024-11-17 14:54:31', 3);
INSERT INTO `comment` VALUES (13, 4, 11, '1111', '2024-11-17 18:43:57', NULL);
INSERT INTO `comment` VALUES (14, 22, 11, '1233', '2024-11-18 19:25:40', 11);
INSERT INTO `comment` VALUES (15, 26, 11, '写的好', '2024-11-23 15:56:17', NULL);
INSERT INTO `comment` VALUES (16, 26, 2, '是的', '2024-11-29 12:13:29', 15);
INSERT INTO `comment` VALUES (17, 27, 1, '嗯~ o(*￣▽￣*)o', '2024-11-29 15:33:33', NULL);
INSERT INTO `comment` VALUES (18, 27, 11, '嗯', '2024-12-03 17:28:44', 17);
INSERT INTO `comment` VALUES (19, 27, 1, '哈哈', '2024-12-04 21:35:01', 18);
INSERT INTO `comment` VALUES (20, 47, 11, '哈哈', '2024-12-16 16:50:54', NULL);
INSERT INTO `comment` VALUES (21, 47, 11, '写的不错', '2024-12-16 16:51:03', 20);
INSERT INTO `comment` VALUES (22, 47, 11, '谢谢', '2024-12-16 16:51:13', 21);
INSERT INTO `comment` VALUES (29, 51, 11, '不错', '2024-12-16 17:05:05', NULL);
INSERT INTO `comment` VALUES (30, 51, 11, '写得好', '2024-12-16 17:05:20', 29);
INSERT INTO `comment` VALUES (31, 51, 11, '谢谢', '2024-12-16 17:05:26', 30);

SET FOREIGN_KEY_CHECKS = 1;
