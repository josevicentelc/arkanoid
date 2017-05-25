package es.jvlc.arkanoid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;

/**
 * Created by Josev on 03/07/2015.
 */
public class CajaDeMusica {
    private static HashMap<String, Sound> sonidos;

    private static Music musica;

    private static float volumen = 0.3f;

    static {
        sonidos = new HashMap<String, Sound>();
    }

    public static void loadMusica(String path)
    {
        musica = Gdx.audio.newMusic(Gdx.files.internal(path));
    }

    public static void playMusica()
    {
        musica.setLooping(true);
        musica.setVolume(volumen);
        musica.play();
    };

    public static void load(String path, String nombre)
    {
        Sound sonido = Gdx.audio.newSound(Gdx.files.internal(path));
        sonidos.put(nombre, sonido);
    }

    public static void play(String nombre)

    {
        sonidos.get(nombre).play();
    }

    public static void loop(String nombre)

    {
        sonidos.get(nombre).loop();
    }

    public static void stop(String nombre)

    {
        sonidos.get(nombre).stop();
    }

    public static void stopAll()
    {
        for (Sound s : sonidos.values())
        {
            s.stop();
        }
    }

    public static void stopMusica()
    {
        musica.stop();
    }


}
