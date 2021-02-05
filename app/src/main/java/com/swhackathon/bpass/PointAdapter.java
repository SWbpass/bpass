package com.swhackathon.bpass;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.naver.maps.map.overlay.InfoWindow;

public class PointAdapter extends InfoWindow.DefaultViewAdapter
{
    private final Context mContext;
    private final ViewGroup mParent;
    private String storeName;
    private String storePhoneNumber;
    private String address;
    private double latitude;
    private double longitude;

    public PointAdapter(@NonNull Context context, ViewGroup parent, String storeName, String storePhoneNumber, String address, double latitude, double longitude)
    {
        super(context);
        mContext = context;
        mParent = parent;
        this.storeName = storeName;
        this.storePhoneNumber = storePhoneNumber;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @NonNull
    @Override
    protected View getContentView(@NonNull InfoWindow infoWindow)
    {
        View view = (View) LayoutInflater.from(mContext).inflate(R.layout.item_point, mParent, false);

        TextView tv_storeName = (TextView) view.findViewById(R.id.tv_storeName);
        TextView tv_address = (TextView) view.findViewById(R.id.tv_address);
        TextView tv_storePhoneNumber = (TextView) view.findViewById(R.id.tv_storePhoneNumber);

        tv_storeName.setText(storeName);
        tv_address.setText(address);
        tv_storePhoneNumber.setText(storePhoneNumber);

        tv_storePhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + storePhoneNumber));
                mContext.startActivity(callIntent);
            }
        });

        return view;
    }
}