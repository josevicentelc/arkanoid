package es.jvlc.arkanoid;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Vector;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;

	private Nivel nivel;    	//Nivel en curso
	private int level = 0;		//Indica el numero de nivel en el que estamos
	private Vector<String> listaNiveles;
	TextureRegion fondoStart;	//Pantalla de inicio

	/**
	 * *********************************************************************************************
	 Inicializacion de componentes
	 * *********************************************************************************************
	 */

	@Override
	public void create () {

		//Precarga de los niveles
		listaNiveles = new Vector<String>();
		cargarNiveles();

		//Precarga de los sonidos
		CajaDeMusica.loadMusica("Sounds/musica1.ogg");
		CajaDeMusica.load("Sounds/ping1.ogg", "ping1");
		CajaDeMusica.load("Sounds/ping2.ogg", "ping2");
		CajaDeMusica.load("Sounds/ping3.ogg", "ping3");
		CajaDeMusica.load("Sounds/nuevoNivel.ogg", "nuevoNivel");
		CajaDeMusica.load("Sounds/hacerseGrande.ogg", "ancho");
		CajaDeMusica.load("Sounds/vidaExtra.ogg", "vida");
		CajaDeMusica.load("Sounds/muerte.ogg", "muerte");
		CajaDeMusica.load("Sounds/disparo.ogg", "disparo");

		batch = new SpriteBatch();

		//Cargo la textura que contiene todas las imagenes
		img = new Texture("sprite_arkanoid.png");

		//Imagen de fondo de la pantalla de bienvenida
		fondoStart = new TextureRegion(img, 0, 310, 218, 202);
	}

	/**
	 * *********************************************************************************************
	 Bucle principal del juego
	 * *********************************************************************************************
	 */

	@Override
	public void render (){
		batch.begin();
		//Si no hay ningun objeto nivel, se muestra la pantalla de bienvenida
			if (nivel == null){
				//bienvenida
				Gdx.gl.glClearColor( 0, 0, 0, 1 );
				Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
				batch.draw(fondoStart, 0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

				//Al tocarse la pantalla se crea un nuevo juego a partir del primer nivel
				if (Gdx.input.isTouched()){
					level = 0;
					nivel = new Nivel(img, listaNiveles.elementAt(level), level%4+1, 3, 0);
				}
			}
		else{
				//Al finalizar la partida, se destruye el nivel para que se muestre de nuevo la bienvenida
				if (nivel.isFinPartida())
					nivel = null;
				else{
					//Si se supera el nivel, se carga el siguiente nivel del vector de niveles
					if (nivel.isNivelsuperado()){
						nivel.puntuacion.incValue(10000);
						level++;
						nivel = new Nivel(		img, 							//Texturas
												listaNiveles.elementAt(level), 	//Mapa del nivel
												level%4+1, 						//Numero de fondo
												nivel.getJugador().getVidas()+1, //Vidas del jugador
												nivel.puntuacion.getPuntos());	 //Puntuacion del jugador
					}
					nivel.draw(batch);
				}
			}
		batch.end();
	}

	/**
	 * *********************************************************************************************
	 Mapas de los niveles
	 * *********************************************************************************************
	 */

	private void cargarNiveles(){
		listaNiveles.clear();
/** Nivel 1*/
		listaNiveles.addElement("000000000000011111111111112222222222222777777777777744444444444443333333333333");

/** Nivel 2*/
		String st;
		st= "6            ";
		st+="68           ";
		st+="689          ";
		st+="6893         ";
		st+="68931        ";
		st+="689317       ";
		st+="6893174      ";
		st+="68931742     ";
		st+="689317426    ";
		st+="6893174268   ";
		st+="68931742689  ";
		st+="689317426893 ";
		st+="0000000000001";
		listaNiveles.addElement(st);
/** Nivel 3*/
		st= "3333333333333";
		st+="             ";
		st+="666AAAAAAAAAA";
		st+="             ";
		st+="1111111111111";
		st+="             ";
		st+="AAAAAAAAAA666";
		st+="             ";
		st+="4444444444444";
		st+="             ";
		st+="777AAAAAAAAAA";
		st+="             ";
		st+="9999999999999";
		st+="             ";
		st+="AAAAAAAAAA999";
		listaNiveles.addElement(st);
/** Nivel 4*/
		st= " 89307 26893 ";
		st+=" 93074 68930 ";
		st+=" 30742 89307 ";
		st+=" 07426 93074 ";
		st+=" 74268 30742 ";
		st+=" 42689 07426 ";
		st+=" 26893 74268 ";
		st+=" 68930 42689 ";
		st+=" 89307 26893 ";
		st+=" 93074 68930 ";
		st+=" 30742 89307 ";
		st+=" 07426 93074 ";
		st+=" 74268 30742 ";
		st+=" 42689 07426 ";
		listaNiveles.addElement(st);
/** Nivel 5*/
		st= "   2     2   ";
		st+="   2     2   ";
		st+="    2   2    ";
		st+="    2   2    ";
		st+="   0000000   ";
		st+="   0000000   ";
		st+="  001000100  ";
		st+="  001000100  ";
		st+=" 00000000000 ";
		st+=" 00000000000 ";
		st+=" 00000000000 ";
		st+=" 0 0000000 0 ";
		st+=" 0 0     0 0 ";
		st+=" 0 0     0 0 ";
		st+="    00 00    ";
		st+="    00 00    ";
		listaNiveles.addElement(st);
/** Nivel 6*/
		st= "             ";
		st+="7 1 3 9 3 1 7";
		st+="7 1 3 9 3 1 7";
		st+="7 1 3 9 3 1 7";
		st+="7 1 3 9 3 1 7";
		st+="7 1 3 9 3 1 7";
		st+="7 A8A8A8A8A 7";
		st+="7 1 3 9 3 1 7";
		st+="7 1 3 9 3 1 7";
		st+="7 1 3 9 3 1 7";
		st+="7 1 3 9 3 1 7";
		st+="A A A A A A A";
		st+="7 1 3 9 3 1 7";

		listaNiveles.addElement(st);
/** Nivel 7*/
		st= "     224     ";
		st+="    22447    ";
		st+="   2244771   ";
		st+="   2447711   ";
		st+="  244771133  ";
		st+="  447711339  ";
		st+="  477113399  ";
		st+="  771133998  ";
		st+="  711339988  ";
		st+="  113399886  ";
		st+="   3399886   ";
		st+="   3998866   ";
		st+="    98866    ";
		st+="     866     ";
		listaNiveles.addElement(st);
/** Nivel 8*/
		st= "   A A A A   ";
		st+=" A         A ";
		st+=" AA A   A AA ";
		st+="      0      ";
		st+=" A   A1A   A ";
		st+="   A  2  A   ";
		st+="      3      ";
		st+="   A  4  A   ";
		st+=" A   A5A   A ";
		st+="      6      ";
		st+=" AA A   A AA ";
		st+=" A         A ";
		st+="   A A A A   ";
		listaNiveles.addElement(st);
/** Nivel 9*/
		st= " A A     A A ";
		st+=" A2A     A1A ";
		st+=" A1A     A2A ";
		st+=" AAA     AAA ";
		st+="             ";
		st+="    12223    ";
		st+="    14443    ";
		st+="    15553    ";
		st+="    16663    ";
		st+="    17773    ";
		st+="    18883    ";
		listaNiveles.addElement(st);
/** Nivel 10*/
		st= " A           ";
		st+="             ";
		st+=" A           ";
		st+=" A           ";
		st+=" A     3     ";
		st+=" A    343    ";
		st+=" A   34543   ";
		st+=" A  3456543  ";
		st+=" A 345676543 ";
		st+=" A  3456543  ";
		st+=" A   34543   ";
		st+=" A    343    ";
		st+=" A     3     ";
		st+=" A           ";
		st+=" A           ";
		st+=" AAAAAAAAAAAA";
		listaNiveles.addElement(st);

		listaNiveles.addElement("000000000000011111111111112222222222222777777777777744444444444443333333333333");
		listaNiveles.addElement("000000000000011111111111112222222222222777777777777744444444444443333333333333");
		listaNiveles.addElement("000000000000011111111111112222222222222777777777777744444444444443333333333333");

	}
}
