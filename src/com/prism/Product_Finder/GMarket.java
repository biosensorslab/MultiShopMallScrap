package com.prism.Product_Finder;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.validator.routines.UrlValidator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by hadoop on 2017. 6. 2..
 */
class GMarket{

    HashMap<String, Integer> Item_Size_Spec = new HashMap<String, Integer>();
    HashMap<String, Integer> Item_Height_Spec = new HashMap<String, Integer>();
    HashMap<String, Integer> Item_Spec = new HashMap<String, Integer>();

//    HashMap<String, G_Market_ProectInformation> Category5 = new HashMap<String, ProectInformation>();
    public GMarket(String argu)
    {
        System.out.println("G_Market_Object Created with " + argu);
    }

//    public void Dept_3_PowerClick() {
//        try {
//            String address = "http://category.gmarket.co.kr//listview/List.aspx?gdsc_cd=300019139&amp;ecp_gdlc=100000049&amp;ecp_gdmc=200000376%27";
//            Document doc = Jsoup.connect(address).get();
//            //
//            Elements elements = doc.select("div.store_info span.seller");
//            for (Element element : elements) {
//                String text = element.text();
//                System.out.println(text);
//            }
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }

    //Gmarkt의 메인 카테고리 목록과 주소를 얻어온다.
    public HashMap<String, String> Main_Cateory() {
        HashMap<String, String> category_address = new HashMap<String, String>();
        try {
            String address = "http://www.gmarket.co.kr/";
            Document doc = Jsoup.connect(address).get();
//            System.out.println(doc.toString());
            Elements elements1 = doc.select("div.smenu div.cate_wrap");
            int img_idx = 0;
            int category_idx = 0;
            for (Element element : elements1) {
                Elements element_LINK = element.select("a");
                for (Element link_address : element_LINK) {
                    String title = link_address.text();
                    if(title.equals("신발")) {

                        System.out.println("category:" + title);
                        String http_address = link_address.attr("href");
                        if (http_address.contains("http://") == true) {
                            category_address.put(category_idx + ":" + title, http_address);
                            category_idx++;
                        }
                    }
                }

            }
            doc = null;
            elements1 = null;

            return category_address;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return category_address;
    }

    public HashMap<String, String> Main_Sub1_Cateory(String address) {
        HashMap<String, String> Main_Sub1_address = new HashMap<String, String>();
        try {
//            System.out.println("Start Main_Sub1 Category");
            Document doc = Jsoup.connect(address).get();
            Elements elements1 = doc.select("div.layer-box");
            int img_idx = 0;
            for (Element element : elements1) {
                Elements element_LINK = element.select("a");
                for (Element link_address : element_LINK) {
                    String title = link_address.text();

                    String raw_address = link_address.attr("href");
                    String[] js_include_address = raw_address.split(",");
                    for (int jsi = 0; jsi < js_include_address.length; jsi++) {
                        if (js_include_address[jsi].contains("http") == true) {
                            if (title.length() > 0) {
                                String http_address = js_include_address[jsi].replace("'", "");
                                Main_Sub1_address.put(title, http_address);
                            }
                        }
                    }
                }
            }
            return Main_Sub1_address;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println(e.toString());
        }
        return Main_Sub1_address;
    }

    public HashMap<String, String> Main_Sub2_Cateory(String address) {
        HashMap<String, String> Main_Sub2_address = new HashMap<String, String>();
        try {
//            System.out.println("Start Main_Sub2 Category");
            Document doc = Jsoup.connect(address).get();
            int item_count = 0;
            Elements elements1 = doc.select("div.item_info");
            int img_idx = 0;
            for (Element element : elements1) {
                Elements element_LINK = element.select("a");
                for (Element link_address : element_LINK) {
                    String title = link_address.text();

                    String raw_address = link_address.attr("href");
                    String[] js_include_address = raw_address.split(",");
                    for (int jsi = 0; jsi < js_include_address.length; jsi++) {
                        if (js_include_address[jsi].contains("http") == true) {
                            if (title.length() > 0) {
                                item_count++;
                                String http_address = js_include_address[jsi].replace("'", "");
//								System.out.println(item_count + " Title : " + title);
//								System.out.println("Address : " + http_address);
                                Main_Sub2_address.put(title, http_address);
                            }
                        }
                    }
                }
                element_LINK = null;
            }
            elements1 = null;
            doc = null;
            return Main_Sub2_address;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println(e.toString());
        }
        return Main_Sub2_address;
    }

    public void Dept_3_FullStore() {
        try {
            String address = "http://category.gmarket.co.kr//listview/List.aspx?gdsc_cd=300019139&amp;ecp_gdlc=100000049&amp;ecp_gdmc=200000376%27";
            Document doc = Jsoup.connect(address).get();
            //<ul class="item_list type_thumb" id="searchListItems">
            Elements elements1 = doc.select("ul#searchListItems");
            int img_idx = 0;
            for (Element element : elements1) {
                Elements plusitems = element.select("div.item_info");
                for (Element plusitem : plusitems) {
                    System.out.println(plusitem.text());
                    String plusitemHtml = plusitem.html();

                    Document PlusItemdoc = Jsoup.parse(plusitemHtml);
                    Element link = PlusItemdoc.select("a").first();
                    String linkHref = link.attr("href"); // "http://example.com/"
                    System.out.println(linkHref);
                    plusStore(linkHref);
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void Dept_3_FullStore(String category, String shop_title, String address) {
        WebClient webClient = null;
        HtmlPage page_esm = null;
        String final_page = "";
//        String root = System.getProperty("user.dir")  +"/scrap_data";
        String root = "/tmp"  +"/scrap_data";
        boolean bSuccess = false;
        while(bSuccess == false) {
            try {
                webClient = new WebClient(BrowserVersion.CHROME);
                webClient.setAjaxController(new NicelyResynchronizingAjaxController());
                webClient.getOptions().setCssEnabled(true);
                webClient.getOptions().setDoNotTrackEnabled(true);
                webClient.getOptions().setMaxInMemory(10000);
                webClient.getOptions().setJavaScriptEnabled(true);
                webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
                webClient.getOptions().setThrowExceptionOnScriptError(false);
                webClient.getOptions().setPrintContentOnFailingStatusCode(false);
                webClient.getOptions().setTimeout(10000);
                webClient.getCurrentWindow().setInnerHeight(Integer.MAX_VALUE);
                page_esm = webClient.getPage(address);
                final_page = page_esm.asXml();
                webClient.close();
                webClient = null;
                bSuccess = true;
            } catch (FailingHttpStatusCodeException e) {
                e.printStackTrace();
                try {
                    Thread.sleep(3000);
                    System.out.println("Retry");
                }
                catch (Exception se)
                {

                }
                bSuccess = false;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                try {
                    Thread.sleep(3000);
                    System.out.println("Retry");
                }
                catch (Exception se)
                {

                }
                bSuccess = false;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Throwable e) {
                try {
                    Thread.sleep(3000);
                    System.out.println("Retry");
                }
                catch (Exception se)
                {

                }
                e.printStackTrace();
                bSuccess = false;

            }
        }

        Document doc_esm = Jsoup.parse(final_page);

        page_esm = null;
        Elements seller_infos = doc_esm.select("dl.exchange_data");

        String corp_name = "", ceo = "";
        int element_id_idx = 0;
        int dd_idx = 0;

        boolean detected = false;
        for (Element s_info : seller_infos) {
            Elements etype1 = s_info.select("dt");
            for(Element element : etype1)
            {
                String field = element.text();
                if(field.contains("판매자명")) {
                    System.out.println(field);
                    dd_idx = element_id_idx;
                    detected = true;
                    break;
                }
                element_id_idx++;
            }
            element_id_idx = 0;
            if(detected == true) {

                Elements etype2 = s_info.select("dd");
                for (Element element2 : etype2) {
                    String value = element2.text();
//                    System.out.println(value);
                    if(dd_idx == element_id_idx) {
                        value = element2.text();
                        System.out.println("Deteced:" + value);
                        corp_name = value;
                        break;
                    }
                    element_id_idx++;
                }
            }
        }


        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf =
                new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String currentTime = sdf.format(dt);

        //상점과 주소 저장
        Shop_AddressTable.Map_ShopAddress.put(corp_name, address);

        //카테고리에 상점 저장.
        if(!Shop_CategoryTable.Map_ShopCategory.containsKey(category))
        {
            List<String> listCorp = new ArrayList<String>();
            listCorp.add(corp_name);
            Shop_CategoryTable.Map_ShopCategory.put(category, listCorp);

        }
        else
        {
            List<String> listCorp = Shop_CategoryTable.Map_ShopCategory.get(category);
            if(!listCorp.contains(corp_name))
                listCorp.add(corp_name);

        }
        corp_name  = corp_name.replace("&", "and");
        category  = category.replace("&", "and");

        String match = "[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]";
        corp_name =corp_name.replaceAll(match, "");
        category =category.replaceAll(match, "");

        int shop_id = -1;
        DBConfig dbwork = new DBConfig();
        if(corp_name.length() > 1) {
            int id = dbwork.GET_SHOP_ID(category, corp_name, address);
            if(id == -1)
            dbwork.INSERT_SHOP_TABLE(category, corp_name, address, final_page, 1, currentTime);
        }else{
            int id = dbwork.GET_SHOP_ID(category, corp_name, address);
            if(id == -1)
                dbwork.INSERT_SHOP_TABLE(category, corp_name,address,final_page, 2, currentTime );
        }

        shop_id = dbwork.GET_SHOP_ID(category,corp_name, address);

        Elements tags_review = doc_esm.select("span[data-goodscode]");
        String goodscode ="";
        for(Element tag_review: tags_review)
        {
//            System.out.println(tag_review.html());
//            System.out.println(tag_review.text());
//            System.out.println(tag_review.toString());
            String good_html = tag_review.toString();
            good_html = good_html.replace(">", "");
            good_html = good_html.replace("\"", "");
            good_html = good_html.replace("<span", "");
            good_html = good_html.replace("</span>", "");
            String[] tags = good_html.split(" ");
            String[] code = tags[3].split("=");
            if(code[0].equals("data-goodscode"))
            {
                goodscode = code[1];
//                System.out.println(goodscode);
                break;
            }
        }
        File file = null;

        //***Get item review
        category = category.replace("/" , "and");

        Item_Review(shop_id, root + "/" + category +"/review/" + corp_name, goodscode);

        try
        {
            file = new File(root + "/" + category +  "/html/");
            if(!file.exists())
            {
                file.mkdirs();
            }
            FileWriter fw = new FileWriter(root + "/" + category + "/html/" + corp_name+"_html.txt");
            fw.write(address+"\n");
            fw.write(final_page);
            fw.flush();
            fw.close();
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
        }

        GMarket_Category_Link.Shop_address.put(address, corp_name);
        //Get_Main_Image
        String string_image_folder = root + "/" + category + "/data/" + shop_id +"_" + corp_name + "_image";
        String string_resize_image_folder = root + "/" + category + "/data_227x227/" + shop_id + "_" + corp_name + "_image";
        File data_validation = null;
        data_validation = new File(string_image_folder);
        if (!data_validation.exists()) {
            data_validation.mkdirs();
        }
        data_validation = new File(string_resize_image_folder);
        if (!data_validation.exists()) {
            data_validation.mkdirs();
        }
        Elements image_tags = doc_esm.select("ul.viewer");
        int idx = 0;
        for (Element image_tag : image_tags){
            for(Element li_image: image_tag.select("li.on"))
            {
                Element image = li_image.select("img").first();
                String img_src_url = image.absUrl("src");
                System.out.println(img_src_url);

                if(img_src_url.length()> 0){
//                    shop_title = shop_title.replaceAll("\\p{Z}", "");
//                    shop_title = shop_title.trim();
                    getImages(shop_id , string_image_folder, string_resize_image_folder, img_src_url, idx);
                    idx++;
                }
            }
        }
        //get iFrame content

        System.out.println("address:" + address);
        Elements es = doc_esm.select("iframe[id=detail1]");
        String[] iframesrc;
        int iframeCount = es.size();
        iframesrc = new String [iframeCount];
        Document[] iframeDoc = null;
        if(iframeCount > 0) {
            //extract iFrame sources:
            try{
                int i = 0;
                for (Element e : es) {
                    iframesrc[i] = e.getElementsByTag("iframe").attr("src");
                    i++;
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            }
            try{
                iframeDoc = new Document[iframeCount];
                int j = 0;
                for (String s : iframesrc) {
                    System.out.println(s);
                    if (s.contains("about:blank"))
                        continue;
                    System.out.println("IFrame address:" + s);
                    iframeDoc[j] = Jsoup.connect(iframesrc[j]).get();
                    Detail_Image(root, category, shop_id, corp_name, iframeDoc[j]);
                    j++;
                }

            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
    }
    private void Detail_Image(String root, String category, int shop_id, String corp_name, Document document)
    {
        category = category.replace("&", "and");
        corp_name = corp_name.replace("&", "and");

        String string_detail_image_folder = root + "/" + category + "/data_detail/" +  shop_id +"_"  + corp_name + "_image";
        String string_detail_160ximage_folder = root + "/" + category + "/data_detail_227x227/" + shop_id +"_" +  corp_name + "_image";


        File data_validation = null;
        data_validation = new File(string_detail_image_folder);
        if (!data_validation.exists()) {
            data_validation.mkdirs();
        }
        data_validation = new File(string_detail_160ximage_folder);
        if (!data_validation.exists()) {
            data_validation.mkdirs();
        }

        Elements elements = document.select("div.vip-detailarea_seller");
        for(Element element: elements)
        {
            String strElement = element.toString();

            Elements images = element.select("img");
            for(Element image: images)
            {
                String img_src_url = image.absUrl("src");
                System.out.println("img url:" + img_src_url);
                getDetailImages(shop_id , string_detail_image_folder, string_detail_160ximage_folder, img_src_url);
            }
        }
    }
    private String shop_size(String review_detail_review) {

        String spec = "";
        if (review_detail_review.indexOf("230M") > 0) {
            spec = "230";
        } else if (review_detail_review.indexOf("235M") > 0) {
            spec = "235";
        } else if (review_detail_review.indexOf("240M") > 0) {
            spec = "240";
        } else if (review_detail_review.indexOf("245M") > 0) {
            spec = "245";
        } else if (review_detail_review.indexOf("250M") > 0) {
            spec = "250";
        } else if (review_detail_review.indexOf("255M") > 0) {
            spec = "255";
        } else if (review_detail_review.indexOf("260M") > 0) {
            spec = "260";
        } else if (review_detail_review.indexOf("265M") > 0) {
            spec = "265";
        } else if (review_detail_review.indexOf("270M") > 0) {
            spec = "270";
        } else if (review_detail_review.indexOf("275M") > 0) {
            spec = "275";
        } else if (review_detail_review.indexOf("280M") > 0) {
            spec = "280";
        }


        return spec;
    }

    private String shop_height(String review_detail_review) {

        String spec = "";
        spec = spec.toUpperCase();
        if (review_detail_review.indexOf("5CM") > 0) {
            spec = "5CM";
        } else if (review_detail_review.indexOf("7CM") > 0) {
            spec = "7CM";
        } else if (review_detail_review.indexOf("8CM") > 0) {
            spec = "8CM";
        } else if (review_detail_review.indexOf("9CM") > 0) {
            spec = "9CM";
        } else if (review_detail_review.indexOf("10CM") > 0) {
            spec = "10CM";
        } else if (review_detail_review.indexOf("11CM") > 0) {
            spec = "11CM";
        } else if (review_detail_review.indexOf("12CM") > 0) {
            spec = "12CM";
        }

        return spec;
    }

    private void plusStore(String address) {
        try {
            //Call SHOP TITLE.

            Document main_doc = Jsoup.connect(address).get();
            Elements main_elements = main_doc.select("p.minishop_title");
//			System.out.println(main_elements.text());
            String shop_title = main_elements.text();
            shop_title = shop_title.replaceAll("amp;", "").replaceAll("\\p{Z}", "");
            String root = System.getProperty("user.dir");
            String string_image_folder = root + "/data/" + shop_title + "_image";
            String string_resize_image_folder = root + "/data_160x160/" + shop_title + "_image";
            File image_folder = new File(string_image_folder);
            if (image_folder.exists() == false) {
                image_folder.mkdirs();
            }
            image_folder = new File(string_resize_image_folder);
            if (image_folder.exists() == false) {
                image_folder.mkdirs();
            }
            String item_address = address.replace("http://item2.gmarket.co.kr/item/detailview/Item.aspx?", "http://mitem.gmarket.co.kr/Item?");

            //Photo review
            int goodcodeIdx = item_address.indexOf("goodscode");
            int pos_shop_cd_idx = item_address.indexOf("&pos_shop_cd");
            String goodscode = item_address.substring(goodcodeIdx + "goodscode".length() + 1, pos_shop_cd_idx);
            for (int page_no = 1; ; page_no++) {
                String review_address = "http://mitem.gmarket.co.kr/Review/PhotoReview?goodscode=" + goodscode + "&page_no=" + page_no;
                Document review_doc = Jsoup.connect(review_address).get();
                Elements review_element = review_doc.select("div.vertical_list ul.photo_rv li");
                try {
                    // BufferedWriter 와 FileWriter를 조합하여 사용 (속도 향상)
                    BufferedWriter fw = new BufferedWriter(new FileWriter(string_image_folder + "/Photo_review.txt", true));

                    // 파일안에 문자열 쓰기
                    if (review_element.size() == 0)
                        break;
                    for (Element el : review_element) {
                        String html_detail_review = el.html();
                        int start_href = html_detail_review.indexOf("<a href=") + "<a href=".length() + 1;
                        int end_href = html_detail_review.indexOf(">") - 1;

                        String address_detail_review = html_detail_review.substring(start_href, end_href);
                        address_detail_review = address_detail_review.replaceAll("amp;", "");

                        String review_title = el.text();
                        String review_detail_address = "http://mitem.gmarket.co.kr" + address_detail_review;
                        Document review_detail_doc = Jsoup.connect(review_detail_address).get();
                        Elements review_detail_elements = review_detail_doc.select("ul#update-ul li div");
                        for (Element review_detail_element : review_detail_elements) {
                            String review_detail_review = review_detail_element.text();

                            String spec_size = "";
                            String spec_height = "";
                            review_detail_review = review_detail_review.toUpperCase();
//							System.out.println(review_detail_review);
                            spec_size = shop_size(review_detail_review);
                            spec_height = shop_height(review_detail_review);
                            String spec = spec_size + ":" + spec_height;

                            if (spec.length() > 1) {
                                if (Item_Spec.containsKey(spec) == true) {
                                    int count = Item_Spec.get(spec);
                                    Item_Spec.put(spec, count + 1);
                                } else {
                                    Item_Spec.put(spec, 1);
                                }

                            }
                            fw.write(review_detail_review + "\n");
                            fw.flush();
                        }
                    }
                    fw.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(e.toString());
                }
            }
            //Text revidw
            //http://mitem.gmarket.co.kr/Review/TextReview?goodscode=915406663&page_no=3
            for (int page_no = 1; ; page_no++) {
                String review_address = "http://mitem.gmarket.co.kr/Review/TextReview?goodscode=" + goodscode + "&page_no=" + page_no;
                Document review_doc = Jsoup.connect(review_address).get();
                Elements review_element = review_doc.select("div.ctns div.review ul.buycomt ul.norml_rv li");
                try {
                    // BufferedWriter 와 FileWriter를 조합하여 사용 (속도 향상)
                    BufferedWriter fw = new BufferedWriter(new FileWriter(string_image_folder + "/review.txt", true));

                    // 파일안에 문자열 쓰기
                    if (review_element.size() == 0)
                        break;
                    for (Element el : review_element) {

                        String html_detail_review = el.html();

                        String review_detail_review = el.text();
                        fw.write(review_detail_review + "\n");
                        fw.flush();

                        String spec_size = "";
                        String spec_height = "";
                        review_detail_review = review_detail_review.toUpperCase();
//						System.out.println(review_detail_review);
                        spec_size = shop_size(review_detail_review);
                        spec_height = shop_height(review_detail_review);
                        String spec = spec_size + ":" + spec_height;
                        if (spec.length() > 1) {
                            if (Item_Spec.containsKey(spec) == true) {
                                int count = Item_Spec.get(spec);
                                Item_Spec.put(spec, count + 1);
                            } else {
                                Item_Spec.put(spec, 1);
                            }
                        }
                    }

                    fw.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            WebClient webClient = new WebClient(BrowserVersion.INTERNET_EXPLORER_11);
            webClient.setAjaxController(new NicelyResynchronizingAjaxController());
            webClient.getOptions().setJavaScriptEnabled(true);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getCurrentWindow().setInnerHeight(Integer.MAX_VALUE);

            try {
                HtmlPage page_esm = webClient.getPage(item_address);
                String final_page = page_esm.asXml();
                Document doc_esm = Jsoup.parse(final_page);

                Elements img = doc_esm.select("img");
                int idx = 0;
                for (Element el : img) {
                    String src = el.absUrl("src");
//					System.out.println(src);

                    shop_title = shop_title.replaceAll("\\p{Z}", "");
                    shop_title = shop_title.trim();

                    getImages(1, string_image_folder, string_resize_image_folder, src, idx);
                    idx++;
                }

            } catch (FailingHttpStatusCodeException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();

            } catch (Throwable e) {

                // TODO Auto-generated catch block

                e.printStackTrace();

            }
            webClient.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void Item_Review(int shop_id, String string_image_folder, String goodscode) {
        try {
           for (int page_no = 1; ; page_no++) {
                String review_address = "http://mitem.gmarket.co.kr/Review/PhotoReview?goodscode=" + goodscode + "&page_no=" + page_no;
                Document review_doc = Jsoup.connect(review_address).get();
                Elements review_element = review_doc.select("div.vertical_list ul.photo_rv li");

                // BufferedWriter 와 FileWriter를 조합하여 사용 (속도 향상)
                File file = new File(string_image_folder);
                if(!file.exists())
                {
                    file.mkdirs();
                }
                BufferedWriter fw = new BufferedWriter(new FileWriter(string_image_folder + "/Photo_review.txt", true));

                // 파일안에 문자열 쓰기
                if (review_element.size() == 0)
                    break;
                for (Element el : review_element) {
                    String html_detail_review = el.html();
                    int start_href = html_detail_review.indexOf("<a href=") + "<a href=".length() + 1;
                    int end_href = html_detail_review.indexOf(">") - 1;

                    String address_detail_review = html_detail_review.substring(start_href, end_href);
                    address_detail_review = address_detail_review.replaceAll("amp;", "");

                    String review_title = el.text();
                    String review_detail_address = "http://mitem.gmarket.co.kr" + address_detail_review;
                    Document review_detail_doc = Jsoup.connect(review_detail_address).get();
                    Elements review_detail_elements = review_detail_doc.select("ul#update-ul li div");
                    for (Element review_detail_element : review_detail_elements) {
                        String review_detail_review = review_detail_element.text();

                        String spec_size = "";
                        String spec_height = "";
                        review_detail_review = review_detail_review.toUpperCase();
//							System.out.println(review_detail_review);
                        spec_size = shop_size(review_detail_review);
                        spec_height = shop_height(review_detail_review);
                        String spec = spec_size + ":" + spec_height;

                        if (spec.length() > 1) {
                            if (Item_Spec.containsKey(spec) == true) {
                                int count = Item_Spec.get(spec);
                                Item_Spec.put(spec, count + 1);
                            } else {
                                Item_Spec.put(spec, 1);
                            }

                        }
                        fw.write(review_detail_review + "\n");
                        fw.flush();

                        DBConfig dbconfig = new DBConfig();
                        int check = dbconfig.CHECK_EXIST_REVIEW(shop_id, review_detail_review);
                        if(check == 0) {
                            dbconfig.INSERT_REVIEW_TABLE(shop_id, review_detail_review);
                        }
                    }
                }
                fw.close();

            }
            //Text revidw
            //http://mitem.gmarket.co.kr/Review/TextReview?goodscode=915406663&page_no=3
            for (int page_no = 1; ; page_no++) {
                String review_address = "http://mitem.gmarket.co.kr/Review/TextReview?goodscode=" + goodscode + "&page_no=" + page_no;
                Document review_doc = Jsoup.connect(review_address).get();
                Elements review_element = review_doc.select("div.ctns div.review ul.buycomt ul.norml_rv li");
                try {
                    // BufferedWriter 와 FileWriter를 조합하여 사용 (속도 향상)
                    BufferedWriter fw = new BufferedWriter(new FileWriter(string_image_folder + "/review.txt", true));

                    // 파일안에 문자열 쓰기
                    if (review_element.size() == 0)
                        break;
                    for (Element el : review_element) {

                        String html_detail_review = el.html();

                        String review_detail_review = el.text();
                        fw.write(review_detail_review + "\n");
                        fw.flush();

                        String spec_size = "";
                        String spec_height = "";
                        review_detail_review = review_detail_review.toUpperCase();
//						System.out.println(review_detail_review);
                        spec_size = shop_size(review_detail_review);
                        spec_height = shop_height(review_detail_review);
                        String spec = spec_size + ":" + spec_height;
                        if (spec.length() > 1) {
                            if (Item_Spec.containsKey(spec) == true) {
                                int count = Item_Spec.get(spec);
                                Item_Spec.put(spec, count + 1);
                            } else {
                                Item_Spec.put(spec, 1);
                            }
                        }
                    }

                    fw.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    private int resize_image(String source_path, String target_path) {
        int rtn = 0;
        try {


            Thumbnails.of(new File(source_path))
                    .size(227, 227)
                    .toFile(new File(target_path));

        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return rtn;
    }

    private int skeep_file_name(String name) {

        if (name.indexOf("img_load") >= 0) {
            return 1;
        }
        if (name.indexOf("info") >= 0) {
            return 1;
        }
        if (name.indexOf("%") >= 0) {
            return 1;
        }
        if (name.indexOf("information") >= 0) {
            return 1;
        }

        if (name.indexOf("nb") >= 0) {
            return 1;
        }
        if (name.indexOf("click") >= 0) {
            return 1;
        }
        return 0;
    }

    private void getImages(int shop_id, String local_source_Storage_path, String local_target_Storage_path, String src, int idx)
    {

        String folder = null;

        //Exctract the name of the image from the src attribute
        int indexname = src.lastIndexOf("/");
        if (indexname == src.length()) {
            src = src.substring(1, indexname);
        }
        indexname = src.lastIndexOf("/");
        String name = src.substring(indexname, src.length());

        //Open a URL Stream
        InputStream in = null;
        try {
            UrlValidator urlValidator = new UrlValidator();
            //valid URL
            if (!urlValidator.isValid(src)) {
                System.out.println("url is invalid");
                return;
            }

            URL url = new URL(src);
            in = url.openStream();
            local_source_Storage_path = local_source_Storage_path.replaceAll("&npsp;", "");
            try {
                name = "M_" + name.replace("/", "");
                String full_image_path = local_source_Storage_path + "/" + name;
                String resize_image_path = local_target_Storage_path + "/" + name;

                File image_path = new File(full_image_path);
                DBConfig dbwork = new DBConfig();
                int check = dbwork.CHECK_IMAGE_TABLE(shop_id, full_image_path);
                if(check == 0) {
                    if (src.indexOf(".jpg") > 0) {

                        OutputStream out = new BufferedOutputStream(new FileOutputStream(full_image_path));
                        for (int b; (b = in.read()) != -1; ) {
                            out.write(b);
                        }
                        out.close();
                        in.close();

                        resize_image(full_image_path, resize_image_path);
                    } else if (src.indexOf(".gif") > 0) {
                        OutputStream out = new BufferedOutputStream(new FileOutputStream(full_image_path));
                        for (int b; (b = in.read()) != -1; ) {
                            out.write(b);
                        }
                        out.close();
                        in.close();
                        resize_image(local_source_Storage_path + "/" + name, resize_image_path);
                    }
                    dbwork.INSERT_IMAGE_TABLE(shop_id, name, full_image_path, resize_image_path );
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            }
            in = null;
        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }
    private void getDetailImages(int shop_id, String local_source_Storage_path, String local_target_Storage_path, String src)
    {
        String full_image_path = "";
        String resize_image_path = "";
        try {
            String folder = null;

            //Exctract the name of the image from the src attribute
            int indexname = src.lastIndexOf("/");
            if (indexname == src.length()) {
                src = src.substring(1, indexname);
            }
            indexname = src.lastIndexOf("/");
            String name = src.substring(indexname, src.length());
            if (skeep_file_name(name) == 1) {
                return;
            }
            //Open a URL Stream
            URL url = new URL(src);
            InputStream in = url.openStream();
            local_source_Storage_path = local_source_Storage_path.replaceAll("&npsp;", "");
            name = "D_" + name.replace("/", "");

            full_image_path = local_source_Storage_path + "/" + name;
            resize_image_path = local_target_Storage_path + "/" + name;
            if(local_source_Storage_path.indexOf("&") > 0)
            {
                System.out.println("Warning");
            }
            File image_path = new File(full_image_path);
            DBConfig dbwork = new DBConfig();
            int check = dbwork.CHECK_IMAGE_TABLE(shop_id, full_image_path);
            if(check == 0) {
                if (src.indexOf(".jpg") > 0) {

                    OutputStream out = new BufferedOutputStream(new FileOutputStream(full_image_path));
                    for (int b; (b = in.read()) != -1; ) {
                        out.write(b);
                    }
                    out.close();
                    in.close();

                    resize_image(full_image_path, resize_image_path);
                    dbwork.INSERT_IMAGE_TABLE(shop_id, name, full_image_path, resize_image_path );
                } else if (src.indexOf(".gif") > 0) {
                    OutputStream out = new BufferedOutputStream(new FileOutputStream(full_image_path));
                    for (int b; (b = in.read()) != -1; ) {
                        out.write(b);
                    }
                    out.close();
                    in.close();
                    resize_image(local_source_Storage_path + "/" + name, resize_image_path);
                    dbwork.INSERT_IMAGE_TABLE(shop_id, name, full_image_path, resize_image_path );
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
