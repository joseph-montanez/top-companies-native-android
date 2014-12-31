package com.comentum.topcompanies.topcompanies;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by Joseph on 12/31/2014.
 */
public class Backdrop {
    private final List<Integer> images = Arrays.asList(
            R.drawable.backdrop_106387740,
            R.drawable.backdrop_118368146,
            R.drawable.backdrop_137875651,
            R.drawable.backdrop_146822494,
            R.drawable.backdrop_152503797,
            R.drawable.backdrop_164149948,
            R.drawable.backdrop_177133164,
            R.drawable.backdrop_177273726,
            R.drawable.backdrop_178639802,
            R.drawable.backdrop_462884189,
            R.drawable.backdrop_464198883,
            R.drawable.backdrop_464222553,
            R.drawable.backdrop_464222587,
            R.drawable.backdrop_464245129,
            R.drawable.backdrop_465574415,
            R.drawable.backdrop_467400545,
            R.drawable.backdrop_467499073,
            R.drawable.backdrop_476107639,
            R.drawable.backdrop_476212207,
            R.drawable.backdrop_487441425,
            R.drawable.backdrop_491939507,
            R.drawable.backdrop_76945379,
            R.drawable.backdrop_76945380,
            R.drawable.backdrop_76945404,
            R.drawable.backdrop_94192864,
            R.drawable.backdrop_94681342
    );

    public Integer getRandomImage() {
        Random rand = new Random();
        return images.get(rand.nextInt(images.size()));
    }
}
