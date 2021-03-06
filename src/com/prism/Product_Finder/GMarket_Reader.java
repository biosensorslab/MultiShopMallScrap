package com.prism.Product_Finder;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by hadoop on 2017. 6. 13..
 */
public class GMarket_Reader implements Runnable{
        private List<String> Main_Sub1 = null;
        //    private static String store_map;
        public GMarket_Reader(List<String> Main_Sub1)
        {
            this.Main_Sub1 = Main_Sub1;
//        this.store_map = store_map;
        }
        public void run()
        {
            try
            {
                while(true) {
                    Random random = new Random();
                    GMarket g_market = new GMarket("Tree Scan");
                    for (String title_url : Main_Sub1) {
                        String category_title = title_url.split("\u9999")[0];
                        String sub_address_map = title_url.split("\u9999")[1];

                        int sleep_time = random.nextInt(10) + 1;
                        System.out.println("Sleep:" + sleep_time * 500 + "Title:" + category_title + " Address :" + sub_address_map);
                        Thread.sleep(sleep_time * 500);

                        HashMap<String, String> Main_Sub2 = g_market.Main_Sub2_Cateory(sub_address_map);
                        for (String sub2_address_map : Main_Sub2.keySet()) {
                            System.out.println("-Sub2:" + sub2_address_map + ":" + Main_Sub2.get(sub2_address_map));
                            //SSL 오규 구간.
                            g_market.Dept_3_FullStore(category_title, sub2_address_map, Main_Sub2.get(sub2_address_map));
                        }
                    }
                    //ADDRESS WRITE
                    BufferedWriter fw = null;
                    String address_path = "/tmp/shop_name_address.txt";
                    fw = new BufferedWriter(new FileWriter(address_path, true));
                    for(String shop_name: Shop_AddressTable.Map_ShopAddress.keySet())
                    {
                        String address_map = shop_name + "$" + Shop_AddressTable.Map_ShopAddress.get(shop_name);
                        fw.write(address_map);
                        fw.flush();
                    }
                    fw.close();

                    String category_path = "/tmp/category_list.txt";
                    fw = new BufferedWriter(new FileWriter(category_path, true));
                    String category_shop_list = "";
                    for(String category: Shop_CategoryTable.Map_ShopCategory.keySet())
                    {
                        category_shop_list = category + ":";

                        for(String corp_name : Shop_CategoryTable.Map_ShopCategory.get(category))
                        {
                            category_shop_list += corp_name + "$";
                        }
                        category_shop_list = category_shop_list.substring(0, category_shop_list.length()-1);
                        fw.write(category_shop_list);
                        fw.flush();
                    }
                    fw.close();

                }
            }catch(Exception e)
            {
                System.out.println("Tree Scan Error:" + e.toString());
            }
        }
}

