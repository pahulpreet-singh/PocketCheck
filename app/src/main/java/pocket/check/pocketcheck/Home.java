package pocket.check.pocketcheck;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Button expenses, income, review;
    TextView month1, month2, navName, navEmail;
    public static TextView total;
    SQLiteDatabase dbcall;
    public static TextView monExpense;
    public static TextView budget;
    public static TextView uname;
    String monthTable;
    ArrayList<Long> calc;
    ArrayList<Long> inc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        month1 = (TextView) findViewById(R.id.textView4);
        month2 = (TextView) findViewById(R.id.textView5);
        total = (TextView) findViewById(R.id.textView13);
        setBalance();

        monExpense = (TextView) findViewById(R.id.textView6);
        setExpenses();
        budget = (TextView) findViewById(R.id.budget);

        currentMonth();

        //set Remaining budget
        setBudget();

        //set Username
        setUname();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent next = new Intent(Home.this, Expenses.class);
                startActivity(next);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Expenses button
        expenses = (Button) findViewById(R.id.button2);
        expenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next = new Intent(Home.this, Expenses.class);
                startActivity(next);
            }
        });

        //income
        income = (Button) findViewById(R.id.button3);
        income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next = new Intent(Home.this, Income.class);
                startActivity(next);
            }
        });

        //Review button
        review = (Button) findViewById(R.id.button);
        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next = new Intent(Home.this, Review.class);
                startActivity(next);
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.add_account) {
            Intent next = new Intent(getBaseContext(), AddAccount.class);
            startActivity(next);
        }
        if (id == R.id.dash) {
            Intent next = new Intent(getBaseContext(), Dashboard.class);
            startActivity(next);
        }

        return super.onOptionsItemSelected(item);
    }

    //@SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.homeNav) {
            // Handle the camera action
            Intent next = new Intent(getBaseContext(), Home.class);
            finish();
            next.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(next);
        } else if (id == R.id.dashNav) {

            Intent next=new Intent(getBaseContext(),Dashboard.class);
            startActivity(next);

        } else if (id == R.id.revNav) {
            Intent next=new Intent(getBaseContext(),Review.class);
            startActivity(next);

        } else if (id == R.id.expNav) {

            Intent next=new Intent(getBaseContext(),Expenses.class);
            startActivity(next);

        } else if (id == R.id.incNav) {

            Intent next=new Intent(getBaseContext(),Income.class);
            startActivity(next);

        } else if (id == R.id.shareNav) {

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey my app is about ready :D");
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, "Share this app with your friends"));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void currentMonth() {
        final Calendar c = Calendar.getInstance();

        int month = c.get(Calendar.MONTH);
        String mname = theMonth(month);
        month1.setText(mname);
        month2.setText(mname);

    }

    public static String theMonth(int month) {
        String[] monthNames = {"January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"};

        return monthNames[month];
    }

    //total balance
    void setBalance() {
        Dbclass obj = new Dbclass(getApplicationContext());
        obj.caller();
        dbcall = obj.getReadableDatabase();
        String getBalanceQ = "select * from balanceTable";

        try {
            Cursor rs = dbcall.rawQuery(getBalanceQ, null);
            if (rs.moveToNext()) {
                total.setText(rs.getString(rs.getColumnIndex("balance")));
            }
        } catch (Exception e) {
        }

    }

    //month Expense
    void setExpenses() {
        getMonth();
        Dbclass obj = new Dbclass(getApplicationContext());
        obj.caller();
        dbcall = obj.getReadableDatabase();
        String getExpenseQ = "select * from " + monthTable;

        try {
            Cursor rs = dbcall.rawQuery(getExpenseQ, null);
            if (rs.moveToNext()) {
                calc = new ArrayList<Long>();
                do {
                    calc.add(Long.parseLong(rs.getString(rs.getColumnIndex("expense"))));
                } while (rs.moveToNext());
                long total = calculateExpense();
                monExpense.setText(String.valueOf(total));
            } else {
                monExpense.setText("0");
            }
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "Error in setting expenses. Contact developer", Toast.LENGTH_LONG).show();
        }
    }

    //what table to fetch from
    void getMonth() {
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH);
        if (month == 0 || month == 4 || month == 8) {
            monthTable = "mon1";
        } else if (month == 1 || month == 5 || month == 9) {
            monthTable = "mon2";
        } else if (month == 2 || month == 6 || month == 10) {
            monthTable = "mon3";
        } else if (month == 3 || month == 7 || month == 11) {
            monthTable = "mon4";
        }

    }

    //calculate all expenses of current month
    long calculateExpense() {
        long total = 0;
        for (int i = 0; i < calc.size(); i++) {
            total = total + calc.get(i);
        }
        return total;
    }

    //set Remaining budget
    public void setBudget() {

        SharedPreferences pref = getSharedPreferences("DashFlag", Context.MODE_PRIVATE);
        long per = Long.parseLong(pref.getString("perflag", "0"));
        long expense = Long.parseLong(monExpense.getText().toString());
        long tot = getTotalInc();
        long calcPer = (per * tot) / 100;
        //Toast.makeText(getBaseContext(),String.valueOf(calcPer),Toast.LENGTH_LONG).show();
        long calc = tot - calcPer - expense;
        if (calc < 0) {
            calc = 0;
        }
        budget.setText(String.valueOf(calc));

    }

    long getTotalInc() {

        String q = "select * from incomes";
        inc = new ArrayList<Long>();

        Dbclass obj = new Dbclass(getApplicationContext());
        dbcall = obj.getReadableDatabase();

        Cursor cur = dbcall.rawQuery(q, null);

        if (cur.moveToNext()) {
            do {
                inc.add(Long.parseLong(cur.getString(cur.getColumnIndex("amount"))));
            } while (cur.moveToNext());
        }
        long tot = 0;
        for (int i = 0; i < inc.size(); i++) {
            tot += inc.get(i);
        }
        return tot;

    }

    //set username textView
    void setUname() {
        String q = "select * from users";
        Dbclass obj = new Dbclass(getApplicationContext());
        dbcall = obj.getReadableDatabase();

        Cursor next = dbcall.rawQuery(q, null);

        if (next.moveToNext()) {
            uname = (TextView) findViewById(R.id.uname);
            uname.setText(next.getString(next.getColumnIndex("name")));
            // View v = getLayoutInflater().inflate(R.layout.nav_header_home, null);
            NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
            View v = navView.inflateHeaderView(R.layout.nav_header_home);
            navName = (TextView) v.findViewById(R.id.navName);
            navEmail = (TextView) v.findViewById(R.id.navEmail);


            navName.setText(next.getString(next.getColumnIndex("name")));
            //Toast.makeText(getBaseContext(),"yo"+navName.getText().toString(),Toast.LENGTH_LONG).show();
            navEmail.setText(next.getString(next.getColumnIndex("email")));

            //navView.addHeaderView(v);

        }

    }


}

