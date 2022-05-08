package Tankwar;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * @author 魏志远
 * @version 1.0
 * 坦克大战的开始界面区域
 */
//就是一个提示作用
class MyStartPanel extends JPanel implements Runnable
{
    int times=0;

    public void paint(Graphics g)
    {
//        super.paint(g);
//        g.fillRect(0, 0, 800, 800);
        BufferedImage bg = null;
        try {
            bg = ImageIO.read(new File("./start-bg.jpeg"));
        }catch(Exception e) {
            e.printStackTrace();
        }

        g.drawImage(bg, 0, 0, 1000,812,null);
        //提示信息

        if(times%2==0)
        {
            g.setColor(Color.yellow);
            //开关信息的字体
            Font myFont=new Font("华文新魏",Font.BOLD,50);
            g.setFont(myFont);
            g.drawString("坦克大战-风云再起", 300, 350);
            g.drawString("开始战斗吧", 370, 420);
        }

    }

    public void run() {
        // TODO Auto-generated method stub
        while(true)
        {
            //休眠
            try {

                Thread.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
                // TODO: handle exception
            }

            times++;

            //重画
            this.repaint();
        }

    }

}
