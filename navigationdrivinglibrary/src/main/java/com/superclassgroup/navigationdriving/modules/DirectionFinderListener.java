package com.superclassgroup.navigationdriving.modules;

import java.util.ArrayList;

/**
 * Created by Mai Thanh Hiep on 4/3/2016.
 */
public interface DirectionFinderListener {
    public void onDirectionFinderStart();
    public void onDirectionFinderResult(ArrayList<Route> route, boolean isSuccess);
}
