package gzpzg;

public class StringUtil {
    public static  boolean checkPhone(String phone){
        if(phone!=null&&phone.length()==11)return true;
        return false;
    }
    public static boolean checkNull(Object object){ //为空返回false
        if(object==null||(object instanceof String && ((String)object).length()==0)){
            return false;
        }
        return true;
    }
    public static String smallString(String string,int length){ //字符串缩略
        if(string!=null&&string.length()>length){
            return string.substring(0,length)+"…'";
        }else{
            return string;
        }
    }
    public static Integer getNumInString(String str){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0;i<str.length();i++){
            if('0'<=str.charAt(i)&&'9'>=str.charAt(i)){
                stringBuilder.append(str.charAt(i));
            }
        }
        return Integer.valueOf(stringBuilder.toString());
    }

    public static void main(String[] args) {
        System.out.println(getNumInString("asdasd125asd51"));
    }
}
