package com.xxx.seckill.util;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.xxx.seckill.pojo.User;
import com.xxx.seckill.vo.RespBean;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 生成用户工具类
 *
 */

public class UserUtil {
    private static void createUser(int count) throws Exception {
        List<User> users = new ArrayList<>(count);

        for(int i = 0; i < count; i++){
            User user = new User();
            user.setId(13000000000L + i);
            user.setNickname("user" + i);
            user.setSlat("1a2b3c4d");
            user.setLoginCount(1);
            user.setRegisterDate(new Date());
            user.setPassword(MD5Util.inputPassToDBPass("123456", user.getSlat()));
            users.add(user);
        }
        System.out.println("create user");

//        //插入数据库
//        Connection conn = getConn();
//        String sql = "insert into t_user(login_count, nickname, register_date, slat, password, id) values(?,?,?,?,?,?)";
//
//        PreparedStatement pstmt = conn.prepareStatement(sql);
//        for(int i = 0; i < users.size(); i++){
//            User user = users.get(i);
//            pstmt.setInt(1, user.getLoginCount());
//            pstmt.setString(2, user.getNickname());
//            pstmt.setTimestamp(3, new Timestamp(user.getRegisterDate().getTime()));
//            pstmt.setString(4, user.getSlat());
//            pstmt.setString(5, user.getPassword());
//            pstmt.setLong(6, user.getId());
//            pstmt.addBatch();
//        }
//
//        pstmt.executeBatch();
//        pstmt.clearParameters();
//        conn.close();
        System.out.println("insert to do");


        //登录，生成UserTicket
        String urlString = "http://localhost:8080/login/doLogin";
        File file = new File("C:\\Users\\water\\Desktop\\config.txt");
        if(file.exists()){
            file.delete();
        }
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        raf.seek(0);
        for(int i = 0; i < users.size(); i++){
            User user = users.get(i);
            URL url = new URL(urlString);
            HttpURLConnection co = (HttpURLConnection) url.openConnection();
            co.setRequestMethod("POST");
            co.setDoOutput(true);
            OutputStream out = co.getOutputStream();
            String params = "mobile=" + user.getId() + "&password="+MD5Util.inputPassToFromPass("123456");
            out.write(params.getBytes());
            out.flush();
            InputStream inputStream = co.getInputStream();
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            byte[] buff = new byte[1024];
            int len = 0;
            while((len = inputStream.read(buff)) >= 0){
                bout.write(buff, 0, len);
            }
            inputStream.close();
            bout.close();
            String response = new String(bout.toByteArray());
            ObjectMapper mapper = new ObjectMapper();
            RespBean respBean = mapper.readValue(response, RespBean.class);
            String userTicket = (String) respBean.getObj();
            System.out.println("create userTicket : " + user.getId());
            String row = user.getId() + "," + userTicket;
            raf.seek(raf.length());
            raf.write(row.getBytes());
            raf.write("\r\n".getBytes());
            System.out.println("write to file :" + user.getId());
        }
        raf.close();
        System.out.println("over");

    }

    private static Connection getConn() throws ClassNotFoundException, SQLException {
        String url = "jdbc:mysql:///seckill?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai";
        String username = "root";
        String password = "123";
        String driver = "com.mysql.cj.jdbc.Driver";
        Class.forName(driver);
        return DriverManager.getConnection(url, username, password);

    }


    public static void main(String[] args) throws Exception {
        createUser(5000);
    }
}
