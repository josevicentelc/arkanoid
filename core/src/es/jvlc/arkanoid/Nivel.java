package es.jvlc.arkanoid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Random;
import java.util.Vector;

/**
 * Created by Josev on 18/07/2015.
 */
public class Nivel {

    private int margenIzquierdo = 0;
    private int margenDerecho = 0;

    private TextureRegion fondoNivel;
    private Texture texturas;
    private Vector<Ladrillo> ladrillos;
    private Vector<Pelota> pelotas;
    private Vector<Premio> premios;
    private Vector<Laser> lasers;
    private Barra jugador;
    private boolean finPartida = false;
    private boolean nivelsuperado = false;
    public Puntuacion puntuacion;

    public Vector<Ladrillo> getLadrillos(){return ladrillos;}
    public Barra getJugador(){return jugador;}
    public boolean isFinPartida(){return finPartida;}
    public int getMargenIzquierdo(){return margenIzquierdo;};
    public int getMargenDerecho(){return margenDerecho;};
    public int limiteIzquierdo(){return margenIzquierdo;}
    public int limiteDerecho(){return Gdx.graphics.getWidth() - margenDerecho;}
    public boolean isNivelsuperado(){return nivelsuperado;}

    /** Retorna la y que limita el campo por arriba, esto es un 11% del tamanyo de la pantalla*/
    public int limiteSuperior(){return (int)(Gdx.graphics.getHeight()-Gdx.graphics.getHeight()*0.11f);}

    /**Constructor del nivel
     *
     * Recibe en un string la cadena de ladrillos que debe colocar y los genera de forma dinamica
     * Tambien inicializa el jugador y la pelota*/
    public Nivel(Texture img, String cadenaDeCuadros, int idFondo, int vidasIniciales, int puntosIniaciales)
    {
        margenIzquierdo = (int)(Gdx.graphics.getWidth()*0.038f);
        margenDerecho=margenIzquierdo;

        //Cargo una imagen de fondo por defecto, si recibo un parametro no valido, esta sera la imagen que se carara
        fondoNivel = new TextureRegion(img, 0, 0, 180, 210);
        if (idFondo==1) fondoNivel = new TextureRegion(img, 0, 0, 180, 210);
        if (idFondo==2) fondoNivel = new TextureRegion(img, 186, 0, 180, 210);
        if (idFondo==3) fondoNivel = new TextureRegion(img, 375, 0, 180, 210);
        if (idFondo==4) fondoNivel = new TextureRegion(img, 564, 0, 180, 210);


        texturas = img;
        ladrillos = new Vector<Ladrillo>();
        pelotas = new Vector<Pelota>();
        premios = new Vector<Premio>();
        lasers = new Vector<Laser>();
        puntuacion = new Puntuacion(img, 40, Gdx.graphics.getHeight() - 40, this, 4, puntosIniaciales);
        puntuacion.alignIzquierda();

        for (int i=0; i<cadenaDeCuadros.length();i++)
        {
            //Destruibles
            if (cadenaDeCuadros.charAt(i)=='0') ladrillos.addElement(new Ladrillo(texturas,i, 0, false, this));
            if (cadenaDeCuadros.charAt(i)=='1') ladrillos.addElement(new Ladrillo(texturas,i, 1, false, this));
            if (cadenaDeCuadros.charAt(i)=='2') ladrillos.addElement(new Ladrillo(texturas,i, 2, false, this));
            if (cadenaDeCuadros.charAt(i)=='3') ladrillos.addElement(new Ladrillo(texturas,i, 3, false, this));
            if (cadenaDeCuadros.charAt(i)=='4') ladrillos.addElement(new Ladrillo(texturas,i, 4, false, this));
            if (cadenaDeCuadros.charAt(i)=='5') ladrillos.addElement(new Ladrillo(texturas,i, 5, false, this));
            if (cadenaDeCuadros.charAt(i)=='6') ladrillos.addElement(new Ladrillo(texturas,i, 6, false, this));
            if (cadenaDeCuadros.charAt(i)=='7') ladrillos.addElement(new Ladrillo(texturas,i, 7, false, this));
            if (cadenaDeCuadros.charAt(i)=='8') ladrillos.addElement(new Ladrillo(texturas,i ,8, false, this));
            if (cadenaDeCuadros.charAt(i)=='9') ladrillos.addElement(new Ladrillo(texturas,i ,9, false, this));
            //Indestructibles
            if (cadenaDeCuadros.charAt(i)=='A') ladrillos.addElement(new Ladrillo(texturas,i ,8, true, this));
        }

        jugador = new Barra(texturas, this, vidasIniciales);
        posicionInicial();
        CajaDeMusica.play("nuevoNivel");
    }

    /**Metodo que recibe las coordenadas donde se acaba de destruir un cuadro, y con una probabilidad de 1/10
     * genera un nuevo premio en esas coordenadas*/
    public void generaPremio(float px, float py)
    {
        Random rn = new Random();
        if  ( rn.nextInt()%5 == 0){
            premios.addElement(new Premio(texturas, px, py, this));
        }
    }

    /** Nuevo laser, se me pasan las coordenadas del jugador para iniciar en ese punto*/
    public void fuego(float posx, float posy){
            lasers.addElement(new Laser(texturas, posx, posy, this));
            CajaDeMusica.play("disparo");
    }

    /** pone todos los elementos de la partida en la posicion inicial, sin regenerar los ladrillos*/
    public void posicionInicial()
    {
        CajaDeMusica.stopMusica();
        jugador.posicionInicial();
        destruirPelotas();
        nuevaPelota();
        CajaDeMusica.playMusica();
    }

    /** Dispara una de las varias bolar que jugador tenga afheridas*/
    public void disparar()
    {
        //Se me ordena disparar una bola, recorro el vector de bolas hasta encontrar una
        //que este pegada al jugador y la disparo

        boolean salir = false; //cuando se dispara una bola esto se pone a true para salir del bucle

        for (int i = 0; i < pelotas.size() && !salir; i++)
        {
            if (pelotas.elementAt(i).pegada())
            {
                salir = true;
                pelotas.elementAt(i).disparar();
            }
        }
    }

    /**Borra todas las pelotas del tablero*/
    public void destruirPelotas() {
        pelotas.clear();
    }

    /** Crea una nueva pelota que comienza estando pegada al jugador*/
    public void nuevaPelota()
    {
        pelotas.addElement(new Pelota(jugador, texturas, this));
    }

    /**Metodo que dibuja una secuencia del juego llamando a su vez a draw de los elementos de la partida*/
    public void draw(Batch batch)
    {
        //Antes de hacer nada, cuento el numero de ladrillos pendientes de destruir
        //si es 0, el nivel esta superado
        int cuenta =0;
        for (int i=0; i < ladrillos.size();i++)
            if (ladrillos.elementAt(i).isIndestructible()==false)
                cuenta++;

        if (cuenta == 0)    nivelsuperado = true;

        batch.draw(fondoNivel, 0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        for (int i=0; i<ladrillos.size();i++)
        {
            ladrillos.elementAt(i).draw(batch);
        }
        jugador.draw(batch);
        //Me cepillo cualquier pelota que alcance el limite inferior
        for (int i=0; i<pelotas.size();i++)
            if (pelotas.elementAt(i).getY()<=0)
                pelotas.removeElementAt(i);

        //si no quedan, el jugador muere
        if (pelotas.size()==0)  muerteDeJugador();

        for (int i=0; i<pelotas.size();i++)
        {
            pelotas.elementAt(i).draw(batch);
        }

        //Dibujo los premios
        for (int i=premios.size()-1;i>=0;i--){
            if (premios.elementAt(i).getY()<=0){
                premios.removeElementAt(i);
            }
            else{
                premios.elementAt(i).draw(batch);
            }
        }

        //Dibujo los lasers
        for (int i=lasers.size()-1;i>=0;i--){
            if (lasers.elementAt(i).getY()>limiteSuperior() || lasers.elementAt(i).Destruir()) {
                lasers.removeElementAt(i);
            }
            else{
                lasers.elementAt(i).draw(batch);
            }
        }

        //dibujo la puntuacion
        puntuacion.draw(batch);
    }

    /** Se le indica al jugador que ha muerto
     *
     * A continuacion se comprueba si quedan vidas para continuar o dar la partida por finalizada*/
    public void muerteDeJugador(){
        //Se le indica al jugador que ha muerto para que haga sus sonidos, lucecitas y reste una vida
        jugador.muerte();

        //compruebo si quedan vidas y si debo seguir jugando o salir de la partida
        if (jugador.getVidas()<0){
            gameOver();
        }
        else{
            posicionInicial();
        }
    }

    /** Se da la partida por finalizada*/
    private void gameOver()
    {
        /** Se vacian los vectores*/
        pelotas.clear();
        premios.clear();
        lasers.clear();
        ladrillos.clear();
        /** Se detiene la musica y se finaliza la partida*/
        CajaDeMusica.stopMusica();
        finPartida = true;
    }

    /** coge la primera bola del vector, la elimina y en su lugar pone 3 bolas en diferentes angulos*/
    public void x3(){
        //Si no hay bolas o si la bola esta pegada al jugador, no se hace nada
        if (pelotas.size()>0 && pelotas.elementAt(0).pegada() == false)
        {
            Pelota p = pelotas.elementAt(0);    //cojo la primera pelota del vector
            if (p.getSectorDireccion()==1){     //Si va arriba y a la izquierda
                pelotas.addElement(new Pelota(texturas, p.getX(), p.getY(), p.getModulo(), 1, this));
                pelotas.addElement(new Pelota(texturas, p.getX(), p.getY(), p.getModulo(), 2, this));
                pelotas.addElement(new Pelota(texturas, p.getX(), p.getY(), p.getModulo(), 3, this));
            }
            else
            if (p.getSectorDireccion()==2){     //Si va arriba y a la derecha
                pelotas.addElement(new Pelota(texturas, p.getX(), p.getY(), p.getModulo(), 4, this));
                pelotas.addElement(new Pelota(texturas, p.getX(), p.getY(), p.getModulo(), 5, this));
                pelotas.addElement(new Pelota(texturas, p.getX(), p.getY(), p.getModulo(), 6, this));
            }
            else
            if (p.getSectorDireccion()==3){     //Si va abajo a la izquierda
                pelotas.addElement(new Pelota(texturas, p.getX(), p.getY(), p.getModulo(), 7, this));
                pelotas.addElement(new Pelota(texturas, p.getX(), p.getY(), p.getModulo(), 8, this));
                pelotas.addElement(new Pelota(texturas, p.getX(), p.getY(), p.getModulo(), 9, this));
            }
            else
            if (p.getSectorDireccion()==4){     //Si va a abajo y a la derecha
                pelotas.addElement(new Pelota(texturas, p.getX(), p.getY(), p.getModulo(), 10, this));
                pelotas.addElement(new Pelota(texturas, p.getX(), p.getY(), p.getModulo(), 11, this));
                pelotas.addElement(new Pelota(texturas, p.getX(), p.getY(), p.getModulo(), 12, this));
            }
            else

            pelotas.removeElementAt(0);
        }
    }
}

