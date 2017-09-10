package pocket.check.pocketcheck;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class GridCheck extends AppCompatActivity {

    public static GridView gv;

    int[] images;
    String[] list;
    int gpos;

    int selectediv;
    String temp[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_check);

        gv= (GridView) findViewById(R.id.gridView);
        gv.setAdapter(new GridAdap(this));

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gpos=position;
                listMethod(position);
                if(position==0)
                {
                    finish();
                    Expenses.safe.setImageResource(R.drawable.ic_box_50);
                    Expenses.chota.setVisibility(View.INVISIBLE);
                    Expenses.type.setText("General");
                    finish();
                }
            }
        });

        //listMethod();


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
            temp=res.getStringArray(R.array.gridu);
            images=new int[] {R.drawable.ic_box_50,R.drawable.ic_person_50,R.drawable.ic_home_50,R.drawable.ic_food_50,
                    R.drawable.ic_car_50,R.drawable.ic_shop_50,R.drawable.ic_fun_50,R.drawable.ic_misc_50};

            for(int i=0;i<8;i++)
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
            TextView tv;
            ViewHolder(View v)
            {
                iv= (ImageView) v.findViewById(R.id.imageView10);
                tv= (TextView) v.findViewById(R.id.textView44);
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
            holder.tv.setText(temp.name);

            return v;
        }
    }

    void listMethod(int pos)
    {

        String[] showlist = getList(pos);
        ListView lv= (ListView) findViewById(R.id.listView);
        lv.setAdapter(new ListAdap(getApplicationContext(),android.R.layout.simple_list_item_1,
                showlist));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Expenses.safe.setImageResource(selectediv);
                Expenses.chota.setText(list[position]);
                Expenses.chota.setVisibility(View.VISIBLE);
                Expenses.type.setText(temp[gpos]);
                finish();
            }
        });



    }

    String[] getList(int pos)
    {

        if(pos==0)
        {
            list =new String [] {};
            return list;
        }
        else if(pos==1)
        {
            list = new String[] {"Mobile","Medical","Taxes","Insurance","Personal care",
                            "Gadgets","Pets","Education","Fitness","Loan","Vouchers","Subscriptions"};
            return list;
        }
        else if(pos==2)
        {
            list = new String[] {"Groceries","Rent","Phone","Electricity","Internet",
                    "Cable","Water","Repairs","Plants","Electronics","Furniture"};
            return list;
        }
        else if(pos==3)
        {
            list = new String[] {"Clothing","Shoes","Accessories","Bags"};
            return list;
        }
        else if(pos==4)
        {
            list = new String[] {"Takeout","Fastfood","Dinning in","Café","Drinks"};
            return list;
        }
        else if(pos==5)
        {
            list = new String[] {"Gas","Maintenance","Public Transport","Taxi","Insurance",
                    "Car loan","Fine","Flight","Parking","Car rental"};
            return list;
        }
        else if(pos==6)
        {
            list = new String[] {"Events","Movies","Recreation","Cultral","Sports",
                    "Books","Magazines","Music","Apps","Software","TV Shows"};
            return list;
        }
        else if(pos==7)
        {
            list = new String[] {"Gift","Office","Charity","Lodging","Service",
                    "Toys"};
            return list;
        }
        list =new String [] {};
        return list;

    }

    class ListAdap extends ArrayAdapter
    {

        public ListAdap(Context context, int resource, String[] objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater li= (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v=li.inflate(R.layout.listlay_4_gridview,parent,false);

            ImageView iv= (ImageView) v.findViewById(R.id.imageView11);
            TextView tv= (TextView) v.findViewById(R.id.textView45);

            iv.setImageResource(images[gpos]);
            selectediv=images[gpos];
            tv.setText(list[position]);

            return v;
        }
    }

}
/*class Adap extends ArrayAdapter<String>
{

    public Adap(Context context, int resource, ArrayList<String> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater li= (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v=li.inflate(R.layout.gridimage,parent,false);

        ImageView iv= (ImageView) v.findViewById(R.id.imageView10);
        iv.setImageResource(images.get(position));

        return v;

    }
}*/