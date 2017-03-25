package com.mcdull.cert.domain;

import com.amap.api.maps.model.LatLng;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by mcdull on 15/7/17.
 */
public class SchoolLoc {
    private static final LatLng J01 = new LatLng(28.736633, 115.870810);//教1栋  原教1栋  理学院大楼
    private static final LatLng J02 = new LatLng(28.736942, 115.870821);//教2栋  原教2栋  基础实验室
    private static final LatLng J03 = new LatLng(28.737280, 115.870905);//教3栋  原教3栋  基础科学学院
    private static final LatLng J04 = new LatLng(28.735953, 115.869481);//教4栋  原教4栋  信息工程学院
    private static final LatLng J05 = new LatLng(28.736308, 115.869473);//教5栋  原教5栋  信息工程学院实验室
    private static final LatLng J06 = new LatLng(28.738013, 115.869364);//教6栋  原教6栋  机电学院
    private static final LatLng J07 = new LatLng(28.737576, 115.870845);//教7栋  原教7栋  办公楼
    private static final LatLng J08 = new LatLng(28.737425, 115.869840);//教8栋  原教8栋  综合楼
    private static final LatLng J09 = new LatLng(28.736157, 115.870338);//教9栋  原教9栋  土木学院大楼
    private static final LatLng J10 = new LatLng(28.735342, 115.870984);//教10栋 原教10栋 电气学院
    private static final LatLng J11 = new LatLng(28.735945, 115.871464);//教11栋         振动与噪声教育部工程中心
    private static final LatLng J12 = new LatLng(28.736830, 115.871641);//教12栋         体育学院办公楼
    private static final LatLng J13 = new LatLng(28.738477, 115.869083);//教13栋 原教11栋 机电学院大楼
    private static final LatLng J14 = new LatLng(28.736760, 115.867775);//教14栋 原教12栋 逸夫楼
    private static final LatLng J15 = new LatLng(28.738318, 115.867399);//教15栋 原教13栋 外国语学院 艺术学院
    private static final LatLng J16 = new LatLng(28.738533, 115.872634);//教16栋         大学生事务中心
    private static final LatLng J17 = new LatLng(28.739559, 115.871615);//教17栋         研究生楼
    private static final LatLng J18 = new LatLng(28.742133, 115.872717);//教18栋         实训车间
    private static final LatLng J19 = new LatLng(28.742915, 115.873373);//教19栋         创新大楼

    private static final LatLng J31 = new LatLng(28.744449, 115.867080);//教31栋 原教15栋 北区综合楼
    private static final LatLng J32 = new LatLng(28.744869, 115.866719);//教32栋 原教16栋 辅导员办公室
    private static final LatLng J33 = new LatLng(28.745004, 115.866101);//教33栋 原教17栋 后勤办公楼
    private static final LatLng J34 = new LatLng(28.745434, 115.866188);//教34栋 原教18栋 人文学院大楼
    private static final LatLng J35 = new LatLng(28.746248, 115.868212);//教35栋 原教19栋 科技楼
    private static final LatLng J36 = new LatLng(28.747164, 115.868680);//教36栋 原教20栋 轨道交通学院大楼
    private static final LatLng J37 = new LatLng(28.747937, 115.869985);//教37栋 原教21栋 国际学院大楼 档案馆
    private static final LatLng J38 = new LatLng(28.748589, 115.869352);//教38栋 原教22栋 继续教育学院大楼

    private static final LatLng ST1 = new LatLng(28.737557, 115.872931);//南区一食堂
    private static final LatLng ST2 = new LatLng(28.740495, 115.871117);//南区二食堂
    private static final LatLng ST3 = new LatLng(28.735519, 115.867731);//南区三食堂
    private static final LatLng ST4 = new LatLng(28.738955, 115.873349);//南区四食堂
    private static final LatLng ST5 = new LatLng(28.746007, 115.866062);//北区一、二、三食堂
    private static final LatLng ST6 = new LatLng(28.748030, 115.871582);//北区四食堂

    private static final LatLng XYY = new LatLng(28.739154, 115.869919);//校医院

    private static final LatLng NLT = new LatLng(28.739169, 115.867792);//南区礼堂
    private static final LatLng NCS = new LatLng(28.740462, 115.871140);//南区超市
    private static final LatLng JH  = new LatLng(28.740462, 115.871140);//建设银行
    private static final LatLng ZJL = new LatLng(28.742811, 115.869504);//专家楼
    private static final LatLng JD  = new LatLng(28.742264, 115.870083);//京东快递
    private static final LatLng NCN = new LatLng(28.742979, 115.868091);//南区菜鸟驿站
    private static final LatLng NTS = new LatLng(28.736903, 115.869274);//南区图书馆

    private static final LatLng BLT = new LatLng(28.746320, 115.869368);//北区礼堂
    private static final LatLng ZDS = new LatLng(28.748195, 115.868044);//北区招待所
    private static final LatLng BCS = new LatLng(28.746975, 115.867831);//北区校园超市
    private static final LatLng BKD = new LatLng(28.747672, 115.867449);//北区快递领取

    private static final LatLng KMH = new LatLng(28.733232, 115.874596);//孔目湖

    private static final LatLng NYG = new LatLng(28.739813, 115.873090);//南区运动馆
    private static final LatLng NFY = new LatLng(28.738845, 115.872182);//南区风雨球馆
    private static final LatLng NYC = new LatLng(28.739193, 115.872701);//南区游泳池
    private static final LatLng NHB = new LatLng(28.739355, 115.870605);//南区旱冰
    private static final LatLng NYM = new LatLng(28.739327, 115.871026);//南区羽毛球
    private static final LatLng NPQ = new LatLng(28.739046, 115.870681);//南区排球
    private static final LatLng NLQ = new LatLng(28.738346, 115.871621);//南区篮球
    private static final LatLng NWQ = new LatLng(28.738354, 115.870618);//南区网球
    private static final LatLng NTJ = new LatLng(28.737009, 115.872199);//南区田径场

    private static final LatLng BYG = new LatLng(28.745314, 115.867806);//北区运动馆
    private static final LatLng BWQ = new LatLng(28.749332, 115.867629);//北区网球场
    private static final LatLng BLQ = new LatLng(28.749276, 115.868960);//北区篮球场
    private static final LatLng BTJ = new LatLng(28.751434, 115.868794);//北区田径场
    private static final LatLng BPP = new LatLng(28.746011, 115.865888);//北区乒乓球馆
    private static final LatLng BYM = new LatLng(28.746011, 115.865888);//北区羽毛球馆
    private static final LatLng BTQ = new LatLng(28.746011, 115.865888);//北区台球馆

    private static final LatLng NYS = new LatLng(28.740309, 115.873243);//南区浴室
    private static final LatLng BYS = new LatLng(28.748348, 115.865665);//北区浴室

    private static final LatLng X01 = new LatLng(28.736374, 115.873328);//原01
    private static final LatLng X02 = new LatLng(28.736626, 115.873374);//原02
    private static final LatLng X03 = new LatLng(28.737042, 115.873339);//原03
    private static final LatLng X04 = new LatLng(28.737030, 115.874073);//原22 留学生&研究生楼
    private static final LatLng X05 = new LatLng(28.737943, 115.874936);//原14
    private static final LatLng X06 = new LatLng(28.738329, 115.874762);//原15
    private static final LatLng X07 = new LatLng(28.737991, 115.874171);//原06
    private static final LatLng X08 = new LatLng(28.737990, 115.873041);//原04
    private static final LatLng X09 = new LatLng(28.738325, 115.873118);//原05
    private static final LatLng X10 = new LatLng(28.738311, 115.874093);//原07
    private static final LatLng X11 = new LatLng(28.738599, 115.873915);//原08
    private static final LatLng X12 = new LatLng(28.738886, 115.873915);//原09
    private static final LatLng X13 = new LatLng(28.739153, 115.873964);//原10
    private static final LatLng X14 = new LatLng(28.739425, 115.873838);//原11
    private static final LatLng X15 = new LatLng(28.739694, 115.873818);//原12
    private static final LatLng X16 = new LatLng(28.740022, 115.873862);//原13
    private static final LatLng X17 = new LatLng(28.741204, 115.873267);//原16
    private static final LatLng X18 = new LatLng(28.741331, 115.872801);//原17
    private static final LatLng X19 = new LatLng(28.741596, 115.873235);//原18
    private static final LatLng X20 = new LatLng(28.741782, 115.872789);//原19
    private static final LatLng X21 = new LatLng(28.741915, 115.873255);//原20
    private static final LatLng X22 = new LatLng(28.733377, 115.868120);//原24
    private static final LatLng X23 = new LatLng(28.733998, 115.867999);//原23
    private static final LatLng X24 = new LatLng(28.734748, 115.867845);//原21 国防生大楼

    private static final LatLng X31 = new LatLng(28.745831, 115.872201);//原44
    private static final LatLng X32 = new LatLng(28.746380, 115.872056);//原43
    private static final LatLng X33 = new LatLng(28.746892, 115.872100);//原42
    private static final LatLng X34 = new LatLng(28.747297, 115.870845);//原46
    private static final LatLng X35 = new LatLng(28.747614, 115.870520);//原45
    private static final LatLng X36 = new LatLng(28.750461, 115.872064);//原47
    private static final LatLng X37 = new LatLng(28.745725, 115.867922);//原34
    private static final LatLng X38 = new LatLng(28.746046, 115.867595);//原35
    private static final LatLng X39 = new LatLng(28.745979, 115.867131);//原33
    private static final LatLng X40 = new LatLng(28.746326, 115.867150);//原36
    private static final LatLng X41 = new LatLng(28.747260, 115.866557);//原40
    private static final LatLng X42 = new LatLng(28.747822, 115.866431);//原41
    private static final LatLng X43 = new LatLng(28.746891, 115.865718);//原37
    private static final LatLng X44 = new LatLng(28.747373, 115.865625);//原38
    private static final LatLng X45 = new LatLng(28.747888, 115.865626);//原39


    private static List<Location> nStayList = new LinkedList<>();
    private static List<Location> nStudyList = new LinkedList<>();
    private static List<Location> nLiefList = new LinkedList<>();
    private static List<Location> nPlayList = new LinkedList<>();
    private static List<Location> nOtherList = new LinkedList<>();
    private static List<Location> bStayList = new LinkedList<>();
    private static List<Location> bStudyList = new LinkedList<>();
    private static List<Location> bLiefList = new LinkedList<>();
    private static List<Location> bPlayList = new LinkedList<>();
    private static List<Location> bOtherList = new LinkedList<>();

    static {
        nStayList.add(new Location(1, "学1栋(原1栋)", X01));
        nStayList.add(new Location(2, "学2栋(原2栋)", X02));
        nStayList.add(new Location(3, "学3栋(原3栋)", X03));
        nStayList.add(new Location(4, "学4栋(原22栋)", X04));
        nStayList.add(new Location(5, "学5栋(原14栋)", X05));
        nStayList.add(new Location(6, "学6栋(原15栋)", X06));
        nStayList.add(new Location(7, "学7栋(原6栋)", X07));
        nStayList.add(new Location(8, "学8栋(原4栋)", X08));
        nStayList.add(new Location(9, "学9栋(原5栋)", X09));
        nStayList.add(new Location(10, "学10栋(原7栋)", X10));
        nStayList.add(new Location(11, "学11栋(原8栋)", X11));
        nStayList.add(new Location(12, "学12栋(原9栋)", X12));
        nStayList.add(new Location(13, "学13栋(原10栋)", X13));
        nStayList.add(new Location(14, "学14栋(原11栋)", X14));
        nStayList.add(new Location(15, "学15栋(原12栋)", X15));
        nStayList.add(new Location(16, "学16栋(原13栋)", X16));
        nStayList.add(new Location(17, "学17栋(原16栋)", X17));
        nStayList.add(new Location(18, "学18栋(原17栋)", X18));
        nStayList.add(new Location(19, "学19栋(原18栋)", X19));
        nStayList.add(new Location(20, "学20栋(原19栋)", X20));
        nStayList.add(new Location(21, "学21栋(原20栋)", X21));
        nStayList.add(new Location(22, "学22栋(原24栋)", X22));
        nStayList.add(new Location(23, "学23栋(原23栋)", X23));
        nStayList.add(new Location(24, "学24栋(原21栋)", X24));

        bStayList.add(new Location(25, "学31栋(原44栋)", X31));
        bStayList.add(new Location(26, "学32栋(原43栋)", X32));
        bStayList.add(new Location(27, "学33栋(原42栋)", X33));
        bStayList.add(new Location(28, "学34栋(原46栋)", X34));
        bStayList.add(new Location(29, "学35栋(原45栋)", X35));
        bStayList.add(new Location(30, "学36栋(原47栋)", X36));
        bStayList.add(new Location(31, "学37栋(原34栋)", X37));
        bStayList.add(new Location(32, "学38栋(原35栋)", X38));
        bStayList.add(new Location(33, "学39栋(原33栋)", X39));
        bStayList.add(new Location(34, "学40栋(原36栋)", X40));
        bStayList.add(new Location(35, "学41栋(原40栋)", X41));
        bStayList.add(new Location(36, "学42栋(原41栋)", X42));
        bStayList.add(new Location(37, "学43栋(原37栋)", X43));
        bStayList.add(new Location(38, "学44栋(原38栋)", X44));
        bStayList.add(new Location(39, "学45栋(原39栋)", X45));

        nStudyList.add(new Location(40, "教1栋(理学院)", J01));
        nStudyList.add(new Location(41, "教2栋(理学院实验室)", J02));
        nStudyList.add(new Location(42, "教3栋(理学院)", J03));
        nStudyList.add(new Location(43, "教4栋(信息工程学院)", J04));
        nStudyList.add(new Location(44, "教5栋(信息工程学院实验室)", J05));
        nStudyList.add(new Location(45, "教6栋(机电学院)", J06));
        nStudyList.add(new Location(46, "教7栋(办公楼)", J07));
        nStudyList.add(new Location(47, "教8栋(综合楼)", J08));
        nStudyList.add(new Location(48, "教9栋(土木学院)", J09));
        nStudyList.add(new Location(49, "教10栋(电气学院)", J10));
        nStudyList.add(new Location(50, "教11栋(振动与噪声教育部工程中心)", J11));
        nStudyList.add(new Location(51, "教12栋(体育学院)", J12));
        nStudyList.add(new Location(52, "教13栋(原教11栋 机电学院)", J13));
        nStudyList.add(new Location(53, "教14栋(原教12栋 逸夫楼)", J14));
        nStudyList.add(new Location(54, "教15栋(原教13栋 外国语/艺术学院)", J15));
        nStudyList.add(new Location(55, "教16栋(大学生事务中心)", J16));
        nStudyList.add(new Location(56, "教17栋(研究生大楼)", J17));
        nStudyList.add(new Location(57, "教18栋(实训车间)", J18));
        nStudyList.add(new Location(58, "教19栋(创新大楼)", J19));
        nStudyList.add(new Location(59, "南区图书馆", NTS));

        bStudyList.add(new Location(60, "教31栋(原教15栋 北区综合楼)", J31));
        bStudyList.add(new Location(61, "教32栋(原教16栋 辅导员办公室)", J32));
        bStudyList.add(new Location(62, "教33栋(原教17栋 后勤办公楼)", J33));
        bStudyList.add(new Location(63, "教34栋(原教18栋 人文学院)", J34));
        bStudyList.add(new Location(64, "教35栋(原教19栋 科技楼)", J35));
        bStudyList.add(new Location(65, "教36栋(原教20栋 轨道交通学院)", J36));
        bStudyList.add(new Location(66, "教37栋(原教21栋 国际学院)", J37));
        bStudyList.add(new Location(67, "教38栋(原教22栋 继续教育学院)", J38));

        nPlayList.add(new Location(68, "南区运动馆", NYG));
        nPlayList.add(new Location(69, "南区风雨球馆", NFY));
        nPlayList.add(new Location(70, "南区游泳池", NYC));
        nPlayList.add(new Location(71, "南区旱冰场", NHB));
        nPlayList.add(new Location(72, "南区羽毛球场", NYM));
        nPlayList.add(new Location(73, "南区排球场", NPQ));
        nPlayList.add(new Location(74, "南区篮球场", NLQ));
        nPlayList.add(new Location(75, "南区网球场", NWQ));
        nPlayList.add(new Location(76, "南区田径场", NTJ));

        bPlayList.add(new Location(77, "北区运动馆", BYG));
        bPlayList.add(new Location(78, "北区网球场", BWQ));
        bPlayList.add(new Location(79, "北区篮球场", BLQ));
        bPlayList.add(new Location(80, "北区田径场", BTJ));
        bPlayList.add(new Location(81, "北区乒乓球馆", BPP));
        bPlayList.add(new Location(82, "北区羽毛球馆", BYM));
        bPlayList.add(new Location(83, "北区台球馆", BTQ));


        nLiefList.add(new Location(84, "南区一食堂", ST1));
        nLiefList.add(new Location(85, "南区二食堂", ST2));
        nLiefList.add(new Location(86, "南区三食堂", ST3));
        nLiefList.add(new Location(87, "南区四食堂", ST4));
        nLiefList.add(new Location(88, "校医院", XYY));
        nLiefList.add(new Location(89, "京东快递", JD));
        nLiefList.add(new Location(90, "南区菜鸟驿站", NCN));
        nLiefList.add(new Location(91, "南区浴室", NYS));
        nLiefList.add(new Location(92, "南区超市", NCS));
        nLiefList.add(new Location(93, "建设银行", JH));

        bLiefList.add(new Location(94, "北区一/二/三食堂", ST5));
        bLiefList.add(new Location(95, "北区四食堂", ST6));
        bLiefList.add(new Location(96, "北区快递领取", BKD));
        bLiefList.add(new Location(97, "北区浴室", BYS));
        bLiefList.add(new Location(98, "北区超市", BCS));
        bLiefList.add(new Location(99, "北区招待所", ZDS));

        bOtherList.add(new Location(100, "北区礼堂", BLT));

        nOtherList.add(new Location(101, "南区礼堂", NLT));
        nOtherList.add(new Location(102, "专家楼", ZJL));
        nOtherList.add(new Location(103, "孔目湖", KMH));
    }

    public static List<Location> getnStayList() {
        return nStayList;
    }

    public static List<Location> getbStayList() {
        return bStayList;
    }

    public static List<Location> getnStudyList() {
        return nStudyList;
    }

    public static List<Location> getbStudyList() {
        return bStudyList;
    }

    public static List<Location> getnLiefList() {
        return nLiefList;
    }

    public static List<Location> getbLiefList() {
        return bLiefList;
    }

    public static List<Location> getnPlayList() {
        return nPlayList;
    }

    public static List<Location> getbPlayList() {
        return bPlayList;
    }

    public static List<Location> getnOtherList() {
        return nOtherList;
    }

    public static List<Location> getbOtherList() {
        return bOtherList;
    }
}
