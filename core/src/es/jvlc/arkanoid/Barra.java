package es.jvlc.arkanoid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Josev on 18/07/2015.
 */
public class Barra {

    //Parametros
    private float porcVelocidad = 0.03f;
    private float tamanyo =0.14f;
    private int vidas = 3;

    private Controlador controlador;
    private Rectangle rect;

    private float x;
    private float y;
    private TextureRegion imagen;
    private TextureRegion imagenLaser;
    private TextureRegion imagenVidas;
    private int Velocidad;
    private int ancho;
    private int alto;
    private int anchodef;
    private Nivel nivel;

    //Movimiento por puntero
    private boolean moviendoAx = false;
    private boolean tengoLaser = false;
    private boolean tengoPegamento = false;
    private int destinoX;

    //getters
    public float getX(){return x;}
    public float getY(){return y;}
    public int getWidth(){return ancho;}
    public int getHeight(){return alto;}
    public int getVidas(){return vidas;}
    public boolean TengoPegamento(){return tengoPegamento;}
    public Rectangle getRect(){return rect;}

    /** Si el jugador tiene laser, entonces dispara*/
    public void fuego(){
        if (tengoLaser){
            nivel.fuego(x, y);
        }
    }

    public void muerte()
    {
        vidas--;
        CajaDeMusica.play("muerte");
    }

    public void moveToX(int toX)
    {
        //Solo se atienden las peticiones que esten dentro de los limites del campo
        if (toX > nivel.getMargenIzquierdo() && toX < Gdx.graphics.getWidth()-nivel.getMargenDerecho())
        {
            destinoX = toX;
            moviendoAx = true;
        }
    }

    /**Recibe un String con el nuevo estado que debe asumir el jugador*/
    public void setEstado(String nuevoEstado)
    {


        if (nuevoEstado=="Largo")  {
            tengoPegamento = false;
            ancho = anchodef;
            tengoLaser = false;
            ancho = anchodef*2;
            CajaDeMusica.play("ancho");
        }

        if (nuevoEstado=="Vida"){
            vidas++;
            CajaDeMusica.play("vida");
        }

        if (nuevoEstado=="3bolas"){
            nivel.x3();
        }

        if (nuevoEstado=="Laser"){
            tengoPegamento = false;
            ancho = anchodef;
            tengoLaser = true;
        }

        if (nuevoEstado=="Normal"){
            tengoPegamento = false;
            ancho = anchodef;
            tengoLaser = false;
        }

        if (nuevoEstado=="Pegamento"){
            tengoPegamento = true;
            ancho = anchodef;
            tengoLaser = false;
        }
    }

    public void posicionInicial()
    {
        ancho = anchodef;
        tengoLaser = false;

        x = Gdx.graphics.getWidth()/2-getWidth()/2;
        y = Gdx.graphics.getHeight()*0.15f;
    }

    public void disparar()
    {
        //La barra no tiene acceso al vector de bolas, envio la orden de disparar a nivel
        nivel.disparar();
    }

    //constructor
    public Barra(Texture img, Nivel level, int vid){
        vidas = vid;
        nivel = level;
        Velocidad = (int)(Gdx.graphics.getWidth()*porcVelocidad);

        imagen = new TextureRegion(img, 2, 216, 38, 8);
        imagenVidas = new TextureRegion(img, 5, 230, 10, 4);
        imagenLaser = new TextureRegion(img,121,243, 89, 21);

        //Establezco el tamanyo inicial
        anchodef = (int)(Gdx.graphics.getWidth()*tamanyo);
        ancho = anchodef;
        alto = anchodef/4;

        //Creo el objeto que maneja el player
        controlador = new Controlador(this);

        //posicion inicial
        posicionInicial();

        rect = new Rectangle();
    }


    public void draw(Batch batch)
    {
        mover();

        if (tengoLaser)
            batch.draw(imagenLaser, x, y, ancho, alto);
        else
            batch.draw(imagen, x, y, ancho, alto);

        //Dibujo los marcadores de vidas
        for (int i=0; i<vidas; i++)
        {
                batch.draw(imagenVidas, nivel.getMargenIzquierdo()+10 + anchodef/3*i + 10, 20, anchodef/3, alto/2);
        }
        rect.x = x;
        rect.y = y;
        rect.width = ancho;
        rect.height = alto;
    }


    private void mover()
    {
        if (moviendoAx)
        {
            if (Math.abs(x + getWidth()/2 - destinoX) > Velocidad)
            {
                if (x+ getWidth()/2 > destinoX)
                    x = x - Velocidad;
                else
                    x = x + Velocidad;
            }
            else
            {
                moviendoAx = false;
            }
        }

        if (controlador.MoverDerecha() && x + ancho < nivel.limiteDerecho()){
            x = + x + Velocidad;
        }
        else
        if (controlador.MoverIzquierda() && x > nivel.limiteIzquierdo()){
            x = x - Velocidad;
        }

        rect.x = x;
        rect.y = y;
        rect.height = getHeight();
        rect.width = getWidth();
    }

}
