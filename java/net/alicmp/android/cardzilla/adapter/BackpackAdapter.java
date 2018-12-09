package net.alicmp.android.cardzilla.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.alicmp.android.cardzilla.PacketInfoActivity;
import net.alicmp.android.cardzilla.R;
import net.alicmp.android.cardzilla.model.Packet;

import java.util.List;

/**
 * Created by ali on 8/13/15.
 */
public class BackpackAdapter extends RecyclerView.Adapter<BackpackAdapter.ViewHolder> {

    private List<Packet> mValues;
    private final Context mContext;

    public BackpackAdapter(Context context, List<Packet> packages) {
        this.mContext = context;
        this.mValues = packages;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mTitleView;
        public final TextView infoView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTitleView = (TextView) itemView.findViewById(R.id.list_item_title_tv);
            infoView = (TextView) itemView.findViewById(R.id.list_item_tv_info);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Intent intent = new Intent(mContext, PacketInfoActivity.class);
            if (mValues.get(adapterPosition) == null)
                Log.v("BackPackAdapter", " EMPTY ");
            intent.putExtra("packet", mValues.get(adapterPosition));
            mContext.startActivity(intent);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.list_item_backpack, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.mTitleView.setText(mValues.get(i).getPacketName());

        int numberOfCards = mValues.get(i).getNumberOfCards();
        String info;
        if (numberOfCards > 1)
            info = numberOfCards + " cards";
        else {
            info = numberOfCards + " card";
            viewHolder.infoView.setPadding(15, 0, 0, 0);
        }
        viewHolder.infoView.setText(info);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

}
