package es.jvlc.arkanoid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import org.w3c.dom.css.Rect;

/**
 * Created by Josev on 20/07/2015.
 */
public class Laser {

    private float velocidad = 9;
    private Rectangle rect;
    private float x;
    private float y;
    private TextureRegion imagen;
    private Nivel nivel;
    private int alto;
    private int ancho;
    private boolean destruir = false;

    public boolean Destruir(){return destruir;}

    public Laser(Texture img, float posx, float posy, Nivel level){

        velocidad = Gdx.graphics.getWidth()*0.01875f;
        imagen = new TextureRegion(img,140, 222, 51, 17);
        nivel = level;
        x = posx+nivel.getJugador().getWidth()/4;   //posicion del jugador mas 1/4 de su anchura
        y = posy + nivel.getJugador().getHeight();  //posicion del jugador mas su altura
        alto = nivel.getJugador().getHeight();
        ancho = nivel.getJugador().getWidth()/2;
        rect = new Rectangle();
    }

    public float getY(){return y;}

    public Rectangle getRect(){return rect;}

    public void draw(Batch batch){
        y = y + velocidad;
        batch.draw(imagen, x, y, ancho, alto);
        rect.x = x;
        rect.y = y;
        rect.height = alto;
        rect.width = ancho;

        //busco colisiones con ladrillos
        for (int i=nivel.getLadrillos().size()-1;i>=0 ;i--){

            if (    nivel.getLadrillos().elementAt(i).getRect().overlaps(rect)  //Si colisiona con un ladrillo
                    &&
                    !nivel.getLadrillos().elementAt(i).isIndestructible()){     //y no es indestructible

                destruir = true;                                                //El rayo muere
                nivel.generaPremio(                                             //Se genera un premio en la posicion del ladrillo
                        nivel.getLadrillos().elementAt(i).getRect().getX(),
                        nivel.getLadrillos().elementAt(i).getRect().getY());
                nivel.getLadrillos().removeElementAt(i);                        //El ladrillo muere
            }
        }
    }
}
