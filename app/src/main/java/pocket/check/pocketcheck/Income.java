package pocket.check.pocketcheck;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class Income extends AppCompatActivity {

    ImageView aau,cal;
    Spinner spin;
    ArrayList<String> accType;
    ArrayList<String> accSrno;
    SQLiteDatabase dbcall;
    String def,defAcc;

    //spinner selected item and its srno
    String accName;
    int acc_Srno;

    EditText amt,notes;
    TextView dateInc;
    int year,month,day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insert();
                //balanceUpdate();
            }
        });

        //rounded
        aau= (ImageView) findViewById(R.id.imageView6);
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.incomeicon);
        aau.setImageBitmap(icon);

        //back
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //setDate
        dateInc= (TextView) findViewById(R.id.dateInc);
        Calendar c=Calendar.getInstance();
        year=c.get(Calendar.YEAR);
        month=c.get(Calendar.MONTH);
        day=c.get(Calendar.DAY_OF_MONTH);
        showDate(year,month+1,day);
        cal= (ImageView) findViewById(R.id.imageView8);
        cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(999);
            }
        });
        dateInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(999);
            }
        });


        spin= (Spinner) findViewById(R.id.spinner2);
        getTypes();
        setSpinner();
        //get spinner selected item and its srno for insertion
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                accName=spin.getSelectedItem().toString();
                acc_Srno=Integer.parseInt(accSrno.get(spin.getSelectedItemPosition()));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //amount and labels for insertion
        amt= (EditText) findViewById(R.id.editText5);
        notes= (EditText) findViewById(R.id.editText6);




    }

    void getTypes()
    {
        accType=new ArrayList<String>();
        accSrno=new ArrayList<String>();

        //accSrno.add("0");
        //accType.add("Cash");

        Dbclass obj=new Dbclass(getApplicationContext());
        dbcall=obj.getReadableDatabase();
        String q="select * from accounts";
        Cursor rs=dbcall.rawQuery(q,null);
        if(rs!=null)
        {
            if(rs.moveToNext())
            {
                do
                {

                    accType.add(rs.getString(rs.getColumnIndex("name")));
                    accSrno.add(rs.getString(rs.getColumnIndex("srno")));
                    def=rs.getString(rs.getColumnIndex("defaultt"));
                    if(def.equals("true"))
                    {
                        defAcc=rs.getString(rs.getColumnIndex("name"));

                    }



                }while(rs.moveToNext());
            }
        }

    }
    void setSpinner()
    {
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,
        android.R.layout.simple_spinner_dropdown_item, accType);
        spin.setAdapter(adapter);
        if(defAcc!=null)
        {
            int spinnerPosition = adapter.getPosition(defAcc);
            spin.setSelection(spinnerPosition);
        }


    }



    //Database
    void insert()
    {
        if(amt.getText().toString().equals(""))
        {
            Toast.makeText(getBaseContext(),"Enter income amount",Toast.LENGTH_LONG).show();
        }
        else {
            Dbclass obj = new Dbclass(getApplicationContext());
            obj.caller();
            long status = -1;
            status = obj.addIncome(Long.parseLong(amt.getText().toString()), accName,
                    acc_Srno, notes.getText().toString(), dateInc.getText().toString());

            if (status > 0) {

                balanceUpdate();

                Toast.makeText(getApplicationContext(), "New income in bag!", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(getApplicationContext(), "Error. Please contact the developer.",
                        Toast.LENGTH_LONG).show();
            }
        }
    }
    void balanceUpdate()
    {
        long bal=Long.parseLong(amt.getText().toString());

        String getBal="select * from balanceTable";
        Dbclass obj=new Dbclass(getApplicationContext());
        obj.caller();
        dbcall=obj.getReadableDatabase();

        Cursor rs = dbcall.rawQuery(getBal, null);
        if (rs.moveToNext())
        {
            bal = bal + Long.parseLong(rs.getString(rs.getColumnIndex("balance")));
            Home.total.setText(String.valueOf(bal));

        }

            //update
        long stat;

        stat = obj.balance(bal);

        if (stat > 0)
        {

            Toast.makeText(getApplicationContext(), "Total Balance updated", Toast.LENGTH_SHORT)
                    .show();
            Intent next = new Intent(getBaseContext(), Home.class);
            finish();
            next.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(next);

        }
        else
        {
            Toast.makeText(getApplicationContext(), "Error. Please contact the developer.",
                    Toast.LENGTH_LONG).show();
        }

    }
    //Database end


    //Date
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }
    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // arg1 = year
            // arg2 = month
            // arg3 = day

            showDate(arg1, arg2+1, arg3);

        }
    };

    private void showDate(int year, int month, int day) {
        dateInc.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }
    //date end


}
