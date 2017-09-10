package pocket.check.pocketcheck;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExpenseReview extends Fragment {

    SQLiteDatabase dbcall;
    String monthTable;
    ArrayList<String> rs,st,date,type,accName,notes,srno;
    AdapClass adap;
    ImageView iv;

    public ExpenseReview() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_expense_review, container, false);

        ListView lv= (ListView) rootView.findViewById(android.R.id.list);
        getContent();
        adap=new AdapClass(getActivity().getBaseContext(), android.R.layout.simple_list_item_1, rs);
        lv.setAdapter(adap);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Class destination = Class.forName("pocket.check.pocketcheck.ReviewExpanded");

                    Intent next = new Intent(getActivity().getBaseContext(), destination);
                    next.putExtra("expense", rs.get(position));
                    next.putExtra("type", type.get(position));
                    next.putExtra("subtype", st.get(position));
                    next.putExtra("accName", accName.get(position));
                    next.putExtra("notes", notes.get(position));
                    next.putExtra("date", date.get(position));
                    //next.putExtra("image",img);
                    startActivity(next);


                } catch (ClassNotFoundException e) {
                    Toast.makeText(getActivity().getBaseContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        });


        //context menu
        this.registerForContextMenu(lv);

        return rootView;



    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.expense_review, menu);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo)
                item.getMenuInfo();
        int position=info.position;

        if(item.getItemId()==R.id.openExp)
        {
            Intent next=new Intent(getActivity().getBaseContext(),ReviewExpanded.class);
            next.putExtra("expense",rs.get(position));
            next.putExtra("type",type.get(position));
            next.putExtra("subtype",st.get(position));
            next.putExtra("accName",accName.get(position));
            next.putExtra("notes",notes.get(position));
            next.putExtra("date",date.get(position));
            //next.putExtra("image",img);
            startActivity(next);
        }

        else if(item.getItemId()==R.id.delExp)
        {
            deleteExp(srno.get(position),rs.get(position));
            setHomeBud(rs.get(position));
            adap.remove(rs.get(position));
        }

        return super.onContextItemSelected(item);
    }




    class AdapClass extends ArrayAdapter<String>
    {
        Context context;
        public AdapClass(Context context, int resource, ArrayList<String> objects) {
            super(context, resource, objects);
            this.context=context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater li= (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v=li.inflate(R.layout.list_review,parent,false);

            //fetch from database

            //getContent();
            //
            iv= (ImageView) v.findViewById(R.id.imageView9);
            TextView rupee= (TextView) v.findViewById(R.id.textView35);
            TextView subtype= (TextView) v.findViewById(R.id.textView25);
            TextView dateTV= (TextView) v.findViewById(R.id.textView29);

            //Toast.makeText(getBaseContext(),rs.get(position),Toast.LENGTH_LONG).show();
            //setting things
                rupee.setText(rs.get(position));
                String st1=setSubtype(st.get(position));
                subtype.setText(st1);
                dateTV.setText(date.get(position));
                int img=setImage(type.get(position));
                iv.setImageResource(img);


            return v;
        }
    }

    //set image according to type
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

    void getContent()
    {
        Dbclass obj=new Dbclass(getActivity().getApplicationContext());
        dbcall=obj.getReadableDatabase();


        getMonth();
        String q="select * from "+monthTable;

        rs=new ArrayList<String>();
        st=new ArrayList<String>();
        date=new ArrayList<String>();
        type=new ArrayList<String>();
        accName=new ArrayList<String>();
        notes=new ArrayList<String>();
        srno=new ArrayList<String>();


        Cursor next=dbcall.rawQuery(q,null);
        if(next.moveToNext())
        {
            do {
                rs.add(next.getString(next.getColumnIndex("expense")));
                type.add(next.getString(next.getColumnIndex("expType")));
                st.add(next.getString(next.getColumnIndex("expSubtype")));
                date.add(next.getString(next.getColumnIndex("date")));
                accName.add(next.getString(next.getColumnIndex("accName")));
                notes.add(next.getString(next.getColumnIndex("notes")));
                srno.add(next.getString(next.getColumnIndex("srno")));
            }while (next.moveToNext());

            Collections.reverse(rs);
            Collections.reverse(type);
            Collections.reverse(st);
            Collections.reverse(date);
            Collections.reverse(accName);
            Collections.reverse(notes);
            Collections.reverse(srno);
        }
        else
        {
            Toast.makeText(getActivity().getBaseContext(),"No Expenses for this month",Toast.LENGTH_LONG).show();
        }


    }
    void getMonth()
    {
        Calendar c=Calendar.getInstance();
        int month=c.get(Calendar.MONTH);
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
    String setSubtype(String st)
    {
        String st1="Personal";
        if(st.equals("per"))
        {
            st1="Personal";
        }
        else if(st.equals("soc"))
        {
            st1="Social";
        }
        else if(st.equals("fam"))
        {
            st1="Family";
        }
        else if(st.equals("work"))
        {
            st1="Work";
        }
        return st1;
    }



    void deleteExp(String sr,String amt)
    {
        Dbclass obj=new Dbclass(getActivity().getApplicationContext());
        obj.caller();
        long status=obj.deleteExp(sr,monthTable);
        if(status>0)
        {

            Toast.makeText(getActivity().getBaseContext(),"Deleted",Toast.LENGTH_SHORT).show();
            balanceUpdate(amt);

        }
        else
        {
            Toast.makeText(getActivity().getBaseContext(),"Error. Please contact the developer.",Toast.LENGTH_LONG).show();
        }
    }

    void balanceUpdate(String amt)
    {
        long bal=Long.parseLong(amt);

        String getBal="select * from balanceTable";
        Dbclass obj=new Dbclass(getActivity().getApplicationContext());
        obj.caller();
        dbcall=obj.getReadableDatabase();

        Cursor cur = dbcall.rawQuery(getBal, null);
        if (cur.moveToNext())
        {
            bal = Long.parseLong(cur.getString(cur.getColumnIndex("balance")))+bal;
            Home.total.setText(String.valueOf(bal));

        }

        //update
        long stat;

        stat = obj.balance(bal);

        if (stat > 0)
        {

            setMonExpense(amt);
            Toast.makeText(getActivity().getApplicationContext(), "Total Balance updated", Toast.LENGTH_SHORT)
                    .show();

        }
        else
        {
            Toast.makeText(getActivity().getApplicationContext(), "Error. Please contact the developer.",
                    Toast.LENGTH_LONG).show();
        }

    }
    void setMonExpense(String amt)
    {
        String exp=Home.monExpense.getText().toString();
        long newExp=Long.parseLong(exp)-Long.parseLong(amt);
        if(newExp<0)
        {
            Home.monExpense.setText("0");
        }
        else if(newExp>=0)
        {
            Home.monExpense.setText(String.valueOf(newExp));
        }

    }

    void setHomeBud(String s)
    {
        long getBud=Long.parseLong(Home.budget.getText().toString());
        long calc=getBud+Long.parseLong(s);
        Home.budget.setText(String.valueOf(calc));

    }


}
