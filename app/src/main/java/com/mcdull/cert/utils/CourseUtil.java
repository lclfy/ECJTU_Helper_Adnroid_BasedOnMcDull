package com.mcdull.cert.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 课表数据操作
 * Created by mcdull on 15/8/25.
 */
public class CourseUtil {
    private static String pattern = "[\\u4e00-\\u9fa5a-zA-Z0-9-._,（）#()\\[\\]]* |[\\u4e00-\\u9fa5a-zA-Z0-9-.#_,（）()\\[\\]]*";

    /**
     * 保存课表
     *
     * @param json    传入课表JSON
     * @param context context
     */
    public static void saveCourse(String json, Context context) {
        if ( TextUtils.isEmpty(json))
            return;
        Map<Integer, Map<Integer, List<String>>> courseMap = jsonParseCourse(json);
        if (courseMap == null || courseMap.size() == 0)
            return;
        SharedPreferences SP = context.getSharedPreferences("course", Context.MODE_PRIVATE);
        Editor edit = SP.edit();
        for (int i = 0; i < 7; i++)
            for (int j = 0; j < 5; j++)
                for (int k = 0; k < 5; k++)
                    edit.remove(i + "-" + j + "-" + k);
        edit.apply();
        for (int i = 0; i < 7; i++)
            if (courseMap.get(i) != null)
                for (int j = 0; j < 5; j++)
                    if (courseMap.get(i).get(j) != null)
                        for (int k = 0; k < 5; k++)
                            edit.putString(i + "-" + j + "-" + k, courseMap.get(i).get(j).get(k));
        edit.apply();
    }

    /**
     * 保存课表
     *
     * @param json    传入课表JSON
     * @param context context
     */
    public static void saveNewCourse(String json, Context context) {
        if ( TextUtils.isEmpty(json))
            return;
        Map<Integer, Map<Integer, List<String>>> courseMap = jsonParseNewCourse(json);
        if (courseMap == null || courseMap.size() == 0)
            return;
        SharedPreferences SP = context.getSharedPreferences("course", Context.MODE_PRIVATE);
        Editor edit = SP.edit();
        for (int i = 0; i < 7; i++)
            for (int j = 0; j < 5; j++)
                for (int k = 0; k < 5; k++)
                    edit.remove(i + "-" + j + "-" + k);
        edit.apply();
        for (int i = 0; i < 7; i++)
            if (courseMap.get(i) != null)
                for (int j = 0; j < 5; j++)
                    if (courseMap.get(i).get(j) != null)
                        for (int k = 0; k < 5; k++)
                            edit.putString(i + "-" + j + "-" + k, courseMap.get(i).get(j).get(k));
        edit.apply();
    }

    /**
     * 修改指定课表
     *
     * @param i       星期数 eg：星期一 i=0
     * @param j       第几节 eg：3-4节 j=1
     * @param list    课程信息集合
     * @param context context
     */
    public static void modifyCourse(int i, int j, List<String> list, Context context) {
        SharedPreferences SP = context.getSharedPreferences("course", Context.MODE_PRIVATE);
        Editor edit = SP.edit();
        for (int k = 0; k < 4; k++)
            edit.remove(i + "-" + j + "-" + k);
        edit.apply();
        for (int k = 0; k < 4; k++)
            edit.putString(i + "-" + j + "-" + k, list.get(k));
        edit.apply();
    }

    /**
     * 获取所有课表
     *
     * @param context context
     * @return 返回的是所有课表的集合
     */
    public static List<List<String>> getCourse(Context context) {
        List<List<String>> list = new LinkedList<>();
        SharedPreferences SP = context.getSharedPreferences("course", Context.MODE_PRIVATE);
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 5; j++) {
                List<String> l = new LinkedList<>();
                for (int k = 0; k < 5; k++) {
                    String s = SP.getString(i + "-" + j + "-" + k, null);
                    l.add(s);
                }
                list.add(l);
            }
        }
        return list;
    }

    /**
     * 获取指定星期的课表
     *
     * @param i       星期数 eg：星期一的话i=0
     * @param context context
     * @return 返回该星期的课表
     */
    public static List<List<String>> getCourse(int i, Context context) {
        List<List<String>> list = new LinkedList<>();
        SharedPreferences SP = context.getSharedPreferences("course", Context.MODE_PRIVATE);
        for (int j = 0; j < 5; j++) {
            List<String> l = new LinkedList<>();
            for (int k = 0; k < 5; k++) {
                String s = SP.getString(i + "-" + j + "-" + k, null);
                l.add(s);
            }
            list.add(l);
        }
        return list;
    }

    /**
     * 获取指定星期的指定课程的课表
     *
     * @param i       星期数 eg：星期一 i=0
     * @param j       第几节 eg：3-4节 j=1
     * @param context context
     * @return 返回该课的信息
     */
    public static List<String> getCourse(int i, int j, Context context) {
        List<String> l = new LinkedList<>();
        SharedPreferences SP = context.getSharedPreferences("course", Context.MODE_PRIVATE);
        for (int k = 0; k < 5; k++) {
            String s = SP.getString(i + "-" + j + "-" + k, null);
            l.add(s);
        }
        return l;
    }

    /**
     * 删除所有课程信息
     *
     * @param context context
     */
    public static void removeAll(Context context) {
        SharedPreferences SP = context.getSharedPreferences("course", Context.MODE_PRIVATE);
        Editor edit = SP.edit();
        for (int i = 0; i < 7; i++)
            for (int j = 0; j < 5; j++)
                for (int k = 0; k < 5; k++)
                    edit.remove(i + "-" + j + "-" + k);
        edit.apply();
    }

    /**
     * 删除指定课程信息
     *
     * @param context context
     * @param i       星期数 eg：星期一 i=0
     * @param j       第几节 eg：3-4节 j=1
     */
    public static void removeCourse(Context context, int i, int j) {
        SharedPreferences SP = context.getSharedPreferences("course", Context.MODE_PRIVATE);
        Editor edit = SP.edit();
        for (int k = 0; k < 5; k++)
            edit.remove(i + "-" + j + "-" + k);
        edit.apply();
    }


    private static Map<Integer, Map<Integer, List<String>>> jsonParseNewCourse(String json) {
        if (json == null) {
            return new ArrayMap<>();
        } else {
            try {
                JSONArray jsonArray = new JSONArray(json);
                Map<Integer, Map<Integer, List<String>>> courseMap = new ArrayMap<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONArray array = jsonArray.getJSONArray(i);
                    Map<Integer, List<String>> map = new ArrayMap<>();
                    for (int j = 0; j < array.length(); j++) {
                        JSONObject obj = array.getJSONObject(j);
                        List<String> list = new LinkedList<>();
                        list.add(obj.getString("name"));
                        list.add(obj.getString("teacher"));
                        list.add(obj.getString("className"));
                        list.add(obj.getString("week"));
                        list.add(obj.getString("time"));
                        map.put(j, list);
                    }
                    courseMap.put(i,map);
                }
                return courseMap;
            } catch (JSONException e) {
                return new ArrayMap<>();
            }
        }
    }

    /**
     * 课表json解析
     *
     * @param classString 传入课表JSON
     * @return 返回课表集合
     */
    private static Map<Integer, Map<Integer, List<String>>> jsonParseCourse(String classString) {
        if (null == classString) {
            return new ArrayMap<>();
        }
        try {
            JSONObject jsonObject = new JSONObject(classString);
            String mon = jsonObject.getString("Mon");
            String tues = jsonObject.getString("Tues");
            String wed = jsonObject.getString("Wed");
            String thur = jsonObject.getString("Thur");
            String fri = jsonObject.getString("Fri");
            String sat = jsonObject.getString("Sat");
            String sun = jsonObject.getString("Sun");

            Map<Integer, Map<Integer, List<String>>> courseMap = new ArrayMap<>();
            Map<Integer, List<String>> monCourseMap = formatCourse(mon);
            courseMap.put(0, monCourseMap);
            Map<Integer, List<String>> tuesCourseMap = formatCourse(tues);
            courseMap.put(1, tuesCourseMap);
            Map<Integer, List<String>> wedCourseMap = formatCourse(wed);
            courseMap.put(2, wedCourseMap);
            Map<Integer, List<String>> thurCourseMap = formatCourse(thur);
            courseMap.put(3, thurCourseMap);
            Map<Integer, List<String>> friCourseMap = formatCourse(fri);
            courseMap.put(4, friCourseMap);
            Map<Integer, List<String>> satCourseMap = formatCourse(sat);
            courseMap.put(5, satCourseMap);
            Map<Integer, List<String>> sunCourseMap = formatCourse(sun);
            courseMap.put(6, sunCourseMap);
            return courseMap;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 格式化课表
     *
     * @param string 传入单一天得课表数据
     * @return 返回Map集合 键是课程的位置 值是课程的信息
     */
    private static Map<Integer, List<String>> formatCourse(String string) {
        String[] string2 = string.split("\\|");
        List<List<String>> list = new ArrayList<>(5);
        for (String s : string2) {
            List<String> l = new LinkedList<>();
            Pattern mpattern = Pattern.compile(pattern);
            Matcher mmatcher = mpattern.matcher(s);
            while (mmatcher.find())
                if (mmatcher.group().length() != 1 && mmatcher.group().length() != 0) {
                    l.add(mmatcher.group());
                }
            if (l.size() != 0) {
                if (l.size() == 3) {
                    String s1 = l.get(0);
                    String s2 = "";
                    String s3 = "";
                    String s4 = l.get(1);
                    String s5 = l.get(2);
                    l = new LinkedList<>();
                    l.add(s1);
                    l.add(s2);
                    l.add(s3);
                    l.add(s4);
                    l.add(s5);
                }
                if (l.size() == 4) {
                    String s1 = l.get(0);
                    String s2 = l.get(1);
                    String s3 = "";
                    String s4 = l.get(2);
                    String s5 = l.get(3);
                    l = new LinkedList<>();
                    l.add(s1);
                    l.add(s2);
                    l.add(s3);
                    l.add(s4);
                    l.add(s5);
                }
                if (l.size() == 6) {
                    String s1 = l.get(0) + l.get(1);
                    String s2 = l.get(2);
                    String s3 = l.get(3);
                    String s4 = l.get(4);
                    String s5 = l.get(5);
                    l = new LinkedList<>();
                    l.add(s1);
                    l.add(s2);
                    l.add(s3);
                    l.add(s4);
                    l.add(s5);
                }
                if (l.size() == 10) {
                    String s1 = l.get(0) + "|" + l.get(5);
                    String s2 = l.get(1) + "|" + l.get(6);
                    String s3 = l.get(2) + "|" + l.get(7);
                    String s4 = l.get(3) + "|" + l.get(8);
                    String s5 = l.get(4) + "|" + l.get(9);
                    l = new LinkedList<>();
                    l.add(s1);
                    l.add(s2);
                    l.add(s3);
                    l.add(s4);
                    l.add(s5);
                }
                list.add(l);
            }
        }
        Map<Integer, List<String>> map = new ArrayMap<>(5);
        for (int i = 0; i < 5; i++) {
            map.put(i, null);
        }
        for (List<String> list2 : list) {
            String a = list2.get(list2.size() - 1);
            char c = a.charAt(a.length() - 1);
            switch (c) {
                case '1':
                case '2':
                    map.put(0, list2);
                    break;
                case '3':
                case '4':
                    map.put(1, list2);
                    break;
                case '5':
                case '6':
                    map.put(2, list2);
                    break;
                case '7':
                case '8':
                    map.put(3, list2);
                    break;
                case '9':
                case '0':
                    map.put(4, list2);
                    break;
            }
            c = a.charAt(0);
            switch (c) {
                case '1':
                case '2':
                    map.put(0, list2);
                    break;
                case '3':
                case '4':
                    map.put(1, list2);
                    break;
                case '5':
                case '6':
                    map.put(2, list2);
                    break;
                case '7':
                case '8':
                    map.put(3, list2);
                    break;
                case '9':
                case '0':
                    map.put(4, list2);
                    break;
            }
        }
        return map;
    }
}