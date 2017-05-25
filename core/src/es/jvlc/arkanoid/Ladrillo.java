package es.jvlc.arkanoid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Josev on 18/07/2015.
 */
public class Ladrillo {

    static int ladrillosPorFila = 13;
    static float ancho = 0;
    static float alto = 0;

    private Rectangle rect;

    private TextureRegion imgLadrillo;
    private TextureRegion imagen;
    private float posX;
    private float posY;
    private boolean indestructible;
    private int vida;
    private Nivel nivel;

    public Rectangle getRect(){return rect;}

    public boolean isIndestructible(){return indestructible;}

    public Ladrillo(Texture texturas, int numero, int color, boolean indes, Nivel level)
    {
        nivel = level;


        if (imgLadrillo==null)
            imgLadrillo = new TextureRegion(texturas, 50, 217+6*color,12,6);


        if (ancho==0 || alto ==0)
        {
            //Calculo el tamanyo que deben tener los ladrillos, esto adecta a todos los ladrillos
            ancho = (Gdx.graphics.getWidth()-nivel.getMargenDerecho()-nivel.getMargenIzquierdo())/ladrillosPorFila;
            alto=Gdx.graphics.getHeight()/30;
        }

        //Calculo la posicion del ladrillo, la fila
        posY= Gdx.graphics.getHeight() - (alto*5) - alto * (numero/ladrillosPorFila);

        //Calcullo la posicion del ladrillo en X, la columna
        posX = nivel.getMargenIzquierdo() + ancho* (numero%ladrillosPorFila);

        //establezco el valor de indestructible segun parametros
        indestructible = indes;

        vida = 1;

        rect = new Rectangle();
        rect.x = posX;
        rect.y = posY;
        rect.height = alto;
        rect.width  = ancho;
    }

    public void draw(Batch batch)
    {
        batch.draw(imgLadrillo, posX, posY, ancho, alto);
    }

}
