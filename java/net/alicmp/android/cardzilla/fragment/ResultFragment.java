package net.alicmp.android.cardzilla.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import net.alicmp.android.cardzilla.helper.NotificationUtility;
import net.alicmp.android.cardzilla.R;
import net.alicmp.android.cardzilla.StudyActivity;
import net.alicmp.android.cardzilla.helper.Utility;
import net.alicmp.android.cardzilla.model.Packet;
import net.alicmp.android.cardzilla.model.PacketStatistics;

/**
 * Created by ali on 9/16/15.
 */
public class ResultFragment extends Fragment {

    public Packet mPacket;
    private Button continueView;
    private Button laterView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_result, container, false);

        mPacket = ((StudyActivity) getActivity()).mPacket;

        continueView = (Button) rootView.findViewById(R.id.bt_continue);
        laterView = (Button) rootView.findViewById(R.id.bt_later);

        TextView readingResultInfoView = (TextView) rootView.findViewById(R.id.readingResultInfo);
        readingResultInfoView.setText(generateReadingResultText());

        continueView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((StudyActivity) getActivity()).getCards();
                ((StudyActivity) getActivity()).fragmentTransaction(0);
                ((StudyActivity) getActivity()).setReadInfoText();
            }
        });

        laterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long readTime = Utility.nextReadTime(getActivity(), mPacket.getId());
                NotificationUtility.makeNotification(getActivity(), mPacket, readTime);
                getActivity().finish();
            }
        });

        return rootView;
    }

    private String generateReadingResultText() {

        int numberOfStudiedCards = ((StudyActivity) getActivity()).numberOfCards;
        PacketStatistics statistics = Utility.getStatics(getActivity(), mPacket.getId());
        int newCardsCount = statistics.getNewCardsCount();

        String firstLine = "You studied "
                + numberOfStudiedCards
                + ((numberOfStudiedCards > 1) ? " cards, " : " card, ");
        String secondLine;
        String nextReadTime = "Next read time: "
                + mPacket.timeToReadString(Utility.nextReadTime(getActivity(), mPacket.getId()));

        if (newCardsCount == 0) {
            continueView.setVisibility(View.GONE);
            laterView.setText("OK");
            secondLine = "and there isn't any unread card left in this packet, so you should stick to the schedule.";
        } else if (newCardsCount == 1)
            secondLine = "and there is 1 unread card left in this packet. You can read it now or you can " +
                    "stick to the schedule, it's your choice.";
        else
            secondLine = "and there are " + newCardsCount + " unread cards left in this packet. " +
                    "You can read them now or stick to the schedule.";

        return firstLine + secondLine + "\n\n" + nextReadTime;
    }
}
