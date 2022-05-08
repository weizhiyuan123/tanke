package Tankwar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Tankwar.MyPanel;
/**
 * @author 魏志远
 * @version 1.0
 * 坦克大战的主界面
 */
public class TankGame extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;

    MyPanel mp=null;

    //定义一个开始面板
    MyStartPanel msp=null;

    //作出我需要的菜单
    JMenuBar jmb=null;
    //开始游戏
    JMenu jm1=null;
    JMenuItem jmil=null;
    //退出系统
    JMenuItem jmi2=null;
    //存盘退出
    JMenuItem jmi3=null;
    JMenuItem jmi4=null;
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        TankGame mtg=new TankGame();
    }

    //构造函数
    public TankGame()
    {
        //mp=new MyPanel();

        //启动mp线程
        //Thread t=new Thread(mp);
        //t.start();
        //this.add(mp);
        //注册监听
        //this.addKeyListener(mp);

        //创建菜单及菜单选项
        jmb=new JMenuBar();
        jm1 =new JMenu("游戏(G)");
        jm1.setFont(new Font("宋体", Font.ITALIC, 22)); //设置字体
        //设置快捷方式
        jm1.setMnemonic('G');
        jmil =new JMenuItem("开始新游戏(N)");
        jmi2 =new JMenuItem("退出游戏(E)");
        jmi3 =new JMenuItem("存盘退出游戏(C)");
        jmi4 =new JMenuItem("继续上局游戏(S)");
        jmil.setFont(new Font("宋体", Font.ITALIC, 16)); //设置字体
        jmi2.setFont(new Font("宋体", Font.ITALIC, 16)); //设置字体
        jmi3.setFont(new Font("宋体", Font.ITALIC, 16)); //设置字体
        jmi4.setFont(new Font("宋体", Font.ITALIC, 16)); //设置字体

        //注册监听
        jmi4.addActionListener(this);
        jmi4.setActionCommand("conGame");

        //注册监听
        jmi3.addActionListener(this);
        jmi3.setActionCommand("saveExit");

        jmi2.addActionListener(this);
        jmi2.setActionCommand("exit");
        jmi2.setMnemonic('E');
        //对jmil相应
        jmil.addActionListener(this);
        jmil.setActionCommand("newgame");
        jm1.add(jmil);
        jm1.add(jmi2);
        jm1.add(jmi3);
        jm1.add(jmi4);
        jmb.add(jm1);

        msp=new MyStartPanel();
        Thread t=new Thread(msp);
        t.start();

        this.setJMenuBar(jmb);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(msp);
        this.setSize(1000, 900);
        this.setVisible(true);
    }

    public void actionPerformed(ActionEvent arg0) {
        // TODO Auto-generated method stub
        //对用户不同的点击作出不同的处理
        if(arg0.getActionCommand().equals("newgame"))
        {
            //创建战场面板
            mp=new MyPanel("newGame");

            //启动mp线程
            Thread t=new Thread(mp);
            t.start();
            //先删除旧的开始面板
            this.remove(msp);
            this.add(mp);
            //注册监听
            this.addKeyListener(mp);
            //显示,刷新JFrame
            this.setVisible(true);


        }else if(arg0.getActionCommand().equals("exit"))
        {
            //用户点击了退出系统的菜单
            //保存击毁敌人数量.
            Recorder.keepRecording();

            System.exit(0);
        }//对存盘退出左处理
        else if(arg0.getActionCommand().equals("saveExit"))
        {
            System.out.println("111");
            System.out.println("mp.ets.size="+mp.ets.size());
            //工作
            Recorder rd=new Recorder();
            rd.setEts(mp.ets);
            //保存击毁敌人的数量和敌人的坐标
            rd.keepRecAndEnemyTank();

            //退出
            System.exit(0);
        }else if(arg0.getActionCommand().equals("conGame"))
        {
            //创建战场面板
            mp=new MyPanel("con");


            //启动mp线程
            Thread t=new Thread(mp);
            t.start();
            //先删除旧的开始面板
            this.remove(msp);
            this.add(mp);
            //注册监听
            this.addKeyListener(mp);
            //显示,刷新JFrame
            this.setVisible(true);
        }
    }

}
