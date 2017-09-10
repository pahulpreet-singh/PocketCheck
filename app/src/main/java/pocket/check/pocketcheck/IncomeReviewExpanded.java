package pocket.check.pocketcheck;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class IncomeReviewExpanded extends AppCompatActivity {

    String amt,accname,notes,date;

    EditText amtET,notesET;
    TextView acc,dateTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_expanded);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent exp=new Intent(getBaseContext(),Expenses.class);
                finish();
                startActivity(exp);
            }
        });


        //Gettin extras from intent
        Intent prev=getIntent();
        amt=prev.getStringExtra("amount");
        accname=prev.getStringExtra("accName");
        notes=prev.getStringExtra("notes");
        date=prev.getStringExtra("date");

        //integrating Views
        amtET= (EditText) findViewById(R.id.editText5);
        notesET= (EditText) findViewById(R.id.editText6);
        acc= (TextView) findViewById(R.id.textView36);
        dateTV= (TextView) findViewById(R.id.dateInc);

        //setting the values
        amtET.setText(amt);
        amtET.setFocusable(false);
        amtET.setClickable(false);

        notesET.setText(notes);
        notesET.setFocusable(false);
        notesET.setClickable(false);
        acc.setText(accname);
        dateTV.setText(date);


    }


}
