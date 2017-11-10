package com.task.job.utils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by weihonglin on 16/5/27.
 */
public  class StringUtil {

    public static   String join(String separator,List beanList,String propertyname){
           List<String>  list = BeanUtils.getBeanPropertyList(beanList, String.class, propertyname, true);
           return join(separator,list);

    }

    public static String join(String separator,List<String> args){
        if(StringUtils.isBlank(separator)){
            throw new RuntimeException("separator must be not null");
        }
        if(CollectionUtils.isEmpty(args)){
           return "";
        }
        return StringUtils.join(args,separator);
    }

    public static String join(String separator,String... args){
        if(StringUtils.isBlank(separator)){
            throw new RuntimeException("separator must be not null");
        }
        if(args==null || args.length==0){
            return "";
        }
        List list = Arrays.asList(args);
        return StringUtils.join(list.toArray(),separator);
    }

    public static String filterNull(String separator,String str){
        List<String> list = StringUtil.split(separator,str);
        return  join(separator,BeanUtils.filterNull(list));
    }

    public static List<String> split(String separator,String str){
        if(StringUtils.isBlank(separator)){
            throw new RuntimeException("separator must be not null");
        }
        if(StringUtils.isBlank(str)){
            return new ArrayList<>();
        }
        String[] args = str.split(separator);
        return Arrays.asList(args);
    }

    public  static  String trunc(String original,int length){
        if(StringUtils.isBlank(original)){
            return  original;
        }
        int ln = getUnicodeLength(original);
        length = length>ln?ln:length;
        return getUnicode(original).substring(0,length);
    }

    public static String toString(Object obj){
        return obj!=null?ReflectionToStringBuilder.toString(obj):"";
    }

    /**
     * 获取unicode编码长度
     * @param original
     * @return
     */
    public  static  int getUnicodeLength(String original){
        String unicodeStr = getUnicode(original);
        if(unicodeStr==null){
            return  0;
        }
        return  unicodeStr.length();
    }

    /**
     * 获取unicode编码
     * @param original
     * @return
     */
    public static String getUnicode(String original) {
        if (original == null)
            return null;
        String result = "";
        for (int i = 0, length = original.length(); i < length; i++) {
            if (original.charAt(i) > 0 && original.charAt(i) < 256)
                result += original.charAt(i);
            else
                result += "\\u" + Integer.toHexString(original.charAt(i)).toUpperCase();
        }
        return result;
    }

    public static void main(String[] args) {
        String s = "121.205.198.92 12 login  - - [21/Feb/2014:00:00:07 +0800] \"GET /archives/417.html HTTP/1.1\" 200 11465 \"http://shiyanjun.cn/archives/417.html/\" \"Mozilla/5.0 (Windows NT 5.1; rv:11.0) Gecko/20100101 Firefox/11.0\"\n" +
                "121.205.198.92 12 login  - - [21/Feb/2014:00:00:11 +0800] \"POST /wp-comments-post.php HTTP/1.1\" 302 26 \"http://shiyanjun.cn/archives/417.html/\" \"Mozilla/5.0 (Windows NT 5.1; rv:23.0) Gecko/20100101 Firefox/23.0\"\n" +
                "121.205.198.92 12 regis  - - [21/Feb/2014:00:00:12 +0800] \"GET /archives/417.html/ HTTP/1.1\" 301 26 \"http://shiyanjun.cn/archives/417.html/\" \"Mozilla/5.0 (Windows NT 5.1; rv:11.0) Gecko/20100101 Firefox/11.0\"\n" +
                "121.205.198.92 12 bind   - - [21/Feb/2014:00:00:12 +0800] \"GET /archives/417.html HTTP/1.1\" 200 11465 \"http://shiyanjun.cn/archives/417.html\" \"Mozilla/5.0 (Windows NT 5.1; rv:11.0) Gecko/20100101 Firefox/11.0\"\n" +
                "121.205.241.22 13 regis  - - [21/Feb/2014:00:00:13 +0800] \"GET /archives/526.html HTTP/1.1\" 200 12080 \"http://shiyanjun.cn/archives/526.html/\" \"Mozilla/5.0 (Windows NT 5.1; rv:11.0) Gecko/20100101 Firefox/11.0\"\n" +
                "121.205.241.22 13 login  - - [21/Feb/2014:00:00:15 +0800] \"POST /wp-comments-post.php HTTP/1.1\" 302 26 \"http://shiyanjun.cn/archives/526.html/\" \"Mozilla/5.0 (Windows NT 5.1; rv:23.0) Gecko/20100101 Firefox/23.0”\n" +
                "121.205.197.92 14 regis  - - [21/Feb/2014:00:00:07 +0800] \"GET /archives/417.html HTTP/1.1\" 200 11465 \"http://shiyanjun.cn/archives/417.html/\" \"Mozilla/5.0 (Windows NT 5.1; rv:11.0) Gecko/20100101 Firefox/11.0\"\n" +
                "121.205.197.92 14 regis  - - [21/Feb/2014:00:00:11 +0800] \"POST /wp-comments-post.php HTTP/1.1\" 302 26 \"http://shiyanjun.cn/archives/417.html/\" \"Mozilla/5.0 (Windows NT 5.1; rv:23.0) Gecko/20100101 Firefox/23.0\"\n" +
                "121.205.197.92 14 bind   - - [21/Feb/2014:00:00:12 +0800] \"GET /archives/417.html/ HTTP/1.1\" 301 26 \"http://shiyanjun.cn/archives/417.html/\" \"Mozilla/5.0 (Windows NT 5.1; rv:11.0) Gecko/20100101 Firefox/11.0\"\n" +
                "121.205.197.92 14 login  - - [21/Feb/2014:00:00:12 +0800] \"GET /archives/417.html HTTP/1.1\" 200 11465 \"http://shiyanjun.cn/archives/417.html\" \"Mozilla/5.0 (Windows NT 5.1; rv:11.0) Gecko/20100101 Firefox/11.0\"\n" +
                "121.205.221.21 15 login  - - [21/Feb/2014:00:00:13 +0800] \"GET /archives/526.html HTTP/1.1\" 200 12080 \"http://shiyanjun.cn/archives/526.html/\" \"Mozilla/5.0 (Windows NT 5.1; rv:11.0) Gecko/20100101 Firefox/11.0\"\n" +
                "121.205.221.21 15 login  - - [21/Feb/2014:00:00:15 +0800] \"POST /wp-comments-post.php HTTP/1.1\" 302 26 \"http://shiyanjun.cn/archives/526.html/\" \"Mozilla/5.0 (Windows NT 5.1; rv:23.0) Gecko/20100101 Firefox/23.0”\n";
        Pattern p = Pattern.compile("(\\d+\\.\\d+\\.\\d+\\.\\d+)\\s+(\\d*)\\s+(.*?)\\s+");
        Matcher m = p.matcher(s);
        while(m.find()){
            System.out.println(m.group(1)+":"+m.group(2)+":"+m.group(3));
        }
    }
}
