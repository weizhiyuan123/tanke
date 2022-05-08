package Tankwar;

/**
 * @author 魏志远
 * @version 1.0
 * 坦克大战的父类坦克
 */

//坦克类
class Tank
{
    //表示坦克的横坐标
    int x=0;
    //坦克纵坐标
    int y=0;

    //坦克方向
    //0表示上 1表示 右 2表示下  3表示左
    int direct=0;
    int color;

    boolean isLive=true;

    //坦克的速度
    int speed=6;
    public Tank(int x,int y)
    {
        this.x=x;
        this.y=y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getDirect() {
        return direct;
    }

    public void setDirect(int direct) {
        this.direct = direct;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

}
