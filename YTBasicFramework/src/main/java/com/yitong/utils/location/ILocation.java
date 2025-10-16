package com.yitong.utils.location;

/**
 * Created by zpz on 2015/9/30.
 */
public interface ILocation {

    public Double getLongitude();

    public Double getLatitude();

    public void startLocation();
    
    public void stopLocaiton();
}
