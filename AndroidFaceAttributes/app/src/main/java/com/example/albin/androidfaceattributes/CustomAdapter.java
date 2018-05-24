package com.example.albin.androidfaceattributes;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import com.microsoft.projectoxford.face.contract.Face;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter{

    private Face[] mFace;
    private Context mContext;
    private LayoutInflater mInflater;
    private  Bitmap mOriginalBitmap;

    public CustomAdapter(Face[] face, Context context, Bitmap originalBitmap) {
        this.mFace = face;
        this.mContext = context;
        this.mOriginalBitmap = originalBitmap;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mFace.length;
    }

    @Override
    public Object getItem(int position) {
        return mFace[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(convertView == null)
            view =mInflater.inflate(R.layout.listview_layout, null, false);

        TextView txtAge, txtGender, txtFacialHair, txtHeadPose, txtSmile;
        ImageView imageView;

        txtAge = (TextView)view.findViewById(R.id.txtAge);
        txtFacialHair = (TextView)view.findViewById(R.id.txtFacialHair);
        txtHeadPose = (TextView)view.findViewById(R.id.txtHeadPose);
        txtSmile = (TextView)view.findViewById(R.id.txtSmile);
        txtGender = (TextView)view.findViewById(R.id.txtGender);

        imageView = (ImageView) view.findViewById(R.id.imgTh);

        txtAge.setText("Edad :" + mFace[position].faceAttributes.age);
        txtGender.setText("Genero :" + mFace[position].faceAttributes.gender);
        txtSmile.setText("Sonrisa :" + mFace[position].faceAttributes.smile);
        txtFacialHair.setText(String.format("Pelo en rostro : %f %f %f", mFace[position].faceAttributes.facialHair.moustache,
                mFace[position].faceAttributes.facialHair.sideburns,
                mFace[position].faceAttributes.facialHair.beard));
        txtHeadPose.setText(String.format("Pose de cabeza : %f %f %f", mFace[position].faceAttributes.headPose.pitch,
                mFace[position].faceAttributes.headPose.yaw,
                mFace[position].faceAttributes.headPose.roll));
        Bitmap bitmap = ImageHelper.generateThumbnail(mOriginalBitmap, mFace[position].faceRectangle);

        imageView.setImageBitmap(bitmap);

        return view;
    }
}
