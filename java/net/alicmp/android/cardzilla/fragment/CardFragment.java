package net.alicmp.android.cardzilla.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import net.alicmp.android.cardzilla.R;
import net.alicmp.android.cardzilla.ReviewActivity;
import net.alicmp.android.cardzilla.StudyActivity;
import net.alicmp.android.cardzilla.helper.Utility;
import net.alicmp.android.cardzilla.model.Card;

/**
 * Created by ali on 8/23/15.
 */
public class CardFragment extends Fragment {
    private static final String ARG_CARD = "card";
    private static final String ARG_IS_FRONT = "isFront";
    private static final String ARG_CARD_POSITION = "cardPosition";
    private static final String ARG_IS_REVIEW_MODE = "isReviewMode";

    private int cardPosition;
    private Card mCard;
    private boolean isFront;
    private boolean isReviewMode;

    public static CardFragment newInstance(Card mCard, boolean isFront, int position, boolean isReviewMode) {
        CardFragment fragment = new CardFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CARD, mCard);
        args.putBoolean(ARG_IS_FRONT, isFront);
        args.putInt(ARG_CARD_POSITION, position);
        args.putBoolean(ARG_IS_REVIEW_MODE, isReviewMode);
        fragment.setArguments(args);
        return fragment;
    }

    public CardFragment() {
        // Empty Public Constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCard = (Card) getArguments().getSerializable(ARG_CARD);
            isFront = getArguments().getBoolean(ARG_IS_FRONT);
            cardPosition = getArguments().getInt(ARG_CARD_POSITION);
            isReviewMode = getArguments().getBoolean(ARG_IS_REVIEW_MODE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_card, container, false);
        TextView text = (TextView) rootView.findViewById(R.id.tv_text);
        ImageButton flip = (ImageButton) rootView.findViewById(R.id.bt_flip);
        ImageButton right = (ImageButton) rootView.findViewById(R.id.bt_right);
        ImageButton wrong = (ImageButton) rootView.findViewById(R.id.bt_wrong);
        TextView situation = (TextView) rootView.findViewById(R.id.tv_situation);

        if (isReviewMode) {
            right.setImageResource(R.drawable.ic_keyboard_arrow_right_black_48dp);
            wrong.setImageResource(R.drawable.ic_keyboard_arrow_left_black_24dp);
        }

        int level = mCard.getLevel();
        if (level == 0) {
            situation.setText("UNREAD");
        } else if (level == 1 || level == 2) {
            situation.setText("LEARNING");
        } else {
            situation.setText("REVIEWING");
        }

        if (isFront)
            text.setText(mCard.getFrontText());
        else
            text.setText(mCard.getBackText());

        flip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isReviewMode) {
                    ((StudyActivity) getActivity()).onFlipButtonClickListener(!isFront, cardPosition);
                } else {
                    ((ReviewActivity) getActivity()).onFlipButtonClickListener(!isFront, cardPosition);
                }

            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isReviewMode) {
                    Utility.onCardAnswered(getActivity(), mCard, true);
                    ((StudyActivity) getActivity()).onCardAnswered(cardPosition);
                } else {
                    ((ReviewActivity) getActivity()).onForwardButtonClickListener(cardPosition);
                }
            }
        });
        wrong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isReviewMode) {
                    mCard.setLevel(1);
                    Utility.onCardAnswered(getActivity(), mCard, false);
                    StudyActivity.mCards.add(mCard);
                    ((StudyActivity) getActivity()).onCardAnswered(cardPosition);
                } else {
                    ((ReviewActivity) getActivity()).onBackwardButtonClickListener(cardPosition);
                }
            }
        });

        return rootView;
    }
}
