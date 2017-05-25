package es.jvlc.arkanoid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Josev on 18/07/2015.
 */
public class Pelota {

    //Valor por defecto de cada cuantos rebotes acelera la pelota
    private int rebotesParaAcelerarDef=20;
    //Contador de cuantos rebotes lleva hasta acelerar
    private int rebotesParaAcelerar=0;

    private TextureRegion imagen;

    private Rectangle rect;

    private float tamanyo = 0.03f;

    private float x;    //Posicion en X

    private float y;    //Posicion en Y

    private float Vx;   //Velocidad en X

    private float Vy;   //Velocidad en Y

    private Nivel nivel;

    private Barra siguiendoBarra;

    private int desviacionX = 0;    //Cuando la pelota esta pegada a una barra, indica cuantos pixels se desvia del centro

    public Rectangle getRect(){return rect;}

    public float getY(){return y;}
    public float getX(){return x;}

    private void incVelocidad(){
        Vx = Vx*1.1f;
        Vy = Vy*1.1f;
    }

    /**Retorna el numero de cuadrante en que va el modulo de la velocidad de la pelota*/
    public int getSectorDireccion()
    {
        if (Vx<0 && Vy >0)  return 1;
        if (Vx>0 && Vy >0)  return 2;
        if (Vx<0 && Vy <0)  return 3;
        if (Vx>0 && Vy <0)  return 4;

        return 0;    }

    /**Retorna el modulo de la velocidad de la pelota*/
    public float getModulo(){
        return (float)Math.sqrt(Vx*Vx+Vy*Vy);
    }

    public Pelota(Texture img, float px, float py, float velocidad, int angulo, Nivel level)
    {
        nivel = level;
        imagen = new TextureRegion(img, 74, 217, 8 ,8);
        siguiendoBarra = null;
        rect = new Rectangle();
        x = px;
        y = py;

        //Angulos arriba izquierda
        if (angulo==1){            Vx = -0.92f*velocidad;            Vy = 0.39F*velocidad;        }
        if (angulo==2){            Vx = -0.71f*velocidad;            Vy = 0.71F*velocidad;        }
        if (angulo==3){            Vx = -0.37f*velocidad;            Vy = 0.92F*velocidad;        }
        //Angulos arriba derecha
        if (angulo==4){            Vx = 0.92f*velocidad;            Vy = 0.39F*velocidad;        }
        if (angulo==5){            Vx = 0.71f*velocidad;            Vy = 0.71F*velocidad;        }
        if (angulo==6){            Vx = 0.37f*velocidad;            Vy = 0.92F*velocidad;        }
        //Angulos abajo izquierda
        if (angulo==7){            Vx = -0.92f*velocidad;            Vy = -0.39F*velocidad;        }
        if (angulo==8){            Vx = -0.71f*velocidad;            Vy = -0.71F*velocidad;        }
        if (angulo==9){            Vx = -0.37f*velocidad;            Vy = -0.92F*velocidad;        }
        //Angulos abajo derecha
        if (angulo==10){           Vx = 0.92f*velocidad;            Vy = -0.39F*velocidad;        }
        if (angulo==11){           Vx = 0.71f*velocidad;            Vy = -0.71F*velocidad;        }
        if (angulo==12){            Vx = 0.37f*velocidad;            Vy = -0.92F*velocidad;        }
    }

    /** Constructor de la Barra, la pelota comienza estando pegada al jugador*/
    public Pelota(Barra barra, Texture img, Nivel level){
        nivel = level;
        imagen = new TextureRegion(img, 74, 217, 8 ,8);
        siguiendoBarra = barra;
        rect = new Rectangle();
    }

    /** Motodo que mueve un ciclo la pelota y a continuacion la dibuja en pantalla*/
    public void draw(Batch batch){
        mover();
        batch.draw(imagen, x, y, Radio()*2, Radio()*2);
    }

    /** Retorna true si la pelota esta pegada al jugador*/
    public boolean pegada(){
        return !(siguiendoBarra==null);
    }

    /** Nos da el radio de la pelota, es un valor dinamico que depende de la pantalla*/
    private int Radio(){
        return (int)(Gdx.graphics.getWidth()*tamanyo/2);
    }

    /** Si la bola esta pegada al jugador, la dispara*/
    public void disparar(){
        if (pegada()){
            siguiendoBarra = null;

            //calculo la velocidad de la pelota en funcion del tamanyo de la pantalla
            Vx = Gdx.graphics.getWidth()*0.0104f;
            Vy = Gdx.graphics.getWidth()*0.0104f;

            //Elevo ligeramente la pelota (su altura) para que no se vuelva a quedar pegada al jugador si este tiene pegamento
            y = y +Radio()*2;

            calcularReboteJugador();
        }
    }

    /** ordena a la pelota que se mueva segun corresponda en un ciclo*/
    private void mover(){

        if (siguiendoBarra != null){ //Si la pelota esta pegada al jugador...

            //La posicion en x es la central de la barra mas la desviacion
            x = siguiendoBarra.getX()+siguiendoBarra.getWidth()/2 - Radio() + desviacionX;
            y = siguiendoBarra.getY() + siguiendoBarra.getHeight()/2;
        }
        else    //Si la pelota es libre
        {
            //mover la pelota segun su velocidad
            x = x + Vx;
            y = y + Vy;

            //Rebote contra la pared izquierda
            if (x < nivel.limiteIzquierdo() && Vx < 0 ){
                CajaDeMusica.play("ping1");
                nivel.puntuacion.incValue(-2);
                Vx = Vx * -1;
                rebotesParaAcelerar++;
            }

            //Rebote contra la pared derecha
            if (x >nivel.limiteDerecho() && Vx > 0 ){
                nivel.puntuacion.incValue(-2);
                CajaDeMusica.play("ping1");
                Vx = Vx * -1;
                rebotesParaAcelerar++;
            }

            //Rebote contra el techo
            if (y > nivel.limiteSuperior() && Vy > 0){
                CajaDeMusica.play("ping1");
                nivel.puntuacion.incValue(-2);
                Vy = Vy  *-1;
                rebotesParaAcelerar++;
            }

            //Rebote contra el jugador
            if (rect.overlaps(nivel.getJugador().getRect())){
                //Me aseguro de que la bola vuelve hacia arriba

                nivel.puntuacion.incValue(5);

                //Si el jugador tiene pegamento, se quedara adherida
                if (nivel.getJugador().TengoPegamento()){
                    siguiendoBarra = nivel.getJugador();

                    //Calculo los pixels de difrencia entre el centro del jugador y el centro de la pelota y los recuerdo
                    desviacionX = (int)(rect.x+rect.getWidth()/2 - (nivel.getJugador().getRect().x+nivel.getJugador().getRect().getWidth()/2));
                    System.out.println(desviacionX);
                }
                else{   //Si no, pues rebota
                    calcularReboteJugador();
                    CajaDeMusica.play("ping2");
                }
            }

            /**Rebote contra un ladrillo,
            Recorro todos los ladrillos del nivel*/

            for (int i=0; i<nivel.getLadrillos().size(); i++)
            {
                //Encuentro un ladrillo con el que se colisiona
                if (rect.overlaps(nivel.getLadrillos().elementAt(i).getRect()))
                {
                    //Almaceno el ladrillo por comodidad
                    Ladrillo ladrillo = nivel.getLadrillos().elementAt(i);

                    /** Ahora dependiendo de los valores de x e y determino la direccion del rebote
                     * Las verificaciones de la direccion de rebote se realizan de forma exclusiva, es decir
                     * no puede realizar dos rebotes en el mismo ciclo, asi se evitan ciclos de rebotes infinitos
                     */

                    float xcentral = x + Radio();
                    float ycentral = y + Radio();
                    float absVx = Math.abs(Vx);
                    float absVy = Math.abs(Vy);


                    //Si el punto central de la bola es inferior a la coordenada y del cuadro, rebota hacia abajo
                    if (ycentral - absVy < ladrillo.getRect().getY()){
                        if (Vy > 0) Vy = Vy*-1; //Si va hacia arriba, le doy la vuelta
                    }
                    else //Si el punto central de la bola es superior al superior del ladrillo, rebota hacia arriba

                    if (ycentral + absVy > ladrillo.getRect().getY() + ladrillo.getRect().getHeight()) {
                        if (Vy < 0) Vy = Vy*-1; //Si va hacia abajo, le doy la vuelta
                    }
                    else //Si el punto central de la bola esta a la izquierda de la X del ladrillo, rebote a la izquierda

                    if (xcentral -absVx < ladrillo.getRect().getX()){
                        if (Vx > 0) Vx = Vx*-1; //Si va hacia la derecha, le hago ir a la izquierda
                    }
                    else //Si el punto central de la bola esta a la derecha del ladrillo, rebote a la derecha

                    if (xcentral + absVx> ladrillo.getRect().getX() + ladrillo.getRect().getWidth()){
                        if (Vx < 0) Vx = Vx*-1; //Si va hacia la izquierda, le hago ir a la derecha
                    }

                    //Si no es indestructible, lo elimino
                    if (!nivel.getLadrillos().elementAt(i).isIndestructible()){
                        nivel.getLadrillos().removeElementAt(i);
                        CajaDeMusica.play("ping1");
                        nivel.puntuacion.incValue(20);

                        nivel.generaPremio(x, y);
                    }
                    else
                    {
                        CajaDeMusica.play("ping3");
                        nivel.puntuacion.incValue(10);
                        rebotesParaAcelerar++;
                    }
                }
            }
        }

        /** Ha rebotado suficientes veces como para tener que acelerar, vuelvo a poner el contador a 0 e incremento la velocidad*/

        if (rebotesParaAcelerar>= rebotesParaAcelerarDef){
            rebotesParaAcelerar=0;

            //TODO Si la pelota acelera mucho, sobrepasa el margen de colision y atraviesa los objetos
            //de momento se anula la aceleracion

            //incVelocidad();
        }

        //Despues de mover la bola, actualizo el Rectangle que determina la colision
        rect.x = x;
        rect.y = y;
        rect.height = Radio()*2;
        rect.width  = Radio()*2;
    }


    private void calcularReboteJugador()
    {
        //obtengo el modulo de la velocidad
        float M = (float)Math.sqrt(Vy*Vy + Vx*Vx);

        //La barra del jugador se divide en 8 regiones con distintos angulos de rebote siendo sus angulos
        // 157 135 112 90 90 68 45 23

        int tamSector = nivel.getJugador().getWidth()/8;

        //157
        if (x+Radio() >= nivel.getJugador().getX() && x+Radio()< nivel.getJugador().getX()+tamSector){
            Vx = -0.92f*M;
            Vy = 0.39F*M;
        }
        //135
        if (x+Radio() >= nivel.getJugador().getX()+tamSector && x+Radio()< nivel.getJugador().getX()+tamSector*2){
            Vx = -0.71f*M;
            Vy = 0.71F*M;
        }
        //112
        if (x+Radio() >= nivel.getJugador().getX()+tamSector*2 && x+Radio()< nivel.getJugador().getX()+tamSector*3){
            Vx = -0.37f*M;
            Vy = 0.92F*M;
        }
        //100
        if (x+Radio() >= nivel.getJugador().getX()+tamSector*3 && x+Radio()< nivel.getJugador().getX()+tamSector*4){
            Vx = -0.1736f*M;
            Vy = 0.9848f*M;
        }
        //80
        if (x+Radio() >= nivel.getJugador().getX()+tamSector*4 && x+Radio()< nivel.getJugador().getX()+tamSector*5){
            Vx = 0.1736f*M;
            Vy = 0.9848f*M;
        }
        //68
        if (x+Radio() >= nivel.getJugador().getX()+tamSector*5 && x+Radio()< nivel.getJugador().getX()+tamSector*6){
            Vx = 0.37f*M;
            Vy = 0.92F*M;
        }
        //45
        if (x+Radio() >= nivel.getJugador().getX()+tamSector*6 && x+Radio()< nivel.getJugador().getX()+tamSector*7){
            Vx = 0.71f*M;
            Vy = 0.71F*M;
        }
        //23
        if (x+Radio() >= nivel.getJugador().getX()+tamSector*7 && x+Radio()< nivel.getJugador().getX()+tamSector*8){
            Vx = 0.92f*M;
            Vy = 0.39F*M;
        }
    }

}
