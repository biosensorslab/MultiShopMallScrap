package com.prism.Product_Finder;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;



/**
 * Created by hadoop on 2017. 6. 2..
 */
class Amazone_LinkHandler implements Runnable{

    public <T> List<List<T>> split(List<T> resList, int count) {
        if (resList == null || count <1)
            return null;
        List<List<T>> ret = new ArrayList<List<T>>();
        int size = resList.size();
        if (size <= count) {
            // 데이터 부족 count 지정 크기
            ret.add(resList);
        } else {
            int pre = size / count;
            int last = size % count;
            // 앞 pre 개 집합, 모든 크기 다 count 가지 요소
            for (int i = 0; i <pre; i++) {
                List<T> itemList = new ArrayList<T>();
                for (int j = 0; j <count; j++) {
                    itemList.add(resList.get(i * count + j));
                }
                ret.add(itemList);
            }
            // last 진행이 처리
            if (last > 0) {
                List<T> itemList = new ArrayList<T>();
                for (int i = 0; i <last; i++) {
                    itemList.add(resList.get(pre * count + i));
                }
                ret.add(itemList);
            }
        }
        return ret;
    }
    public void run()
    {
        Amazone amazone = new Amazone("Main Scan");
        HashMap<String, String> main_category = amazone.Main_Cateory();
        try
        {
            List<String> ALL_Address_code = new ArrayList<String>();
            List<List<String>> ret = null;
            int address_idx = 0;
            HashMap<Integer, String> HashMap_store_map = new HashMap<Integer, String>();
            HashMap<String, String> HashMap_Main_Sub1 = null;
            Thread[] thread_web_get  =  null;
            String full_depth = "";

            for( String address_map : main_category.keySet()) {
                System.out.println("Main:" + address_map + ":" + main_category.get(address_map));
                HashMap<String, String> Main_Sub1 = amazone.Main_Sub1_Cateory(main_category.get(address_map));
                for (String sub_address_map : Main_Sub1.keySet()){
                    full_depth = sub_address_map + "\u9999" + Main_Sub1.get(sub_address_map);
                    if (ALL_Address_code.contains(full_depth)== false) {
                        ALL_Address_code.add(full_depth);
                        System.out.println("-Sub1:" + sub_address_map + ":" + Main_Sub1.get(sub_address_map));
                    }
                }
            }
            amazone = null;
            thread_web_get  = new Thread[ALL_Address_code.size()];
            ret = split(ALL_Address_code, 500);
            for (int j = 0; j < ret.size(); j++)
            {
                System.out.println("Tree Scann Init:" + j + "/"  + ret.size());
                Amazone_Reader obj_web_get = new Amazone_Reader(ret.get(j));
                thread_web_get[j]  = new Thread(obj_web_get);
            }
            for (int j = 0; j < ret.size(); j++)
            {
                System.out.println("Tree Scann Start:" + j + "/"  + ret.size());
                thread_web_get[j].start();
            }
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }
    }
}
