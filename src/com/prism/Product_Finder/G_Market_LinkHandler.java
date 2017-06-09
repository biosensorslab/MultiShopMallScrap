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
class G_Market_LinkHandler implements Runnable{

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
        G_Market g_market = new G_Market("Main Scan");
        HashMap<String, String> main_category = g_market.Main_Cateory();
        try
        {
            List<String> ALL_Address_code = new ArrayList<String>();
            List<List<String>> ret = null;
            int address_idx = 0;
            HashMap<Integer, String> HashMap_store_map = new HashMap<Integer, String>();
            HashMap<String, String> HashMap_Main_Sub1 = null;
            Thread[] thread_web_get  =  null;
            String full_depth = "";
//            for( String category_name : main_category.keySet()) {
//                System.out.println("Main Catetorys:" + category_name);
//            }
            for( String address_map : main_category.keySet()) {
                System.out.println("Main:" + address_map + ":" + main_category.get(address_map));
                HashMap<String, String> Main_Sub1 = g_market.Main_Sub1_Cateory(main_category.get(address_map));
                for (String sub_address_map : Main_Sub1.keySet()){
                    full_depth = sub_address_map + "\u9999" + Main_Sub1.get(sub_address_map);
                    if (ALL_Address_code.contains(full_depth)== false) {
                        ALL_Address_code.add(full_depth);
                        System.out.println("-Sub1:" + sub_address_map + ":" + Main_Sub1.get(sub_address_map));
                    }
//                    HashMap<String, String> Main_Sub2 = g_market.Main_Sub2_Cateory(Main_Sub1.get(sub_address_map));
//                    for (String sub2_address_map : Main_Sub2.keySet()) {
//                        System.out.println("-Sub2:" + sub2_address_map + ":" + Main_Sub2.get(sub2_address_map));
//						g_market.Dept_3_FullStore( sub2_address_map, Main_Sub2.get(sub2_address_map));
//						for (String key : Category_Link.Shop_address.keySet()) {
//							System.out.println(String.format("Address : %s, Name : %s", key, Category_Link.Shop_address.get(key)));
//
//							Category_Link.Shop_address.clear();
//						}
//                    }
                }
            }
            g_market = null;
            thread_web_get  = new Thread[ALL_Address_code.size()];
            ret = split(ALL_Address_code, 500);
            for (int j = 0; j < ret.size(); j++)
            {
                System.out.println("Tree Scann Init:" + j + "/"  + ret.size());
                Market_Reader obj_web_get = new Market_Reader(ret.get(j));
                thread_web_get[j]  = new Thread(obj_web_get);
//                thread_web_get[j].setDaemon(true);
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
class Market_Reader implements Runnable{
    private List<String> Main_Sub1 = null;
    //    private static String store_map;
    public Market_Reader(List<String> Main_Sub1)
    {
        this.Main_Sub1 = Main_Sub1;
//        this.store_map = store_map;
    }
    public void run()
    {
        try
        {
            Random random = new Random();
            G_Market g_market = new G_Market("Tree Scan");
            for(String title_url: Main_Sub1) {
                String title = title_url.split("\u9999")[0];
                String sub_address_map = title_url.split("\u9999")[1];

                int sleep_time = random.nextInt(10)+1;
                System.out.println("Sleep:" + sleep_time * 500 + "Title:"+ title + " Address :" + sub_address_map);
                Thread.sleep(sleep_time * 500);

                HashMap<String, String> Main_Sub2 = g_market.Main_Sub2_Cateory(sub_address_map);
                for (String sub2_address_map : Main_Sub2.keySet()) {
                    System.out.println("-Sub2:" + sub2_address_map + ":" + Main_Sub2.get(sub2_address_map));
                    g_market.Dept_3_FullStore(sub2_address_map, Main_Sub2.get(sub2_address_map));
//                    for (String key : Category_Link.Shop_address.keySet()) {
//                        System.out.println(String.format("Address : %s, Name : %s", key, Category_Link.Shop_address.get(key)));
//                        Category_Link.Shop_address.clear();
//                    }
                }
            }
        }catch(Exception e)
        {
            System.out.println("Tree Scan Error:" + e.toString());
        }
    }
}