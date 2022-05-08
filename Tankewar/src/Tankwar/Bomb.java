package Tankwar;

/**
 * @author 魏志远
 * @version 1.0
 * 坦克大战的炸弹区域
 */
//炸弹类
class Bomb
{
    //定义炸弹的坐标
    int x,y;
    //炸弹的生命
    int life=9;
    boolean isLive=true;
    public Bomb(int x,int y)
    {
        this.x=x;
        this.y=y;
    }

    //减少生命值
    public void lifeDown()
    {
        if(life>0)
        {
            life--;
        }else{
            this.isLive=false;
        }

    }



}
