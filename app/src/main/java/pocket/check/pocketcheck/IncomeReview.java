package pocket.check.pocketcheck;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.util.Collections;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class IncomeReview extends Fragment {

    SQLiteDatabase dbcall;
    String monthTable;
    ArrayList<String> amt,date,accName,notes,srno;

    AdapClass adap;
    TextView total;

    public IncomeReview() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_income_review, container, false);

        ListView lv= (ListView) rootView.findViewById(android.R.id.list);
        getContent();
        adap=new AdapClass(getActivity().getBaseContext(), android.R.layout.simple_list_item_1, amt);
        lv.setAdapter(adap);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try {
                    Class destination = Class.forName("pocket.check.pocketcheck.IncomeReviewExpanded");

                    Intent next = new Intent(getActivity().getBaseContext(), destination);
                    next.putExtra("amount", amt.get(position));
                    next.putExtra("accName", accName.get(position));
                    next.putExtra("notes", notes.get(position));
                    next.putExtra("date", date.get(position));
                    startActivity(next);


                } catch (ClassNotFoundException e) {
                    Toast.makeText(getActivity().getBaseContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }


            }
        });
        this.registerForContextMenu(lv);

        total= (TextView) rootView.findViewById(R.id.textView39);
        setTotal();


        return rootView;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.income_review, menu);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo)
                item.getMenuInfo();
        int pos=info.position;

        if(item.getItemId()==R.id.open)
        {
            Intent next=new Intent(getActivity().getBaseContext(),IncomeReviewExpanded.class);
            next.putExtra("amount", amt.get(pos));
            next.putExtra("accName", accName.get(pos));
            next.putExtra("notes", notes.get(pos));
            next.putExtra("date", date.get(pos));
            startActivity(next);
        }

        else if(item.getItemId()==R.id.del)
        {
            deleteInc(srno.get(pos), amt.get(pos));
            setHomeBud(amt.get(pos));
            adap.remove(amt.get(pos));
            setTotal();

        }

        return super.onContextItemSelected(item);
    }

    class AdapClass extends ArrayAdapter<String>
    {

        public AdapClass(Context context, int resource, ArrayList<String> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater li= (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v=li.inflate(R.layout.list_review,parent,false);

            TextView rupee= (TextView) v.findViewById(R.id.textView35);
            TextView acc= (TextView) v.findViewById(R.id.textView25);
            TextView dateTV= (TextView) v.findViewById(R.id.textView29);


            rupee.setText(amt.get(position));
            acc.setText(accName.get(position));
            dateTV.setText(date.get(position));

            try {
                ImageView iv = (ImageView) v.findViewById(R.id.imageView9);
                Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.incomeicon);
                iv.setImageBitmap(icon);
            }
            catch (Exception e)
            {
                Toast.makeText(getActivity().getBaseContext(),"Errorwa "+e.getMessage(),Toast.LENGTH_LONG).show();
            }

            return v;
        }
    }
    void getContent()
    {
        Dbclass obj=new Dbclass(getActivity().getApplicationContext());
        dbcall=obj.getReadableDatabase();

        String q="select * from incomes";

        amt=new ArrayList<String>();
        accName=new ArrayList<String>();
        date=new ArrayList<String>();
        notes=new ArrayList<String>();
        srno=new ArrayList<String>();


        Cursor next=dbcall.rawQuery(q,null);

        if(next.moveToNext())
        {
            do {

                amt.add(next.getString(next.getColumnIndex("amount")));
                accName.add(next.getString(next.getColumnIndex("accName")));
                notes.add(next.getString(next.getColumnIndex("label")));
                date.add(next.getString(next.getColumnIndex("date")));
                srno.add(next.getString(next.getColumnIndex("srno")));

            }while (next.moveToNext());
            Collections.reverse(amt);
            Collections.reverse(accName);
            Collections.reverse(notes);
            Collections.reverse(date);
            Collections.reverse(srno);
        }
        else
        {
            Toast.makeText(getActivity().getBaseContext(), "No income in the bag!", Toast.LENGTH_LONG).show();
        }

    }
    void deleteInc(String sr,String rs)
    {
        //String q="delete from incomes where srno="+sr;

        Dbclass obj=new Dbclass(getActivity().getApplicationContext());
        obj.caller();
        long status=obj.deleteInc(sr);
        if(status>0)
        {

            Toast.makeText(getActivity().getBaseContext(),"Deleted",Toast.LENGTH_SHORT).show();
            balanceUpdate(rs);

        }
        else
        {
            Toast.makeText(getActivity().getBaseContext(),"Error. Please contact the developer.",Toast.LENGTH_LONG).show();
        }

    }
    void balanceUpdate(String rs)
    {
        long bal=Long.parseLong(rs);

        String getBal="select * from balanceTable";
        Dbclass obj=new Dbclass(getActivity().getApplicationContext());
        obj.caller();
        dbcall=obj.getReadableDatabase();

        Cursor cur = dbcall.rawQuery(getBal, null);
        if (cur.moveToNext())
        {
            bal = Long.parseLong(cur.getString(cur.getColumnIndex("balance")))-bal;
            Home.total.setText(String.valueOf(bal));

        }

        //update
        long stat;

        stat = obj.balance(bal);

        if (stat > 0)
        {

            Toast.makeText(getActivity().getApplicationContext(), "Total Balance updated", Toast.LENGTH_SHORT)
                    .show();

        }
        else
        {
            Toast.makeText(getActivity().getApplicationContext(), "Error. Please contact the developer.",
                    Toast.LENGTH_LONG).show();
        }

    }
    void setTotal()
    {
        long sum=0;
        if(!amt.isEmpty()) {
            for (int i = 0; i < amt.size(); i++) {
                sum += Long.parseLong(amt.get(i));
            }
            total.setText(String.valueOf(sum));
        }

    }
    void setHomeBud(String s)
    {
        long getBud=Long.parseLong(Home.budget.getText().toString());
        long calc=getBud-Long.parseLong(s);
        Home.budget.setText(String.valueOf(calc));

    }

}
