package com.prism.Product_Finder;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by hadoop on 2017. 6. 13..
 */
class Amazone_Market_Reader implements Runnable{
    private List<String> Main_Sub1 = null;
    //    private static String store_map;
    public Amazone_Market_Reader(List<String> Main_Sub1)
    {
        this.Main_Sub1 = Main_Sub1;
//        this.store_map = store_map;
    }
    public void run()
    {
        try
        {
            Random random = new Random();
            Amazone g_market = new Amazone("Tree Scan");
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