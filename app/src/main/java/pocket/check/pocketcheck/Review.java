package pocket.check.pocketcheck;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.LayoutInflater;
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
import java.util.List;

import tabs.SlidingTabLayout;

public class Review extends AppCompatActivity{


    SlidingTabLayout mTabs;
    ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //back button
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent exp=new Intent(getBaseContext(),Expenses.class);
                startActivity(exp);
            }
        });
        mTabs= (SlidingTabLayout) findViewById(R.id.tabs);
        mPager= (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new pageAdap(getSupportFragmentManager()));
        mTabs.setDistributeEvenly(true);
        mTabs.setSelectedIndicatorColors(getResources().getColor(R.color.colorAccent));
        mTabs.setViewPager(mPager);

    }

    class pageAdap extends FragmentPagerAdapter
    {

        String[] names;
        public pageAdap(FragmentManager fm) {
            super(fm);
            names=getResources().getStringArray(R.array.tab);
        }

        @Override
        public Fragment getItem(int position)
        {
            Fragment frag=null;
            if(position==0)
            {
                frag=new ExpenseReview();
            }
            else if(position==1)
            {
                frag=new IncomeReview();
            }
            return frag;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return names[position];
        }

        @Override
        public int getCount() {
            return 2;
        }
    }




}
