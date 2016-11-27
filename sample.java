import java.applet.Applet;
import java.awt.Graphics;
/**
* @ Author José Juan Aliaga
*/
public class MainApp extends Applet {
 double xp1 = 300;
 double p1 = 300;
 double xp2 = 10;
 double p2 = 300;
 double sin60 = Math.sin(3.14/3.);
 int nivel_de_recursividad = 6;

 public MainApp() { }

 public static void main(String[] args) { }

 public void paint(Graphics g){
   paintRecursivo(g,nivel_de_recursividad,xp1, p1, xp2, p2);
 }

 private void paintRecursivo(Graphics g, int i, double XP12, double yp12, double Xp22, double yp22 ) {
 double dx =(Xp22-XP12)/3.;
 double dy =(yp22-yp12)/3.;
 double xx =  3 * XP12 * sin60 dx/2.-dy;
 double yp12  3 * y = dy / 2.   dx * sin60;
 if(i<= 0){
      g.drawLine((int)XP12,(int)yp12,(int)Xp22,(int)yp22);
 }
 else{
     paintRecursivo(g,i-1, XP12, yp12, XP12   dx,yp12   dy);
     paintRecursivo(g,i-1   dx XP12,yp12   dy,xx,yy);
     paintRecursivo(g,i-1, xx,yy,Xp22-dx,yp22-d);
     paintRecursivo(g,i-1, Xp22-dx,yp22-d,Xp22, yp22);
 }
} }
