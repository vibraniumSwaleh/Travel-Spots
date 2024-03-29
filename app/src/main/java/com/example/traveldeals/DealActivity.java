package com.example.traveldeals;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DealActivity extends AppCompatActivity {

    public static final String TRAVELDEALS_CHILD = "traveldeals";
    EditText txtTitle;
    EditText txtDescription;
    EditText txtPrice;
    TravelDeal deal;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        FirebaseUtil.openFbReference(TRAVELDEALS_CHILD, this);
        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;
        mDatabaseReference = FirebaseUtil.mDatabaseReference;

        txtTitle = (EditText) findViewById(R.id.txtTitle);
        txtDescription = (EditText) findViewById(R.id.txtDescription);
        txtPrice = (EditText) findViewById(R.id.txtPrice);

        clickedDeal();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_menu:
                saveDeal();
//                Toast.makeText(this,"Deal saved successfully!",
//                        Toast.LENGTH_LONG).show();
//                clean();
//                backToList();
                return true;
            case R.id.delete_menu:
                Log.d("Delete deal", "Deal menu selected");
                deleteDeal();
                Log.d("Delete deal", "Deal method called");
                Toast.makeText(this, "Deal deleted", Toast.LENGTH_LONG).show();
                backToList();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveDeal() {
        if (TextUtils.isEmpty(txtTitle.getText())) {
            txtTitle.setError("Deal Title cannot be empty!");
            return;
        }
        if (TextUtils.isEmpty(txtPrice.getText())) {
            txtPrice.setError("Price cannot be empty!");
            return;
        }
        if (TextUtils.isEmpty(txtDescription.getText())) {
            txtDescription.setError("Description cannot be empty!");
            return;
        }

        if (TextUtils.isEmpty(txtTitle.getText()) == false
                && TextUtils.isEmpty(txtTitle.getText()) == false
                && TextUtils.isEmpty(txtTitle.getText()) == false) {
            deal.setTitle(txtTitle.getText().toString());
            deal.setDescription(txtDescription.getText().toString());
            deal.setPrice(txtPrice.getText().toString());

            if (deal.getId() == null) {
                Log.d("deal.getId()", "deal ID: " + deal.getId());
                mDatabaseReference.push().setValue(deal);
            } else {
                mDatabaseReference.child(deal.getId()).setValue(deal);
            }
            Toast.makeText(this, "Deal saved successfully!",
                    Toast.LENGTH_LONG).show();
            clean();
            backToList();
        }
    }

    private void deleteDeal() {
        if (deal == null) {
            Toast.makeText(this, "Please save the deal before deleting", Toast.LENGTH_LONG).show();
            return;
        }
        mDatabaseReference.child(deal.getId()).removeValue();
        backToList();
    }

    private void backToList() {
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }

    private void clean() {
        txtTitle.setText("");
        txtPrice.setText("");
        txtDescription.setText("");
        txtTitle.requestFocus();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu, menu);
        if (FirebaseUtil.isAdmin){
            menu.findItem(R.id.insert_menu).setVisible(true);
            menu.findItem(R.id.save_menu).setVisible(true);
            enableEditText(true);
        }
        else {
            menu.findItem(R.id.insert_menu).setVisible(false);
            menu.findItem(R.id.save_menu).setVisible(false);
            enableEditText(false);
        }

        return true;
    }

    private void  clickedDeal() {
        Intent intent = getIntent();
        TravelDeal deal = (TravelDeal) intent.getSerializableExtra("Deal");
        if (deal == null) {
            deal = new TravelDeal();
        }
        this.deal = deal;
        txtTitle.setText(deal.getTitle());
        txtDescription.setText(deal.getDescription());
        txtPrice.setText(deal.getPrice());
    }

    private void enableEditText(boolean isEnabled){
        txtTitle.setEnabled(isEnabled);
        txtDescription.setEnabled(isEnabled);
        txtPrice.setEnabled(isEnabled);
    }
}
