package com.gzpzg.gamesearch;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
@ResponseBody
public class controller {

    static HttpUtil httpUtil = new HttpUtil();

    @RequestMapping(value = "/data",method = RequestMethod.POST)
    public String data(@RequestBody Data data) throws IOException {
        if(data==null||data.getGremlin()==null){
            return "{}";
        }
        String result = httpUtil.post("http://106.14.5.35:8182","{\"gremlin\":\""+data.getGremlin()+"\"}");
        result = removeStr(result);
        return result;
    }


    public static String removeStr(String str){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0;i<str.length();i++){
            char c = str.charAt(i);
            if(c != '@'){
                stringBuilder.append(c);
            }
        }
        return stringBuilder.toString();
    }
}
