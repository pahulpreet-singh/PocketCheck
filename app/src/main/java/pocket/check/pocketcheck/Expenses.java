package pocket.check.pocketcheck;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Expenses extends AppCompatActivity {

    ImageView cal,tpic;
    Spinner spin;
    public static TextView per,soc,fam,work;
    public static String expenseFlag="per";;


    TextView dateExp;
    int year,month,day;

    ArrayList<String> accType;
    ArrayList<String> accSrno;
    SQLiteDatabase dbcall;
    String def,defAcc;

    String monthTable,accName;
    EditText exp,notes;
    public static TextView type;

    ArrayList<String> gvtry;
    ArrayList<Integer> images;

    //chota type
    public static TextView chota;
    public static ImageView safe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    expense();

            }
        });

        toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //chota textview type
        chota= (TextView) findViewById(R.id.textView46);
        chota.setVisibility(View.INVISIBLE);

        //rounded image in header
        safe= (ImageView) findViewById(R.id.imageView60);
        //safe.setBackgroundColor(Color.rgb(255, 255, 255));
        safe.setImageResource(R.drawable.ic_box_50);

        //spinner
        spin= (Spinner) findViewById(R.id.spinner);
        getTypes();
        setSpinner();
        //get spinner selected item and its srno for insertion
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                accName = spin.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //Setting textview background onClick
        per= (TextView) findViewById(R.id.textView16);
        soc= (TextView) findViewById(R.id.textView17);
        fam= (TextView) findViewById(R.id.textView18);
        work= (TextView) findViewById(R.id.textView19);
        onTVclick();


        //setDate
        dateExp= (TextView) findViewById(R.id.dateExp);
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
        dateExp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(999);
            }
        });

        //for database
        exp= (EditText) findViewById(R.id.editText3);
        notes= (EditText) findViewById(R.id.editText4);
        type= (TextView) findViewById(R.id.type);
            //get subtype from expenseFlag


        //GRIDVIEW
        safe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dial=new Intent(getBaseContext(),GridCheck.class);
                startActivity(dial);
            }
        });
        RelativeLayout rl= (RelativeLayout) findViewById(R.id.relativeLayout6);
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dial=new Intent(getBaseContext(),GridCheck.class);
                startActivity(dial);
            }
        });


    }

    void onTVclick()
    {
        per.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                per.setBackgroundResource(R.drawable.roundedwhitebg);
                soc.setBackgroundResource(0);
                fam.setBackgroundResource(0);
                work.setBackgroundResource(0);
                expenseFlag="per";
            }
        });
        soc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soc.setBackgroundResource(R.drawable.roundedwhitebg);
                per.setBackgroundResource(0);
                fam.setBackgroundResource(0);
                work.setBackgroundResource(0);
                expenseFlag="soc";
            }
        });
        fam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fam.setBackgroundResource(R.drawable.roundedwhitebg);
                soc.setBackgroundResource(0);
                per.setBackgroundResource(0);
                work.setBackgroundResource(0);
                expenseFlag="fam";
            }
        });
        work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                work.setBackgroundResource(R.drawable.roundedwhitebg);
                soc.setBackgroundResource(0);
                fam.setBackgroundResource(0);
                per.setBackgroundResource(0);
                expenseFlag="work";
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();
        if(id==R.id.home)
        {
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }

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
        dateExp.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }
    //date end


    //spinner
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
    //spinner end


    //Database
        //in what month we will insert
    void getMonth()
    {
        if(month==0||month==4||month==8)
        {
            monthTable="mon1";
        }
        else if(month==1||month==5||month==9)
        {
            monthTable="mon2";
        }
        else if(month==2||month==6||month==10)
        {
            monthTable="mon3";
        }
        else if(month==3||month==7||month==11)
        {
            monthTable="mon4";
        }

    }
    void expense()
    {
        if(exp.getText().toString().equals(""))
        {
            Toast.makeText(getBaseContext(),"Enter expense amount",Toast.LENGTH_LONG).show();
        }
        else {
            Dbclass obj = new Dbclass(getApplicationContext());
            obj.caller();
            //dbcall=obj.getWritableDatabase();
            getMonth();
            long status;
            status = obj.addExp(monthTable, Long.parseLong(exp.getText().toString()), type.getText().toString(),
                    expenseFlag, accName, notes.getText().toString(), dateExp.getText().toString());
            if (status > 0) {
                balanceUpdate();
                Toast.makeText(getApplicationContext(), "Expense Saved", Toast.LENGTH_SHORT).show();
                Intent next = new Intent(getBaseContext(), Home.class);
                finish();
                next.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(next);
            } else {
                Toast.makeText(getApplicationContext(), "Error. Please contact the developer.",
                        Toast.LENGTH_LONG).show();
            }
        }

    }

    void balanceUpdate()
    {
        long bal=Long.parseLong(exp.getText().toString());

        String getBal="select * from balanceTable";
        Dbclass obj=new Dbclass(getApplicationContext());
        obj.caller();
        dbcall=obj.getReadableDatabase();

        Cursor rs = dbcall.rawQuery(getBal, null);
        if (rs.moveToNext())
        {
            bal = Long.parseLong(rs.getString(rs.getColumnIndex("balance")))-bal;
            Home.total.setText(String.valueOf(bal));

        }

        //update
        long stat;

        stat = obj.balance(bal);

        if (stat > 0)
        {
            Toast.makeText(getApplicationContext(), "Total Balance updated", Toast.LENGTH_SHORT)
                    .show();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Error2. Please contact the developer.",
                    Toast.LENGTH_LONG).show();
        }
    }


    //Database end

    void getContent()
    {
        gvtry=new ArrayList<String>();
        images=new ArrayList<Integer>();
        gvtry.add("peli");
        gvtry.add("duji");
        gvtry.add("teeji");

        images.add(R.drawable.ic_about);
        images.add(R.drawable.ic_dash);
        images.add(R.drawable.ic_exp);
    }

    class Country
    {
        int image;
        String name;

        Country(int image,String name)
        {
            this.image=image;
            this.name=name;
        }

    }

    class GridAdap extends BaseAdapter
    {

        Context con;
        ArrayList<Country> alist;

        GridAdap(Context context)
        {
            con=context;
            alist=new ArrayList<Country>();

            Resources res= context.getResources();
            String temp[]=res.getStringArray(R.array.gridu);
            int[] images={R.drawable.ic_home,R.drawable.ic_menu_camera,R.drawable.ic_rev};

            for(int i=0;i<3;i++)
            {
                Country c=new Country(images[i],temp[i]);
                alist.add(c);
            }

        }

        @Override
        public int getCount() {
            return alist.size();
        }

        @Override
        public Object getItem(int position) {
            return alist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        class ViewHolder
        {
            ImageView iv;
            ViewHolder(View v)
            {
                iv= (ImageView) v.findViewById(R.id.imageView10);
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v=convertView;
            ViewHolder holder=null;
            if(v==null)
            {
                LayoutInflater li= (LayoutInflater) con.getSystemService(LAYOUT_INFLATER_SERVICE);
                v=li.inflate(R.layout.gridimage,parent,false);

                holder=new ViewHolder(v);
                v.setTag(holder);

            }
            else
            {
                holder= (ViewHolder) v.getTag();
            }
            Country temp=alist.get(position);
            holder.iv.setImageResource(temp.image);

            return v;
        }
    }

}
