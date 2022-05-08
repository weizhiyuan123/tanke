package Tankwar;

import com.sun.xml.internal.bind.v2.model.core.ID;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Vector;
import java.sql.*;

/**
 * @author 魏志远
 * @version 1.0
 * 坦克大战的记录成绩，坦克坐标
 */
//记录类,同时也可以保存玩家的设置
class Recorder
{
    //记录每关有多少敌人
    private static int enNum=20;
    //设置我有多少可以用的人
    private static int myLife=3;
    //记录总共消灭了多少敌人
    private static int allEnNum=0;
    //从文件中恢复记录点
    static Vector<Node> nodes=new Vector<Node>();

    private static FileWriter fw=null;
    private static BufferedWriter bw=null;
    private static FileReader fr=null;
    private static BufferedReader br=null;

    private  Vector<YourTank> ets=new Vector<YourTank>();

    // MySQL 8.0 以上版本 - JDBC 驱动名及数据库 URL
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://124.221.217.3:3306/tankes?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";


    // 数据库的用户名与密码，需要根据自己的设置
    static final String USER = "root";
    static final String PASS = "123456";



    //完成读取认为
    public Vector<Node> getNodesAndEnNums()
    {
        try {
            fr=new FileReader("d:\\myRecording.txt");
            br=new BufferedReader(fr);
            String n="";
            //先读取第一行
            n=br.readLine();
            allEnNum=Integer.parseInt(n);
            while((n=br.readLine())!=null)
            {
                String []xyz=n.split(" ");

                Node node=new Node(Integer.parseInt(xyz[0]),Integer.parseInt(xyz[1]),Integer.parseInt(xyz[2]));
                nodes.add(node);
            }


        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }finally{

            try {
                //后打开则先关闭
                br.close();
                fr.close();
            } catch (Exception e) {
                e.printStackTrace();
                // TODO: handle exception
            }
        }

        return nodes;

    }


    //保存击毁敌人的数量和敌人坦克坐标,方向

    public  void keepRecAndEnemyTank()
    {
        try {

            //创建
            fw=new FileWriter("d:\\myRecording.txt");
            bw=new BufferedWriter(fw);

            bw.write(allEnNum+"\r\n");
            System.out.println("size="+ets.size());
            //保存当前活的敌人坦克的坐标和方向
            for(int i=0;i<ets.size();i++)
            {
                //取出第一个坦克
                YourTank et=ets.get(i);

                if(et.isLive)
                {
                    //活的就保存
                    String recode=et.x+" "+et.y+" "+et.direct;

                    //写入
                    bw.write(recode+"\r\n");

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }finally{

            //关闭流
            try {
                //后开先关闭
                bw.close();
                fw.close();
            } catch (Exception e) {
                e.printStackTrace();
                // TODO: handle exception
            }
        }
        Connection conn = null;
        Statement stmt = null;
        try{
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);

            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            //执行查询
            System.out.println(" 实例化Statement对象...");
            stmt = conn.createStatement();

            String sql;
            sql = "SELECT id FROM user";
            ResultSet rs = stmt.executeQuery(sql);

            int[] arr = new int[100];
            int i=0;
            // 展开结果集数据库
            while(rs.next()){
                i++;
                // 通过字段检索
                int id  = rs.getInt("id");
                    arr[i] = id;
                // 输出数据
            }
                //System.out.print(Arrays.toString(arr));
            int id1 =1;
            for(int j=0;j<arr.length;j++)
            {
                if(arr[j] == id1){
                    //刷新的sql语句
                    String sql1 = "UPDATE user SET gra = '"+allEnNum+"' WHERE id = '1';";
                    //建立PreparedStatement对象
                    PreparedStatement pst = conn.prepareStatement(sql1);
                    pst.executeUpdate();
                    //System.out.println(pst);
                    break;
                }
            }
//            //刷新的sql语句
//            String sql1 = "INSERT INTO `user` (`"+id1+"`, `us`, `pass`, `intro`, `gra`) VALUES ('5', 'wei11', 'wei11', 'wei11', '"+allEnNum+"');";
//            //建立PreparedStatement对象
//            PreparedStatement pst = conn.prepareStatement(sql1);
//            pst.executeUpdate();
//            //System.out.println(pst);

            //完成后关闭
            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally{
            // 关闭资源
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se2){
            }// 什么都不做
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
        System.out.println("Goodbye!");
    }


    //从文件中读取，记录
    public static void getRecoring()
    {
        try {
            fr=new FileReader("d:\\myRecording.txt");
            br=new BufferedReader(fr);
            String n=br.readLine();
            allEnNum=Integer.parseInt(n);
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }finally{

            try {
                //后打开则先关闭
                br.close();
                fr.close();
            } catch (Exception e) {
                e.printStackTrace();
                // TODO: handle exception
            }
        }
    }

    //把玩家击毁敌人坦克数量保存到文件中
    public static void keepRecording()
    {
        try {

            //创建
            fw=new FileWriter("d:\\myRecording.txt");
            bw=new BufferedWriter(fw);

            bw.write(allEnNum+"\r\n");
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }finally{

            //关闭流
            try {
                //后开先关闭
                bw.close();
                fw.close();
            } catch (Exception e) {
                e.printStackTrace();
                // TODO: handle exception
            }
        }
    }

    public static int getEnNum() {
        return enNum;
    }
    public static void setEnNum(int enNum) {
        Recorder.enNum = enNum;
    }
    public static int getMyLife() {
        return myLife;
    }
    public static void setMyLife(int myLife) {
        Recorder.myLife = myLife;
    }

    //减少敌人数
    public static void reduceEnNum()
    {
        enNum--;
    }
    //消灭敌人
    public static void addEnNumRec()
    {
        allEnNum++;
    }
    public static int getAllEnNum() {
        return allEnNum;
    }
    public static void setAllEnNum(int allEnNum) {
        Recorder.allEnNum = allEnNum;
    }


    public  Vector<YourTank> getEts() {
        return ets;
    }


    public  void setEts(Vector<YourTank> ets1) {

        this.ets = ets1;
        System.out.println("ok");
    }
}
