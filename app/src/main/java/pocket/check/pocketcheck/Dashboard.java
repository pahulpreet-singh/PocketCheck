package pocket.check.pocketcheck;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;

public class Dashboard extends AppCompatActivity {

    TextView exp,total,bud,per,month1,month2,uname;
    SeekBar sb;
    EditText savED;

    long tot=0,budPref;

    SharedPreferences pref;
    int flag;
    public static final String def="25";

    SQLiteDatabase dbcall;

    ArrayList<Long> inc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("perflag", String.valueOf(sb.getProgress()));
                editor.putString("amt", String.valueOf(budPref));
                //editor.commit();
                editor.apply();

                Toast.makeText(getBaseContext(),"Changes have been saved",Toast.LENGTH_LONG).show();

                Intent home=new Intent(getBaseContext(),Home.class);
                finish();
                home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(home);


            }
        });

        //back
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //integrating views
        exp= (TextView) findViewById(R.id.textView6);
        total= (TextView) findViewById(R.id.textView13);
        bud= (TextView) findViewById(R.id.textView10);
        per= (TextView) findViewById(R.id.textView37);
        month1= (TextView) findViewById(R.id.textView4);
        month2= (TextView) findViewById(R.id.textView5);
        currentMonth();
        uname= (TextView) findViewById(R.id.textView14);

        uname.setText(Home.uname.getText().toString());

        sb= (SeekBar) findViewById(R.id.seekBar);

        savED= (EditText) findViewById(R.id.editText9);
        savED.setClickable(false);
        savED.setFocusable(false);


        //initialising values
        exp.setText(Home.monExpense.getText().toString());
        total.setText(Home.total.getText().toString());
        bud.setText(Home.budget.getText().toString());

        //get TotalIncome
        getTotalInc();


        //tot = Long.parseLong(total.getText().toString());

        //initialise the editText and textView
        pref=getSharedPreferences("DashFlag", Context.MODE_PRIVATE);
        flag=Integer.parseInt(pref.getString("perflag", def));
        sb.setProgress(flag);
        long clac=sbSav(flag);
        budPref=sbBud(clac);


        //seekbar listner
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                long calc = sbSav(progress);
                budPref=sbBud(calc);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //



    }

    long sbSav(int progress)
    {

        per.setText("Savings Target: " + progress + "% of Total Income");

        long calc;
        calc=(progress*tot)/100;
        savED.setText(String.valueOf(calc));
        return calc;

    }

    long sbBud(long calc)
    {

        long cal=tot-calc-(Long.parseLong(exp.getText().toString()));
        if(cal<0)
        {
            cal=0;
        }
        bud.setText(String.valueOf(cal));
        return cal;

    }


    //setting the month name
    void currentMonth()
    {
        final Calendar c = Calendar.getInstance();

        int month=c.get(Calendar.MONTH);
        String mname=theMonth(month);
        month1.setText(mname);
        month2.setText(mname);

    }
    public static String theMonth(int month){
        String[] monthNames = {"January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"};

        return monthNames[month];
    }

    //get total income from database
    void getTotalInc()
    {

        String q="select * from incomes";
        inc=new ArrayList<Long>();

        Dbclass obj=new Dbclass(getApplicationContext());
        dbcall=obj.getReadableDatabase();

        Cursor cur = dbcall.rawQuery(q,null);

        if(cur.moveToNext())
        {
            do {
                inc.add(Long.parseLong(cur.getString(cur.getColumnIndex("amount"))));
            }while (cur.moveToNext());
        }
        for(int i=0;i<inc.size();i++)
        {
            tot+=inc.get(i);
        }
        TextView incTV= (TextView) findViewById(R.id.textView43);
        incTV.setText(String.valueOf(tot));

    }



}
