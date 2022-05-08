package Tankwar;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Vector;


/**
 * @author 魏志远
 * @version 1.0
 * 坦克大战的绘图区域
 */
//我的面板
class MyPanel extends JPanel implements KeyListener,Runnable
{

    //定义一个我的坦克
    MyTank hero=null;




    //定义敌人的坦克组
    Vector<YourTank> ets=new Vector<YourTank>();
    Vector<Node> nodes=new Vector<Node>();

    //定义炸弹集合
    Vector<Bomb> bombs=new Vector<Bomb>();

    //定义敌方坦克数
    int enSize=10;

    //定义三张图片,三张图片才能组成一颗炸弹
    Image image1=null;
    Image image2=null;
    Image image3=null;

    //构造函数
    public MyPanel(String flag)
    {

        //恢复记录
        Recorder.getRecoring();

        //我们的坦克开始坐标
        hero=new MyTank(800,700);




        if(flag.equals("newGame"))
        {
            //初始化敌人的坦克
            for(int i=0;i<enSize;i++)
            {
                //创建一辆敌人的坦克对象
                YourTank et=new YourTank((i+1)*50,0);
                et.setColor(0);
                et.setDirect(2);
                //将MyPanel的敌人坦克向量交给该敌人坦克
                et.setEts(ets);

                //启动敌人的坦克
                Thread t=new Thread(et);
                t.start();
                //给敌人坦克添加一颗子弹
                Shot s=new Shot(et.x+10,et.y+30,2);
                //加入给敌人坦克
                et.ss.add(s);
                Thread t2=new Thread(s);
                t2.start();
                //加入
                ets.add(et);
            }
        }else{

            System.out.println("接着玩");
            nodes=new Recorder().getNodesAndEnNums();
            //初始化敌人的坦克
            for(int i=0;i<nodes.size();i++)
            {
                Node node=nodes.get(i);
                //创建一辆敌人的坦克对象
                YourTank et=new YourTank(node.x,node.y);
                et.setColor(0);
                et.setDirect(node.direct);
                //将MyPanel的敌人坦克向量交给该敌人坦克
                et.setEts(ets);

                //启动敌人的坦克
                Thread t=new Thread(et);
                t.start();
                //给敌人坦克添加一颗子弹
                Shot s=new Shot(et.x+10,et.y+30,2);
                //加入给敌人坦克
                et.ss.add(s);
                Thread t2=new Thread(s);
                t2.start();
                //加入
                ets.add(et);
            }
        }


        try {
            image1=ImageIO.read(new File("./bomb_1.gif"));
            image2=ImageIO.read(new File("./bomb_2.gif"));
            image3=ImageIO.read(new File("./bomb_3.gif"));
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }

        //播放开战声音
        AePlayWave apw=new AePlayWave("./111.wav");
        apw.start();

        //初始化图片d
//		image1=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb_1.gif"));
//		image2=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb_2.gif"));
//		image3=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb_3.gif"));
    }


    //画出提示信息
    public void showInfo(Graphics g)
    {
        //画出提示信息坦克(该坦克不参与战斗)
        this.drawTank(810,180, g, 0, 0);
        g.setColor(Color.black);
        g.drawString(Recorder.getEnNum()+"", 840, 200);
        this.drawTank(880, 180, g, 0, 1);
        g.setColor(Color.black);
        g.drawString(Recorder.getMyLife()+"", 910, 200);

        //画出玩家的总成绩
        g.setColor(Color.black);
        Font f=new Font("宋体",Font.BOLD,20);
        g.setFont(f);
        g.drawString("您的总成绩", 820, 80);

        this.drawTank(810, 110, g, 0, 0);

        g.setColor(Color.black);
        g.drawString(Recorder.getAllEnNum()+"", 840, 130);
    }

    //重新paint
    public void paint(Graphics g)
    {
//        super.paint(g);
//        g.fillRect(0, 0, 800, 800);
        //游戏主面板
        BufferedImage bg = null;
        try {
            bg = ImageIO.read(new File("./Game-bg.png"));
        }catch(Exception e) {
            e.printStackTrace();
        }

        g.drawImage(bg, 0, 0, 980,820,null);

        //画出提示信息
        this.showInfo(g);

        //画出自己的坦克
        if(hero.isLive)
        {
            this.drawTank(hero.getX(), hero.getY(), g, this.hero.direct, 1);
        }
        //从ss,中取出每颗子弹，并画出
        for(int i=0;i<hero.ss.size();i++)
        {
            Shot myShot=hero.ss.get(i);

            //画出子弹,画出一颗子弹
            if(myShot!=null&&myShot.isLive==true)
            {
                g.draw3DRect(myShot.x, myShot.y, 3, 3,false);
            }
            if(myShot.isLive==false)
            {
                //从ss中删除掉该子弹
                hero.ss.remove(myShot);
            }

        }



        //画出炸弹
        for(int i=0;i<bombs.size();i++)
        {
            System.out.println("bombs.size()="+bombs.size());

            //取出炸弹
            Bomb b=bombs.get(i);

            if(b.life>6)
            {
                g.drawImage(image1, b.x, b.y, 30, 30, this);
            }else if(b.life>3)
            {
                g.drawImage(image2, b.x, b.y, 30, 30, this);
            }else{
                g.drawImage(image3, b.x, b.y, 30, 30, this);
            }

            //让b的生命值减小
            b.lifeDown();
            //如果炸弹生命值为0,就把该炸弹重bombs向量去掉
            if(b.life==0)
            {
                bombs.remove(b);
            }


        }

        //画出敌人的坦克
        for(int i=0;i<ets.size();i++)
        {
            YourTank et=ets.get(i);

            if(et.isLive)
            {

                this.drawTank(et.getX(), et.getY(), g, et.getDirect(), 0);
                //再画出敌人的子弹
                //System.out.println("坦克子弹有:"+et.ss.size());
                for(int j=0;j<et.ss.size();j++)
                {
                    //取出子弹
                    Shot enemyShot=et.ss.get(j);
                    if(enemyShot.isLive)
                    {
                        //System.out.println("第 "+i+"坦克的 "+j+"颗子弹x="+enemyShot.x);
                        g.draw3DRect(enemyShot.x, enemyShot.y, 3, 3,false);
                    }else{
                        //如果敌人的坦克死亡就从Vector去掉
                        et.ss.remove(enemyShot);
                    }
                }
            }
        }


    }

    //敌人的坦克是否击中我
    public void hitMe()
    {
        //取出每一个敌人的坦克
        for(int i=0;i<this.ets.size();i++)
        {
            //取出坦克
            YourTank et=ets.get(i);

            //取出每一颗子弹
            for(int j=0;j<et.ss.size();j++)
            {
                //取出子弹
                Shot enemyShot=et.ss.get(j);
                if(hero.isLive)
                {
                    if(this.hitTank(enemyShot, hero))
                    {

                    }
                }
            }
        }
    }


    //判断我的子弹是否击中敌人的坦克
    public void hitEnemyTank()
    {
        //判断是否击中敌人的坦克
        for(int i=0;i<hero.ss.size();i++)
        {
            //取出子弹
            Shot myShot=hero.ss.get(i);
            //判断子弹是否有效
            if(myShot.isLive)
            {
                //取出每个坦克，与它判断
                for(int j=0;j<ets.size();j++)
                {
                    //取出坦克
                    YourTank et=ets.get(j);

                    if(et.isLive)
                    {
                        if(this.hitTank(myShot, et))
                        {
                            //减少敌人数量
                            Recorder.reduceEnNum();
                            //增加我的记录
                            Recorder.addEnNumRec();
                        }
                    }

                }
            }
        }
    }

    //写一个函数专门判断子弹是否击中敌人坦克
    public boolean hitTank(Shot s,Tank et)
    {
        boolean b2=false;

        //判断该坦克的方向
        switch(et.direct)
        {
            //如果敌人坦克的方向是上或者是下
            case 0:
            case 2:
                if(s.x>et.x&&s.x<et.x+20&&s.y>et.y&&s.y<et.y+30)
                {
                    //击中
                    //子弹死亡
                    s.isLive=false;
                    //敌人坦克死亡
                    et.isLive=false;
                    b2=true;
                    //创建一颗炸弹,放入Vector
                    Bomb b=new Bomb(et.x,et.y);
                    //放入Vector
                    bombs.add(b);

                }

                break;
            case 1:
            case 3:
                if(s.x>et.x&&s.x<et.x+30&&s.y>et.y&&s.y<et.y+20)
                {
                    //击中
                    //子弹死亡
                    s.isLive=false;
                    //敌人坦克死亡
                    et.isLive=false;
                    b2=true;
                    //创建一颗炸弹,放入Vector
                    Bomb b=new Bomb(et.x,et.y);
                    //放入Vector
                    bombs.add(b);

                }
        }

        return b2;

    }

    //画出坦克的函数(扩展)
    public void drawTank(int x,int y,Graphics g,int direct,int type)
    {
        //判断是什么类型的坦克
        switch(type)
        {
            case 0:
                g.setColor(Color.cyan);
                break;
            case 1:
                g.setColor(Color.yellow);
                break;
        }

        //判断方向
        switch(direct)
        {
            //向上
            case 0:
                //画出我的坦克(到时再封装成一个函数)
                //1.画出左边的矩形
                g.fill3DRect(x, y, 5, 30,false);
                //2.画出右边矩形
                g.fill3DRect(x+15,y , 5, 30,false);
                //3.画出中间矩形
                g.fill3DRect(x+5,y+5 , 10, 20,false);
                //4.画出圆形
                g.fillOval(x+5, y+10, 10, 10);
                //5.画出线
                g.drawLine(x+10, y+15, x+10, y);
                break;
            case 1:
                //炮筒向右
                //画出上面矩形
                g.fill3DRect(x, y, 30, 5,false);
                //画出下面的矩形
                g.fill3DRect(x, y+15, 30, 5, false);
                //画出中间的矩形
                g.fill3DRect(x+5, y+5, 20, 10, false);
                //画出圆形
                g.fillOval(x+10, y+5, 10, 10);
                //画出线
                g.drawLine(x+15, y+10, x+30, y+10);
                break;
            case 2:
                //向下
                //画出我的坦克(到时再封装成一个函数)
                //1.画出左边的矩形
                g.fill3DRect(x, y, 5, 30,false);
                //2.画出右边矩形
                g.fill3DRect(x+15,y , 5, 30,false);
                //3.画出中间矩形
                g.fill3DRect(x+5,y+5 , 10, 20,false);
                //4.画出圆形
                g.fillOval(x+5, y+10, 10, 10);
                //5.画出线
                g.drawLine(x+10, y+15, x+10, y+30);
                break;
            case 3:
                //向左

                //画出上面矩形
                g.fill3DRect(x, y, 30, 5,false);
                //画出下面的矩形
                g.fill3DRect(x, y+15, 30, 5, false);
                //画出中间的矩形
                g.fill3DRect(x+5, y+5, 20, 10, false);
                //画出圆形
                g.fillOval(x+10, y+5, 10, 10);
                //画出线
                g.drawLine(x+15, y+10, x, y+10);
                break;

        }


    }

    //键按下处理 a 表示向左 s 表示 下 w 表示向上  d表示右

    public void keyPressed(KeyEvent arg0) {
        // TODO Auto-generated method stub

        if(arg0.getKeyCode()==KeyEvent.VK_W || arg0.getKeyCode() == KeyEvent.VK_UP)
        {
            //设置我的坦克的方向
            this.hero.setDirect(0);
            //this.hero.moveUp();
            if (hero.getY() > 30) {
                hero.moveUp();
            }

        }else if(arg0.getKeyCode()==KeyEvent.VK_D || arg0.getKeyCode() == KeyEvent.VK_RIGHT)
        {
            //向右
            this.hero.setDirect(1);
            //this.hero.moveRight();
            if (hero.getX() < 920) {
                hero.moveRight();
            }
        }else if(arg0.getKeyCode()==KeyEvent.VK_S || arg0.getKeyCode() == KeyEvent.VK_DOWN)
        {
            //向下
            this.hero.setDirect(2);
            //this.hero.moveDown();
            if (hero.getY() < 757) {
                hero.moveDown();
            }

        }else if(arg0.getKeyCode()==KeyEvent.VK_A || arg0.getKeyCode() == KeyEvent.VK_LEFT)
        {
            //向左
            this.hero.setDirect(3);
            //this.hero.moveLeft();
            if (hero.getX() > 25) {
                hero.moveLeft();
            }
        }

        if(arg0.getKeyCode()==KeyEvent.VK_J || arg0.getKeyCode() == KeyEvent.VK_SPACE)
        {
            //判断玩家是否按下j

            //开火
            //System.out.println("this.hero.ss.size()="+this.hero.ss.size());
            if(this.hero.ss.size()<=4)
            {
                this.hero.shotEnemy();
            }

        }




        //必须重新绘制Panel
        this.repaint();
    }

    public void keyReleased(KeyEvent arg0) {
        // TODO Auto-generated method stub

    }

    public void keyTyped(KeyEvent arg0) {
        // TODO Auto-generated method stub

    }

    public void run() {
        // TODO Auto-generated method stub
        //每隔100毫秒去重绘
        while(true)
        {
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
                // TODO: handle exception
            }


            this.hitEnemyTank();
            //函数，判断敌人的子弹是否击中我
            this.hitMe();


            //重绘
            this.repaint();
        }
    }
}
