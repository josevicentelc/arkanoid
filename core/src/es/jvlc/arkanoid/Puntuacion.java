package es.jvlc.arkanoid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Vector;

/**
 * Created by Josev on 21/07/2015.
 */
public class Puntuacion {

    private TextureRegion numeros[];  //Vector de 10 posiciones con las 10 imagenes de los numeros
    private float x;    //posicion x
    private float y;    //posicion y


    private float tamanyo = 0;  //Porcentaje del ancho de la pantalla que ocupara cada caracter
    private int puntos = 0;

    private String alineacion;

    /** Metodos que modifican la alineacion del texto respecto de la coordenada x*/
    public void alignIzquierda(){alineacion="izquierda";}
    public void alignDerecha(){alineacion="derecha";}
    public void alignCentro(){alineacion="centro";}

    /** Establece el tamanyo de la fuente en un % de la pantalla*/
    public void setSize(int n){
        if (n>=1 && n <= 100){
            tamanyo = n * 0.01f;
        }
    }

    /** Constructor por defecto, acarga las texturas en un array e inicializa el valor a 0*/
    public Puntuacion(Texture img, float posx, float posy, Nivel lvl, int tam, int punt){

        alineacion = "izquierda";
        x = posx;
        y = posy;
        puntos = punt;

        tamanyo = tam * 0.01f;  //Almaceno el tamanyo como %

        numeros = new TextureRegion[10];
        numeros[0] = new TextureRegion(img, 249, 232, 26, 35);
        numeros[1] = new TextureRegion(img, 276, 232, 26, 35);
        numeros[2] = new TextureRegion(img, 303, 232, 26, 35);
        numeros[3] = new TextureRegion(img, 330, 232, 26, 35);
        numeros[4] = new TextureRegion(img, 357, 232, 26, 35);
        numeros[5] = new TextureRegion(img, 384, 232, 26, 35);
        numeros[6] = new TextureRegion(img, 411, 232, 26, 35);
        numeros[7] = new TextureRegion(img, 438, 232, 26, 35);
        numeros[8] = new TextureRegion(img, 465, 232, 26, 35);
        numeros[9] = new TextureRegion(img, 492, 232, 26, 35);
    }

    /** Incrementa el valor de la puntuacion en el valor pasado por parametro*/
    public void incValue(int valor){
        puntos = puntos + valor;
    }

    /** Establece como puntuacion el valor absoluto pasado por parametro*/
    public void setValue(int valor){
        puntos = valor;
    }

    /** Getter de la puntuacion*/
    public int getPuntos(){return puntos;}

    /** Dibuja en pantalla la puntuacion*/
    public void draw(Batch batch){
        int p = puntos;


        //Vector para ir almacenando las imagenes que formaran la imagen total
        Vector<TextureRegion> temporal;
        temporal = new Vector<TextureRegion>();

        //Añadimos todas als imagenes al vector
        //El numero mas a la derecha (unidad) ocupa la posicion 0
        //El siguiente (decena) la posicion 1
        while (p>0){
            int numAdibujar = p - (p / 10)*10;
            p = p /10;
            temporal.addElement(numeros[numAdibujar]);
        }

        //Si la puntuacion era 0, no se ha anyadido nada, asi que pongo un 0 para que se muestre
        if (temporal.size()==0) temporal.addElement(numeros[0]);

        //Calculo el ancho total del texto para poder calcular el punto de partida segun la alineacion
        float anchoTotal = Gdx.graphics.getWidth() * tamanyo * temporal.size();

        //Calculo el punto de partida segun la alineacion
        float xInicio = 0;
        if (alineacion=="centro")
            xInicio = x - anchoTotal/2;
        else
            if (alineacion == "izquierda")
                xInicio = x;
        else
                if (alineacion=="derecha")
                    xInicio=xInicio-anchoTotal;

        //Ahora recorro todas las posiciones del vector de la ultima hacia el principio dibujando en pantalla
        for (int i = temporal.size()-1;i>=0;i--){
            batch.draw(temporal.elementAt(i), xInicio, y, Gdx.graphics.getWidth() * tamanyo, Gdx.graphics.getHeight() * tamanyo);
            xInicio = xInicio +Gdx.graphics.getWidth() * tamanyo;
        }



        temporal.clear();


    }


}
