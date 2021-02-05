package com.swhackathon.bpass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.swhackathon.bpass.network.data.responsedata.VisitListData;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    public Context context;
    public ArrayList<VisitListData> mData = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.item_list, parent, false) ;
        ViewHolder vh = new ViewHolder(view) ;

        return vh ;
    }

    @Override
    public void onBindViewHolder(ListAdapter.ViewHolder holder, int position) {
        holder.onBind(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name, tv_entry, tv_exit, tv_tel;

        ViewHolder(final View itemView) {
            super(itemView);
            // 뷰 객체에 대한 참조. (hold strong reference)
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_entry = itemView.findViewById(R.id.tv_entrance);
            tv_exit = itemView.findViewById(R.id.tv_exit);
            tv_tel = itemView.findViewById(R.id.tv_phone);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }

        void onBind(VisitListData data) {
            tv_name.setText(data.getVisitor().getName());
            tv_entry.setText(data.getEntryTime());
            tv_exit.setText(data.getExitTime());
            tv_tel.setText("T. " + data.getStroe().getStorePhoneNumber());
        }

    }
}
