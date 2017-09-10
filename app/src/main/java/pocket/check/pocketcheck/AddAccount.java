package pocket.check.pocketcheck;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AddAccount extends AppCompatActivity {

    ArrayList<String> accType;
    Spinner spinner;
    String type;
    EditText name,balance;
    CheckBox def,inc;
    String defaul,include;
    SQLiteDatabase dbcall;
    long balValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insert();
            }
        });


        accType=new ArrayList<String>();
        accType.add("Cash");
        accType.add("Credit Card");
        accType.add("Loan");
        accType.add("Bank Account");
        spinner= (Spinner) findViewById(R.id.spinner3);
        spinner.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, accType));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = accType.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        name= (EditText) findViewById(R.id.editText7);
        balance= (EditText) findViewById(R.id.editText8);

        def= (CheckBox) findViewById(R.id.checkBox3);
        inc= (CheckBox) findViewById(R.id.checkBox);


    }

    void insert()
    {

        if(name.getText().toString().equals(""))
        {
            Toast.makeText(getBaseContext(),"Please give a name to new account",Toast.LENGTH_LONG).show();
        }
        else {

            if (inc.isChecked()) {
                include = "true";
                balanceUpdate();
            }
            else {
                include = "false";
            }

            if (def.isChecked()) {
                defaul = "true";
                //DIALOG BOX
                setDefaultt();
            }
            else {
                defaul = "false";
            }

            if(balance.getText().toString().equals(""))
            {
                balValidation=0;
            }
            else
            {
                balValidation=Long.parseLong(balance.getText().toString());
            }


            Dbclass obj = new Dbclass(getApplicationContext());

            obj.caller();
            long status = -1;
            status = obj.addAccount(name.getText().toString(),type,balValidation,include, defaul);
            if (status > 0) {

                Toast.makeText(getApplicationContext(), "New Account '" + name.getText().toString()
                        + "' added successfully", Toast.LENGTH_LONG).show();
                Intent next = new Intent(getBaseContext(), Income.class);
                finish();
                startActivity(next);

            } else {
                Toast.makeText(getApplicationContext(), "Error. Please contact the developer.", Toast.LENGTH_LONG).show();
            }
        }


    }

    //update balance
    void balanceUpdate()
    {
        if(balance.getText().toString().equals(""))
        {
            balValidation=0;
        }
        else {
            balValidation = Long.parseLong(balance.getText().toString());
            long bal = balValidation;

            String getBal = "select * from balanceTable";
            Dbclass obj = new Dbclass(getApplicationContext());
            obj.caller();
            dbcall = obj.getReadableDatabase();

            Cursor rs = dbcall.rawQuery(getBal, null);
            if (rs.moveToNext()) {
                bal = bal + Long.parseLong(rs.getString(rs.getColumnIndex("balance")));
                Home.total.setText(String.valueOf(bal));

            }

            //update
            long stat;

            stat = obj.balance(bal);

            if (stat > 0) {

                Toast.makeText(getApplicationContext(), "Total Balance updated", Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(getApplicationContext(), "Error. Please contact the developer.",
                        Toast.LENGTH_LONG).show();
            }
        }
    }


    //set default account (pre-selected account in income)
    void setDefaultt()
    {

        String q="update accounts set defaultt = 'false'";
        Dbclass obj=new Dbclass(getApplicationContext());
        dbcall=obj.getWritableDatabase();
        try {
            dbcall.execSQL(q);

        }catch (Exception e)
        {

        }

    }

}
