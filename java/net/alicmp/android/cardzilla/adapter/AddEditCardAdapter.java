package net.alicmp.android.cardzilla.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.alicmp.android.cardzilla.AddEditCardDialog;
import net.alicmp.android.cardzilla.R;
import net.alicmp.android.cardzilla.helper.Constants;
import net.alicmp.android.cardzilla.model.Card;
import net.alicmp.android.cardzilla.model.Packet;

import java.util.List;

/**
 * Created by ali on 9/8/15.
 */
public class AddEditCardAdapter extends RecyclerView.Adapter<AddEditCardAdapter.ViewHolder> {

    private List<Card> mCards;
    private Context mContext;
    private Packet mPacket;

    public AddEditCardAdapter(Context context, List<Card> cards, Packet mPacket) {
        this.mCards = cards;
        this.mContext = context;
        this.mPacket = mPacket;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView frontCardView;
        public final TextView backCardView;

        public ViewHolder(View itemView) {
            super(itemView);
            frontCardView = (TextView) itemView.findViewById(R.id.tv_front_text);
            backCardView = (TextView) itemView.findViewById(R.id.tv_back_text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();

            Intent intent = new Intent(mContext, AddEditCardDialog.class);
            intent.putExtra("card", mCards.get(adapterPosition));
            intent.putExtra("packet", mPacket);
            intent.putExtra("position", adapterPosition);
            ((Activity) mContext).startActivityForResult(intent, Constants.REQUEST_EDIT);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.list_item_add_edit_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.frontCardView.setText(mCards.get(position).getFrontText());
        holder.backCardView.setText(mCards.get(position).getBackText());
    }

    @Override
    public int getItemCount() {
        return mCards.size();
    }

    public void add(int position, Card mCard) {
        mCards.add(position, mCard);
        notifyItemInserted(position);
    }

    public void edit(int position, Card mCard) {
        mCards.set(position, mCard);
        notifyItemChanged(position);
    }

    public void remove(int position) {
        mCards.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mCards.size());
    }

}