package es.jvlc.arkanoid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

/**
 * Created by Josev on 19/07/2015.
 */
public class Premio {

    private TextureRegion imagen;

    private float tamanyo = 0.1f;
    private int velocidad = 2;

    private float x;
    private float y;
    private Rectangle rect;
    private Nivel nivel;
    private boolean destruir=false;

    private int tipo; //Rand 0-5 -> 6

        //L - Rojo -> Laser
        //C - Verde -> pegamento
        //F - Azul -> largo
        //P - gris oscuro -> normalizar
        //S - dorado -> vida Extra
        //D - azul claro -> 3 bolas

    public Premio(Texture img, float px, float py, Nivel lvl)
    {
        velocidad = (int)(Gdx.graphics.getWidth()*0.0042f);
        nivel = lvl;
        x = px;
        y = py;
        rect = new Rectangle();
        Random rn = new Random();

        //Tomo un numero al azar de 0 a 5
        tipo = Math.abs(rn.nextInt() % 6);

        imagen = new TextureRegion(img, 88, 218 + 9*tipo, 24, 9);
    }

    public float getY(){return y;}

    public void draw(Batch batch)
    {
        y = y - velocidad;

        if (!destruir)
        {
            batch.draw(imagen, x, y, Gdx.graphics.getWidth()*tamanyo, Gdx.graphics.getHeight()*tamanyo*0.375f);
            //Actualizado el rectangulo de colisiones
            rect.x = x;
            rect.y = y;
            rect.width = Gdx.graphics.getWidth()*tamanyo;
            rect.height = Gdx.graphics.getHeight()*tamanyo*0.375f;

            //Verifico la posible colision con el jugador
            if (rect.overlaps(nivel.getJugador().getRect()))
            {
                if (tipo == 0)
                    nivel.getJugador().setEstado("Laser");
                else if (tipo == 1)
                    nivel.getJugador().setEstado("Pegamento");
                else if (tipo == 2)
                    nivel.getJugador().setEstado("Largo");
                else if (tipo == 3)
                    nivel.getJugador().setEstado("Normal");
                else if (tipo == 4)
                    nivel.getJugador().setEstado("Vida");
                else if (tipo == 5)
                    nivel.getJugador().setEstado("3bolas");
                destruir = true;
            }
        }
    }

}
