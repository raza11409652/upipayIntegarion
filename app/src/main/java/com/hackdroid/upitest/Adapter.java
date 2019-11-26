package com.hackdroid.upitest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    List<ResolveInfo> mlist ;
    Context context ;
    Intent intent  ;
    Activity activity ;

    public Adapter(List<ResolveInfo> mlist, Context context, Intent intent , Activity activity) {
        this.mlist = mlist;
        this.context = context;
        this.intent = intent;
        this.activity = activity ;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_app , parent , false) ;

        return new Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ResolveInfo info = mlist.get(position);
        String name = String.valueOf(info.loadLabel(context.getPackageManager()));
        final Drawable icon = info.loadIcon(context.getPackageManager());
       // holder.bind(name, icon);
        holder.text.setText(name);
        holder.imageView.setImageDrawable(icon);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//        intent.removeCategory(Intent.D);
                intent.setPackage(info.activityInfo.packageName);
                //;
                activity.startActivityForResult(intent , 400);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView ;
        TextView text ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.appIconView) ;
            text = itemView.findViewById(R.id.appNameView) ;
        }
    }
}
