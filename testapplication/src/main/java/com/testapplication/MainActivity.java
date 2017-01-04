package com.testapplication;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int color = Color.argb(100, 70,130,180);

        NativeBufferCreator nbc = new NativeBufferCreator();
        BufferCreator bc = new BufferCreator();

        /*int[] c = nbc.extractColor(color);
        int[] jc = bc.extractColor(color);

        int r = nbc.randomNoise();
        int jr = bc.randomNoise();

        float h = nbc.translateColorToHeight(color);
        int jh = bc.translateColorToHeight(color);
*/

        int[] positions = new GridCreator().makeGrid(2, 2);
        int[] imageDataFlat = new int [] {  1, 2, 2, 3  };

        float[] b = nbc.createVertexBufferFlat(positions, imageDataFlat, 2);
        float[] jb = bc.createVertexBufferFlat(positions, imageDataFlat, 2);

        float[] cb = nbc.createColorBufferFlat(positions, imageDataFlat,  2);
        float[] jcb = bc.createColorBufferFlat(positions, imageDataFlat,  2);


        Log.v("-------", new NativeBufferCreator().randomNoise() + "");

    }
}
