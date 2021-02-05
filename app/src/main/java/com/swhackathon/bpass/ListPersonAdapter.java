package com.swhackathon.bpass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.swhackathon.bpass.db.Visit;

import java.util.List;

public class ListPersonAdapter extends RecyclerView.Adapter<ListPersonAdapter.ViewHolder> implements OnListItemClickListener{

    public Context context;
    public List<Visit> mData;
    OnListItemClickListener listener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.item_list, parent, false) ;
        ViewHolder vh = new ViewHolder(view, this) ;

        return vh ;
    }

    @Override
    public void onBindViewHolder(ListPersonAdapter.ViewHolder holder, int position) {
        holder.onBind(mData.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    void addItem(Visit data) {
        // 외부에서 item을 추가시킬 함수입니다.
        mData.add(data);
    }

    public void setOnItemClicklistener(OnListItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onItemClick(ViewHolder holder, View view, int position) {
        if(listener != null){
            listener.onItemClick(holder,view,position);
        }
    }

    public Visit getItem(int position) {
        return mData.get(position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name, tv_entry, tv_exit, tv_tel;
        View divide_line;

        ViewHolder(final View itemView, final OnListItemClickListener listener) {
            super(itemView);
            // 뷰 객체에 대한 참조. (hold strong reference)
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_entry = itemView.findViewById(R.id.tv_entrance);
            tv_exit = itemView.findViewById(R.id.tv_exit);
            tv_tel = itemView.findViewById(R.id.tv_phone);
            divide_line = itemView.findViewById(R.id.divide_line);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener != null){
                        listener.onItemClick(ViewHolder.this, v, position);
                    }
                }
            });
        }

        void onBind(Visit data, int position) {
            tv_name.setText(data.getStoreName());
            tv_entry.setText(data.getEntryTime());
            tv_exit.setText(data.getExitTime());
            tv_tel.setText("T. " + data.getStoreNumber());
            if(getItemCount() - 1 == position)
                divide_line.setVisibility(View.INVISIBLE);
        }

    }
}




