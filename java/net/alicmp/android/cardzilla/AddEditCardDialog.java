package net.alicmp.android.cardzilla;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.alicmp.android.cardzilla.helper.Constants;
import net.alicmp.android.cardzilla.helper.Utility;
import net.alicmp.android.cardzilla.model.Card;
import net.alicmp.android.cardzilla.model.Packet;

/**
 * Created by ali on 3/17/16.
 */
public class AddEditCardDialog extends AppCompatActivity {

    private boolean isEditMode;
    private Packet mPacket;
    private Card mCard;

    private Button removeButton, editButton;
    private EditText frontOfCardEditText, backOfCardEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(net.alicmp.android.cardzilla.R.layout.dialog_edit_card);

        initializeViews();
        getPassedInIntents();

        if (!isEditMode) {
            setTitle("Add Card");
            mCard = new Card();
            removeButton.setVisibility(View.GONE);
            editButton.setText("Add");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        populateFields();
    }

    public void editOrAdd(View view) {
        mCard.setFrontText(frontOfCardEditText.getText().toString());
        mCard.setBackText(backOfCardEditText.getText().toString());

        if (mCard.getBackText().equals("") && mCard.getFrontText().equals("")) {
            Toast.makeText(AddEditCardDialog.this, "Fill in the blanks.", Toast.LENGTH_LONG).show();
        } else {
            if (isEditMode) {
                Utility.editCard(AddEditCardDialog.this, mCard);
            } else
                Utility.addCard(AddEditCardDialog.this, mCard, mPacket.getId());

            Intent intent = getIntent();
            intent.putExtra("card", mCard);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    public void removeCard(View view) {
        Utility.deleteCard(AddEditCardDialog.this, mCard.getId());
        mCard = null;
        Intent intent = getIntent();
        intent.putExtra("card", mCard);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void cancel(View view) {
        finish();
    }

    private void getPassedInIntents() {
        mCard = (Card) getIntent().getSerializableExtra(Constants.ARG_CARD);
        mPacket = getIntent().getParcelableExtra("packet");
        isEditMode = getIntent().getBooleanExtra("isEditMode", true);
    }

    private void initializeViews() {
        editButton = (Button) findViewById(net.alicmp.android.cardzilla.R.id.bt_edit);
        removeButton = (Button) findViewById(net.alicmp.android.cardzilla.R.id.bt_remove);

        frontOfCardEditText = (EditText) findViewById(net.alicmp.android.cardzilla.R.id.et_card_front);
        backOfCardEditText = (EditText) findViewById(net.alicmp.android.cardzilla.R.id.et_card_back);
    }

    private void populateFields() {
        frontOfCardEditText.setText(mCard.getFrontText());
        backOfCardEditText.setText(mCard.getBackText());
    }

}
