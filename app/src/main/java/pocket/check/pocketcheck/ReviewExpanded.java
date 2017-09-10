package pocket.check.pocketcheck;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ReviewExpanded extends AppCompatActivity {

    String expense,type,subtype,accName,notes,date;
    int typeimg;

    TextView acc,amt,dateTV,typeTV,per,soc,fam,work;
    EditText notesET;
    ImageView typeIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_expanded);
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
        expense=prev.getStringExtra("expense");
        type=prev.getStringExtra("type");
        subtype=prev.getStringExtra("subtype");
        accName=prev.getStringExtra("accName");
        notes=prev.getStringExtra("notes");
        date=prev.getStringExtra("date");
        typeimg=prev.getIntExtra("image", R.drawable.ic_box_50);

        //integrating Views
        acc= (TextView) findViewById(R.id.textView31);
        amt= (TextView) findViewById(R.id.textView33);
        typeTV= (TextView) findViewById(R.id.type);
        dateTV= (TextView) findViewById(R.id.dateExp);
        notesET= (EditText) findViewById(R.id.editText4);
        typeIV= (ImageView) findViewById(R.id.imageView6);

        per= (TextView) findViewById(R.id.textView16);
        soc= (TextView) findViewById(R.id.textView17);
        fam= (TextView) findViewById(R.id.textView18);
        work= (TextView) findViewById(R.id.textView19);

        //setting values
        acc.setText(accName);
        amt.setText(expense);
        typeTV.setText(type);
        dateTV.setText(date);
        notesET.setText(notes);
        notesET.setFocusable(false);
        notesET.setClickable(false);

        int img=setImage(type);
        typeIV.setImageResource(img);

        //set subtype
        setSubtype();

    }
    int setImage(String typ)
    {
        if(typ.equals("General"))
        {
            return R.drawable.ic_box_50;
        }
        else if(typ.equals("Personal"))
        {
            return R.drawable.ic_person_50;
        }
        else if(typ.equals("House"))
        {
            return R.drawable.ic_home_50;
        }
        else if(typ.equals("Clothes"))
        {
            return R.drawable.ic_shop_50;
        }
        else if(typ.equals("Food & Drinks"))
        {
            return R.drawable.ic_food_50;
        }
        else if(typ.equals("Transport & Travel"))
        {
            return R.drawable.ic_car_50;
        }
        else if(typ.equals("Fun"))
        {
            return R.drawable.ic_fun_50;
        }
        else if(typ.equals("Misc"))
        {
            return R.drawable.ic_misc_50;
        }
        return R.drawable.ic_box_50;
    }


    void setSubtype()
    {
        if(subtype.equals("per"))
        {
            per.setBackgroundResource(R.drawable.roundedwhitebg);
            soc.setBackgroundResource(0);
            fam.setBackgroundResource(0);
            work.setBackgroundResource(0);
        }
        else if(subtype.equals("soc"))
        {
            soc.setBackgroundResource(R.drawable.roundedwhitebg);
            per.setBackgroundResource(0);
            fam.setBackgroundResource(0);
            work.setBackgroundResource(0);
        }
        else if(subtype.equals("fam"))
        {
            fam.setBackgroundResource(R.drawable.roundedwhitebg);
            soc.setBackgroundResource(0);
            per.setBackgroundResource(0);
            work.setBackgroundResource(0);
        }
        else if(subtype.equals("work"))
        {
            work.setBackgroundResource(R.drawable.roundedwhitebg);
            soc.setBackgroundResource(0);
            fam.setBackgroundResource(0);
            per.setBackgroundResource(0);
        }
    }


}
