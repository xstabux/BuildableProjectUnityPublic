package unity.graphics.menu;

import arc.*;
import arc.graphics.Blending;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.graphics.*;

import java.util.*;

import static mindustry.Vars.*;

public class UnityMenuRenderer extends MenuRenderer{
    public float slideDuration = 1200, transitionTime = 60;
    private float time;
    private final int width = !mobile ? 200 : 120, height = !mobile ? 50 : 40;
    private final int viewWidth = !mobile ? 100 : 60;
    private int index = 0;

    private final MenuSlide[] menus = {
        MenuSlides.stone,
        MenuSlides.grass
    };

    public UnityMenuRenderer(){
        unityGenerate();
    }

    // causes method signature conflicts if i just named it "generate()"
    public void unityGenerate(){
        // shuffle the menus
        for(int i = menus.length - 1; i >= 0; i--){
            int ii = (int)Mathf.randomSeed(Time.nanos(), i);
            MenuSlide temp = menus[i];
            menus[i] = menus[ii];
            menus[ii] = temp;
        }

        for(MenuSlide menu : menus){
            menu.generateWorld(width, height);
        }
    }

    @Override
    public void render(){
        time += Time.delta;

        float in = Mathf.curve(time, 0, transitionTime / 2);
        float out = Mathf.curve(time, slideDuration - transitionTime / 2, slideDuration);
        float dark = in - out;

        menus[index].render(time, slideDuration, viewWidth, height);

        Draw.color(0, 0, 0, 1 - dark);
        Fill.crect(0, 0, Core.graphics.getWidth(), Core.graphics.getHeight());

        Draw.blend(Blending.additive);
        Draw.color(1, 1, 1, 0.5f);

        float offset = (Time.time * 1.1f) % 160;
        for(int x = 0; x < Core.graphics.getWidth() / 160 + 1; x++){
            for(int y = 0; y < Core.graphics.getHeight() / 160 + 1; y++ ){
                Fill.circle(160 * x - offset, y * 160, 2);
            }
        }

        Draw.blend();
        Draw.color();

        if(time > slideDuration){
            if(index + 1 < menus.length){
                index++;
            }else{
                index = 0;
            }
            time = 0;
        }
    }
}
