package com.wamel.beaconear.callbacks;

import android.view.View;

/**
 * Created by mreverter on 31/3/16.
 */
public interface LongSelectionCallback<T>  {
    void onSelected(T selected, View view);
}
