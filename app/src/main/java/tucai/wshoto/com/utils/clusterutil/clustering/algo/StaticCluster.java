/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */

package tucai.wshoto.com.utils.clusterutil.clustering.algo;
import com.baidu.mapapi.model.LatLng;
import tucai.wshoto.com.utils.clusterutil.clustering.Cluster;
import tucai.wshoto.com.utils.clusterutil.clustering.ClusterItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A cluster whose center is determined upon creation.
 */
public class StaticCluster<T extends ClusterItem> implements Cluster<T> {
    private final LatLng mCenter;
    private final List<T> mItems = new ArrayList<T>();

    public StaticCluster(LatLng center) {
        mCenter = center;
    }



    public boolean add(T t) {
        return mItems.add(t);
    }

    @Override
    public LatLng getPosition() {
        return mCenter;
    }

    public boolean remove(T t) {
        return mItems.remove(t);
    }

    @Override
    public Collection<T> getItems() {
        return mItems;
    }

    @Override
    public int getSize() {
        return mItems.size();
    }

    @Override
    public String toString() {
        return "StaticCluster{"
                + "mCenter=" + mCenter
                + ", mItems.size=" + mItems.size()
                + '}';
    }
}