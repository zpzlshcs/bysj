package gzpzg;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;

public class WebUtil {
    public static void addZhuangbei() throws IOException {
        HttpUtil httpUtil = new HttpUtil();
        Gson gson = new Gson();
        Document document = Jsoup.connect("http://db.18183.com/wzry/equip/").get();
        Elements elements = document.getElementById("all_equipment").getElementsByTag("a");
        Iterator<Element> it = elements.iterator();
        WebClient webclient = new WebClient();
        webclient.waitForBackgroundJavaScript(10*1000);
        webclient.setJavaScriptTimeout(5*1000);
        webclient.getOptions().setJavaScriptEnabled(true);
        webclient.getOptions().setThrowExceptionOnScriptError(false);
        while (it.hasNext()) {
            Element element = it.next();
//            Document document1 = Jsoup.connect("http://db.18183.com" + element.attr("href")).get();

            HtmlPage page = webclient.getPage("http://db.18183.com" + element.attr("href"));
            Document document1 = Jsoup.parse(page.asXml());

            Element element1 = document1.getElementsByAttributeValue("class", "mod-bg fl").get(0);
            Elements elements1 = element1.getElementsByAttributeValue("class", "equip-base-info").get(0).getElementsByTag("dl");
            HashMap<String, Object> hashMap = new HashMap<>();
            String typeName = "equip";//在图谱中该节点的type
            String equipName = elements1.get(0).getElementsByTag("dd").get(0).text();//装备名称
            String imgUrl = element1.getElementsByAttributeValue("class", "equip-base-info").get(0)
                    .getElementsByTag("img").attr("src");
            try {
                saveImg(imgUrl,equipName+".jpg");//保存装备图片
            } catch (Exception e) {
                e.printStackTrace();
            }
            hashMap.put("img",equipName+".jpg");
            hashMap.put(elements1.get(1).getElementsByTag("dt").get(0).text(),
                    Integer.valueOf(elements1.get(1).getElementsByTag("dd").get(0).text()));
            Elements elements2 = element1.getElementsByAttributeValue("class", "equip-panel-info");
            Iterator<Element> it1 = elements2.iterator();
            if(it1.hasNext()){
                Iterator<Element> it2 = it1.next().getElementsByTag("li").iterator();
                while(it2.hasNext()){
                    String[] shuxings = it2.next().text().split("：");
                    String shuxingName = shuxings[0];
                    Integer shuxingValue = StringUtil.getNumInString(shuxings[1]);
                    hashMap.put(shuxingName,shuxingValue);
                    //System.out.println(shuxings[0]+"【我是分割线】"+shuxings[1]);
                }
                if(it1.hasNext()){
                    Iterator<Element> it3 = it1.next().getElementsByTag("li").iterator();
                    while(it3.hasNext()){
                        String[] shuxings = it3.next().text().split("：");
                        String shuxingName = shuxings[0];
                        String shuxingValue = shuxings[1];
                        hashMap.put(shuxingName,shuxingValue);
                        //System.out.println(shuxings[0]+"【我是分割线】"+shuxings[1]);
                    }
                }
            }
            StringBuilder shuxingStr = new StringBuilder();
            for(String key:hashMap.keySet()){
                Object o = hashMap.get(key);
                if(o instanceof Integer){
                    shuxingStr.append(",'"+key+"',"+o+"");
                }else{
                    shuxingStr.append(",'"+key+"','"+o+"'");
                }
            }
            String jsonData = "{\"gremlin\":\"graph.addVertex" +
                    "('type', '"+typeName+"', 'name', '"+equipName+"'" +shuxingStr.toString()+
                    ")\"}";
            System.out.println(jsonData);
            httpUtil.post("http:\\\\自己的服务器ip:8182",jsonData);
            //System.out.println("装备名称："+equipName+"，装备属性："+gson.toJson(hashMap));
        }

    }
    public static void connectZhuangbei() throws IOException {
        HttpUtil httpUtil = new HttpUtil();
        Gson gson = new Gson();
        Document document = Jsoup.connect("http://db.18183.com/wzry/equip/").get();
        Elements elements = document.getElementById("all_equipment").getElementsByTag("a");
        Iterator<Element> it = elements.iterator();
        while (it.hasNext()) {
            Element element = it.next();
            Document document1 = Jsoup.connect("http://db.18183.com" + element.attr("href")).get();
            Element element1 = document1.getElementsByAttributeValue("class", "mod-bg fl").get(0);
            Elements elements1 = element1.getElementsByAttributeValue("class", "equip-base-info").get(0).getElementsByTag("dl");
            HashMap<String,Object> hashMap = new HashMap<>();
            String typeName = "equip";//在图谱中该节点的type
            String equipName = elements1.get(0).getElementsByTag("dd").get(0).text();//装备名称


            Elements elements2 = document1.getElementsByAttributeValue("class", "section compose-equip-box mod-bg clearfix")
                    .get(0).getElementsByTag("li");
            Iterator<Element> iterator = elements2.iterator();
            while(iterator.hasNext()){
                String json = "{" +
                        "\"gremlin\":\"g.V().has('name','"+equipName+"')" +
                        ".addE('need').from(g.V().has('name','"+iterator.next().text()+"'))\"" +
                        "}";
                System.out.println(json);
                httpUtil.post("http:\\\\自己的服务器ip:8182",json);
            }
        }
    }



    public static void addHero() throws IOException {
        HashMap<String,String> errorHero = new HashMap<>();
        HttpUtil httpUtil = new HttpUtil();
        Gson gson = new Gson();
        Document document = Jsoup.connect("http://db.18183.com/wzry/").get();
        Elements elements = document.getElementsByAttributeValue("class","section hero-result-box mod-bg clearfix").get(0).getElementsByTag("a");
        Iterator<Element> it = elements.iterator();
        WebClient webclient = new WebClient();

        webclient.waitForBackgroundJavaScript(10*1000);
        webclient.setJavaScriptTimeout(5*1000);
        webclient.getOptions().setJavaScriptEnabled(true);
        webclient.getOptions().setThrowExceptionOnScriptError(false);
        while (it.hasNext()) {
            Element element = it.next();
//            Document document1 = Jsoup.connect("http://db.18183.com" + element.attr("href")).get();
            String url = "http://db.18183.com" + element.attr("href");
            String heroName = element.text();
            try{
                solveHero(webclient,url,httpUtil);
            }catch (Exception e){
                e.printStackTrace();
                errorHero.put(heroName,e.getLocalizedMessage());
            }

        }
        File file = new File("error.txt");
        if(!file.exists()){
            file.createNewFile();
        }
        FileWriter fileWritter = new FileWriter(file.getName(),true);
        fileWritter.write(gson.toJson(errorHero));
        fileWritter.close();
        System.out.println(gson.toJson(errorHero));


    }
    public static void solveHero(WebClient webclient,String url,HttpUtil httpUtil) throws IOException {

        HtmlPage page = webclient.getPage(url);
        Document document1 = Jsoup.parse(page.asXml());
        Element element1 = document1.getElementsByAttributeValue("class", "hero-basicinfo-box fl").get(0);

        String typeName = "hero";
        String heroName = element1.getElementsByAttributeValue("class","name").get(0).getElementsByTag("h1").get(0).text();
        String imgUrl = element1.getElementsByAttributeValue("class","name").get(0).getElementsByTag("img").get(0).attr("src");
            try {
                System.out.println(imgUrl);
                saveImg(imgUrl,heroName+".jpg");
            } catch (Exception e) {
                e.printStackTrace();
            }
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("img",heroName+".jpg");
            Elements baseShuxing = element1.getElementsByAttributeValue("class","base").get(0).getElementsByTag("dl");
            Iterator<Element> baseIt = baseShuxing.iterator();
            while(baseIt.hasNext()){
                Element oneBase = baseIt.next();
                if(oneBase.getElementsByTag("dt").text().trim().equals("英雄礼包"))continue;
                hashMap.put(oneBase.getElementsByTag("dt").text().trim(),oneBase.getElementsByTag("dd").text().trim());
            }

            Elements attrShuxing = element1.getElementsByAttributeValue("class","attr-list").get(0).getElementsByTag("dl");
            Iterator<Element> attrIt = attrShuxing.iterator();
            while(attrIt.hasNext()){
                Element oneAttr = attrIt.next();
                hashMap.put(oneAttr.getElementsByTag("dt").text().trim(),StringUtil.getNumInString(oneAttr.getElementsByTag("dd").toString()));
            }
            hashMap.put("背景故事",document1.getElementsByAttributeValue("class","otherinfo-item").get(3).text());

            Elements shuxing = document1.getElementsByAttributeValue("class","otherinfo-datapanel").get(0).getElementsByTag("p");//基础属性
            Iterator<Element> sxIt = shuxing.iterator();
            NumberFormat nf=NumberFormat.getPercentInstance();
            while(sxIt.hasNext()){
                Element oneAttr = sxIt.next();
                String[] strs = oneAttr.text().split("：");
                if(strs.length==1){
                    hashMap.put(strs[0].trim(),0);
                }else if(strs.length==2){
                    try {
                        hashMap.put(strs[0].trim(),nf.parse(strs[1].trim()));
                    } catch (ParseException e) {
                        hashMap.put(strs[0].trim(),0);
                    }
                }

            }



            StringBuilder shuxingStr = new StringBuilder();
            for(String key:hashMap.keySet()){
                Object o = hashMap.get(key);
                if(o instanceof Number){
                    if(o instanceof Double){
                        shuxingStr.append(",'"+key+"',"+o+"d");
                    }else{
                        shuxingStr.append(",'"+key+"',"+o+"");
                    }
                }else{
                    shuxingStr.append(",'"+key+"','"+o+"'");
                }
            }
            String jsonData = "{\"gremlin\":\"graph.addVertex" +
                    "('type', '"+typeName+"', 'name', '"+heroName+"'" +shuxingStr.toString()+
                    ")\"}";
            System.out.println(jsonData);
            httpUtil.post("http:\\\\自己的服务器ip:8182",jsonData);

            String skillTypename = "skill";//技能节点的type
            Element skillshuju = document1.getElementById("hero_skill");
            Elements skillImg = skillshuju.getElementsByAttributeValue("class","clearfix").get(0).getElementsByTag("img");
            Elements skillInfo = skillshuju.getElementsByAttributeValue("class","skill-cont").get(0).getElementsByAttributeValue("class","skill-contitem");
            Iterator<Element> skillImgIt = skillImg.iterator();
            Iterator<Element> skillInfoIt = skillInfo.iterator();
            while(skillImgIt.hasNext()&&skillInfoIt.hasNext()){
                Element infoE = skillInfoIt.next();
                //String skillImgUrl = skillImgIt.next().attr("src");
                Elements skillAllInfo = infoE.getElementsByTag("span");
                String skillName = skillAllInfo.get(0).text();
                String skillType = skillAllInfo.get(1).text();
                String skillDesc = infoE.getElementsByTag("p").get(0).text().trim().substring(5).trim();
                StringBuilder stringBuilder = new StringBuilder();
                for(int i=0;i<skillDesc.length();i++){
                    char c = skillDesc.charAt(i);
                    if(c=='\\'){
                        stringBuilder.append("/");
                    }else{
                        stringBuilder.append(c);
                    }
                }
                skillDesc = stringBuilder.toString();

                String skillJsonData = "{\"gremlin\":\"graph.addVertex" +
                        "('type', '"+skillTypename+"', 'name', '"+skillName+"','skillType','"+skillType+"','skillDesc','"+skillDesc +"'"+
                        ")\"}";//添加技能节点
                System.out.println(skillJsonData);
                httpUtil.post("http:\\\\自己的服务器ip:8182",skillJsonData);
                String skillConnJson = "{" +
                        "\"gremlin\":\"g.V().has('name','"+heroName+"')" +
                        ".addE('has').to(g.V().has('type','"+skillTypename+"').has('name','"+skillName+"'))\"" +
                        "}";
                System.out.println(skillConnJson);
                httpUtil.post("http:\\\\自己的服务器ip:8182",skillConnJson);
            }

            Set<String> zhuangbeiSet = new HashSet<>();//适合的装备名称
            Elements zhaungbeiConn = document1.getElementById("tjcz").getElementsByTag("li");
            Iterator<Element> zhaungbeiConnIt = zhaungbeiConn.iterator();
            while(zhaungbeiConnIt.hasNext()){
                zhuangbeiSet.add(zhaungbeiConnIt.next().text().trim());
            }
            for(String s:zhuangbeiSet){
                String zbConnJson = "{" +
                        "\"gremlin\":\"g.V().has('type','"+typeName+"').has('name','"+heroName+"')" +
                        ".addE('fit').to(g.V().has('type','equip').has('name','"+s+"'))\"" +
                        "}";
                httpUtil.post("http:\\\\自己的服务器ip:8182",zbConnJson);
                System.out.println(zbConnJson);
            }

            Elements heroConns = document1.getElementsByAttributeValue("class","relation-hero-item mod-bg clearfix");
            Iterator<Element> heroConnsIt = heroConns.iterator();
            while(heroConnsIt.hasNext()){
                Element heroConn = heroConnsIt.next();
                String heroConnName = heroConn.getElementsByAttributeValue("class","title").get(0).text().trim();
                Elements heros = heroConn.getElementsByTag("a");
                for(int i=0;i<heros.size();i++){
                    String heroConnJson = "{" +
                            "\"gremlin\":\"g.V().has('type','"+typeName+"').has('name','"+heroName+"')" +
                            ".addE('"+heroConnName+"').to(g.V().has('name','"+heros.get(i).text().trim()+"'))\"" +
                            "}";
                    System.out.println(heroConnJson);
                    httpUtil.post("http:\\\\自己的服务器ip:8182",heroConnJson);
                }
            }
    }
    public static void main(String[] args) throws Exception {
//        HttpUtil httpUtil = new HttpUtil();
//        WebClient webclient = new WebClient();
//        webclient.waitForBackgroundJavaScript(10*1000);
//        webclient.setJavaScriptTimeout(5*1000);
//        webclient.getOptions().setJavaScriptEnabled(true);
//        webclient.getOptions().setThrowExceptionOnScriptError(false);
//        solveHero(webclient,"http://db.18183.com/wzry/hero/12551.html",httpUtil);
        addHero();
    }
    public static void saveImg(String urlStr,String imgName) throws Exception {
        if(urlStr.indexOf("https:")==-1&&urlStr.indexOf("http:")==-1){
            urlStr="http:"+urlStr;
        }
        //new一个URL对象
        URL url = new URL(urlStr);
        //打开链接
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        //设置请求方式为"GET"
        conn.setRequestMethod("GET");
        //超时响应时间为5秒
        conn.setConnectTimeout(5 * 1000);
        //通过输入流获取图片数据
        InputStream inStream = conn.getInputStream();
        //得到图片的二进制数据，以二进制封装得到数据，具有通用性
        byte[] data = readInputStream(inStream);
        //new一个文件对象用来保存图片，默认保存当前工程根目录
        File imageFile = new File(imgName);
        //创建输出流
        FileOutputStream outStream = new FileOutputStream(imageFile);
        //写入数据
        outStream.write(data);
        //关闭输出流
        outStream.close();
    }

    public static byte[] readInputStream(InputStream inStream) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        //创建一个Buffer字符串
        byte[] buffer = new byte[1024];
        //每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len = 0;
        //使用一个输入流从buffer里把数据读取出来
        while( (len=inStream.read(buffer)) != -1 ){
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        //关闭输入流
        inStream.close();
        //把outStream里的数据写入内存
        return outStream.toByteArray();
    }
}
