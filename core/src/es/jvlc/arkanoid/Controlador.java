package es.jvlc.arkanoid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

/**
 * Created by Josev on 18/07/2015.
 */
public class Controlador extends InputAdapter {

    private Barra barra;

    private float lastx;
    private float lasty;

    private boolean derecha = false;
    private boolean izquierda = false;

    public boolean MoverDerecha() {
        return derecha;
    }

    public boolean MoverIzquierda() {
        return izquierda;
    }


    public Controlador(Barra bar) {
        barra = bar;
        lastx = barra.getX();
        lasty = 0;
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        barra.moveToX(screenX);
        barra.fuego();          //Dispara, solo disparara si tiene laser
        lasty = screenY;
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        barra.moveToX(screenX);

        //Si se produce un movimiento rapido hacia arriba, se ordena disparar
        if (Math.abs(screenY - lasty) > 10 && screenY < lasty) barra.disparar();

        lasty = screenY;
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {

        if (keycode == Input.Keys.LEFT) {
            izquierda = true;
        } else {
            if (keycode == Input.Keys.RIGHT) {
                derecha = true;
            }
            else
                if (keycode==Input.Keys.UP){
                    barra.disparar();
                }
            else
                    if (keycode==Input.Keys.SPACE){
                        barra.fuego();
                    }
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {

        if (keycode == Input.Keys.LEFT) {
            izquierda = false;
        } else {
            if (keycode == Input.Keys.RIGHT) {
                derecha = false;
            }
        }
        return true;
    }
}
